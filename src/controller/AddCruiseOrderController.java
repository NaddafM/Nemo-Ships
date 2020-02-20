package controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;
import model.Cruise;
import model.CruiseOrder;
import model.DataAccessObject;
import model.Person;
import model.Room;

public class AddCruiseOrderController implements Initializable {

	@FXML
	private JFXComboBox<Integer> cruiseId;

	@FXML
	private JFXTextField shipNumber;

	@FXML
	private JFXComboBox<Integer> roomNumber;

	@FXML
	private JFXComboBox<String> personId;
	

	@FXML
	void addCruiseOrder() {

		
		
		if (cruiseId.getValue() != null && shipNumber.getText() != null && roomNumber.getValue() != null
				&& personId != null) {

			try {
				CruiseOrder co = new CruiseOrder(cruiseId.getValue(), Integer.valueOf(shipNumber.getText()),
						roomNumber.getValue(), personId.getValue());
				DataAccessObject.addCruiseOrder(co);
				mainPageController.getInstance().fillCruiseOrdersTable("");
	
				Alerts.generateSuccessAlert("transaction success", "cruise order was added successfully",
						"cruise number " + co.getCruiseId() + " , person id " + co.getPersonId() + " ,room number "
								+ co.getRoomNumber());

				cruiseId.setValue(null);
				roomNumber.setValue(null);
				personId.setValue(null);
				
				mainPageController.getInstance().fillDashBoard();
				mainPageController.getInstance().fillPersonsTable();
				Stage stage = (Stage) cruiseId.getScene().getWindow();
				stage.close();

			} catch (SQLException e) {
				if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

					Alerts.generateErrorAlert(e, "Exception Has Occurred",
							"Sorry, we are unable to add this Order . this room is already taken",
							"Cruise Order was not added to dataBase!");

				} else
					Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
							"Cruise Order was not added to dataBase!");

			}

		} else {
			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");
		}

	}

	@FXML
	public void setShip() {

		roomNumber.getItems().clear();
		try {
			shipNumber.setText(DataAccessObject.getShipByCruiseNumber(cruiseId.getValue()) + "");

		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"ship textfield was not filled with data");
		}

		try {

			for (Room r : DataAccessObject.getRooms(Integer.valueOf(shipNumber.getText()))) {
				roomNumber.getItems().add(r.getRoomNumber());

			}
		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"rooms comboBox was not filled with data");
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {

			for (Person p : DataAccessObject.getPersons())
				personId.getItems().add(p.getId());

		} catch (SQLException | ParseException e) {

			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"person comboBox was not filled with data");

		}

		try {

			for (Cruise c : DataAccessObject.getCruises())
				cruiseId.getItems().add(c.getNumber());

		} catch (SQLException | ParseException e) {

			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"cruise comboBox was not filled with data");

		}

	}

}