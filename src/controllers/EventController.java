package controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import socket.Requester;
import models.Calendar;
import models.Event;
import models.Notification;
import models.PersonInfo;
import models.Room;
import models.UserGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;


public class EventController {
	
	private Stage stage;
	private Room room;
	private ArrayList<UserGroup> participants;
	private ArrayList<Room> rooms;
	private ArrayList<UserGroup> addedParticipants = new ArrayList<UserGroup>();
	ObservableList<UserGroup> pol;
	ObservableList<UserGroup> apol;
	
	@FXML private Button cancelButton;
	@FXML private Button saveButton;
	@FXML private Button addParticipantsButton;
	@FXML private Button removeParticipantsButton;
	@FXML private TextField title;
	@FXML private DatePicker fromDate;
	@FXML private DatePicker toDate;
	@FXML private ComboBox<String> fromHours;
	@FXML private ComboBox<String> fromMinutes;
	@FXML private ComboBox<String> toHours;
	@FXML private ComboBox<String> toMinutes;
	@FXML private TextArea note;
	@FXML private ComboBox<UserGroup> addParticipantsSearch;
	@FXML private ComboBox<Room> roomLocation;
	@FXML private ListView<UserGroup> participantsStatus;
	
	
	@FXML public void cancelButtonOnAction(){
		HeaderController.getController().drawEventsForWeek();
		WindowController.setEventWindowIsOpenOrClosed(false);
		stage.close();
	}
	
	@FXML public void saveButtonOnAction(){
		if (createEvent()){
			WindowController.setEventWindowIsOpenOrClosed(false);
			stage.close();
		}else{
			System.out.println("Something went wrong.");
		}
	}
	
	@FXML public void addParticipant(){
		apol.add(pol.remove(addParticipantsSearch.getSelectionModel().getSelectedIndex()));
		
	}
	
	@FXML public void removeParticipant(){
		pol.add(apol.remove(participantsStatus.getSelectionModel().getSelectedIndex()));
	}
	
	
	private boolean createEvent() {
		Requester r = new Requester();
		Calendar cal = new Calendar (2, "Eirik", null);
		Event ev = new Event(0, title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), cal);
		ev = r.createEvent(ev);
		r.closeConnection();

		r = new Requester();
		Room room = roomLocation.getSelectionModel().getSelectedItem();
		if (room != null){
			r.bookRoom(ev, room);
		}
		r.closeConnection();
		
		r = new Requester();
		r.setNotification(new Notification(0, "Invite to: " + ev.getName(), PersonInfo.getPersonInfo().getPersonalUserGroup(), addedParticipants, ev, 1));
		return (ev.getEventID() != 0);
	}


	@FXML
	private void initialize(){
		initializeHourAndMinutes();
		Requester r = new Requester();
		participants = r.getPrivateUserGroups();
		pol = FXCollections.observableArrayList(participants);
		addParticipantsSearch.setItems(pol);
		apol = FXCollections.observableArrayList(addedParticipants);
		participantsStatus.setItems(apol);
	}
	
	public LocalDateTime getFromTime(){
		return LocalDateTime.parse(fromDate.getValue().toString() + " " + fromHours.getValue() + ":" + fromMinutes.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
	
	public LocalDateTime getToTime(){
		return LocalDateTime.parse(toDate.getValue().toString() + " " + toHours.getValue() + ":" + toMinutes.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
	
	public void getAvailableRooms(){
		Requester r = new Requester();
		Event ev = new Event(0, null, null, addedParticipants, getFromTime(), getToTime(), null);
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
		fromDate.setValue(event.getFrom().toLocalDate());
		toDate.setValue(event.getTo().toLocalDate());
		String FromHours = Integer.toString(event.getFrom().toLocalTime().getHour());
		String ToHours = Integer.toString(event.getTo().toLocalTime().getHour());
		String FromMinutes = Integer.toString(event.getFrom().toLocalTime().getMinute());
		String ToMinutes = Integer.toString(event.getTo().toLocalTime().getMinute());
		if (FromHours.length() == 1){
			FromHours = "0"+FromHours;
		}
		if (ToHours.length() == 1){
			ToHours = "0"+ToHours;
		}
		if (FromMinutes.length() == 1){
			FromMinutes = "0"+FromMinutes;
		}
		if (ToMinutes.length() == 1){
			ToMinutes = "0"+ToMinutes;
		}
		fromHours.setValue(FromHours);
		toHours.setValue(ToHours);
		fromMinutes.setValue(FromMinutes);
		toMinutes.setValue(ToMinutes);
		note.setText(event.getNote());
	}
}
