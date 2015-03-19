package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.shape.SVGPath;
import javafx.geometry.Pos;

public class EventController {
	
	private Stage stage;
	private Room room;
	private Event calendarEvent = null;
	private ArrayList<UserGroup> participants;
	private ArrayList<Attendant> attendants = null;
	private ArrayList<Room> rooms;
	private ArrayList<UserGroup> addedParticipants = new ArrayList<UserGroup>();
	private ArrayList<UserGroup> userGroupsInCalendar;
	ObservableList<UserGroup> pol;
	ObservableList<UserGroup> apol;
	private boolean statusChanged = false;
	private int attendStatus;
	
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
	@FXML private Rectangle eventBar;
	
	@FXML public void cancelButtonOnAction() {
		HeaderController.getController().drawEventsForWeek();
		WindowController.setEventWindowIsOpen(false);
		stage.close();
	}
	
	@FXML public void saveButtonOnAction() {
		if (createEvent()) {
			WindowController.setEventWindowIsOpen(false);
			stage.close();
		} else {
			System.out.println("Something went wrong.");
		}
	}
	
	@FXML public void addParticipant() {
		apol.add(pol.remove(addParticipantsSearch.getSelectionModel().getSelectedIndex()));
		addParticipantsSearch.getSelectionModel().clearSelection();
		validateGuests();
	}
	
	@FXML public void removeParticipant() {
		pol.add(apol.remove(participantsStatus.getSelectionModel().getSelectedIndex()));
		validateGuests();
	}
	
	@FXML public boolean validateTitle() {
        if (title.getText().length() > 40){
			System.out.println("Max 40 characters.");
			return false;
		} else if (title.getText().length() == 0){
			System.out.println("Empty title.");
			return false;
		}
		return true;
	}
	
	@FXML public boolean validateTime() {
        LocalDateTime from = null;
		LocalDateTime to = null;
		try {
			if (toDate.getValue().isBefore(fromDate.getValue())) {
				toDate.setValue(fromDate.getValue());
			}	
		} catch (Exception e){return false;}

		try {
			 from = getFromTime();
		} catch (DateTimeParseException e) {
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
			return false;
		} catch (NullPointerException e) {
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
			return false;
		}

		try {
			 to = getToTime();
		} catch (DateTimeParseException e) {
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
			return false;
		} catch (NullPointerException e) {
			roomLocation.setDisable(true);
			roomLocation.getSelectionModel().clearSelection();
			return false;
		}
        try {
            if (!from.isBefore(to)) {
                roomLocation.setDisable(true);
                System.out.println("From date is before to date.");
                roomLocation.getSelectionModel().clearSelection();
                return false;
            }
        } catch (NullPointerException e) {return false;}

		roomLocation.setDisable(false);
		Room room = roomLocation.getSelectionModel().getSelectedItem();
		Calendar cal = new Calendar (2, "Eirik", null);
		Event ev = null;
		try{
			if (calendarEvent != null || calendarEvent.getEventID() < 0){
					ev = new Event(calendarEvent.getEventID(), title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), cal);
			} else {
				ev = new Event(0, title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), cal);
			}
		}catch (Exception e){
			//System.out.println("Denne er ment å bli catchet");
		}
		ArrayList<Room> avRooms = null;
		if (ev != null){
			Requester r = new Requester();
			avRooms = r.getAvailableRooms(ev);
			r.closeConnection();
		}
		boolean stillAvailableRoom = false;
		if (avRooms != null){
			try {
				for (Room rm : avRooms){
					if (room.getRoomName().equals(rm.getRoomName())){
						stillAvailableRoom = true;
						break;
					}
				}
			} catch(NullPointerException e) {}
		}
		if(!stillAvailableRoom) {
			roomLocation.getSelectionModel().clearSelection();
		}
		return true;
	}
	
	@FXML public boolean validateGuests() {
		try {
			int roomSize = roomLocation.getSelectionModel().getSelectedItem().getCapacity();
			int participants = apol.size();
			if (participants > roomSize) {
				System.out.println("The selected room is not large enough.");
				roomLocation.getSelectionModel().clearSelection();
				return false;
			} else {
				return true;
			}
		} catch (NullPointerException e) {
			System.out.println("Either roomselection or participantlist is empty.");
			return false;
		}
	}
	
	private boolean createEvent() {
		if(eventIsValid()) {
			Requester requester = new Requester();
			Calendar selectedCalendar = PersonInfo.getPersonInfo().getSelectedCalendar();
			Event ev = new Event(0, title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), selectedCalendar);
			ev = requester.createEvent(ev);
			ev.setAttends(1);
			requester.closeConnection();
	
			requester = new Requester();
			try {
				Room room = roomLocation.getSelectionModel().getSelectedItem();
				if (room != null){
					requester.bookRoom(ev, room);
				}
			} catch (Exception e){
				e.printStackTrace();
			}
			requester.closeConnection();
			
			requester = new Requester();
			requester.setNotification(new Notification(0, "Invite to: " + ev.getName(), PersonInfo.getPersonInfo().getPersonalUserGroup(), addedParticipants, ev, 1));
			requester.closeConnection();
				
			requester = new Requester();
			requester.updateAttends(ev, new Attendant(PersonInfo.personInfo.getPersonalUserGroup().getUserGroupID(), PersonInfo.personInfo.getPersonalUserGroup().getName(), 1));
			requester.closeConnection();
			if(ev.getEventID() != 0){
				selectedCalendar.getEvents().add(ev);
				HeaderController.getController().drawEventsForWeek();
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	
	private boolean createGroupEvent() {
		if(eventIsValid()) {
			Requester requester = new Requester();
			Calendar selectedCalendar = PersonInfo.getPersonInfo().getSelectedCalendar();
			Event ev = new Event(0, title.getText(), note.getText(), new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), selectedCalendar);
			ev = requester.createGroupEvent(ev);
			ev.setAttends(1);
			requester.closeConnection();
	
			requester = new Requester();
			try {
				Room room = roomLocation.getSelectionModel().getSelectedItem();
				if (room != null){
					requester.bookRoom(ev, room);
				}
			} catch (Exception e){
				System.out.println("No room selected.");
			}
			requester.closeConnection();
			if(ev.getEventID() != 0){
				selectedCalendar.getEvents().add(ev);
				HeaderController.getController().drawEventsForWeek();
				WindowController.setEventWindowIsOpen(false);
				stage.close();
				return true;
			}
			else{
				return false;
			}
		}
		return false;
	}
	
	private boolean eventIsValid() {
        System.out.println("Checking if event is valid in EventController.");
        boolean valid = true;
		if(!validateTitle()) {
			valid = false;
			System.out.println("Invalid title.");
		}
        if (!validateTime()) {
			valid = false;
			System.out.println("Time is invalid.");
		}
        if (roomLocation.getSelectionModel().getSelectedItem() != null) {
			if (!validateGuests()) {
				valid = false;
				System.out.println("Selected room is not big enough.");
			}
		}
		return valid;
	}

	@FXML
	private void initialize(){
		if (! WeekController.getController().isNewEvent()){
			this.calendarEvent = WeekController.getController().getEvent();
		}
		initializeHourAndMinutes();
		Requester requester = new Requester();
		participants = requester.getPrivateUserGroups();
        requester.closeConnection();
        requester = new Requester();
        try{
        	userGroupsInCalendar = requester.getUserGroupsInCalendar(calendarEvent.getCal());
        }catch (NullPointerException e){
        	requester.closeConnection();
        	requester = new Requester();
        	userGroupsInCalendar = requester.getUserGroupsInCalendar(PersonInfo.getPersonInfo().getSelectedCalendar());
        }
        requester.closeConnection();
        System.out.println("Usergroups in calendar: " + userGroupsInCalendar);
        if(userGroupsInCalendar == null){
        	pol = FXCollections.observableArrayList(participants);
        }
        else if(userGroupsInCalendar.size() < 2){
        	pol = FXCollections.observableArrayList(participants);
        }else{
        	pol = FXCollections.observableArrayList(userGroupsInCalendar);
        }
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
		                grid.getColumnConstraints().add(new ColumnConstraints(195));
		                grid.setHgap(10);
		                
		                Label text = new Label(ug.getName());
		                grid.add(text, 0, 0);
		                Circle statusCircle = new Circle(4);
		                if (attendants != null){
		                	statusCircle.setFill(Color.GOLDENROD);
		                	for (Attendant a : attendants) {
		                		System.out.println("LOOPING ATTENDANTS");
		                		System.out.println(a);
		                		if (a.getUserGroupID() == ug.getUserGroupID()) {
			                		if (a.getStatus() == 1){
			                			statusCircle.setFill(Color.DARKGREEN);
			                			System.out.println("SETTING STATUS GREEN");
			                			break;
			                		} else if (a.getStatus() == 2) {
			                			statusCircle.setFill(Color.BROWN);
			                			System.out.println("SETTING STATUS RED");
			                			break;
			                		} else {
			                			statusCircle.setFill(Color.GOLDENROD);
			                			System.out.println("SETTING STATUS YELLOW");
			                			break;
			                		}
		                		}
		                	}
		            	}else{
		                	if (PersonInfo.personInfo.getPersonalUserGroup().getUserGroupID() == ug.getUserGroupID()) {
	                			statusCircle.setFill(Color.DARKGREEN);
	                		} else {
	                			statusCircle.setFill(Color.GOLDENROD);
	                		}
		                }
		                grid.add(statusCircle, 1, 0);
		                if (ug.getUserGroupID() == PersonInfo.getPersonInfo().getPersonalUserGroup().getUserGroupID()){
		                	try{
		                		if(calendarEvent.getEventID() != 0){
				                	GridPane pane = new GridPane();
				                	SVGPath path = new SVGPath();
				                	path.setContent("M0,0 L5,5 L10,0 L0,0");
				                	pane.add(path, 0, 0);
				                	grid.add(pane, 2, 0);
				                	pane.setAlignment(Pos.CENTER);
				                	pane.setOnMouseClicked(new EventHandler<MouseEvent>() {
				            		    @Override
				            		    public void handle(MouseEvent mouseEvent) {
				            		        if(mouseEvent.getButton() == MouseButton.PRIMARY){
				            		        	final ContextMenu contextMenu = new ContextMenu();
				            		        	MenuItem attend = new MenuItem("Attend");
				            		        	MenuItem dontAttend = new MenuItem("Don't attend");
				            		        	MenuItem noAnswer = new MenuItem("No answer");
				            		        	attend.setOnAction(new EventHandler<ActionEvent>() {
				            		        		@Override public void handle(ActionEvent e) {
				            		        			statusChanged = true;
				            		        			attendStatus = 1;
				            		        			statusCircle.setFill(Color.DARKGREEN);
				            		        		}
				            		        	});
				            		        	dontAttend.setOnAction(new EventHandler<ActionEvent>() {
				            		        		@Override public void handle(ActionEvent e) {
				            		        			statusChanged = true;
				            		        			attendStatus = 2;
				            		        			statusCircle.setFill(Color.BROWN);
				            		        		}
				            		        	});
				            		        	noAnswer.setOnAction(new EventHandler<ActionEvent>() {
				            		        		@Override public void handle(ActionEvent e) {
				            		        			statusChanged = true;
				            		        			attendStatus = 0;
				            		        			statusCircle.setFill(Color.GOLDENROD);
				            		        		}
				            		        	});
				            		        	contextMenu.getItems().addAll(attend, dontAttend, noAnswer);
				            		        	setContextMenu(contextMenu);
				            		        	contextMenu.show(stage, mouseEvent.getScreenX(), mouseEvent.getScreenY());
				            		        }
				            		    }
				            		});
				                }
		                	}
		                catch(NullPointerException e){
		                }	
		              }
		              setGraphic(grid);
		            }
		        }
		    };
		});
		
		addParticipantsSearch.setCellFactory(new Callback<ListView<UserGroup>, ListCell<UserGroup>>(){
			@Override
			public ListCell<UserGroup> call(ListView<UserGroup> param) {	
			    return new ListCell<UserGroup>() {
			        @Override
			        protected void updateItem(UserGroup ug, boolean empty) {
			            super.updateItem(ug, empty);
	
			            if (ug == null || empty) {
			                setText(null);
			            } else {
			                setText(ug.getName()); 
			            }
			        }
			    };
			}
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
		if (apol.size() == 0){
			int index = -1;
			for (UserGroup ug : participants) {
		   		if (personalID == ug.getUserGroupID()) {
		   			index = pol.indexOf(ug);
		   			break;
		    	}
		    }
			if (index != -1) apol.add(pol.remove(index));	
	    }
		if (userGroupsInCalendar == null){
			saveButton.setDisable(true);
			addParticipantsButton.setDisable(true);
			removeParticipantsButton.setDisable(true);
			title.setDisable(true);
			title.setStyle("-fx-opacity: 1");
			fromDate.setDisable(true);
			fromDate.setStyle("-fx-opacity: 1");
			toDate.setDisable(true);
			toDate.setStyle("-fx-opacity: 1");
			fromHours.setDisable(true);
			fromHours.setStyle("-fx-opacity: 1");
			toHours.setDisable(true);
			toHours.setStyle("-fx-opacity: 1");
			fromMinutes.setDisable(true);
			fromMinutes.setStyle("-fx-opacity: 1");
			toMinutes.setDisable(true);
			toMinutes.setStyle("-fx-opacity: 1");
			addParticipantsSearch.setDisable(true);
			addParticipantsSearch.setStyle("-fx-opacity: 1");
			note.setDisable(true);
			note.setStyle("-fx-opacity: 1");
			roomLocation.setDisable(true);
			roomLocation.setStyle("-fx-opacity: 1");
			participantsStatus.setDisable(true);
			participantsStatus.setStyle("-fx-opacity: 1");
		}
		else if (userGroupsInCalendar.size() > 1){
			apol.addAll(pol);
			pol.clear();
			addParticipantsSearch.setDisable(true);
			addParticipantsButton.setDisable(true);
			removeParticipantsButton.setDisable(true);
			saveButton.setOnAction(e -> createGroupEvent());
		}
	}
	
	public LocalDateTime getFromTime() {
		return LocalDateTime.parse(fromDate.getValue().toString() + " " + fromHours.getValue() + ":" + fromMinutes.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
	
	public LocalDateTime getToTime() {
		return LocalDateTime.parse(toDate.getValue().toString() + " " + toHours.getValue() + ":" + toMinutes.getValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
	}
	
	public void getAvailableRooms() {
		Requester requester = new Requester();
		Event ev = new Event(0, null, null, new ArrayList<UserGroup>(apol), getFromTime(), getToTime(), null);
		rooms = requester.getAvailableRooms(ev);
		requester.closeConnection();
		ObservableList<Room> ol = FXCollections.observableArrayList(rooms);
		roomLocation.setItems(ol);
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	private void initializeHourAndMinutes(){
		try{
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
		}catch (Exception e){
			System.out.println("Something failed");
		}
		
	}
	
	void setBarColor(Event event){
		eventBar.fillProperty().set(event.getCal().getColor());
		eventBar.setOpacity(0.7);
	}

	void openEvent(Event event) {
		removeDateListeners();
		title.setText(event.getName());
		fromDate.setValue(event.getFrom().toLocalDate());
		toDate.setValue(event.getTo().toLocalDate());
		String FromHours = Integer.toString(event.getFrom().toLocalTime().getHour());
		String ToHours = Integer.toString(event.getTo().toLocalTime().getHour());
		String FromMinutes = Integer.toString(event.getFrom().toLocalTime().getMinute());
		String ToMinutes = Integer.toString(event.getTo().toLocalTime().getMinute());
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
		setDateListeners();
		eventBar.fillProperty().set(calendarEvent.getCal().getColor());
		eventBar.setOpacity(0.7);
		if(event.getEventID() != 0){
			System.out.println("Fetching event information from database.");
			Requester requester = new Requester();
			try{
				Room evRoom = requester.getEventRoom(event);
				roomLocation.getSelectionModel().select(evRoom);
			}catch (NullPointerException e){
				System.out.println("No rooms for event.");
			}
			requester.closeConnection();
			requester = new Requester();
			try{
			this.attendants = requester.getAttendants(event);
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
			}catch(NullPointerException e){
				System.out.println("No attendants at event");
			}
			requester.closeConnection();
			note.setText(event.getNote());
			if (attendants != null){
				boolean remove = true;
				for (Attendant a : attendants){
					if (a.getUserGroupID() == PersonInfo.getPersonInfo().getPersonalUserGroup().getUserGroupID()){
						remove = false;
					}
				}
				if(remove){
					int index = -1;
					for (UserGroup ug : apol){
						if (ug.getUserGroupID() == PersonInfo.getPersonInfo().getPersonalUserGroup().getUserGroupID()){
							index = apol.indexOf(ug);
						}
					}
					if (index != -1){
						pol.add(apol.remove(index));
					}
				}
			}
			System.out.println("Opened event: " + event);
			saveButton.setOnAction(e -> updateEvent(event));
		}
	}
	
	public void updateEvent(Event event){
		if(statusChanged){
			Requester requester = new Requester();
			requester.updateAttends(calendarEvent, new Attendant(PersonInfo.getPersonInfo().getPersonalUserGroup().getUserGroupID(),PersonInfo.getPersonInfo().getPersonalUserGroup().getName(), attendStatus));
			requester.closeConnection();
			calendarEvent.setAttends(attendStatus);
			HeaderController.getController().drawEventsForWeek();
			for (Attendant a : attendants) {
        		if (a.getUserGroupID() == PersonInfo.getPersonInfo().getPersonalUserGroup().getUserGroupID()) {
            		a.setStatus(attendStatus);
        		}
			}
		}
		
		event.setName(title.getText());
		event.setNote(note.getText());
		event.setParticipants(new ArrayList<UserGroup>(apol));
		event.setFrom(getFromTime());
		event.setTo(getToTime());
		
		Requester requester = new Requester();
		requester.editEvent(event, PersonInfo.getPersonInfo().getPersonalUserGroup());
		requester.closeConnection();
		
		requester = new Requester();
		requester.updateLocation(event, roomLocation.getSelectionModel().getSelectedItem());
		requester.closeConnection();
		
		HeaderController.getController().drawEventsForWeek();
		WindowController.setEventWindowIsOpen(false);
		stage.close();
	}
	
	public void setDateListeners(){
		fromDate.setOnAction((event) -> {
			validateTime();
		});
		toDate.setOnAction((event) -> {
			validateTime();
		});
		fromHours.setOnAction((event) -> {
			validateTime();
		});
		toHours.setOnAction((event) -> {
			validateTime();
		});
		fromMinutes.setOnAction((event) -> {
			validateTime();
		});
		toMinutes.setOnAction((event) -> {
			validateTime();
		});
	}
	
	public void removeDateListeners(){
		fromDate.setOnAction((event) -> {});
		toDate.setOnAction((event) -> {});
		fromHours.setOnAction((event) -> {});
		toHours.setOnAction((event) -> {});
		fromMinutes.setOnAction((event) -> {});
		toMinutes.setOnAction((event) -> {});
	}
}