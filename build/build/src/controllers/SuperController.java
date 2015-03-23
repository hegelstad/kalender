package controllers;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Calendar;
import models.Event;
import models.PersonInfo;
import models.UserGroup;
import socket.Requester;

public class SuperController {
	public static Tooltip toolTip=null;
	@FXML private TextField newCalendarTextField;
	@FXML private Pane newCalendarWindow;
	@FXML private Rectangle newCalendarRectangle;
	@FXML private Button newCalendarButton;
	@FXML private Pane addNewCalendarPaneButton;
	@FXML private ComboBox<UserGroup> userGroupCalendar;
	@FXML private AnchorPane root;
	@FXML private Pane notificationWindow;
	@FXML private Label notificationTitle;
	public static boolean newCalendarWindowIsOpen;
	public static boolean mouseIsOverNewCalendarWindow;
	public static boolean notificationWindowIsOpen;
	public static boolean mouseIsOverNotificationWindow;
	public static boolean mouseIsOverNotificationLabel;
	ObservableList<UserGroup> userGroups;
	
	@FXML
	private void initialize(){
		newCalendarWindowIsOpen=false;
		mouseIsOverNewCalendarWindow=false;
		notificationWindowIsOpen=false;
		mouseIsOverNewCalendarWindow=false;
		Rectangle shape = new Rectangle(1, 1);
		newCalendarTextField.setShape(shape);

		newCalendarTextField.setOnKeyPressed((KeyEvent key) -> {
			if(key.getCode().equals(KeyCode.ENTER)){
				createCalendar(newCalendarTextField.getText());
			}
		});
		addNewCalendarPaneButton.setOnMouseClicked( (mouseEvent) -> {
			createCalendar(newCalendarTextField.getText());
				
		});
		
		userGroups = FXCollections.observableArrayList(PersonInfo.getPersonInfo().getUsergroups());
		userGroupCalendar.setItems(userGroups);
		userGroupCalendar.setCellFactory(new Callback<ListView<UserGroup>, ListCell<UserGroup>>(){

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
		
		notificationTitle.setOnMouseEntered((enterEvent) -> {
			mouseIsOverNotificationLabel=true;
		});
		
		notificationTitle.setOnMouseExited((enterEvent) -> {
			mouseIsOverNotificationLabel=false;
		});
		
		
		newCalendarWindow.setOnMouseEntered(enterEvent -> {
			mouseIsOverNewCalendarWindow=true;
		});
		
		newCalendarWindow.setOnMouseExited(exitEvent -> {
			mouseIsOverNewCalendarWindow=false;
			
		});
		root.setOnMouseClicked(clickEvent -> {
			if(newCalendarWindowIsOpen && !mouseIsOverNewCalendarWindow){
				newCalendarWindow.setVisible(false);
				newCalendarWindowIsOpen=false;
				toolTip.hide();
			}
			else if( notificationWindowIsOpen && (!mouseIsOverNewCalendarWindow && !mouseIsOverNotificationLabel)){
				notificationWindow.setVisible(false);
				newCalendarWindowIsOpen=false;
			}
		});
		
		notificationWindow.setOnMouseEntered(enterEvent -> {
			mouseIsOverNotificationWindow=true;
		});
		
		notificationWindow.setOnMouseExited(exitEvent -> {
			mouseIsOverNotificationWindow=false;
			
		});
		
		userGroupCalendar.getSelectionModel().select(PersonInfo.getPersonInfo().getPersonalUserGroup());
		toolTip=new Tooltip("Calendar name has to be at least one character");
		
	}
	
	private void createCalendar(String calendarName){
		if(calendarName.equals("")){
			System.out.println("Calendarname cannot be an empty String");
			Stage stage = WindowController.thisStage;
			System.out.println(stage.getX() + "  " + stage.getY());
			toolTip.show(stage, stage.getX() + 300, stage.getY() +210);		
		}
		else
		{	if (toolTip != null){
			toolTip.hide();
		}
		Requester requester = new Requester();
		ArrayList<UserGroup> ug = new ArrayList<UserGroup>();
		ug.add(userGroupCalendar.getSelectionModel().getSelectedItem());
		try{
			Calendar cal = requester.createCalendar(new Calendar(0, newCalendarTextField.getText(), ug));
			if (cal.getCalendarID() != 0){
				newCalendarWindow.setVisible(false);
				newCalendarTextField.setText("");
			}
			requester.closeConnection();
			int colorID = SidebarController.getController().getCalendars().size()+1;
			if(colorID>9){
				colorID = colorID%10;
			}
			cal.setColorID(colorID);
			cal.setEvents(new ArrayList<Event>());
			PersonInfo.getPersonInfo().addCalendar(cal);
		} catch (Exception e){
			System.out.println("Something failed while trying to create calendar!");
		}
	}
}

		
	
	@FXML
	public void keyPressed(KeyEvent key){
		if(key.getCode().equals(KeyCode.ESCAPE)){
			WindowController.logOff();
		}
	}
}
