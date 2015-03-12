package controllers;

import models.Calendar;
import models.PersonInfo;
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
				else{
//					Hva gjør vi her mtp calendarID?? Får også feilmelding her
//					Requester requester = new Requester();
//					requester.createCalendar(new Calendar(100, newCalendarTextField.getText(), PersonInfo.getPersonInfo().getUsergroups()));
//					requester.closeConnection();
//				}
			}
		}});
	}
	
	@FXML
	public void keyPressed(KeyEvent key){
		if(key.getCode().equals(KeyCode.ESCAPE)){
			WindowController.logOff();
		}
	}
}
