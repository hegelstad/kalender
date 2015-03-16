package models;
import controllers.SidebarController;
import controllers.WeekController;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
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

    public CalendarCellSearch(){

    }

    private void init(Calendar cal){
        Pane pane = new Pane();
        CheckBox checkbox = new CheckBox();
        ArrayList<Calendar> calendarsUse = PersonInfo.getPersonInfo().getCalendarsInUse();
        checkbox.setSelected(false);
        for (Calendar c : calendarsUse){
            if(c.getCalendarID() == cal.CalendarID){
                checkbox.setSelected(true);
            }
        }
        checkbox.setLayoutY(2);
        label = new Label(cal.getName());
        label.setLayoutX(25);
        label.setLayoutY(2);
        label.setTextFill(Color.web("#0076a3"));
        if(cal.getName().length() > 20) {
            label.setPrefWidth(100);
            label.setPrefHeight(40);
            label.setWrapText(true);
        }
        cal.setColorID(10);
        pane.getStyleClass().add(0, "cornflowerblue");
        pane.getChildren().addAll(checkbox,label);
        setId("calendar-cell");
        checkbox.selectedProperty().addListener( (ob,oldVal,newVal) -> {
            if(newVal)
            {
                Requester r = new Requester();
                ArrayList<Event> calendarEvents = r.getEvents(cal);
                r.closeConnection();
                ArrayList<Event> allEvents = PersonInfo.getPersonInfo().getEvents();
                cal.setEvents(calendarEvents);
                //allEvents.addAll(calendarEvents.stream().collect(Collectors.toList()));
                //PersonInfo.personInfo.setEvents(allEvents);
                PersonInfo.getPersonInfo().addCalendar(cal);
                ArrayList<Calendar> subscribedCalendars = PersonInfo.getPersonInfo().getSubscribedCalendars();
                subscribedCalendars.add(cal);
                PersonInfo.personInfo.setSubscribedCalendars(subscribedCalendars);
            }
            else
            {
                PersonInfo.getPersonInfo().removeCalendar(cal);
                ArrayList <Calendar> subscribedCalendars = PersonInfo.getPersonInfo().getSubscribedCalendars();
                subscribedCalendars.remove(cal);
                PersonInfo.personInfo.setSubscribedCalendars(subscribedCalendars);
                SidebarController.getController().displaySubscribedCalendars();
            }
        });

        //label.setOnMouseClicked(new EventHandler<MouseEvent>() {
        //    @Override
        //    public void handle(MouseEvent mouseEvent) {
        //        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
        //            if(mouseEvent.getClickCount() == 2){
        //                PersonInfo.getPersonInfo().setSelectedCalendar(cal);
        //                System.out.println("Selected calendar: " + PersonInfo.getPersonInfo().getSelectedCalendar());
        //                SidebarController.getController().weekInit();
        //            }
        //        }
        //    }
        //});

        //if (PersonInfo.getPersonInfo().getSelectedCalendar().equals(cal)){
        //    System.out.println("Updating font to bold for: " + cal);
        //    label.setFont(Font.font(null, FontWeight.BOLD, 13));
        //}else{
        //    System.out.println("Setting normal font for : " + cal);
        //    label.setFont(Font.font(null, FontWeight.NORMAL, 13));;
        //}
        setGraphic(pane);
    }

    @Override
    public void updateItem(Calendar cal, boolean empty){
        super.updateItem(cal, empty);
//		if(cal!=null){
//			System.out.println("\n\n");
//			System.out.println("Kalender kommer inn "+cal.getName());
//			System.out.println("\n\n");
//		}

        if(cal!=null&&!empty){
            init(cal);
        }
        else{
            setGraphic(null);
        }
    }



}