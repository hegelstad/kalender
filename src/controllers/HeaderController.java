package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import models.Notification;
import models.NotificationCell;
import models.PersonInfo;
import socket.Requester;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class HeaderController {

	ListView<Notification> notificationList;
    ObservableList<Notification> notifications;
    private static HeaderController controller;
    //sad

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
    @FXML private Button notificationButton;

    LocalDate date = LocalDate.now();
    ArrayList<Label> weekday_labels = new ArrayList<>();
    Notification selected_notification;
    int weekNumber;

    @FXML private void initialize() {
    	controller = this;
		/* Add all labels to an ArrayList<Label> */
        Label[] temp_weekday_labels = {mondayDayOfWeek, tuesdayDayOfWeek, wednesdayDayOfWeek, thursdayDayOfWeek,
                fridayDayOfWeek, saturdayDayOfWeek, sundayDayOfWeek};
        weekday_labels.addAll(Arrays.asList(temp_weekday_labels));
        notificationButton.setText(Integer.toString(PersonInfo.getPersonInfo().getNotifications().size()));
        System.out.println("HeaderController inited");
    }

    @FXML private void addEventOnAction() {
        WindowController.goToEventView(null);
    }

    @FXML private void incrementWeek() {

		/* Add 1 week to LocalDate date */
        date = date.plusWeeks(1);
        updateDates();
    }

    @FXML private void decrementWeek() {

		/* Subtract 1 week of LocalDate date */
        date = date.minusWeeks(1);
        updateDates();
        
    }

    @FXML private void updateDates() {
		
		/* Set current week number */
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        weekNr.setText("" + weekNumber);
        
        /* Update drawn events when changing week */
        drawEventsForWeek();
        
		/* Set current month/year */
        month_Year.setText(date.getMonth() + " " + date.getYear());
		
		/* Set date of weekday_labels */
        for (int i = 0; i < weekday_labels.size(); i++) {
            int date_value = date.with(weekFields.getFirstDayOfWeek()).plusDays(i).getDayOfMonth();
            weekday_labels.get(i).setText("" + date_value + ".");
        }
    }

    @FXML private void logOff() {
		/* Sends the user to the log-in screen */
        WindowController.goToLogin();
    }

    @FXML private void openNotifications() {
        Scene scene = WindowController.thisStage.getScene();
        Pane notificationWindow = (Pane) scene.lookup("#notificationWindow");
        notificationList = (ListView) scene.lookup("#notificationList");

        if (notificationWindow.isVisible()) {
            notificationWindow.setVisible(false);
        } else {
            Requester requester = new Requester();

            /* Replace new Person med PersonInfo.getPerson() */
            ArrayList<Notification> notes = requester.getNotifications(PersonInfo.getPersonInfo().getPersonalUserGroup());
            notifications = FXCollections.observableArrayList(notes);
            notificationList.setItems(notifications);

            notificationList.setCellFactory((list) -> {
                return new NotificationCell(notifications);
            });
            notificationWindow.setVisible(true);
        }
    }
    
    public static HeaderController getController(){
    	return controller;
    }
    
    public void weekInit(){
    	updateDates();
    }
    
    public void drawEventsForWeek(){
    	/* Draw events for this week */
        WeekController.getController().drawEvents(PersonInfo.getPersonInfo().getEventsForWeek(weekNumber));
//		
    }
    
}