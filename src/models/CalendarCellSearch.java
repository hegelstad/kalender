package models;

import controllers.HeaderController;
import controllers.SidebarController;
import controllers.WeekController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import socket.Requester;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CalendarCellSearch extends ListCell<Calendar> {

    public Label label;

    public CalendarCellSearch() {

    }

    private void init(Calendar cal) {
        Pane pane = new Pane();
        CheckBox checkbox = new CheckBox();
        ArrayList<Calendar> calendarsUse = PersonInfo.getPersonInfo().getCalendarsInUse();
        checkbox.setSelected(false);
        for (Calendar c : calendarsUse) {
            if (c.getCalendarID() == cal.CalendarID) {
                checkbox.setSelected(true);
            }
        }
        checkbox.setLayoutY(2);
        label = new Label(cal.getName());
        label.setLayoutX(25);
        label.setLayoutY(2);
        label.setTextFill(Color.web("#0076a3"));
        if (cal.getName().length() > 20) {
            label.setPrefWidth(100);
            label.setPrefHeight(40);
            label.setWrapText(true);
        }
        cal.setColorID(10);
        pane.getStyleClass().add(0, "cornflowerblue");
        pane.getChildren().addAll(checkbox, label);
        setId("calendar-cell");
        checkbox.selectedProperty().addListener((ob, oldVal, newVal) -> {
            if (newVal) {
                Requester r = new Requester();
                ArrayList<Event> calendarEvents = r.getEvents(cal);
                r.closeConnection();
                ArrayList<Event> allEvents = PersonInfo.getPersonInfo().getEvents();
                cal.setEvents(calendarEvents);
                //allEvents.addAll(calendarEvents.stream().collect(Collectors.toList()));
                //PersonInfo.personInfo.setEvents(allEvents);
                PersonInfo.getPersonInfo().addCalendarInUse(cal);
                ArrayList<Calendar> subscribedCalendars = PersonInfo.getPersonInfo().getSubscribedCalendars();
                if (!subscribedCalendars.contains(cal)) {
                    subscribedCalendars.add(cal);
                }
                PersonInfo.personInfo.setSubscribedCalendars(subscribedCalendars);
                HeaderController.getController().drawEventsForWeek();
            } else {
                PersonInfo.getPersonInfo().removeCalendarInUse(cal);
                ArrayList<Calendar> subscribedCalendars = PersonInfo.getPersonInfo().getSubscribedCalendars();
                //subscribedCalendars.remove(cal);
                PersonInfo.personInfo.setSubscribedCalendars(subscribedCalendars);
                SidebarController.getController().displaySubscribedCalendars();
            }
        });

        if(PersonInfo.getPersonInfo().getSubscribedCalendars().contains(cal)) {
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                        final ContextMenu contextMenu = new ContextMenu();
                        MenuItem item = new MenuItem("Unsubscribe");
                        item.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent e) {
                                ArrayList<Calendar> subscribedCalendars = PersonInfo.getPersonInfo().getSubscribedCalendars();
                                subscribedCalendars.remove(cal);
                                PersonInfo.getPersonInfo().removeCalendarInUse(cal);
                                PersonInfo.personInfo.setSubscribedCalendars(subscribedCalendars);
                                SidebarController.getController().displaySubscribedCalendars();
                            }
                        });
                        contextMenu.getItems().addAll(item);
                        setContextMenu(contextMenu);
                    }
                }
            });
        }
        setGraphic(pane);
    }

    @Override
    public void updateItem(Calendar cal, boolean empty) {
        super.updateItem(cal, empty);
//		if(cal!=null){
//			System.out.println("\n\n");
//			System.out.println("Kalender kommer inn "+cal.getName());
//			System.out.println("\n\n");
//		}

        if (cal != null && !empty) {
            init(cal);
        } else {
            setGraphic(null);
        }
    }


}