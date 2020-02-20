package controller;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.CruiseOrder;
import model.DataAccessObject;
import model.Person;
import model.Sailing;

public class personPageController implements Initializable {
	private double xOffset = 0;
	private double yOffset = 0;
	private static personPageController single_instance = null;
	private static String ID;

	@FXML
	private Circle profileCircle;
	@FXML
	private PieChart pieChart;
	@FXML
	private TabPane personTabPane;
	@FXML
	private Tab dashBoardTab;
	@FXML
	private TextField ordersSearch;
	@FXML
	private TextField cruiseSearch;
	@FXML
	private Tab profileTab;
	@FXML
	private Tab upCommingCruisesTab;
	@FXML
	private Tab ordersTab;
	@FXML
	private Label name;
	@FXML
	private Hyperlink email;
	@FXML
	private Label totalOrders;
	@FXML
	private Label id;
	@FXML
	private Label birthDate;
	@FXML
	private Label phoneNumber;
	@FXML
	private Label dashBoardTotalOrders;
	@FXML
	private Label dashBoardLastCruise;
	@FXML
	private Label totalPaid;
	@FXML
	private Label dashBoardVip;
	@FXML
	private TableView<Object> cruiseTbl;
	@FXML
	private TableView<CruiseOrder> orderTbl;

	@FXML
	private TableColumn<CruiseOrder, Integer> ordersCounterCol;

	@FXML
	private TableColumn<CruiseOrder, Integer> orderCruiseCol;

	@FXML
	private TableColumn<CruiseOrder, Integer> orderShipCol;

	@FXML
	private TableColumn<CruiseOrder, Integer> orderRoomCol;

	@FXML
	private TableColumn<CruiseOrder, Integer> orderPriceCol;

	public static personPageController getInstance() {
		if (single_instance == null)
			single_instance = new personPageController();

		return single_instance;
	}

	public personPageController(String personId) {

		ID = personId;

	}

	public personPageController() {

	}

	@FXML
	void openCruiseTab(ActionEvent event) {
		personTabPane.getSelectionModel().select(upCommingCruisesTab);

	}

	@FXML
	void openDashBoard(ActionEvent event) {
		personTabPane.getSelectionModel().select(dashBoardTab);

	}

	@FXML
	void openProfile(ActionEvent event) {
		personTabPane.getSelectionModel().select(profileTab);

	}

	@FXML
	void openOrders(ActionEvent event) {
		personTabPane.getSelectionModel().select(ordersTab);

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		
		
		logInController.getInstance().close();
		single_instance = this;
		personTabPane.getSelectionModel().select(profileTab);
		fillProfile();
		fillCruiseData("");
		cruiseTbl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		cruiseTbl.getColumns().get(0).setMaxWidth(1f * Integer.MAX_VALUE * 4);
		cruiseTbl.getColumns().get(1).setMaxWidth(1f * Integer.MAX_VALUE * 13);
		cruiseTbl.getColumns().get(2).setMaxWidth(1f * Integer.MAX_VALUE * 20);
		cruiseTbl.getColumns().get(3).setMaxWidth(1f * Integer.MAX_VALUE * 20);
		cruiseTbl.getColumns().get(4).setMaxWidth(1f * Integer.MAX_VALUE * 10);
		cruiseTbl.getColumns().get(5).setMaxWidth(1f * Integer.MAX_VALUE * 10);
		fillOrdersData("");
		fillDashBoard();

	}

	public void fillDashBoard() {

		try {
			dashBoardTotalOrders.setText(DataAccessObject.getOrdersAmount(ID) + "");
			if (DataAccessObject.checkVIPcustomer(ID))
				dashBoardVip.setText("Yes");
			else
				dashBoardVip.setText("No");

			Date d = DataAccessObject.getLastCruiseDate(ID);
			dashBoardLastCruise.setText(d.toString());
			totalPaid.setText(DataAccessObject.getTotalPaid(ID) + " $");
			fillPieChart();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void fillPieChart() {

		pieChart.getData().clear();

		ResultSet rs;
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

		try {
			rs = DataAccessObject.getDistinationCounter(ID);

			while (rs.next())
				pieChartData.add(
						new PieChart.Data(rs.getString(1) + " " + rs.getString(2) + " " + rs.getInt(3), rs.getInt(3)));

		} catch (SQLException e) {
			Alerts.generateErrorAlert(e, "filling charts error", "pie chart was not filled", "failure");

		}

		pieChart.setData(pieChartData);

	}

	public void fillProfile() {

		Person p;
		try {
			p = DataAccessObject.getPerson(ID);
			name.setText(p.getFirstName() + " " + p.getLastName());
			email.setText(p.getEmail());
			phoneNumber.setText(p.getPhoneNumber());
			id.setText(p.getId());
			Date date = p.getBirthDate();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			int year = localDate.getYear();
			int month = localDate.getMonthValue();
			int day = localDate.getDayOfMonth();
			birthDate.setText(day + "/" + month + "/" + year);
			totalOrders.setText(DataAccessObject.getOrdersAmount(ID) + "");
			Image im = new Image("/style/pics/profilePic.jpg");
			profileCircle.setFill(new ImagePattern(im));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void fillOrdersData(String searchKeyWord) {
		orderTbl.setFixedCellSize(50.0);

		ObservableList<CruiseOrder> coData;

		coData = FXCollections.observableArrayList();

		try {

			for (CruiseOrder co : DataAccessObject.getOrdersHistory(ID)) {

				if (!searchKeyWord.equals("")) {
					if (String.valueOf(co.getCruiseId()).contains(searchKeyWord)
							|| String.valueOf(co.getCruiseShipId()).contains(searchKeyWord)
							|| String.valueOf(co.getRoomNumber()).contains(searchKeyWord)
							|| String.valueOf(co.getPrice()).contains(searchKeyWord))
						coData.add(co);
				}

				else {
					coData.add(co);

				}

			}

		} catch (Exception e) {
			Alerts.generateErrorAlert(e, "Exception", "Exception Has Occurred",
					"Could not load cruises from DataBase!");

		}

		orderCruiseCol.setCellValueFactory(new PropertyValueFactory<>("cruiseId"));
		orderShipCol.setCellValueFactory(new PropertyValueFactory<>("cruiseShipId"));
		orderRoomCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
		orderPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
		ordersCounterCol.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<>(orderTbl.getItems().indexOf(cellData.getValue()) + 1));

		orderTbl.setItems(coData);

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void fillCruiseData(String searchKeyWords) {

		cruiseTbl.getItems().clear();
		cruiseTbl.getColumns().clear();

		cruiseTbl.setFixedCellSize(50.0);
		ObservableList data = FXCollections.observableArrayList();
		try {

			ResultSet rs = DataAccessObject.getUpComingSailingCruiseJoin();

			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});
				cruiseTbl.getColumns().addAll(col);
			}

			while (rs.next()) {
				// Iterate Row
				ObservableList<String> row = FXCollections.observableArrayList();
				for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
					// Iterate Column
					row.add(rs.getString(i));
				}

				if (rs.getString(2).contains(searchKeyWords) || rs.getString(5).contains(searchKeyWords))
					data.add(row);

			}

			// FINALLY ADDED TO TableView
			cruiseTbl.setItems(data);

			// add action col
			TableColumn<Object, Object> unfriendCol = new TableColumn<>();
			unfriendCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
			unfriendCol.setCellFactory(param -> new TableCell<Object, Object>() {

				private final JFXButton buy = new JFXButton();
				private final JFXButton map = new JFXButton();
				private final HBox pane = new HBox(map, buy);

				{

					map.getStyleClass().add("map");
					buy.getStyleClass().add("buyBtn");

					buy.setOnAction((ActionEvent event) -> {
						Object data = getTableView().getItems().get(getIndex());

						try {

							FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/buyOrder.fxml"));
							PayController pc = new PayController(data.toString(), ID);
							fxmlLoader.setController(pc);
							Parent root = (Parent) fxmlLoader.load();
							Stage stage = new Stage();
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.initStyle(StageStyle.TRANSPARENT);
							stage.setScene(new Scene(root));
							stage.show();

						} catch (Exception e) {

							Alerts.generateErrorAlert(e, "FXML Error", "could not open SailTo Page", "");

						}

					});

					map.setOnAction((ActionEvent event) -> {
						Object data = getTableView().getItems().get(getIndex());
						Sailing s = new Sailing(Integer.valueOf(Integer.valueOf(data.toString().split(", ")[1])));
						try {

							FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/SailTo.fxml"));
							SailToController stc = new SailToController(s, 2);
							fxmlLoader.setController(stc);
							Parent root = (Parent) fxmlLoader.load();
							Stage stage = new Stage();
							stage.initModality(Modality.APPLICATION_MODAL);
							stage.setScene(new Scene(root));
							stage.setTitle("Cruise Distination");
							stage.getIcons().add(new Image(Main.class.getResourceAsStream("/style/pics/mp.png")));
							stage.show();

						} catch (Exception e) {

							Alerts.generateErrorAlert(e, "FXML Error", "could not open SailTo Page", "");

						}

					});

				}

				@Override
				protected void updateItem(Object person, boolean empty) {
					super.updateItem(person, empty);

					if (person == null) {
						setGraphic(null);
						return;
					}

					setGraphic(pane);

				}
			});

			cruiseTbl.getColumns().add(unfriendCol);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error on Building Data");
		}

	}

	@FXML
	private void searchCruise() {

		fillCruiseData(cruiseSearch.getText());

	}

	@FXML
	private void searchOrders() {

		fillOrdersData(ordersSearch.getText());

	}

	@FXML
	private void close(ActionEvent event) {

		try {
			((Node) (event.getSource())).getScene().getWindow().hide();
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
			Alerts.generateErrorAlert(e, "error", "error", "error");

		}

	}

	@FXML
	private void openUpdate() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/UpdatePerson.fxml"));
			updatePersonController upc = new updatePersonController(DataAccessObject.getPerson(ID), 2);
			fxmlLoader.setController(upc);

			Parent root = (Parent) fxmlLoader.load();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Edit Person");
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image(Main.class.getResourceAsStream("/style/pics/Edit_20px.png")));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
			Alerts.generateErrorAlert(e, "opening page", "Error while opening update page", "see exception");

		}

	}

}
