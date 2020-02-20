package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CruiseShip;
import model.DataAccessObject;
import model.Room;

public class AddRoomController implements Initializable {

	private static CruiseShip cs;
	@FXML
	private TextField newRoomNumber;

	@FXML
	private TextField newRoomPrice;

	@FXML
	private JFXComboBox<Integer> bedsAmmount;

	@FXML
	private JFXToggleButton suite;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bedsAmmount.getItems().addAll(2, 3, 4);
		bedsAmmount.setValue(2);

	}

	@FXML
	void addNewRoom(ActionEvent event) {

		if (!newRoomNumber.getText().equals("") && !newRoomPrice.getText().equals("")) {
			if (isNumeric(newRoomNumber.getText()) && isNumeric(newRoomPrice.getText())) {

				try {

					String type;
					if (suite.isSelected())
						type = "suite";
					else
						type = "standard";

					Room r = new Room(Integer.valueOf(cs.getNumber()), Integer.valueOf(newRoomNumber.getText()),
							bedsAmmount.getValue(), type, Integer.valueOf(newRoomPrice.getText()));
					DataAccessObject.addRoom(cs, r);
					roomsPageController.getInstance().fillRooms();

					Alerts.generateSuccessAlert("transaction completed", "data was added successfully",
							"room " + newRoomNumber.getText() + " was added to ship: " + cs.getName());

					newRoomNumber.setText("");
					newRoomPrice.setText("");
					suite.setSelected(false);
					bedsAmmount.setValue(2);

					mainPageController.getInstance().fillDashBoard();
					Stage stage = (Stage) newRoomNumber.getScene().getWindow();
					stage.close();

				} catch (SQLException e) {

					if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

						Alerts.generateErrorAlert(e, "Exception Has Occurred",
								"Sorry, we are unable to add this room . A duplicate value already exists",
								"Could not add room to DataBase!");

					} else
						Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
								"Could not add room to DataBase!");

				}

			} else {

				Alerts.generateWarningAlert("illegal input", "make sure you are using vaild input",
						"room number must be a numaric value");

			}
		} else {
			Alerts.generateWarningAlert("missing fields", "empty fields",
					"Please make sure all required fields are filled out");

		}

	}

	@FXML
	void close(ActionEvent event) {
		((Node) (event.getSource())).getScene().getWindow().hide();

	}

	public AddRoomController(CruiseShip cs) {
		AddRoomController.cs = cs;
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
