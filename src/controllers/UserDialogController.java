package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserDialogController {
	@FXML private Button okButton;
	
	
	/**
	 * Closes the dialogWindow
	 * @return 
	 */
	@FXML
	private void okButtonOnAction(){
		Stage stage = (Stage) okButton.getScene().getWindow();
		stage.close();
	}

}
