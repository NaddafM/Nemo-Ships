package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.DataBaseConnection;

public class Main extends Application {
	private double xOffset = 0;
	private double yOffset = 0;

	@Override
	public void start(Stage stage) throws Exception {


		Parent root = FXMLLoader.load(getClass().getResource("/view/logIn.fxml"));
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

	}

	public static void main(String[] args) {

		try {
			DataBaseConnection.initConn(); // connenting to DB
			launch(args); // starting the app

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
