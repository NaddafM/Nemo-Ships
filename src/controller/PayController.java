package controller;

import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Cruise;
import model.CruiseOrder;
import model.DataAccessObject;
import model.Person;
import model.Room;

public class PayController implements Initializable {

	private Integer cruiseNum;
	private String ID;
	float daysDiff;
	@FXML
	private Label cruiseLabel;
	@FXML
	private Label leavingDate;
	@FXML
	private Label returnDate;
	@FXML
	private Label paymentAmount;
	@FXML
	private JFXTextField shipId;
	@FXML
	private TextField beds;
	@FXML
	private TextField type;
	@FXML
	private TextField perNight;
	@FXML
	private TextField nameOnCard;
	@FXML
	private JFXComboBox<Integer> roomsComboBox;

	public PayController(String codePointCount, String personID) {

		cruiseNum = Integer.valueOf(codePointCount.split(", ")[1]);
		ID = personID;
		try {

			Person p = DataAccessObject.getPerson(ID);
			nameOnCard.setText(p.getFirstName() + " " + p.getLastName());

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Cruise choosenCo = null;

		try {
			for (Cruise c : DataAccessObject.getCruises()) {
				if (c.getNumber() == cruiseNum) {
					choosenCo = c;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		shipId.setText(choosenCo.getShipNumber() + "");
		cruiseLabel.setText("Cruise " + choosenCo.getNumber());

		try {
			for (Integer i : DataAccessObject.getEmptyRooms(cruiseNum))

				roomsComboBox.getItems().add(i);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Date date1 = choosenCo.getDepartureDateAndTime();
		LocalDate localDate = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int year = localDate.getYear();
		int month = localDate.getMonthValue();
		int day = localDate.getDayOfMonth();

		leavingDate.setText(day + "/" + month + "/" + year);
		Date date2 = choosenCo.getArrivaldateAndTime();
		localDate = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		year = localDate.getYear();
		month = localDate.getMonthValue();
		day = localDate.getDayOfMonth();
		returnDate.setText(day + "/" + month + "/" + year);
		daysDiff = getDateDiff(date1, date2, TimeUnit.DAYS);

	}

	@FXML
	private void fillRoomData() {

		Room choosenRoom = null;
		try {

			for (Room r : DataAccessObject.getRooms(Integer.valueOf(shipId.getText()))) {
				if (r.getRoomNumber() == roomsComboBox.getValue()
						&& r.getCruiseShipID() == Integer.valueOf(shipId.getText())) {
					choosenRoom = r;
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		beds.setText(choosenRoom.getBedsAmount() + "");
		type.setText(choosenRoom.getType());
		perNight.setText(choosenRoom.getPricePerNight() + "$");

		paymentAmount.setText(
				daysDiff * Integer.valueOf(perNight.getText().substring(0, perNight.getText().length() - 3)) + "");

	}

	@FXML
	private void buyCruiseOrder() {

		try {

			if (roomsComboBox.getValue() != null) {
				CruiseOrder co = new CruiseOrder(cruiseNum, Integer.valueOf(shipId.getText()), roomsComboBox.getValue(),
						ID);
				DataAccessObject.addCruiseOrder(co);
				Stage stage = (Stage) beds.getScene().getWindow();
				stage.close();
				Alerts.generateSuccessAlert("transaction success", "sailing was added successfully",
						" Check you phone for the receipt");
				personPageController.getInstance().fillOrdersData("");
				personPageController.getInstance().fillProfile();
				personPageController.getInstance().fillDashBoard();

				Person p = DataAccessObject.getPerson(ID);
				String phoneNumber = p.getPhoneNumber().substring(1);
				String smsBody = "Dear " + p.getFirstName() + ",\r\n" + "this is a " + paymentAmount.getText()
						+ "$ receipt\r\n"
						+ "Thank you for shopping with us. We appreciate your business. Nemo Ships goal is to provide pleasurable cruises. We hope to meet your expectations!\r\n"
						+ "cruise number " + co.getCruiseId() + "\r\n" + "departure date " + leavingDate.getText()
						+ "\r\n" + "Nemo Ships Crew";

				SmsGenerator.sendSms(phoneNumber, smsBody);

			} else {
				Alerts.generateWarningAlert("Missing Fields", "", "Select the room that you wish to order");

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	@FXML
	private void close(ActionEvent event) {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

}
