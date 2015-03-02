package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class EventController {
	
	private Stage stage;

	@FXML private Button cancelButton;
	
	@FXML public void cancelButtonOnAction(){
		stage.close();
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
}
