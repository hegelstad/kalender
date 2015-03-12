package controllers;

import java.util.ArrayList;

import models.Calendar;
import models.PersonInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import models.CalendarCell;
public class SidebarController {

	private static SidebarController controller;
	
	@FXML private ListView<Calendar> calendarList;
	@FXML private Pane userManagmentPaneButton;
	
	ObservableList<Calendar> calendars = FXCollections.observableArrayList(new ArrayList<Calendar>());
	
	@FXML private void initialize(){
		controller = this;
		updateCalendarList();
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
    public void openNewCalendar() {
        Scene scene = WindowController.thisStage.getScene();
        Pane newCalendarWindow = (Pane) scene.lookup("#newCalendarWindow");
        TextField newCalendarTextField = (TextField) scene.lookup("#newCalendarTextField");
        if (newCalendarWindow.isVisible()){
            newCalendarWindow.setVisible(false);
            newCalendarTextField.setText("");
        } 
        
        else {
            newCalendarWindow.setVisible(true);
            newCalendarTextField.requestFocus();
            
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
