package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserDialogController {
	@FXML private Button okButton;
	@FXML private AnchorPane UserDialogWindow;
	
	
	/**
	 * Closes the dialogWindow
	 * @return 
	 */
	
	@FXML
	private void initialize(){
		  UserDialogWindow.setOnKeyPressed((KeyEvent key) -> {
		        if (key.getCode().equals(KeyCode.ENTER)) {
		        	Stage stage = (Stage) okButton.getScene().getWindow();
		    		stage.close();
		        } else if(key.getCode().equals(KeyCode.ESCAPE)) {
		        	Stage stage = (Stage) okButton.getScene().getWindow();
		    		stage.close();
		        }
		    });
		    
	}
	@FXML
	private void okButtonOnAction(){
		Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
	}

	
}
