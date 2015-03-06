package controllers;

import java.util.ArrayList;

import models.Calendar;
import models.PersonInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import models.CalendarCell;
public class SidebarController {

	@FXML private CheckBox defaultCalendar;
	
	@FXML private ListView<Calendar> calendarList;
	
	ObservableList<Calendar> calendars = FXCollections.observableArrayList(new ArrayList<Calendar>());
	
	//Is supposed to set the defaultCalendar text to the currently logged in user's username.
	@FXML private void initialize(){
		//defaultCalendar.setText("Test");
		calendarList.setItems(calendars);
		calendarList.setCellFactory( (list) -> {
			return new CalendarCell();
		});
		Calendar calen = new Calendar(0, "Min kalender", null);
		addCalendar(calen);
		//addCalendars(PersonInfo.getPersonInfo().getAllCalendars());
	}
	
	public void addCalendar(Calendar cal){
		calendars.add(cal);
		/* Must add new button and label to scene here */
	}
	public void addCalendars(ArrayList<Calendar> cals){
		calendars.addAll(cals);
	}
	
}
