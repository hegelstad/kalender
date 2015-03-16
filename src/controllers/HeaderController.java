package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import socket.Requester;
import models.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

public class HeaderController {

    ListView<Notification> notificationList;
    ObservableList<Notification> notifications;
    private static HeaderController controller;

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
    @FXML private Button addEventButton;
    @FXML private Button incrementWeekPlusButton;
    @FXML private Button incrementWeekMinusButton;
    @FXML private Button logOffButton;

    LocalDate date = LocalDate.now();
    ArrayList<Label> weekday_labels = new ArrayList<>();
    int weekNumber;
    Timer timer;
    Scheduler scheduler;

    @FXML
    private void initialize() {
        controller = this;
        /* Add all labels to an ArrayList<Label> */
        Label[] temp_weekday_labels = {mondayDayOfWeek, tuesdayDayOfWeek, wednesdayDayOfWeek, thursdayDayOfWeek,
                fridayDayOfWeek, saturdayDayOfWeek, sundayDayOfWeek};
        weekday_labels.addAll(Arrays.asList(temp_weekday_labels));
        notificationButton.setText(Integer.toString(PersonInfo.getPersonInfo().getNotifications().size()));
        System.out.println("HeaderController was initiated.");
        timer = new Timer();
        scheduler = new Scheduler();
        timer.scheduleAtFixedRate(scheduler, 1, 10000);
        addEventButton.setTooltip(new Tooltip("Create a new event"));
        incrementWeekPlusButton.setTooltip(new Tooltip("Go to next week"));
        incrementWeekMinusButton.setTooltip(new Tooltip("Go to previous week"));
        notificationButton.setTooltip(new Tooltip("View your notifications"));
        logOffButton.setTooltip(new Tooltip("Log off and return to the login screen"));
    }

    @FXML
    private void addEventOnAction() {
        WindowController.goToEventView(new Event(-1337, "New Event", null, null, null, null, PersonInfo.getPersonInfo().getSelectedCalendar()));
    }

    @FXML
    private void incrementWeek() {
		/* Add 1 week to LocalDate date */
        date = date.plusWeeks(1);
        updateDates();
    }

    @FXML
    private void decrementWeek() {
		/* Subtract 1 week of LocalDate date */
        date = date.minusWeeks(1);
        updateDates();
    }

    @FXML
    private void updateDates() {
		/* Set current week number */
        WeekFields weekFields = WeekFields.of(Locale.ENGLISH);
        weekNumber = date.get(weekFields.weekOfWeekBasedYear());
        weekNr.setText("" + weekNumber);

        /* Update drawn events when changing week */
        drawEventsForWeek();
        
		/* Set current month/year */
        month_Year.setText(date.getMonth() + " " + date.getYear());
		
		/* Set date of weekday_labels */
        for (int i = 0; i < weekday_labels.size(); i++) {
            int date_value = date.with(DayOfWeek.MONDAY).plusDays(i).getDayOfMonth();
            weekday_labels.get(i).setText("" + date_value + ".");
        }
    }

    @FXML
    private void logOff() {
		/* Sends the user to the log-in screen */
        WindowController.logOff();
        timer.cancel();
    }

    @SuppressWarnings("unchecked")
	@FXML
    private void openNotifications() {
        Scene scene = WindowController.thisStage.getScene();
        Pane notificationWindow = (Pane) scene.lookup("#notificationWindow");
        notificationList = (ListView<Notification>) scene.lookup("#notificationList");
        notificationButton.setStyle("-fx-background-color: #187E96");

        if (notificationWindow.isVisible()) {
            notificationWindow.setVisible(false);
        } else {
            ArrayList<Notification> notes = PersonInfo.getPersonInfo().getNotifications();
            notifications = FXCollections.observableArrayList(notes);
            notificationList.setItems(notifications);

            notificationList.setCellFactory((list) -> {
                return new NotificationCell(notifications);
            });
            notificationWindow.setVisible(true);
        }
    }

    public static HeaderController getController() {
        return controller;
    }

    public void weekInit() {
        updateDates();
    }

    public void updateNotifications() {
        int current_num_notifications = PersonInfo.getPersonInfo().getNotifications().size();

        Requester requester = new Requester();
        ArrayList<Notification> temp_notifications = requester.getNotifications(PersonInfo.getPersonInfo().getPersonalUserGroup());
        requester.closeConnection();

        Collections.reverse(temp_notifications);
        PersonInfo.personInfo.setNotifications(temp_notifications);
        int new_num_notifications = PersonInfo.getPersonInfo().getNotifications().size();

        if (current_num_notifications < new_num_notifications) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                	Media notificationSound = new Media(getClass().getResource("/note.mp3").toString());
                	MediaPlayer mp = new MediaPlayer(notificationSound);
                	mp.play();
                    updateNotificationButton(PersonInfo.getPersonInfo().getNotifications().size());
                    notificationButton.setStyle("-fx-background-color: red");
                }
            });
        } 
    }
    public void updateNotificationButton(int value){
        notificationButton.setText(Integer.toString(value));
    }
    
    public void drawEventsForWeek(){
    	/* Draw events for this week */
        WeekController.getController().drawEvents(PersonInfo.getPersonInfo().getEventsForWeek(weekNumber));
    }
    
    /**
     *  This function is supposed to be called from WeekController to get
     *      the correct date for a new event when you click a column.
     **/
    public LocalDateTime getDateForColumn(int column, int row){
    	LocalDate tempDate = date;
    	int dayOfWeek = tempDate.getDayOfWeek().getValue();
    	int calendarDayOfWeek = column+1;
    	int difDayOfWeek = calendarDayOfWeek-dayOfWeek;
    	int dayOfMonth = tempDate.getDayOfMonth();
    	tempDate = tempDate.plusDays(difDayOfWeek);
    	int calendarDayOfMonth = tempDate.getDayOfMonth();
    	int  year = tempDate.getYear();
    	int month = tempDate.getMonthValue();
//    	System.out.println("Column : " + column + " Row : " + row +  " dateDayOfWeek : " + date.getDayOfWeek().getValue());
    	LocalDateTime newDate = LocalDateTime.of(year, month, calendarDayOfMonth, row, 0);
		return newDate;
    }
}