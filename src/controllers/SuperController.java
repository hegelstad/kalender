package controllers;

import java.util.ArrayList;

import models.Calendar;
import models.PersonInfo;
import models.UserGroup;
import models.Event;
import socket.Requester;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

public class SuperController {

	public static Tooltip toolTip = null;
	@FXML private TextField newCalendarTextField;
	@FXML private Pane newCalendarWindow;
	@FXML private Rectangle newCalendarRectangle;
	@FXML private Pane addNewCalendarPaneButton;
	@FXML private ComboBox<UserGroup> userGroupCalendar;
	ObservableList<UserGroup> userGroups;
	
	@FXML
	private void initialize(){
		Rectangle shape = new Rectangle(1, 1);
		newCalendarTextField.setShape(shape);

		newCalendarTextField.setOnKeyPressed((KeyEvent key) -> {
			if(key.getCode().equals(KeyCode.ENTER)){
				createCalendar(newCalendarTextField.getText());
			}
		});
		addNewCalendarPaneButton.setOnMouseClicked((mouseEvent) -> {
			createCalendar(newCalendarTextField.getText());
		});
		
		userGroups = FXCollections.observableArrayList(PersonInfo.getPersonInfo().getUsergroups());
		userGroupCalendar.setItems(userGroups);
		userGroupCalendar.setCellFactory(new Callback<ListView<UserGroup>, ListCell<UserGroup>>() {

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
		userGroupCalendar.getSelectionModel().select(PersonInfo.getPersonInfo().getPersonalUserGroup());
		toolTip=new Tooltip("Calendar name has to be at least one character");
		toolTip.setStyle("-fx-background-color: #187E96;");
	}
	
	private void createCalendar(String calendarName) {
		if(calendarName.equals("")) {
			System.out.println("Calendarname cannot be an empty String");
			Stage stage = WindowController.thisStage;
			System.out.println(stage.getX() + "  " + stage.getY());
			toolTip.show(stage, stage.getX() + 300, stage.getY() +210);		
		} else {
            if (toolTip != null) {
                toolTip.hide();
		}
		Requester requester = new Requester();
		ArrayList<UserGroup> ug = new ArrayList<UserGroup>();
		ug.add(userGroupCalendar.getSelectionModel().getSelectedItem());
		try {
			Calendar cal = requester.createCalendar(new Calendar(0, newCalendarTextField.getText(), ug));
			if (cal.getCalendarID() != 0) {
				newCalendarWindow.setVisible(false);
				newCalendarTextField.setText("");
			}
			requester.closeConnection();
			int colorID = SidebarController.getController().getCalendars().size()+1;
			if(colorID > 9) {
				colorID = colorID % 10;
			}
			cal.setColorID(colorID);
			cal.setEvents(new ArrayList<Event>());
			PersonInfo.getPersonInfo().addCalendar(cal);
		} catch (Exception e) {
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