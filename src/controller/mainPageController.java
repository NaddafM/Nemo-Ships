package controller;

import model.*;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class mainPageController implements Initializable {
	private double xOffset = 0;
	private double yOffset = 0;
	@FXML
	private JFXComboBox<Integer> sailingComboBox;
	@FXML
	private JFXComboBox<Integer> shipsComboBox;
	@FXML
	private TextField personSearchText;
	@FXML
	private TextField txtTst;
	@FXML
	private PieChart pieChart;
	@FXML
	private BarChart<String, Integer> barChart;
	@FXML
	private NumberAxis profit;
	@FXML
	private CategoryAxis shipNumber;
	@FXML
	private DatePicker bDateMin;
	@FXML
	private DatePicker bDateMax;
	@FXML
	private TableView<CruiseShip> cruiseShipsTable;
	@FXML
	private TableColumn<CruiseShip, Integer> cruiseShipCounterColumn;
	@FXML
	private TableColumn<CruiseShip, String> cruiseShipIDColumn;
	@FXML
	private TableColumn<CruiseShip, String> cruiseShipName;
	@FXML
	private TableColumn<CruiseShip, Date> manufacturingDateColumn;
	@FXML
	private TableColumn<CruiseShip, Integer> maxCapacityColumn;
	@FXML
	private TableColumn<CruiseShip, Integer> maxNumberOfPeopleColumn;
	@FXML
	private TableView<Person> personsTable;
	@FXML
	private TableColumn<Person, Integer> personCounterColumn;
	@FXML
	private TableColumn<Person, ImageView> vipCol;
	@FXML
	private TableColumn<Person, String> personIDColumn;
	@FXML
	private TableColumn<Person, String> personFirstNameColumn;
	@FXML
	private TableColumn<Person, String> personLastNameColumn;
	@FXML
	private TableColumn<Person, Date> personBirthDateColumn;
	@FXML
	private TableColumn<Person, String> personEmailColumn;
	@FXML
	private TableColumn<Person, String> personPhoneNumberColumn;
	@FXML
	private TableView<CruiseOrder> coTable;
	@FXML
	private TableColumn<CruiseOrder, Integer> co_Counter;
	@FXML
	private TableColumn<CruiseOrder, Integer> co_CruiseNumber;
	@FXML
	private TableColumn<CruiseOrder, Integer> co_CruiseShip;
	@FXML
	private TableColumn<CruiseOrder, Integer> co_RoomNumber;
	@FXML
	private TableColumn<CruiseOrder, String> co_PersonId;
	@FXML
	private TableView<Sailing> sailingTbl;
	@FXML
	private TableColumn<Sailing, Integer> sailingCounterColumn;
	@FXML
	private TableColumn<Sailing, Integer> sailingNumberColumn;
	@FXML
	private TableColumn<Sailing, Date> sailingLeavingColumn;
	@FXML
	private TableColumn<Sailing, Date> sailingReturnColumn;
	@FXML
	private TableView<Cruise> cruiseTbl;
	@FXML
	private TableColumn<Cruise, Integer> cruiseCounter;
	@FXML
	private TableColumn<Cruise, Integer> cruiseCruiseNumberColumn;
	@FXML
	private TableColumn<Cruise, Integer> cruiseCruiseShipColumn;
	@FXML
	private JFXTextField newSailingId;
	@FXML
	private JFXDatePicker newSailingLeavingDate;
	@FXML
	private JFXTimePicker newSailingLeavingTime;
	@FXML
	private JFXDatePicker newSailingReturnDate;
	@FXML
	private JFXTimePicker newSailingReturnTime;
	@FXML
	private Tab dashBoardTab;
	@FXML
	private Tab shipsTab;
	@FXML
	private Tab peopleTab;
	@FXML
	private Tab cruiseTab;
	@FXML
	private Tab ordersTab;
	@FXML
	private BarChart<String, Integer> availbleRoomsBySailingChart;
	@FXML
	private AreaChart<String, Number> profitChart;
	@FXML
	private Label totalProfit;
	@FXML
	private Label cruiseShipsNumber;
	@FXML
	private Label totalOrders;
	@FXML
	private Label usersNumber;

	private static mainPageController single_instance = null;
	private static String PersonearchText;
	private static LocalDate searchMinDate;
	private static LocalDate searchMaxDate;

	public static mainPageController getInstance() {
		if (single_instance == null)
			single_instance = new mainPageController();

		return single_instance;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

		logInController.getInstance().close();
		single_instance = this;
		initToolTips();
		cruiseShipsTable.setFixedCellSize(40.0);
		personsTable.setFixedCellSize(40.0);
		coTable.setFixedCellSize(40.0);
		cruiseTbl.setFixedCellSize(40.0);
		sailingTbl.setFixedCellSize(40.0);
		putRistrictions();
		resetPersonFilters();
		personSearchText.setText("");
		fillCruiseShipTable("");
		fillPersonsTable();
		fillCruiseOrdersTable("");
		fillSailingTable("");
		fillCruisesTable("");
		fillSailingComboBox();
		fillShipsComboBox();
		fillDashBoard();


	}

	public void fillDashBoard() {

		pieChart.getData().clear();
		barChart.getData().clear();
		profitChart.getData().clear();
		availbleRoomsBySailingChart.getData().clear();

		fillPieChart();
		fillBarChart();
		fillProfitChart();
		fillavailbleRoomsBySailin();

		try {
			int sum = 0;
			ResultSet rs = DataAccessObject.getProfitPerYear();
			while (rs.next()) {
				sum += rs.getInt(2);

			}
			totalProfit.setText(sum + " $");
		} catch (Exception e) {

			Alerts.generateErrorAlert(e, "Exception", "exception while filling dashboard", "");
		}

		try {

			totalOrders.setText(DataAccessObject.getTotalOrdersAmount() + "");
			cruiseShipsNumber.setText(DataAccessObject.getShipsAmount() + "");
			usersNumber.setText(DataAccessObject.getUsersAmount() + "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initToolTips() {
		Tooltip dashBoardtt = new Tooltip();
		dashBoardtt.setText("dashBoard");
		dashBoardTab.setTooltip(dashBoardtt);

		Tooltip shiptt = new Tooltip();
		shiptt.setText("cruise ships");
		shipsTab.setTooltip(shiptt);

		Tooltip peoplett = new Tooltip();
		peoplett.setText("people");
		peopleTab.setTooltip(peoplett);

		Tooltip cruisett = new Tooltip();
		cruisett.setText("cruise");
		cruiseTab.setTooltip(cruisett);

		Tooltip orderstt = new Tooltip();
		orderstt.setText("orders");
		ordersTab.setTooltip(orderstt);
	}

	private void fillCruisesTable(String search) {

		ObservableList<Cruise> CruisesData;
		CruisesData = FXCollections.observableArrayList();

		try {

			for (Cruise c : DataAccessObject.getCruises()) {
				CruisesData.add(c);
			}

		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load cruises from DataBase!");
		}

		cruiseCruiseNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		cruiseCruiseShipColumn.setCellValueFactory(new PropertyValueFactory<>("shipNumber"));
		cruiseCounter.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<>(cruiseTbl.getItems().indexOf(cellData.getValue()) + 1));
		cruiseTbl.setItems(CruisesData);

		if (cruiseTbl.getColumns().size() == 3) {

			TableColumn<Cruise, Void> deleteCol = new TableColumn<>("Action");
			deleteCol.setPrefWidth(90);
			deleteCol.setMinWidth(90);
			deleteCol.setMaxWidth(90);

			Callback<TableColumn<Cruise, Void>, TableCell<Cruise, Void>> cellFactory = new Callback<TableColumn<Cruise, Void>, TableCell<Cruise, Void>>() {
				@Override
				public TableCell<Cruise, Void> call(final TableColumn<Cruise, Void> param) {
					final TableCell<Cruise, Void> cell = new TableCell<Cruise, Void>() {

						private final JFXButton deleteBtn = new JFXButton();
						private final JFXButton updateBtn = new JFXButton();
						private final HBox pane = new HBox(updateBtn, deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");
							updateBtn.getStyleClass().add("updateBtn");

							updateBtn.setOnAction((ActionEvent event) -> {

								Cruise data = getTableView().getItems().get(getIndex());
								System.out.println("update : " + data);

							});

							deleteBtn.setOnAction((ActionEvent event) -> {
								Cruise data = getTableView().getItems().get(getIndex());

								try {

									DataAccessObject.deleteCruise(data);
									fillCruisesTable("");
									fillDashBoard();
									Alerts.generateSuccessAlert("transaction success",
											"Cruise was deleted successfully",
											"Cruise : " + data.getNumber() + " was deleted from the data base");

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateDeleteConflictionErrorAlert(e, "Cruise");

									} else

										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"Could not delete cruise from DataBase!");

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
			cruiseTbl.getColumns().add(deleteCol);

		}

	}

	public void fillCruiseOrdersTable(String search) {
		ObservableList<CruiseOrder> co_data;

		co_data = FXCollections.observableArrayList();

		try {

			for (CruiseOrder co : DataAccessObject.getCruiseOrders()) {
				co_data.add(co);

			}

		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load cruise orders from DataBase!");

		}

		co_CruiseNumber.setCellValueFactory(new PropertyValueFactory<>("cruiseId"));
		co_CruiseShip.setCellValueFactory(new PropertyValueFactory<>("cruiseShipId"));
		co_RoomNumber.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
		co_PersonId.setCellValueFactory(new PropertyValueFactory<>("personId"));
		co_Counter.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<>(coTable.getItems().indexOf(cellData.getValue()) + 1));

		coTable.setItems(co_data);

		if (coTable.getColumns().size() == 5) {

			TableColumn<CruiseOrder, Void> deleteCol = new TableColumn<>("Action");
			deleteCol.setPrefWidth(90);
			deleteCol.setMinWidth(90);
			deleteCol.setMaxWidth(90);

			Callback<TableColumn<CruiseOrder, Void>, TableCell<CruiseOrder, Void>> cellFactory = new Callback<TableColumn<CruiseOrder, Void>, TableCell<CruiseOrder, Void>>() {
				@Override
				public TableCell<CruiseOrder, Void> call(final TableColumn<CruiseOrder, Void> param) {
					final TableCell<CruiseOrder, Void> cell = new TableCell<CruiseOrder, Void>() {

						private final JFXButton deleteBtn = new JFXButton();
						private final JFXButton updateBtn = new JFXButton();

						private final HBox pane = new HBox(updateBtn, deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");
							updateBtn.getStyleClass().add("updateBtn");
							updateBtn.setOnAction((ActionEvent event) -> {

								CruiseOrder data = getTableView().getItems().get(getIndex());
								System.out.println("update : " + data);

							});

							deleteBtn.setOnAction((ActionEvent event) -> {
								CruiseOrder data = getTableView().getItems().get(getIndex());

								try {
									DataAccessObject.deleteCruiseOrder(data);
									fillCruiseOrdersTable("");
									fillPersonsTable();
									fillDashBoard();

									Alerts.generateSuccessAlert("transaction success",
											"CruiseOrder was deleted successfully", "data was deleted from dataBase");

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateDeleteConflictionErrorAlert(e, "cruise order");

									} else

										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"Could not load cruiseOrders from DataBase!");

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
			coTable.getColumns().add(deleteCol);

		}

	}

	private void fillSailingTable(String searchText) {

		ObservableList<Sailing> sailingData;
		sailingData = FXCollections.observableArrayList();
		try {

			for (Sailing s : DataAccessObject.getSailings()) {

				sailingData.add(s);

			}

		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load sailings data from DataBase!");

		}

		sailingNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		sailingLeavingColumn.setCellValueFactory(new PropertyValueFactory<>("departureDateAndTime"));
		sailingReturnColumn.setCellValueFactory(new PropertyValueFactory<>("arrivaldateAndTime"));
		sailingCounterColumn.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<>(sailingTbl.getItems().indexOf(cellData.getValue()) + 1));

		sailingTbl.setItems(sailingData);

		if (sailingTbl.getColumns().size() == 4) {
			TableColumn<Sailing, Void> deleteCol1 = new TableColumn<>("Action");
			deleteCol1.setPrefWidth(100);
			deleteCol1.setMinWidth(100);
			deleteCol1.setMaxWidth(100);

			Callback<TableColumn<Sailing, Void>, TableCell<Sailing, Void>> cellFactory = new Callback<TableColumn<Sailing, Void>, TableCell<Sailing, Void>>() {
				@Override
				public TableCell<Sailing, Void> call(final TableColumn<Sailing, Void> param) {
					final TableCell<Sailing, Void> cell = new TableCell<Sailing, Void>() {
						private final JFXButton deleteBtn = new JFXButton();
						private final JFXButton updateBtn = new JFXButton();
						private final JFXButton map = new JFXButton();
						private final HBox pane = new HBox(updateBtn, map, deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");
							updateBtn.getStyleClass().add("updateBtn");
							map.getStyleClass().add("maps");

							updateBtn.setOnAction((ActionEvent event) -> {
								Sailing data = getTableView().getItems().get(getIndex());
								System.out.println("update : " + data);
							});

							map.setOnAction((ActionEvent event) -> {

								Sailing data = getTableView().getItems().get(getIndex());
								try {
									FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SailTo.fxml"));
									SailToController stc = new SailToController(data, 1);
									fxmlLoader.setController(stc);
									Parent root = (Parent) fxmlLoader.load();
									Stage stage = new Stage();
									stage.initModality(Modality.APPLICATION_MODAL);
									stage.setTitle("Sailing To");
									stage.setScene(new Scene(root));
									stage.getIcons()
											.add(new Image(Main.class.getResourceAsStream("/style/pics/mp.png")));
									stage.show();

								} catch (Exception e) {

									Alerts.generateErrorAlert(e, "FXML Error", "could not open SailTo Page", "");

								}

							});

							deleteBtn.setOnAction((ActionEvent event) -> {

								Sailing data = getTableView().getItems().get(getIndex());

								try {

									DataAccessObject.deleteSailing(data);
									fillSailingTable("");
									fillSailingComboBox();
									fillDashBoard();
									Alerts.generateSuccessAlert("transaction success",
											"sailing was deleted successfully", "sailing : " + data.getNumber()
													+ " was deleted successfully from the data base");

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateDeleteConflictionErrorAlert(e, "sailing");

									} else

										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"Could not load sailing from DataBase!");

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
			sailingTbl.getColumns().add(deleteCol1);

		}

	}

	@FXML
	public void resetPersonFilters() {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		String date1 = "16/08/1960";
		searchMinDate = LocalDate.parse(date1, formatter);
		searchMaxDate = LocalDate.now();
		PersonearchText = "";
		personSearchText.setText("");
		fillPersonsTable();
		bDateMin.setValue(searchMinDate);
		bDateMax.setValue(searchMaxDate);
	}

	public static Date asDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}

	public void fillPersonsTable() {

		personsTable.getColumns().get(1).setVisible(false);
		personsTable.getColumns().get(1).setVisible(true);

		ObservableList<Person> personsData;

		personsData = FXCollections.observableArrayList();

		try {

			for (Person p : DataAccessObject.getPersons()) {
				if ((p.getBirthDate().before(asDate(searchMaxDate)))
						&& (p.getBirthDate().after(asDate(searchMinDate)))) {

					if (PersonearchText.equals(""))
						personsData.add(p);
					else if (p.getFirstName().contains(PersonearchText) || p.getLastName().contains(PersonearchText)
							|| String.valueOf(p.getId()).contains(PersonearchText))
						personsData.add(p);

				}

			}
		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load persons from DataBase!");

		}

		personIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		personFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		personLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		personBirthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		personPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
		personEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		vipCol.setCellValueFactory(new PropertyValueFactory<>("vip"));
		personCounterColumn.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<>(personsTable.getItems().indexOf(cellData.getValue()) + 1));

		personsTable.setItems(personsData);

		if (personsTable.getColumns().size() == 8) {

			TableColumn<Person, Void> deleteCol = new TableColumn<>("Action");
			deleteCol.setPrefWidth(1000);
			deleteCol.setMinWidth(100);
			deleteCol.setMaxWidth(100);

			Callback<TableColumn<Person, Void>, TableCell<Person, Void>> cellFactory = new Callback<TableColumn<Person, Void>, TableCell<Person, Void>>() {
				@Override
				public TableCell<Person, Void> call(final TableColumn<Person, Void> param) {
					final TableCell<Person, Void> cell = new TableCell<Person, Void>() {

						private final JFXButton deleteBtn = new JFXButton();
						private final JFXButton updateBtn = new JFXButton();

						private final HBox pane = new HBox(updateBtn, deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");
							updateBtn.getStyleClass().add("updateBtn");

							updateBtn.setOnAction((ActionEvent event) -> {

								Person data = getTableView().getItems().get(getIndex());

								try {
									FXMLLoader fxmlLoader = new FXMLLoader(
											getClass().getResource("/view/UpdatePerson.fxml"));
									updatePersonController upc = new updatePersonController(data, 1);
									fxmlLoader.setController(upc);

									Parent root = (Parent) fxmlLoader.load();
									Stage stage = new Stage();
									stage.initModality(Modality.APPLICATION_MODAL);
									stage.setTitle("Edit Person");
									stage.setScene(new Scene(root));
									stage.getIcons().add(
											new Image(Main.class.getResourceAsStream("/style/pics/Edit_20px.png")));
									stage.show();

								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("can not open edit page");
								}

							});

							deleteBtn.setOnAction((ActionEvent event) -> {
								Person data = getTableView().getItems().get(getIndex());

								try {
									DataAccessObject.deletePerson(data);
									resetPersonFilters();
									fillPersonsTable();
									fillDashBoard();

									Alerts.generateSuccessAlert("transaction success",
											"person was deleted successfully",
											data.getFirstName() + " " + data.getLastName()
													+ " was deleted successfully from the data base");

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateDeleteConflictionErrorAlert(e, "person");

									} else

										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"Could not load persons from DataBase!");

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
			personsTable.getColumns().add(deleteCol);

		}

	}

	public void fillCruiseShipTable(String search) {

		ObservableList<CruiseShip> cruiseShipsData;

		cruiseShipsData = FXCollections.observableArrayList();

		try {

			for (CruiseShip cs : DataAccessObject.getAllCruiseShips()) {
				if (search.equals(""))
					cruiseShipsData.add(cs);
				else if (cs.getName().contains(search) || String.valueOf(cs.getNumber()).contains(search))
					cruiseShipsData.add(cs);

			}
		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred", "Could not load ships from DataBase!");

		}

		cruiseShipIDColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		cruiseShipName.setCellValueFactory(new PropertyValueFactory<>("name"));
		manufacturingDateColumn.setCellValueFactory(new PropertyValueFactory<>("ManufacturingDate"));
		maxCapacityColumn.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
		maxNumberOfPeopleColumn.setCellValueFactory(new PropertyValueFactory<>("maxNumberOfPeople"));
		cruiseShipsTable.setItems(cruiseShipsData);

		cruiseShipCounterColumn.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<>(cruiseShipsTable.getItems().indexOf(cellData.getValue()) + 1));

		if (cruiseShipsTable.getColumns().size() == 6) {

			TableColumn<CruiseShip, Void> deleteCol = new TableColumn<>("Action");
			deleteCol.setPrefWidth(100);
			deleteCol.setMinWidth(100);
			deleteCol.setMaxWidth(100);

			Callback<TableColumn<CruiseShip, Void>, TableCell<CruiseShip, Void>> cellFactory = new Callback<TableColumn<CruiseShip, Void>, TableCell<CruiseShip, Void>>() {
				@Override
				public TableCell<CruiseShip, Void> call(final TableColumn<CruiseShip, Void> param) {
					final TableCell<CruiseShip, Void> cell = new TableCell<CruiseShip, Void>() {

						private final JFXButton deleteBtn = new JFXButton();
						private final JFXButton updateBtn = new JFXButton();
						private final JFXButton viewRoomsBtn = new JFXButton();

						private final HBox pane = new HBox(viewRoomsBtn, updateBtn, deleteBtn);

						{
							deleteBtn.getStyleClass().add("deleteBtn");
							updateBtn.getStyleClass().add("updateBtn");
							viewRoomsBtn.getStyleClass().add("view");

							updateBtn.setOnAction((ActionEvent event) -> {

								CruiseShip data = getTableView().getItems().get(getIndex());

								try {
									FXMLLoader fxmlLoader = new FXMLLoader(
											getClass().getResource("/view/UpdateCruiseShip.fxml"));
									updateCruiseShipController ucsc = new updateCruiseShipController(data);
									fxmlLoader.setController(ucsc);
									Parent root = (Parent) fxmlLoader.load();
									Stage stage = new Stage();
									stage.initModality(Modality.APPLICATION_MODAL);
									stage.setTitle("Edit CruiseShip");
									stage.setScene(new Scene(root));
									stage.getIcons().add(
											new Image(Main.class.getResourceAsStream("/style/pics/Edit_20px.png")));
									stage.show();

								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("can not open edit page");
								}
							});

							viewRoomsBtn.setOnAction((ActionEvent event) -> {
								CruiseShip data = getTableView().getItems().get(getIndex());

								try {
									FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/rooms.fxml"));
									roomsPageController rpc = new roomsPageController(data);
									fxmlLoader.setController(rpc);
									Parent root = (Parent) fxmlLoader.load();
									Stage stage = new Stage();
									stage.initModality(Modality.APPLICATION_MODAL);
									stage.setTitle("view rooms");
									stage.setScene(new Scene(root));
									stage.getIcons()
											.add(new Image(Main.class.getResourceAsStream("/style/pics/show2.png")));
									stage.show();

								} catch (Exception e) {
									System.out.println("can not open rooms page");
								}

							});

							deleteBtn.setOnAction((ActionEvent event) -> {
								CruiseShip data = getTableView().getItems().get(getIndex());

								try {
									DataAccessObject.deleteCruiseShip(data);
									fillCruiseShipTable("");
									fillShipsComboBox();
									fillDashBoard();

									Alerts.generateSuccessAlert("transaction competed", "data was deleted successfully",
											"Cruise Ship  " + data.getNumber() + " was deleted from the data Base");

								} catch (SQLException e) {

									if (e.getErrorCode() == 547) {

										Alerts.generateErrorAlert(e, "deleting warning", "cruise ship was not deleted",
												"you can not delete this object since it is related to other data in the data base -  The DELETE statement conflicted with the REFERENCE constraint");

									} else
										Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
												"Could not load ships from DataBase!");

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
			cruiseShipsTable.getColumns().add(deleteCol);

		}

		editCruiseShipTableCols();

	}

	private void editCruiseShipTableCols() {

//		cruiseShipName.setCellFactory(TextFieldTableCell.fo);

		// cruiseShipName.setCellFactory(TextFieldTableCell.forTableColumn());
//		cruiseShipName.setOnEditCommit(e -> {
//
//			try {
//
//				if (e.getNewValue().length() == 0)
//					throw new Exception("1");
//
//				else {
//					DataAccessObject.updateShip(
//							e.getTableView().getItems().get(e.getTablePosition().getRow()).getNumber(), "shipName",
//							e.getNewValue());
//					e.getTableView().getItems().get(e.getTablePosition().getRow()).setName(e.getNewValue());
//				}
//
//			}
//
//			catch (Exception e1) {
//
//				if (e1.getMessage().contentEquals("1")) {
//					Alerts.generateErrorAlert(e1, "input Exception", "value was not edited", "fields can't be empty");
//					e.getTableView().getItems().set(e.getTablePosition().getRow(),
//							e.getTableView().getItems().get(e.getTablePosition().getRow()));
//
//				} else {
//					Alerts.generateErrorAlert(e1, "SQL Exception", "value was not edited",
//							"make sure that you are using legal input");
//
//				}
//
//			}
//
//		}
//
//		);
//
//		maxCapacityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//		maxCapacityColumn.setOnEditCommit(e -> {
//
//			DataAccessObject.updateShip(e.getTableView().getItems().get(e.getTablePosition().getRow()).getNumber(),
//					"maxCapacity", e.getNewValue());
//
//			e.getTableView().getItems().get(e.getTablePosition().getRow()).setMaxCapacity(e.getNewValue());
//
//		});
//
//		maxNumberOfPeopleColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
//		maxNumberOfPeopleColumn.setOnEditCommit(e -> {
//
//			DataAccessObject.updateShip(e.getTableView().getItems().get(e.getTablePosition().getRow()).getNumber(),
//					"maxNumberOfPeople", e.getNewValue());
//
//			e.getTableView().getItems().get(e.getTablePosition().getRow()).setMaxNumberOfPeople(e.getNewValue());
//
//		});

		cruiseShipsTable.setEditable(true);

	}

	public static boolean isNumeric(String str) {

		try {
			Double.parseDouble(str);
			return true;

		} catch (NumberFormatException e) {

			return false;
		}
	}

	@FXML
	private void close(Event event) {
		((Node) (event.getSource())).getScene().getWindow().hide();
	}

	@FXML
	private void openAddShipPage(ActionEvent event) {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addShip.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("ports");
			stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/add.png")));
			stage.show();
		} catch (IOException e) {
			System.out.println("could not open the requested page");

		}

	}

	@FXML
	private void openCountries() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/Countries.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Countries");
			stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/glob.png")));
			stage.show();
		} catch (IOException e) {
			System.out.println("could not open the requested page");

		}

	}

	@FXML
	private void openPorts() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/ports.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("ports");
			stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/mapsPin.png")));
			stage.show();
		} catch (IOException e) {
			System.out.println("could not open the requested page");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void openAddCruiseOrder() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addCruiseOrder.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Add Cruise Order");
			stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/add.png")));
			stage.show();
		} catch (IOException e) {
			System.out.println("could not open the requested page");
			e.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@FXML
	private void openAddPerson(ActionEvent event) {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addPerson.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("add new person");
			stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/add.png")));
			stage.show();
		} catch (IOException e) {
			System.out.println("could not open the requested page");
			e.printStackTrace();

		}

	}

	@FXML
	private void search(KeyEvent event) {

		fillCruiseShipTable(txtTst.getText());

	}

	@FXML
	private void personSearch() {

		PersonearchText = personSearchText.getText();

		fillPersonsTable();

	}

	@FXML
	private void minDateSearch() {

		searchMinDate = bDateMin.getValue();
		fillPersonsTable();

	}

	@FXML
	private void maxDateSearch() {

		searchMaxDate = bDateMax.getValue();
		fillPersonsTable();

	}

	@SuppressWarnings("unchecked")
	private void fillProfitChart() {

		try {
			ResultSet rs = DataAccessObject.getProfitPerYear();
			Series<String, Number> set = new XYChart.Series<>();

			while (rs.next()) {

				set.getData().add(new XYChart.Data<String, Number>(rs.getString(1), rs.getInt(2)));

			}
			profitChart.getData().addAll(set);

		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "filling charts error", "bar chart was not filled", "failure");

		}

	}

	private void fillPieChart() {

		ResultSet rs;
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

		try {
			rs = DataAccessObject.getOrdersCountByDistination();

			while (rs.next())
				pieChartData.add(
						new PieChart.Data(rs.getString(1) + " " + rs.getString(2) + " " + rs.getInt(3), rs.getInt(3)));

		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "filling charts error", "pie chart was not filled", "failure");

		}

		pieChart.setData(pieChartData);

	}

	@SuppressWarnings("unchecked")
	private void fillBarChart() {

		try {
			ResultSet rs = DataAccessObject.getPopularDestinations();
			Series<String, Integer> set = new XYChart.Series<>();

			while (rs.next()) {

				set.getData().add(new XYChart.Data<String, Integer>(
						rs.getInt(1) + "\n" + rs.getString(2) + "\n" + rs.getString(3), rs.getInt(4)));

			}
			barChart.getData().addAll(set);

		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "filling charts error", "bar chart was not filled", "failure");

		}

	}

	@FXML
	private void addNewSailing() {

		if (!newSailingId.getText().equals("") && newSailingLeavingDate.getValue() != null
				&& newSailingLeavingTime.getValue() != null && newSailingReturnDate.getValue() != null
				&& newSailingReturnTime.getValue() != null) {

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.MONTH, newSailingLeavingDate.getValue().getMonthValue() - 1);
			cal.set(Calendar.DATE, newSailingLeavingDate.getValue().getDayOfMonth());
			cal.set(Calendar.YEAR, newSailingLeavingDate.getValue().getYear());
			cal.set(Calendar.HOUR_OF_DAY, newSailingLeavingTime.getValue().getHour());
			cal.set(Calendar.MINUTE, newSailingLeavingTime.getValue().getMinute());
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date leavingDateTime = cal.getTime();
			Calendar cal2 = Calendar.getInstance();
			cal2.set(Calendar.MONTH, newSailingReturnDate.getValue().getMonthValue() - 1);
			cal2.set(Calendar.DATE, newSailingReturnDate.getValue().getDayOfMonth());
			cal2.set(Calendar.YEAR, newSailingReturnDate.getValue().getYear());
			cal2.set(Calendar.HOUR_OF_DAY, newSailingReturnTime.getValue().getHour());
			cal2.set(Calendar.MINUTE, newSailingReturnTime.getValue().getMinute());
			cal2.set(Calendar.SECOND, 0);
			cal2.set(Calendar.MILLISECOND, 0);
			Date returnDateTime = cal2.getTime();

			if (returnDateTime.after(leavingDateTime)) {

				try {

					Sailing s = new Sailing(Integer.valueOf(newSailingId.getText()), leavingDateTime, returnDateTime);
					DataAccessObject.addSailing(s);
					fillSailingTable("");
					fillSailingComboBox();

					newSailingId.setText("");
					newSailingLeavingDate.setValue(null);
					newSailingLeavingTime.setValue(null);
					newSailingReturnDate.setValue(null);
					newSailingReturnTime.setValue(null);

					Alerts.generateSuccessAlert("transaction success", "sailing was added successfully",
							s.getNumber() + " was added to the data base successfully");

					fillDashBoard();

				} catch (SQLException e) {
					if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

						Alerts.generateErrorAlert(e, "Exception Has Occurred",
								"Sorry, we are unable to add this sail. A duplicate value already exists",
								"Could not add sail to DataBase!");

					} else
						Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
								"Could not add sail to DataBase!");

				} catch (Exception e) {
					Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
							"Could not add sail to DataBase!");
				}

			} else
				Alerts.generateWarningAlert("illegal dates", "illegal dates",
						"please make sure that departure date is before returning date");

		} else
			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");

	}

	private void putRistrictions() {
		newSailingId.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					newSailingId.setText(newValue.replaceAll("[^\\d]", ""));
				}

			}
		});
	}

	private void fillSailingComboBox() {

		sailingComboBox.getItems().clear();

		try {
			for (Sailing s : DataAccessObject.getSailings())
				sailingComboBox.getItems().add(s.getNumber());

		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not add sail data to comboBox !");
		}

	}

	@SuppressWarnings("unchecked")
	private void fillavailbleRoomsBySailin() {

		try {
			ResultSet rs = DataAccessObject.getAvailbleRoomsBySailing();
			Series<String, Integer> set = new XYChart.Series<>();

			while (rs.next()) {
				set.getData().add(new XYChart.Data<String, Integer>(rs.getInt(1) + "", rs.getInt(2)));

			}
			availbleRoomsBySailingChart.getData().addAll(set);

		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "filling charts error", "bar chart was not filled", "failure");
		}

	}

	private void fillShipsComboBox() {
		shipsComboBox.getItems().clear();

		try {
			for (CruiseShip cs : DataAccessObject.getAllCruiseShips())
				shipsComboBox.getItems().add(cs.getNumber());

		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not add ships data to comboBox !");
		}
	}

	@FXML
	private void addCruise(ActionEvent event) {

		if (sailingComboBox.getValue() != null && shipsComboBox.getValue() != null) {

			Cruise newCruise = new Cruise(sailingComboBox.getValue(), shipsComboBox.getValue(), new Date(), new Date());
			try {

				DataAccessObject.addCruise(newCruise);
				fillCruisesTable("");
				fillSailingComboBox();
				fillShipsComboBox();
				Alerts.generateSuccessAlert("transaction success", "Cruise was added successfully",
						newCruise.getNumber() + " was added to the data base successfully");
				fillDashBoard();

			} catch (SQLException e) {
				if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {

					Alerts.generateErrorAlert(e, "Exception Has Occurred",
							"Sorry, we are unable to add this Cruise . A duplicate value already exists",
							"cruise was not added to dataBase!");

				} else
					Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
							"Could not add cruise to DataBase!");

			} catch (Exception e) {
				Alerts.generateErrorAlert(e, "Exception", "general Exception Has Occurred",
						"Could not add cruise to DataBase!");

			}

		} else
			Alerts.generateWarningAlert("Missing Fields", "missing fields",
					"Please make sure all required fields are filled out");

	}

	@FXML
	private void openStoredprocedures() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/StoredProcedures.fxml"));
			Parent root1 = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.setScene(new Scene(root1));
			stage.setTitle("Stored Procedures");
			stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/query.png")));
			stage.setResizable(false);
			stage.show();

		} catch (IOException e) {
			System.out.println("could not open the requested page");

		}

	}

	@FXML
	private void refresh() {

		initialize(null, null);

	}

	@FXML
	private void closePage() {

		try {
			Stage s = (Stage) barChart.getScene().getWindow();
			s.close();

			Parent root = FXMLLoader.load(getClass().getResource("/view/logIn.fxml"));
			Stage stage = new Stage();
			stage.initStyle(StageStyle.TRANSPARENT);
			Scene scene = new Scene(root);
			scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
			stage.setScene(scene);
			stage.getIcons().add(new Image(Main.class.getResourceAsStream("/style/pics/nemo.png")));
			stage.show();

			root.setOnMousePressed(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					xOffset = event.getSceneX();
					yOffset = event.getSceneY();
				}
			});
			root.setOnMouseDragged(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					stage.setX(event.getScreenX() - xOffset);
					stage.setY(event.getScreenY() - yOffset);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
