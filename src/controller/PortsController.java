package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import model.Country;
import model.DataAccessObject;
import model.Port;

public class PortsController implements Initializable {

	@FXML
	private JFXComboBox<String> countryName;

	@FXML
	private JFXTextField newPortName;

	@FXML
	private TableView<Port> portsTable;

	@FXML
	private TableColumn<Port, Country> countryNameColumn;

	@FXML
	private TableColumn<Port, String> portNameColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		fillPortsTable();

		try {
			for (Country c : DataAccessObject.getCountries())
				countryName.getItems().add(c.getCountryName());

			countryName.getSelectionModel().select(1);

		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load countries from DataBase!");

		}

		newPortName.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\sa-zA-Z*")) {
				newPortName.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
			}
		});

	}

	private void fillPortsTable() {

		ObservableList<Port> portsData;

		portsData = FXCollections.observableArrayList();

		try {

			for (Port p : DataAccessObject.getPorts()) {
				portsData.add(p);

			}
		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred", "Could not load ports from DataBase!");

		}
		countryNameColumn.setCellValueFactory(new PropertyValueFactory<Port, Country>("country"));
		countryNameColumn.setCellFactory(new Callback<TableColumn<Port, Country>, TableCell<Port, Country>>() {

			@Override
			public TableCell<Port, Country> call(TableColumn<Port, Country> param) {

				TableCell<Port, Country> countryName = new TableCell<Port, Country>() {

					@Override
					protected void updateItem(Country item, boolean empty) {
						if (item != null) {
							javafx.scene.control.Label countryLabel = new javafx.scene.control.Label(
									item.getCountryName());
							setGraphic(countryLabel);
						}
					}
				};

				return countryName;
			}

		});
		portNameColumn.setCellValueFactory(new PropertyValueFactory<>("portName"));
		portsTable.setItems(portsData);

		if (portsTable.getColumns().size() == 2) {

			TableColumn<Port, Void> deleteCol = new TableColumn<Port, Void>("");

			Callback<TableColumn<Port, Void>, TableCell<Port, Void>> cellFactory = new Callback<TableColumn<Port, Void>, TableCell<Port, Void>>() {
				@Override
				public TableCell<Port, Void> call(final TableColumn<Port, Void> param) {
					final TableCell<Port, Void> cell = new TableCell<Port, Void>() {

						private final JFXButton deleteBtn = new JFXButton();
						private final HBox pane = new HBox(deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");

							deleteBtn.setOnAction((ActionEvent event) -> {
								Port data = getTableView().getItems().get(getIndex());

								try {

									DataAccessObject.deletePort(data);
									portsTable.getColumns().remove(deleteCol);
									fillPortsTable();

									Alerts.generateSuccessAlert("transaction competed", "data was deleted successfully",
											data.getPortName() + " was deleted from the data Base");
									mainPageController.getInstance().fillDashBoard();

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateErrorAlert(e, "deleting warning", "Port was not deleted",
												"you can not delete this object since it is related to other data in the data base -  The DELETE statement conflicted with the REFERENCE constraint");

									} else
										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"data was not deleted");

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

			deleteCol.setCellFactory(cellFactory);
			portsTable.getColumns().add(deleteCol);

		}

	}

	@FXML
	private void addNewPort() {

		if (!newPortName.getText().equals("")) {

			try {
				Port newPort = new Port(new Country(countryName.getValue()), newPortName.getText());
				DataAccessObject.addPort(newPort);
				fillPortsTable();
				newPortName.setText("");
				countryName.getSelectionModel().select(1);
				Alerts.generateSuccessAlert("transaction completed", "data was added successfully",
						"port " + newPort.getPortName() + " was added to data base");
				mainPageController.getInstance().fillDashBoard();

			} catch (SQLException e) {
				if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

					Alerts.generateErrorAlert(e, "Exception Has Occurred",
							"Sorry, we are unable to add this port . A duplicate value already exists",
							"port was not added to DataBase!");

				} else
					Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
							"Could not add port to DataBase!");
			}

		} else {

			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");

		}

	}

}