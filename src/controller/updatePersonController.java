package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.DataAccessObject;
import model.Person;

public class updatePersonController implements Initializable {

	@FXML
	private JFXTextField personId;

	@FXML
	private JFXTextField personFirstName;

	@FXML
	private JFXTextField personSurName;

	@FXML
	private JFXDatePicker personBirthDate;

	@FXML
	private JFXTextField personEmail;

	@FXML
	private JFXTextField personPhoneNumber;

	@FXML
	private JFXComboBox<String> emailUrl;

	private Person personToUpdate;
	private int userType;

	public updatePersonController(Person p, int user) {

		personToUpdate = p;
		userType = user;

	}

	@FXML
	void updatePerson(ActionEvent event) {

		if (!personFirstName.getText().equals("") && !personSurName.getText().equals("")
				&& !personPhoneNumber.getText().equals("") && !personEmail.getText().equals("")) {

			if (personBirthDate.getValue() != null) {
				LocalDate now = LocalDate.now();
				if (personBirthDate.getValue().isBefore(now)) {
					if (personPhoneNumber.getText().length() == 10) {

						try {
							Date bDate = Date
									.from(personBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
							Person p = new Person(personId.getText(), personFirstName.getText(),
									personSurName.getText(), bDate, personPhoneNumber.getText(),
									personEmail.getText() + "" + emailUrl.getValue());
							DataAccessObject.updatePerson(p);

							if (userType == 1) {
								mainPageController.getInstance().fillPersonsTable();
								mainPageController.getInstance().fillDashBoard();
								Alerts.generateSuccessAlert("transaction success", "Person was Updated successfully",
										"Person : " + personToUpdate.getId() + " was updated successfully");
							} else {
								personPageController.getInstance().fillProfile();
								Alerts.generateSuccessAlert("transaction success",
										"Your profile was Updated successfully", "");

							}

							Stage stage = (Stage) personBirthDate.getScene().getWindow();
							stage.close();

						} catch (Exception e) {
							Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
									"Could not update Person !");

						}

					} else {

						Alerts.generateWarningAlert("wrong phone number", "illegal phone number",
								"phone number must be a 10 digit format");
					}

				} else {
					Alerts.generateWarningAlert("illegal birth date", "illegal birth date",
							"please choose a ligal birth date");
				}
			} else {
				Alerts.generateWarningAlert("Empty birth date", "empty birth date", "please choose the birth date");
			}

		} else {

			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		personId.setText(personToUpdate.getId() + "");
		personFirstName.setText(personToUpdate.getFirstName());
		personSurName.setText(personToUpdate.getLastName());
		personBirthDate.setValue(convertToLocalDateViaInstant(personToUpdate.getBirthDate()));
		personEmail.setText(personToUpdate.getEmail().split("@")[0]);
		personPhoneNumber.setText(personToUpdate.getPhoneNumber());
		emailUrl.getItems().addAll("@gmail.com", "@hotmail.com", "@hotmail.co.il", "@yahoo.com");
		emailUrl.getSelectionModel().select(0);
		setRistrictions();

	}

	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	private void setRistrictions() {

		personPhoneNumber.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					personPhoneNumber.setText(newValue.replaceAll("[^\\d]", ""));
				}
				if (personPhoneNumber.getText().length() > 10) {
					String s = personPhoneNumber.getText().substring(0, 10);
					personPhoneNumber.setText(s);
				}
			}
		});
	}

}
