package controllers;

<<<<<<< HEAD
import models.Event;
=======
>>>>>>> 7675e999eb492f96de91cf2f00ea25b07811d8b2
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EventController {
	
	private Stage stage;

	@FXML private Button cancelButton;
	@FXML private Button saveButton;
	@FXML private TextField title;
	@FXML private DatePicker fromDate;
	@FXML private DatePicker toDate;
	@FXML private ComboBox<String> fromHours;
	@FXML private ComboBox<String> fromMinutes;
	@FXML private ComboBox<String> toHours;
	@FXML private ComboBox<String> toMinutes;
	
	
	@FXML public void cancelButtonOnAction(){
		stage.close();
	}
	
	@FXML
	private void initialize(){
		initializeHourAndMinutes();
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	private void initializeHourAndMinutes(){
		fromHours.setItems(FXCollections.observableArrayList(
			    "00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"));
		toHours.setItems(FXCollections.observableArrayList(
			    "00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"));
		fromMinutes.setItems(FXCollections.observableArrayList(
			    "00","05","10","15","20","25","30","35","40","45","50","55"));
		toMinutes.setItems(FXCollections.observableArrayList(
			    "00","05","10","15","20","25","30","35","40","45","50","55"));
	}

	void openEvent(Event event){
		title.setText(event.getName());
	}

}
