package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import socket.Requester;
import models.Attendant;
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
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;


public class EventController {
	
	private Stage stage;
	private Room room;
	private Event calendarEvent = null;
	private ArrayList<UserGroup> participants;
	private ArrayList<Attendant> attendants = null;
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
		addParticipantsSearch.getSelectionModel().clearSelection();
		validateGuests();
		
	}
	
	@FXML public void removeParticipant(){
		pol.add(apol.remove(participantsStatus.getSelectionModel().getSelectedIndex()));
		validateGuests();
	}
	
	@FXML public boolean validateTitle(){
		if (title.getText().length() > 40){
			System.out.println("Max 40 characters.");
			return false;
		} else if (title.getText().length() == 0){
			System.out.println("Empty title.");
			return false;
		}
		return true;
	}
	
	@FXML public boolean validateTime(){
		LocalDateTime from = null;
		LocalDateTime to = null;
		try{
			if (toDate.getValue().isBefore(fromDate.getValue())){
				toDate.setValue(fromDate.getValue());
			}	
		}catch (Exception e){}
		try{
			 from = getFromTime();
		}catch (DateTimeParseException e){
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
			return false;
		}catch (NullPointerException e){
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
		}
		try{
			 to = getToTime();
		}catch (DateTimeParseException e){
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
			return false;
		}catch (NullPointerException e){
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
		}
		if (! from.isBefore(to)){
			roomLocation.setDisable(true);
			System.out.println("From date is before to date.");
			roomLocation.getSelectionModel().clearSelection();
			return false;
		}
		roomLocation.setDisable(false);
		Room room = roomLocation.getSelectionModel().getSelectedItem();
		Calendar cal = new Calendar (2, "Eirik", null);
		Event ev;
		if (calendarEvent != null){
			ev = new Event(calendarEvent.getEventID(), title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), cal);
		}else{
			ev = new Event(0, title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), cal);
		}
		Requester r = new Requester();
		ArrayList<Room> avRooms = r.getAvailableRooms(ev);
		r.closeConnection();
		boolean stillAvailableRoom = false;
		try{
			for (Room rm : avRooms){
				if (room.getRoomName().equals(rm.getRoomName())){
					stillAvailableRoom = true;
					break;
				}
			}
		} catch(NullPointerException e) {}
		if(! stillAvailableRoom){
			roomLocation.getSelectionModel().clearSelection();
		}
		return true;
	}
	
	@FXML public boolean validateGuests(){
		System.out.println("Validating guests");
		try{
			int roomSize = roomLocation.getSelectionModel().getSelectedItem().getCapacity();
			int participants = apol.size();
			if (participants > roomSize){
				System.out.println("Room not big enough");
				roomLocation.getSelectionModel().clearSelection();
				return false;
			}else{
				return true;
			}
		}catch (NullPointerException e){
			System.out.println("Either roomselection or participantlist is empty.");
			return false;
		}
	}
	
	private boolean createEvent() {
		if(eventIsValid()){
			Requester r = new Requester();
			Calendar cal = new Calendar (2, "Eirik", null);
			Event ev = new Event(0, title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), cal);
			ev = r.createEvent(ev);
			r.closeConnection();
	
			r = new Requester();
			try{
				Room room = roomLocation.getSelectionModel().getSelectedItem();
				if (room != null){
					r.bookRoom(ev, room);
				}
			}catch (Exception e){}
			r.closeConnection();
			
			r = new Requester();
			r.setNotification(new Notification(0, "Invite to: " + ev.getName(), PersonInfo.getPersonInfo().getPersonalUserGroup(), addedParticipants, ev, 1));
			r.closeConnection();
				
			r = new Requester();
			r.updateAttends(ev, new Attendant(PersonInfo.personInfo.getPersonalUserGroup().getUserGroupID(), PersonInfo.personInfo.getPersonalUserGroup().getName(), 1));
			r.closeConnection();
			return (ev.getEventID() != 0);
		}
		return false;
	}
	
	private boolean eventIsValid() {
		boolean valid = true;
		if(! validateTitle()){
			valid = false;
			System.out.println("Invalid title");
		}if (! validateTime()){
			valid = false;
			System.out.println("Time is invalid");
		}if (roomLocation.getSelectionModel().getSelectedItem() != null){
			if (! validateGuests()){
				valid = false;
				System.out.println("Room is not big enough.");
			}	
		}
		return valid;
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
		participantsStatus.setCellFactory((list) -> {
		    return new ListCell<UserGroup>() {
		        @Override
		        protected void updateItem(UserGroup ug, boolean empty) {
		            super.updateItem(ug, empty);

		            if (ug == null || empty) {
		                setText(null);
		                setGraphic(null);
		            } else {
		                setText(null);
		                GridPane grid = new GridPane();
		                grid.getColumnConstraints().add(new ColumnConstraints(220));
		                grid.setHgap(10);
		                
		                Text text = new Text(ug.getName());
		                grid.add(text, 0, 0);
		                Circle statusCircle = new Circle(4);
		                if (attendants != null){
		                	for (Attendant a : attendants){
		                		if (a.getUserGroupID() == ug.getUserGroupID()){
			                		if (PersonInfo.personInfo.getPersonalUserGroup().getUserGroupID() == ug.getUserGroupID()){
			                			statusCircle.setFill(Color.DARKGREEN);
			                			break;
			                		}
			                		else if (a.getStatus() == 1){
			                			statusCircle.setFill(Color.DARKGREEN);
			                			break;
			                		}else if (a.getStatus() == 2){
			                			statusCircle.setFill(Color.BROWN);
			                			break;
			                		}else{
			                			statusCircle.setFill(Color.GOLDENROD);
			                			break;
			                		}
		                		}
		                	}
		                }else{
		                	if (PersonInfo.personInfo.getPersonalUserGroup().getUserGroupID() == ug.getUserGroupID()){
	                			statusCircle.setFill(Color.DARKGREEN);
	                		}else{
	                			statusCircle.setFill(Color.GOLDENROD);
	                		}
		                }
		             
		                grid.add(statusCircle, 1, 0);
		                setGraphic(grid);
		            }
		        }
		    };
		});
		
		final Callback<DatePicker, DateCell> dayCellFactory = 
	            new Callback<DatePicker, DateCell>() {
	                @Override
	                public DateCell call(final DatePicker datePicker) {
	                    return new DateCell() {
	                        @Override
	                        public void updateItem(LocalDate item, boolean empty) {
	                            super.updateItem(item, empty);
	                            if (item.isBefore(
	                                    fromDate.getValue())
	                                ) {
	                                    setDisable(true);
	                                    setStyle("-fx-background-color: #B8B8B8;");
	                            }
	                    }
	                };
	            }
	        };
	        
	    toDate.setDayCellFactory(dayCellFactory);
	        
		int personalID = PersonInfo.personInfo.getPersonalUserGroup().getUserGroupID();
		for (UserGroup ug : participants){
			if (personalID == ug.getUserGroupID()){
				apol.add(pol.remove(pol.indexOf(ug)));
				break;
			}
		}
	}
	
	public LocalDateTime getFromTime(){
		return LocalDateTime.parse(fromDate.getValue().toString() + " " + fromHours.getValue() + ":" + fromMinutes.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
	
	public LocalDateTime getToTime(){
		return LocalDateTime.parse(toDate.getValue().toString() + " " + toHours.getValue() + ":" + toMinutes.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
	
	public void getAvailableRooms(){
		Requester r = new Requester();
		Event ev = new Event(0, null, null, new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), null);
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
		fromDate.setValue(LocalDate.now().plusDays(1));
		toDate.setValue(LocalDate.now().plusDays(1));
		fromHours.getSelectionModel().select("12");
		fromMinutes.getSelectionModel().select("15");
		toHours.getSelectionModel().select("13");
		toMinutes.getSelectionModel().select("00");
	}

	void openEvent(Event event) {
		this.calendarEvent = event;
		title.setText(event.getName());
		fromDate.setValue(event.getFrom().toLocalDate());
		toDate.setValue(event.getTo().toLocalDate());
		String FromHours = Integer.toString(event.getFrom().toLocalTime().getHour());
		String ToHours = Integer.toString(event.getTo().toLocalTime().getHour());
		String FromMinutes = Integer.toString(event.getFrom().toLocalTime().getMinute());
		String ToMinutes = Integer.toString(event.getTo().toLocalTime().getMinute());
		Requester requester = new Requester();
		Room evRoom = requester.getEventRoom(event);
		requester.closeConnection();
		requester = new Requester();
		this.attendants = requester.getAttendants(event);
		requester.closeConnection();
		roomLocation.getSelectionModel().select(evRoom);
		for (Attendant a : attendants) {
			int index = -1;
			for (UserGroup ug2 : pol) {
				if (a.getUserGroupID() == ug2.getUserGroupID()) {
					index = pol.indexOf(ug2);
				}
			}
			if (index != -1) {
				apol.add(pol.remove(index));
			}
		}
		if (FromHours.length() == 1) {
			FromHours = "0" + FromHours;
		}
		if (ToHours.length() == 1) {
			ToHours = "0" + ToHours;
		}
		if (FromMinutes.length() == 1) {
			FromMinutes = "0" + FromMinutes;
		}
		if (ToMinutes.length() == 1) {
			ToMinutes = "0" + ToMinutes;
		}
		fromHours.setValue(FromHours);
		toHours.setValue(ToHours);
		fromMinutes.setValue(FromMinutes);
		toMinutes.setValue(ToMinutes);
		note.setText(event.getNote());
		System.out.println(event);
		//Oppdaterer event og location
		if (event.getEventID() != 0) {
			saveButton.setOnAction(e -> updatedEvent(event));
		}
	}
	
	public void updatedEvent(Event event){
		
		event.setName(title.getText());
		event.setNote(note.getText());
		event.setParticipants(new ArrayList<UserGroup>(apol));
		event.setFrom(getFromTime());
		event.setTo(getToTime());
		
		Requester req = new Requester();
		req.editEvent(event, PersonInfo.getPersonInfo().getPersonalUserGroup());
		req.closeConnection();
		
		req = new Requester();
		req.updateLocation(event, roomLocation.getSelectionModel().getSelectedItem());
		req.closeConnection();
		
		WindowController.setEventWindowIsOpenOrClosed(false);
		stage.close();
	}
}
