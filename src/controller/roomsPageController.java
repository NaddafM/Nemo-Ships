package controller;

import model.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

public class roomsPageController implements Initializable {

	@FXML
	private TableView<Room> roomsTable;

	@FXML
	private TableColumn<?, ?> roomIdColumn;

	@FXML
	private TableColumn<?, ?> bedsColumn;

	@FXML
	private TableColumn<?, ?> typeColumn;

	@FXML
	private TableColumn<?, ?> priceColumn;

	@FXML
	private Label shipIdLabel;

	private CruiseShip cruiseShip;

	private static roomsPageController single_instance;

	public static roomsPageController getInstance() {

		return single_instance;
	}

	public roomsPageController(CruiseShip cs) {
		this.cruiseShip = cs;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		single_instance = this;
		shipIdLabel.setText(cruiseShip.getName());
		fillRooms();

	}

	public void fillRooms() {

		ObservableList<Room> rooms;

		rooms = FXCollections.observableArrayList();

		try {

			for (Room r : DataAccessObject.getRooms(cruiseShip)) {
				rooms.add(r);
			}
		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred", "Could not load rooms from DataBase!");

		}

		roomIdColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
		bedsColumn.setCellValueFactory(new PropertyValueFactory<>("bedsAmount"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("pricePerNight"));
		roomsTable.setItems(rooms);

		if (roomsTable.getColumns().size() == 4) {

			TableColumn<Room, Void> deleteCol = new TableColumn<Room, Void>("Action");
			deleteCol.setPrefWidth(80);
			deleteCol.setMinWidth(80);
			deleteCol.setMaxWidth(80);

			Callback<TableColumn<Room, Void>, TableCell<Room, Void>> cellFactory = new Callback<TableColumn<Room, Void>, TableCell<Room, Void>>() {
				@Override
				public TableCell<Room, Void> call(final TableColumn<Room, Void> param) {
					final TableCell<Room, Void> cell = new TableCell<Room, Void>() {

						private final JFXButton deleteBtn = new JFXButton();
						private final JFXButton updateBtn = new JFXButton();
						private final HBox pane = new HBox(updateBtn, deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");
							updateBtn.getStyleClass().add("updateBtn");

							updateBtn.setOnAction((ActionEvent event) -> {
								Room data = getTableView().getItems().get(getIndex());

								System.out.println("update : " + data);

							});

							deleteBtn.setOnAction((ActionEvent event) -> {
								Room data = getTableView().getItems().get(getIndex());

								try {
									DataAccessObject.deleteRoom(data);
									roomsTable.getColumns().remove(deleteCol);
									fillRooms();

									Alerts.generateSuccessAlert("transaction completed",
											"Data was deleted successfully",
											"Room " + data.getRoomNumber() + " was deleted from the data Base ");
									mainPageController.getInstance().fillDashBoard();

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateErrorAlert(e, "deleting warning", "room was not deleted",
												"you can not delete this object since it is related to other data in the data base -  The DELETE statement conflicted with the REFERENCE constraint");

									} else
										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"Could not load rooms from DataBase!");

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
			roomsTable.getColumns().add(deleteCol);
		}
	}

	@FXML
	private void openAddRoomPage() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addRoom.fxml"));
			AddRoomController arc = new AddRoomController(cruiseShip);
			fxmlLoader.setController(arc);
			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Add Room");
			stage.getIcons().add(new Image(Main.class.getResourceAsStream("/style/pics/add.png")));

			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
