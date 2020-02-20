package controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Country;
import model.DataAccessObject;
import model.Port;
import model.SailTo;
import model.Sailing;

public class SailToController implements Initializable {

	@FXML
	private JFXTextField sailNumNonEditTextField;

	@FXML
	private JFXComboBox<String> countryComboBox;

	@FXML
	private JFXComboBox<String> portComboBox;

	@FXML
	private JFXDatePicker arrivalDatePicker;

	@FXML
	private JFXDatePicker leavingDatePicker;

	@FXML
	private Label sailingLabel;

	@FXML
	private TableView<SailTo> sailToTbl;

	@FXML
	private TableColumn<SailTo, Integer> counterCol;

	@FXML
	private TableColumn<SailTo, String> countryCol;

	@FXML
	private TableColumn<SailTo, String> portCol;

	@FXML
	private TableColumn<SailTo, Date> arrivalCol;

	@FXML
	private TableColumn<SailTo, Date> leavingCol;

	@FXML
	private TabPane sailToTabPane;

	private Sailing sailing;
	private int userType;

	public SailToController(Sailing sailing, int user) {
		this.sailing = sailing;
		userType = user;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sailToTbl.setFixedCellSize(40.0);
		if (userType == 2) {
			sailToTabPane.setPrefWidth(0);
			sailToTabPane.setMaxWidth(0);
			sailToTabPane.setMinWidth(0);
		}

		sailNumNonEditTextField.setText("Sail " + sailing.getNumber());
		sailingLabel.setText("Sailing " + sailing.getNumber() + " Route");
		fillTable("");
		fillCountryComboBox();

	}

	@FXML
	private void fillPorts() {
		if (countryComboBox.getValue() != null) {

			portComboBox.getItems().clear();
			try {
				for (Port p : DataAccessObject.getPorts()) {
					if (countryComboBox.getValue().equals(p.getCountry().getCountryName()))
						portComboBox.getItems().add(p.getPortName());
				}

			} catch (SQLException e) {
				Alerts.generateErrorAlert(e, "filling ComboBox", "could not fill Ports", "");
			}
		}

	}

	private void fillCountryComboBox() {

		countryComboBox.getItems().clear();
		try {
			for (Country c : DataAccessObject.getCountries()) {
				countryComboBox.getItems().add(c.getCountryName());

			}
		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "filling ComboBox", "could not fill countries", "");
		}
	}

	@FXML
	void addSailTo(ActionEvent event) {

		if (arrivalDatePicker.getValue() != null && leavingDatePicker.getValue() != null
				&& countryComboBox.getValue() != null && portComboBox.getValue() != null) {

			if (arrivalDatePicker.getValue().isBefore(leavingDatePicker.getValue())) {

				try {

					Date arDate = Date
							.from(arrivalDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
					Date leDate = Date
							.from(leavingDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
					SailTo sailTo = new SailTo(countryComboBox.getValue(), portComboBox.getValue(), sailing.getNumber(),
							arDate, leDate);
					DataAccessObject.addSailTo(sailTo);
					fillTable("");
					countryComboBox.setValue(null);
					portComboBox.setValue(null);
					arrivalDatePicker.setValue(null);
					leavingDatePicker.setValue(null);
					Alerts.generateSuccessAlert(
							"transaction success", sailTo.getPortName() + "-" + sailTo.getCountryName()
									+ " was added to sail " + sailTo.getSailingId() + " successfully",
							"data was added to dataBase");

					mainPageController.getInstance().fillDashBoard();

				} catch (SQLException e) {
					if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

						Alerts.generateErrorAlert(e, "Exception Has Occurred",
								"Sorry, we are unable to add the data . A duplicate value already exists",
								"sail can't visit the same port twice !");

					} else
						Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
								"Could not add -sailTo- to DataBase!");

				}
			} else {

				Alerts.generateWarningAlert("illegal dates", "illegal arrival or leaving date",
						"please make sure that leaving date is after arrival date");

			}

		} else {

			Alerts.generateWarningAlert("Missing Fields", "Please make sure all required fields are filled out",
					"missing fields");
		}

	}

	private void fillTable(String search) {

		ObservableList<SailTo> stData;
		stData = FXCollections.observableArrayList();
		try {

			for (SailTo st : DataAccessObject.getSailTo(sailing)) {

				stData.add(st);

			}

		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load SailTo data from DataBase!");

		}

		countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
		portCol.setCellValueFactory(new PropertyValueFactory<>("portName"));
		arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivaleDate"));
		leavingCol.setCellValueFactory(new PropertyValueFactory<>("leavingDate"));
		counterCol.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<Integer>(sailToTbl.getItems().indexOf(cellData.getValue()) + 1));
		sailToTbl.setItems(stData);

		if (userType == 1) {
			if (sailToTbl.getColumns().size() == 5) {
				TableColumn<SailTo, Void> deleteCol1 = new TableColumn<SailTo, Void>("Action");
				Callback<TableColumn<SailTo, Void>, TableCell<SailTo, Void>> cellFactory = new Callback<TableColumn<SailTo, Void>, TableCell<SailTo, Void>>() {
					@Override
					public TableCell<SailTo, Void> call(final TableColumn<SailTo, Void> param) {
						final TableCell<SailTo, Void> cell = new TableCell<SailTo, Void>() {

							private final JFXButton deleteBtn = new JFXButton();
							private final JFXButton updateBtn = new JFXButton();
							private final HBox pane = new HBox(updateBtn, deleteBtn);

							{
								deleteBtn.getStyleClass().add("deleteBtn");
								updateBtn.getStyleClass().add("updateBtn");

								updateBtn.setOnAction((ActionEvent event) -> {
									SailTo data = getTableView().getItems().get(getIndex());
									System.out.println("update : " + data);
								});

								deleteBtn.setOnAction((ActionEvent event) -> {

									SailTo data = getTableView().getItems().get(getIndex());
									try {

										DataAccessObject.deleteSailTo(data);
										fillTable("");
										Alerts.generateSuccessAlert("transaction success",
												"port/country was deleted successfully from sailing "
														+ data.getSailingId(),
												"data was deleted from dataBase");
										mainPageController.getInstance().fillDashBoard();

									} catch (SQLException e) {
										e.printStackTrace();

										if (e.getErrorCode() == 547) {

											Alerts.generateDeleteConflictionErrorAlert(e, "sailing route port");

										} else

											Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
													"data was not deleted from DataBase!");

									}

								});

							}

							@Override
							protected void updateItem(Void item, boolean empty) {
								super.updateItem(item, empty);

								setGraphic(empty ? null : pane);
							}
						};
						return cell;
					}
				};

				deleteCol1.setCellFactory(cellFactory);
				sailToTbl.getColumns().add(deleteCol1);

			}
		}

	}

}
