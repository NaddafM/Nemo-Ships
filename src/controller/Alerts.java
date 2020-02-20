package controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Alerts {

	public static void generateSuccessAlert(String titleText, String headerText, String ContentText) {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(titleText);
		alert.setHeaderText(headerText);
		alert.setContentText(ContentText);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/success.png")));
		alert.showAndWait();

	}

	public static void generateErrorAlert(Exception e, String title, String headerText, String ContentText) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(headerText);
		alert.setTitle(title);
		alert.setContentText(ContentText);
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();
		javafx.scene.control.Label label = new javafx.scene.control.Label("The exception stacktrace was:");
		javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		alert.getDialogPane().setExpandableContent(expContent);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/stop.png")));
		alert.showAndWait();

	}

	public static void generateDeleteConflictionErrorAlert(Exception e, String objName) {

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("deleting warning");
		alert.setHeaderText(objName + " was not deleted");
		alert.setContentText(
				"you can not delete this object since it is related to other data in the data base -  The DELETE statement conflicted with the REFERENCE constraint");
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();
		javafx.scene.control.Label label = new javafx.scene.control.Label("The exception stacktrace was:");
		javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		alert.getDialogPane().setExpandableContent(expContent);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/stop.png")));
		alert.showAndWait();

	}

	public static void generateWarningAlert(String titleText, String headerText, String contentText) {

		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(titleText);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(mainPageController.class.getResourceAsStream("/style/pics/warning.png")));
		alert.showAndWait();
	}

}
