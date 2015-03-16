package controllers;

import java.util.ArrayList;

import models.Calendar;
import models.PersonInfo;
import models.UserGroup;
import socket.Requester;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class SuperController {
	@FXML private TextField newCalendarTextField;
	@FXML private Pane newCalendarWindow;
	@FXML private Rectangle newCalendarRectangle;
	@FXML private Button newCalendarButton;
	@FXML private Pane addNewCalendarPaneButton;
	
	
	@FXML
	private void initialize(){
		Rectangle shape = new Rectangle(1, 1);
		newCalendarTextField.setShape(shape);
		addNewCalendarPaneButton.setOnMouseClicked( (mouseEvent) -> {
			{
				if(newCalendarTextField.getText().equals("")){
					System.out.println("Calendarname cannot be an empty String");
				}
				else
				{
					Requester requester = new Requester();
					ArrayList<UserGroup> ug = new ArrayList<UserGroup>();
					ug.add(PersonInfo.getPersonInfo().getPersonalUserGroup());
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
						PersonInfo.getPersonInfo().addCalendar(cal);
					} catch (Exception e){
						System.out.println("Something failed while trying to create calendar!");
					}
				}
			}
		});
	}
	
	@FXML
	public void keyPressed(KeyEvent key){
		if(key.getCode().equals(KeyCode.ESCAPE)){
			WindowController.logOff();
		}
	}
}
