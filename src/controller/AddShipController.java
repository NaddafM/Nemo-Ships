package controller;

import com.jfoenix.controls.JFXSlider;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DataAccessObject;

public class AddShipController implements Initializable {

	@FXML
	private TextField newShipNumber;
	@FXML
	private TextField newShipName;
	@FXML
	private DatePicker newShipManufacturedDate;
	@FXML
	private JFXSlider newShipMaxCapacity;
	@FXML
	private JFXSlider newShipMaxNumOfPeople;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		String date = "16/08/1994";
		LocalDate localDate = LocalDate.parse(date, formatter);
		newShipManufacturedDate.setValue(localDate);

	}

	@FXML
	private void closeAddShipPage(ActionEvent event) {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	private void addNewShip(ActionEvent event) {

		if (newShipNumber.getText().equals("") || newShipName.getText().equals("")
				|| newShipManufacturedDate.getValue() == null) {

			Alerts.generateWarningAlert("missing fields", "empty fields",
					"Please make sure all required fields are filled out");

		} else if (!isNumeric(newShipNumber.getText())) {
			Alerts.generateWarningAlert("illegal input", "make sure you are using vaild input",
					"ship number must be a numaric value");

		} else {

			try {
				DataAccessObject.addCruiseShip(newShipNumber.getText(), newShipName.getText(),
						newShipManufacturedDate.getValue(), Math.round(newShipMaxCapacity.getValue()) + "",
						Math.round(newShipMaxNumOfPeople.getValue()) + "");

				Alerts.generateSuccessAlert("transaction completed", "data was added successfully",
						"ship " + newShipNumber.getText() + " was added to data base");

				newShipNumber.setText("");
				newShipName.setText("");
				newShipMaxCapacity.setValue(newShipMaxCapacity.getMax() / 2);
				newShipMaxNumOfPeople.setValue(newShipMaxNumOfPeople.getMax() / 2);
				controller.mainPageController.getInstance().fillCruiseShipTable("");
				controller.mainPageController.getInstance().fillDashBoard();
				Stage stage = (Stage) newShipMaxCapacity.getScene().getWindow();
				stage.close();

			} catch (SQLException e) {

				if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601)
					Alerts.generateErrorAlert(e, "duplicate exception", "ship was not added to the data base",
							"Sorry, we are unable to add the ship. A duplicate ship already exists.");

				else
					Alerts.generateErrorAlert(e, "SQL Exception", "ship was not added to the data base",
							"Sql exception");

			}

		}

	}

	public static boolean isNumeric(String str) {

		try {
			Double.parseDouble(str);
			return true;

		} catch (NumberFormatException e) {

			return false;
		}
	}

}
