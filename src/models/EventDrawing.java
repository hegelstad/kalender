package models;

import java.time.LocalDateTime;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.scene.layout.Pane;
import socket.Requester;
import controllers.HeaderController;
import controllers.WeekController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import controllers.WindowController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class EventDrawing {
	Rectangle eventRectangle;
	Text eventName;
	Event event;
	Circle statusCircle;
	public int indent;
	public int reverseIndent;
	boolean isExpanded = false;
	WeekController controller;
	/* Only used if day event is over several days */
	private int dayOfDrawing;
	private boolean contextMenuIsOpen = false;
	public Pane pane =  new Pane();;

	public final double fullEventWidth = 155;
	public final double fullEventWidthPrecise = 153;
	public final double hourHeight = 65;
	public final double hourHeightPrecise = 65;
	public final double indentMargin = 15.0;

	public EventDrawing(Event event, int dayOfEvent, WeekController controller) {
		this.event = event;
		this.dayOfDrawing = dayOfEvent;
		this.controller = controller;
	}

	public void draw() {
		drawEvent();
		setMouseListeners();
	}

	public void toFront() {
		pane.toFront();
	}

	private void setMouseListeners() {
		pane.setOnMouseEntered(enterEvent -> {
			controller.setMouseOverEvent(true);
			toFront();
			eventRectangle.opacityProperty().set(0.9);
			eventRectangle.strokeProperty().set(Color.CADETBLUE);
		});

		pane.setOnMouseExited(exitEvent -> {
			controller.setMouseOverEvent(false);
			// eventRec.setWidth(controller.fullEventWidth-3);
				if (!isExpanded) {
					eventRectangle.opacityProperty().set(0.5);
				}
				eventRectangle.strokeProperty().set(Color.BLACK);
			});

		pane.setOnMouseClicked(clickEvent -> {
			if (clickEvent.getClickCount() == 2) {
				controller.openEvent(event);
				return;
			} else if (clickEvent.getButton() == MouseButton.SECONDARY) {
				if (!contextMenuIsOpen) {
					int calID = event.getCal().CalendarID;
					System.out.println("Calendar : " + calID);
					final ContextMenu contextMenu = new ContextMenu();
					MenuItem item1 = new MenuItem("Edit");
					MenuItem item2 = new MenuItem("Delete");
					item1.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent e) {
							controller.openEvent(event);
						}
					});
					item2.setOnAction(new EventHandler<ActionEvent>() {
						boolean eventWasDeleted = false;
					    @Override public void handle(ActionEvent e) {
					    	ArrayList<Calendar> calendars = PersonInfo.getPersonInfo().getAllCalendars();
					    	for (Calendar cal: calendars){
					    		if (cal.getCalendarID() == calID){
					    			cal.getEvents().remove(event);						    	   
						    		Requester requester = new Requester();
						    		requester.deleteEvent(event);
						    		requester.closeConnection();
						    		HeaderController.getController().drawEventsForWeek();
						    		System.out.println(eventName + " is deleted!");
						    		eventWasDeleted = true;
						    		
					    	}
					    		}
					    	if (!eventWasDeleted){
					    		WindowController.warning("You cannot delete other users events");
						    }
						}});
					contextMenu.getItems().addAll(item1, item2);
					contextMenu.show(eventRectangle, clickEvent.getScreenX(), clickEvent.getScreenY());
					contextMenu.focusedProperty().addListener(new ChangeListener<Boolean>()
							{
						@Override
						public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue)
						{
							if (newPropertyValue)
							{
				
							}
							else
							{
								contextMenu.hide();
								contextMenuIsOpen=false;
							}
						}
							});
					contextMenuIsOpen=true;
				}
				else {
					System.out
							.println("Context menu is already open for this event");
				}

			}
		});
	}

	private boolean isOverMidnight(Event event) {
		LocalDateTime midnight = event.getFrom().withHour(0).withMinute(0);
		return event.getTo().isAfter(midnight);
	}

	public void drawEvent() {
		for (Calendar cal : PersonInfo.getPersonInfo().getAllCalendars()) {
			if (cal.getCalendarID() == event.getCal().getCalendarID()) {
				event.getCal().setColorID(cal.getColorID());
			}
		}
		for (Calendar cal : PersonInfo.getPersonInfo().getSubscribedCalendars()) {
			if (cal.getCalendarID() == event.getCal().getCalendarID()) {
				event.getCal().setColorID(cal.getColorID());
			}
		}
		//System.out.println(event.getCal().getColorID());
		//System.out.println("Drawing : "+event.getName() + " id:" + event.getEventID() + " with colorID : "+event.getCal().getColorID());
		pane.setPrefHeight(getEventHeight(event));
		pane.setMaxWidth(fullEventWidth - 3
				- (indentMargin * reverseIndent));
		Rectangle eventRec = new Rectangle(pane.getMaxWidth(), getEventHeight(event));
		styleRectangle(eventRec, event);
		Text eventName;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
		if(eventRec.getHeight() > 30) {
			eventName = new Text(event.getFrom().format(formatter) + "\n" + event.getName());
		}
		 else {
			eventName = new Text(event.getName());
		}
		eventName.setWrappingWidth(eventRec.getWidth() - 10);
		eventName.setLayoutY(15);
		eventName.setLayoutX(5);
		styleText(eventName);
		Circle statusCircle = new Circle(4);
		statusCircle.setLayoutX(eventRec.getWidth() - 7);
		statusCircle.setLayoutY(7); 

		switch (event.getAttends()) {
		case 0:
			statusCircle.setFill(Color.GOLDENROD);
			break;
		case 1:
			statusCircle.setFill(Color.DARKGREEN);
			break;
		case -1:
			statusCircle.setFill(Color.TRANSPARENT);
			break;
		default:
			statusCircle.setFill(Color.BROWN);
			break;
		}
		
		pane.getChildren().addAll(eventRec,statusCircle, eventName);

		double eventIndentMargin = indentMargin * indent;
		// double reverseIndentMargin = indentMargin*reverseIndent;

		double marginTop = 0.0;
		/* If event is at day 0 draw according to start , else on top */
		if (dayOfDrawing == 0) {
			marginTop = ((double) event.getFrom().getMinute() / 60)
					* hourHeight;
		}

		Insets eventMargin = new Insets(marginTop, 0, 0, eventIndentMargin);
		GridPane.setMargin(pane, eventMargin);
		
		double rightMargin = fullEventWidth - (double) reverseIndent
				* indentMargin + (double) indent * indentMargin - 13;
		// System.out.println("reverse indent : "+reverseIndent+" rightMargin circle : "+rightMargin
		// );
		Insets circleMargin = new Insets(marginTop + 3, 0, 0, rightMargin);
		GridPane.setMargin(statusCircle, circleMargin);

		int dayOfWeek = event.getFrom().getDayOfWeek().getValue() - 1
				+ dayOfDrawing;
		// System.out.println(event.getName()+ " ukedag :"+dayOfWeek);
		int startHour = 0;
		if (dayOfDrawing == 0) {
			startHour = event.getFrom().getHour();
		}
		// weekGrid.setGridLinesVisible(true);
		controller.weekGrid.add(pane, dayOfWeek, startHour, 1, 1);
		// System.out.println(eventRec);
		this.eventRectangle = eventRec;
		this.eventName = eventName;
		this.statusCircle = statusCircle;
	}

	private void styleRectangle(Rectangle r, Event e) {
		r.setArcHeight(3);
		r.setArcWidth(3);
		r.strokeProperty().set(Color.BLACK);
		r.strokeWidthProperty().set(1);
		r.fillProperty().setValue(e.getCal().getColor());
		r.opacityProperty().set(0.5);
	}

	private void styleText(Text t) {

	}

	private double getEventHeight(Event e) {
		LocalDateTime from = e.getFrom().plusSeconds(0);
		LocalDateTime to = e.getTo().plusSeconds(0);
		/* Sjekker om dagen ikke er den første eller siste */
		if (dayOfDrawing != 0
				&& dayOfDrawing + from.getDayOfWeek().getValue() != to
						.getDayOfWeek().getValue()) {
			to = from.withHour(23).withMinute(59);
			from = to.withHour(0).withMinute(0);
		}
		/* Sjekker om dagen er siste i event over flere dager  */
		else if (dayOfDrawing != 0 && from.plusDays(dayOfDrawing).getDayOfWeek() == to.getDayOfWeek()) {
			from = to.withHour(0).withMinute(0);
		}
		/* Sjekker om dagen er første i event over flere dager*/
		else if(to.isAfter(from.withHour(23).withMinute(59))){
			to = from.withHour(23).withMinute(59);
		}
		double minDiff = (to.getHour() - from.getHour()) * 60
				+ (to.getMinute() - from.getMinute());
		double height = (minDiff / 60.0) * 66;
		System.out.println("HEIGHT :    " + height);
		return height;
	}

	public void remove() {
		controller.weekGrid.getChildren().remove(pane);
		//controller.weekGrid.getChildren().remove(statusCircle);
		//controller.weekGrid.getChildren().remove(eventName);
	}

	public Event getEvent() {
		return event;
	}
}
