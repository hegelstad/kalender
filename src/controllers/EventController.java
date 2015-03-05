package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import socket.Requester;
import models.Calendar;
import models.Event;
import models.Room;
import models.UserGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EventController {
	
	private Stage stage;
	private ArrayList<UserGroup> participants;
	private ArrayList<Room> rooms;
	private ArrayList<UserGroup> addedParticipants = new ArrayList<UserGroup>();
	
	@FXML private Button cancelButton;
	@FXML private Button saveButton;
	@FXML private TextField title;
	@FXML private DatePicker fromDate;
	@FXML private DatePicker toDate;
	@FXML private ComboBox<String> fromHours;
	@FXML private ComboBox<String> fromMinutes;
	@FXML private ComboBox<String> toHours;
	@FXML private ComboBox<String> toMinutes;
	@FXML private ComboBox<UserGroup> addParticipantsSearch;
	@FXML private ComboBox<Room> roomLocation;
	
	
	@FXML public void cancelButtonOnAction(){
		stage.close();
	}
	
	@FXML public void saveButtonOnAction(){
		if (createEvent()){
			stage.close();
		}else{
			
		}
	}
	
	private boolean createEvent() {
		
		return false;
	}

	@FXML
	private void initialize(){
		initializeHourAndMinutes();
		Calendar cal = new Calendar(2, "Sondre", null);
		ArrayList<Calendar> callist = new ArrayList<Calendar>();
		callist.add(cal);
		Requester r2 = new Requester();
		participants = r2.getUserGroups(callist);
		ObservableList<UserGroup> ol2 = FXCollections.observableArrayList(participants);
		addParticipantsSearch.setItems(ol2);
	}
	
	public void getAvailableRooms(){
		Requester r = new Requester();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime from = LocalDateTime.parse(fromDate.getValue().toString() + " " + fromHours.getValue() + ":" + fromMinutes.getValue(), formatter);
		LocalDateTime to = LocalDateTime.parse(toDate.getValue().toString() + " " + toHours.getValue() + ":" + toMinutes.getValue(), formatter);
		Event ev = new Event(0, null, null, addedParticipants, from, to, null);
		rooms = r.getAvailableRooms(ev);
		r.closeConnection();
		ObservableList<Room> ol = FXCollections.observableArrayList(rooms);
		roomLocation.setItems(ol);
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
