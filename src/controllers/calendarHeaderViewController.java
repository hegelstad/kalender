package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.Notification;
import models.Person;
import socket.Requester;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class calendarHeaderViewController {
	
	@FXML private AnchorPane calendarHeaderView;
	
	@FXML private Label month_Year;
	
	@FXML private Label weekNr;
	
	@FXML private Label mondayDayOfWeek;

	@FXML private Label tuesdayDayOfWeek;

	@FXML private Label wednesdayDayOfWeek;

	@FXML private Label thursdayDayOfWeek;

	@FXML private Label fridayDayOfWeek;

	@FXML private Label saturdayDayOfWeek;

	@FXML private Label sundayDayOfWeek;
	
	@FXML
	private void addEventOnAction(){
		WindowController.goToEventView(null);
	}

	LocalDate date = LocalDate.now();
	ArrayList<Label> weekday_labels = new ArrayList<>();
	
	@FXML
	private void initialize(){
		
		/* Add all labels to an ArrayList<Label> */
		Label[] temp_weekday_labels = {mondayDayOfWeek, tuesdayDayOfWeek, wednesdayDayOfWeek, thursdayDayOfWeek,
				fridayDayOfWeek, saturdayDayOfWeek, sundayDayOfWeek};
		weekday_labels.addAll(Arrays.asList(temp_weekday_labels));
		updateDates();
	}
	
	@FXML
	private void incrementWeek(){
		
		/* Add 1 week to LocalDate date */
		date = date.plusWeeks(1);
		updateDates();
	} 
	
	@FXML
	private void decrementWeek(){
		
		/* Subtract 1 week of LocalDate date */
		date = date.minusWeeks(1);
		updateDates();
	}
	
	@FXML
	private void updateDates(){
		
		/* Set current week number */
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		int weekNumber = date.get(weekFields.weekOfWeekBasedYear());
		weekNr.setText("" + weekNumber);
		
		/* Set current month/year */
		month_Year.setText(date.getMonth() + " " + date.getYear());
		
		/* Set date of weekday_labels */
		for(int i = 0; i < weekday_labels.size(); i++){
			int date_value = date.with(weekFields.getFirstDayOfWeek()).plusDays(i).getDayOfMonth();
			weekday_labels.get(i).setText("" + date_value + ".");
		}
	}
	
	@FXML
	private void logOff(){
		
		/* Returns the user to the log-in screen */
		WindowController.goToLogin();
	}
	
	@FXML
	private void openNotifications(){
		
		Scene s = WindowController.thisStage.getScene();
		Pane notificationWindow = (Pane) s.lookup("#notificationWindow");
		ListView <Notification> notificationList = (ListView) s.lookup("#notificationList");
		
		if(notificationWindow.isVisible()){
			notificationWindow.setVisible(false);
		} else {
			Requester r = new Requester();
			ArrayList<Notification> notes = r.getNotifications(new Person("Sondre", "Sondre Hjetland", 4));
			ObservableList<Notification> notifications = FXCollections.observableArrayList(notes);
			notificationList.setFixedCellSize(30);

			notificationList.setItems(notifications);
			notificationWindow.setVisible(true);
		}
		//notificationList.setContent((Node) notifications);
		//System.out.println(notes);
		
	}
}
