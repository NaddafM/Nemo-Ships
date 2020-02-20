package controller;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.DataAccessObject;

public class StoredProceduresController implements Initializable {

	@FXML
	private JFXComboBox<String> procedureComboBox;

	@FXML
	private JFXTextArea queryBody;

	@FXML
	void showQuery(ActionEvent event) {
		
		
		try {
			queryBody.setText(DataAccessObject.getStoredProcedure(procedureComboBox.getValue()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {


		try {
			queryBody.setText(DataAccessObject.getStoredProcedure("getPopularDestinations"));

			for (String SPECIFIC_NAME : DataAccessObject.getAllStoredProcedures()) {
				procedureComboBox.getItems().add(SPECIFIC_NAME);

			}

			procedureComboBox.getSelectionModel().select("getPopularDestinations");

			
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
