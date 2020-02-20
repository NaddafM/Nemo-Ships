package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import model.DataAccessObject;

public class logInController implements Initializable {
	private static logInController single_instance;
	private double xOffset = 0;
	private double yOffset = 0;
	@FXML
	private JFXTextField userName;

	@FXML
	private JFXPasswordField Password;
	@FXML
	private Label errorLabel;

	public static logInController getInstance() {
		if (single_instance == null)
			single_instance = new logInController();

		return single_instance;
	}

	@FXML
	void signIn(ActionEvent event) {

		if (!userName.getText().equals("") && !Password.getText().equals("")) {

			if ((userName.getText().equals("Admin") || userName.getText().equals("admin"))
					&& (Password.getText().equals("Admin") || Password.getText().equals("admin"))) {

				openAdmin();

			}

			else {
				try {
					if (DataAccessObject.getPerson(userName.getText()) != null && Password.getText().equals("abc123")) {
						openUser();
					} else {
						errorLabel.setText("Wrong User Name Or Password");

						errorLabel.setVisible(true);

					}
				} catch (SQLException | ParseException e) {
					errorLabel.setText("Wrong User Name Or Password");

					errorLabel.setVisible(true);					
					
				}
			}

		}

		else {
			errorLabel.setText("please enter the account fields");

			errorLabel.setVisible(true);

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		single_instance = this;

	}

	private void openAdmin() {

		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/view/mainPage.fxml"));
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.getIcons().add(new Image(Main.class.getResourceAsStream("/style/pics/nemo.png")));
			stage.setTitle(" Nemo Ships 2019 1.0");
			stage.show();
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {

					Parent root;
					try {
						Stage stage = new Stage();
						root = FXMLLoader.load(getClass().getResource("/view/logIn.fxml"));
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
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void openUser() {

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/personPage.fxml"));
			personPageController ppc = new personPageController(userName.getText());
			fxmlLoader.setController(ppc);
			Parent root = (Parent) fxmlLoader.load();
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
			Alerts.generateErrorAlert(e, "Error", "Error", "Error");
		}
	}

	@FXML
	public void close() {

		Stage stage = (Stage) userName.getScene().getWindow();
		stage.close();

	}

}
