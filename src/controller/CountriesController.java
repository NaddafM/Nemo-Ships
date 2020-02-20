package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
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

public class CountriesController implements Initializable {

	@FXML
	private JFXTextField newCountryName;

	@FXML
	private TableView<Country> countriesTable;

	@FXML
	private TableColumn<Country, String> countryNameColumn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		fillCountriesTable();
		editCountryTable();

		newCountryName.textProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue.matches("\\sa-zA-Z*")) {
				newCountryName.setText(newValue.replaceAll("[^\\sa-zA-Z]", ""));
			}
		});

	}

	@FXML
	void addCountry() {

		if (!newCountryName.getText().equals("")) {

			try {

				Country newCountry = new Country(newCountryName.getText());
				DataAccessObject.addCountry(newCountry);
				fillCountriesTable();
				newCountryName.setText("");
				Alerts.generateSuccessAlert("transaction completed", "data was added successfully",
						newCountry.getCountryName() + " was added to data base");

				mainPageController.getInstance().fillDashBoard();

			} catch (SQLException e) {
				if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

					Alerts.generateErrorAlert(e, "Exception Has Occurred",
							"Sorry, we are unable to add this country . A duplicate value already exists",
							"Could not add country to DataBase!");

				} else
					Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
							"Could not add country to DataBase!");
			}
		} else {

			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");

		}

	}

	private void fillCountriesTable() {

		ObservableList<Country> countriesData;

		countriesData = FXCollections.observableArrayList();

		try {

			for (Country c : DataAccessObject.getCountries()) {

				countriesData.add(c);

			}
		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load countries from DataBase!");

			e.printStackTrace();
		}
		countryNameColumn.setCellValueFactory(new PropertyValueFactory<>("countryName"));
		countriesTable.setItems(countriesData);

		if (countriesTable.getColumns().size() == 1) {

			TableColumn<Country, Void> deleteCol = new TableColumn<Country, Void>("");

			Callback<TableColumn<Country, Void>, TableCell<Country, Void>> cellFactory = new Callback<TableColumn<Country, Void>, TableCell<Country, Void>>() {
				@Override
				public TableCell<Country, Void> call(final TableColumn<Country, Void> param) {
					final TableCell<Country, Void> cell = new TableCell<Country, Void>() {

						private final JFXButton deleteBtn = new JFXButton();
						private final HBox pane = new HBox(deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");

							deleteBtn.setOnAction((ActionEvent event) -> {
								Country data = getTableView().getItems().get(getIndex());

								try {

									DataAccessObject.deleteCountry(data);
									countriesTable.getColumns().remove(deleteCol);
									fillCountriesTable();

									Alerts.generateSuccessAlert("transaction competed", "data was deleted successfully",
											data.getCountryName() + " was deleted from the data Base");
									mainPageController.getInstance().fillDashBoard();

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateErrorAlert(e, "deleting warning", "country was not deleted",
												"you can not delete this object since it is related to other data in the data base -  The DELETE statement conflicted with the REFERENCE constraint");

									} else
										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"Could not load countries from DataBase!");

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
			countriesTable.getColumns().add(deleteCol);

		}

	}

	private void editCountryTable() {

//		countryNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
//
//		countryNameColumn.setOnEditCommit(e -> {
//
//			Country c = e.getRowValue();
//
//			boolean allLetters = e.getNewValue().chars().allMatch(Character::isLetter);
//			if (allLetters) {
//				System.out.println("yes");
//
//				try {
//					
//					DataAccessObject.updateCountry(new Country(e.getNewValue()));
//					Alerts.generateSuccessAlert("transaction completed", "data was updated successfully",
//							c.getCountryName() + " was updated to "+e.getNewValue());
//					
//					
//				} catch (SQLException e1) {
//					e.getTableView().getItems().set(e.getTablePosition().getRow(), c);
//
//					if (e1.getErrorCode() == 2627 || e1.getErrorCode() == 2601) {
//
//						Alerts.generateErrorAlert(e1, "Exception Has Occurred",
//								"Sorry, we are unable to update this country . A duplicate value already exists",
//								"data was not updated!");
//
//					} else
//						Alerts.generateErrorAlert(e1, "Exception", "Exception Has Occurred",
//								"Could not update country!");
//
//				}
//
//			} else {
//				e.getTableView().getItems().set(e.getTablePosition().getRow(), c);
//				System.out.println("no");
//
//			}
//
//		});
//
//		countriesTable.setEditable(true);

	}

}
