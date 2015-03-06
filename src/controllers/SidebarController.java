package controllers;

import models.Calendar;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

public class SidebarController {

	@FXML private CheckBox defaultCalendar;
	
	
	//Is supposed to set the defaultCalendar text to the currently logged in user's username.
	@FXML private void initialize(){
		//defaultCalendar.setText("Test");
	}
	
	private void addCalendar(Calendar cal){
		
		CheckBox checkbox = new CheckBox();
		Label label = new Label(cal.getName());
		
		checkbox.selectedProperty().addListener( (ob,oldVal,newVal) -> {
			if(newVal)
			{
				WeekController.getController().addCalendarEvents(cal);
			}
			else
			{
				WeekController.getController().removeCalendarEvents(cal);
			}
			}
		);
		
		/* Must add new button and label to scene here */
	}
	
}
