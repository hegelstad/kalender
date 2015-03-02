package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class calendarHeaderViewController {

	@FXML private Button addEventButton;
	
	@FXML
	private void addEventOnAction(){
		WindowController.goToEventView();
	}
}
