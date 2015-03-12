package controllers;

import java.util.ArrayList;

import models.Calendar;
import models.Notification;
import models.NotificationCell;
import models.PersonInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import models.CalendarCell;
public class SidebarController {

	private static SidebarController controller;
	
	@FXML private CheckBox defaultCalendar;
	@FXML private ListView<Calendar> calendarList;
	@FXML private Pane userManagmentPaneButton;


	
	ObservableList<Calendar> calendars = FXCollections.observableArrayList(new ArrayList<Calendar>());
	
	//Is supposed to set the defaultCalendar text to the currently logged in user's username.
	@FXML private void initialize(){
		//defaultCalendar.setText("Test");
		controller = this;
		updateCalendarList();
		//Calendar calen = new Calendar(0, "Min kalender", null);
		//addCalendar(calen);
		for(Calendar cal : PersonInfo.getPersonInfo().getAllCalendars()){
			System.out.println(cal);
			
		}
		userManagmentPaneButton.setOnMouseClicked( (mouseEvent) -> {
			{
				WindowController.goToManageUsersView();
			}
		});
		
	}
	
	@FXML
    private void openNewCalendar() {
        Scene scene = WindowController.thisStage.getScene();
        Pane newCalendarWindow = (Pane) scene.lookup("#newCalendarWindow");
        Node node =newCalendarWindow.getChildren().get(2);
        if (newCalendarWindow.isVisible()){
            newCalendarWindow.setVisible(false);
        } 
        
        else {
            newCalendarWindow.setVisible(true);
            node.requestFocus();
        }
    }
	
	public void updateCalendarList(){
		calendarList.setItems(calendars);
		calendarList.setCellFactory( (list) -> {
			return new CalendarCell();
		});
	}
	
	public void addCalendar(Calendar cal){
		calendars.add(cal);
		/* Must add new button and label to scene here */
	}
	public void addCalendars(ArrayList<Calendar> cals){
		calendars.clear();
		calendars.addAll(cals);
	}
	
	public void weekInit(){
		ArrayList<Calendar> incomingCals = PersonInfo.getPersonInfo().getAllCalendars();
		addCalendars(incomingCals);
	}
	
	public static SidebarController getController(){
		return controller;
	}
	
	public ListView<Calendar> getListView(){
		return calendarList;
	}
	

	
}
