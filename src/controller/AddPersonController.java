package controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.DataAccessObject;
import model.Person;

public class AddPersonController implements Initializable {

	@FXML
	private JFXTextField personId;

	@FXML
	private JFXTextField personFirstName;

	@FXML
	private JFXTextField personLastName;

	@FXML
	private JFXTextField personPhoneNumber;

	@FXML
	private JFXTextField personEmail;

	@FXML
	private JFXComboBox<String> emailDomain;
	@FXML
	private JFXComboBox<String> phoneStart;

	@FXML
	private JFXDatePicker personBirthDate;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		putRistrictions();
		emailDomain.getItems().addAll("@gmail.com", "@hotmail.com", "@hotmail.co.il", "@yahoo.com",
				"@campus.haifa.ac.il", "@microsoft.com");
		emailDomain.getSelectionModel().select(0);

		phoneStart.getItems().addAll("050", "052", "053", "054");
		phoneStart.getSelectionModel().select(0);

	}

	private void putRistrictions() {
		personPhoneNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					personPhoneNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
				if (personPhoneNumber.getText().length() > 7) {
					String s = personPhoneNumber.getText().substring(0, 7);
					personPhoneNumber.setText(s);
				}
			}
		});

		personId.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					personId.setText(newValue.replaceAll("[^\\d]", ""));
				}
				if (personId.getText().length() > 9) {
					String s = personId.getText().substring(0, 9);
					personId.setText(s);
				}
			}
		});

	}

	@FXML
	void addPerson() {

		if (!personId.getText().equals("") && !personFirstName.getText().equals("")
				&& !personLastName.getText().equals("") && !personEmail.getText().equals("")
				&& !personPhoneNumber.getText().equals("")) {

			if (personId.getText().length() == 9) {
				if (personPhoneNumber.getText().length() == 7) {
					if (personBirthDate.getValue() != null) {
						LocalDate now = LocalDate.now();
						if (personBirthDate.getValue().isBefore(now)) {

							try {
								Date bDate = Date.from(
										personBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
								Person p = new Person(personId.getText(), personFirstName.getText(),
										personLastName.getText(), bDate,
										phoneStart.getValue() + "" + personPhoneNumber.getText(),
										personEmail.getText() + "" + emailDomain.getValue());
								DataAccessObject.addPerson(p);
								mainPageController.getInstance().resetPersonFilters();
								mainPageController.getInstance().fillPersonsTable();

								Alerts.generateSuccessAlert("transaction success", "Person was added successfully",
										p.getFirstName() + " " + p.getLastName()
												+ " was added to the data base successfully");

								personId.setText("");
								personFirstName.setText("");
								personLastName.setText("");
								personEmail.setText("");
								personPhoneNumber.setText("");
								personBirthDate.setPromptText("birth date");

								mainPageController.getInstance().fillDashBoard();
								Stage stage = (Stage) personBirthDate.getScene().getWindow();
								stage.close();

							} catch (SQLException e) {
								if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

									Alerts.generateErrorAlert(e, "Exception Has Occurred",
											"Sorry, we are unable to add this person . A duplicate value already exists",
											"Could not add person to DataBase!");

								} else
									Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
											"Could not add person to DataBase!");

							} catch (ParseException e) {
								Alerts.generateErrorAlert(e, "Exception", "parsing Exception Has Occurred",
										"Could not add person to DataBase!");
							}

						} else {

							Alerts.generateWarningAlert("illegal birth date", "illegal birth date",
									"please choose a ligal birth date");

						}

					} else {
						Alerts.generateWarningAlert("wrong birth date", "empty birth date",
								"please choose the birth date");

					}

				} else {
					Alerts.generateWarningAlert("wrong phone number", "illegal phone number",
							"phone number must be a 10 digit format");
				}

			} else {
				Alerts.generateWarningAlert("wrong id number", "illegal id", "id number must be a 9 digit number");
			}

		} else {
			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");

		}

	}

}
