package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.CruiseShip;
import model.DataAccessObject;

public class updateCruiseShipController implements Initializable {

	@FXML
	private JFXTextField cruiseShipNumber;

	@FXML
	private JFXTextField cruiseShipName;

	@FXML
	private JFXDatePicker manufacturingDate;

	@FXML
	private JFXTextField maxCapacity;

	@FXML
	private JFXTextField maxNumberOfPeople;

	private CruiseShip cruiseShipToUpdate;

	public updateCruiseShipController(CruiseShip cs) {

		cruiseShipToUpdate = cs;

	}

	@FXML
	void updateCruiseShip(ActionEvent event) {

		if (!cruiseShipName.getText().equals("") && !maxCapacity.getText().equals("")
				&& !maxNumberOfPeople.getText().equals("") && manufacturingDate.getValue() != null) {
			LocalDate now = LocalDate.now();
			if (manufacturingDate.getValue().isBefore(now)) {

				try {

					Date mDate = Date
							.from(manufacturingDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

					CruiseShip cs = new CruiseShip(cruiseShipToUpdate.getNumber(), cruiseShipName.getText(), mDate,
							Integer.valueOf(maxCapacity.getText()), Integer.valueOf(maxNumberOfPeople.getText()));

					DataAccessObject.updateCruiseShip(cs);
					mainPageController.getInstance().fillCruiseShipTable("");

					Alerts.generateSuccessAlert("transaction success", "Ship was Updated successfully",
							"Cruise Ship : " + cruiseShipToUpdate.getNumber() + " was updated successfully");

					Stage stage = (Stage) manufacturingDate.getScene().getWindow();
					stage.close();
					mainPageController.getInstance().fillDashBoard();

				} catch (Exception e) {
					Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
							"Could not update Cruise Ship!");

				}

			} else {

				Alerts.generateWarningAlert("illegal manufacturing date", "illegal manufacturing date",
						"please choose a ligal manufacturing date");

			}

		} else {

			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");

		}

	}

	private void setRistrictions() {

		maxCapacity.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					maxCapacity.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}
		});

		maxNumberOfPeople.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					maxNumberOfPeople.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		setRistrictions();
		cruiseShipNumber.setText(cruiseShipToUpdate.getNumber() + "");
		cruiseShipName.setText(cruiseShipToUpdate.getName());

		manufacturingDate.setValue(convertToLocalDateViaInstant(cruiseShipToUpdate.getManufacturingDate()));
		maxCapacity.setText(cruiseShipToUpdate.getMaxCapacity() + "");
		maxNumberOfPeople.setText(cruiseShipToUpdate.getMaxNumberOfPeople() + "");

	}

	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

}
