package controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import models.Calendar;
import models.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class CalendarEventsViewController {

	final double fullEventWidth = 158;
	final double hourHeight = 66;
	@FXML
	GridPane weekGrid;
	Calendar cal = new Calendar(1, "SuperKalender", null);
	Event event = new Event(1, "Møte", null, LocalDateTime.now(), LocalDateTime.now().plusHours(1) , cal);
	Event event2 = new Event(2, "Annet Møte", null, LocalDateTime.now().plusMinutes(15)
			, LocalDateTime.now().plusHours(1).plusMinutes(15) , cal);
	
	
	
	public CalendarEventsViewController(){
		System.out.println("Konstruktør");
	}
	
	@FXML
	private void initialize(){		
		for(RowConstraints r :weekGrid.getRowConstraints()){
			r.setValignment(VPos.TOP);
		}
		for(ColumnConstraints r :weekGrid.getColumnConstraints()){
			r.setHalignment(HPos.LEFT);
		}
		drawEvent(event);
		drawEvent(event2);
	}
	
	private void drawEvent(Event event){
		
		Rectangle eventRec = new Rectangle(fullEventWidth-3, getEventHeight(event));
		styleRectangle(eventRec);
		Text eventName = new Text(event.getName());
		styleText(eventName);
		
		eventRec.setOnMouseEntered( enterEvent -> {
			eventRec.toFront();
			eventName.toFront();
			eventRec.strokeProperty().set(Color.CADETBLUE);
		});
		
		eventRec.setOnMouseExited( exitEvent -> {
			eventRec.strokeProperty().set(Color.BLACK);
		});
		
		eventRec.setOnMouseClicked(clickEvent -> {
			Object source = clickEvent.getSource();
			openEvent(event);				
		});

		double marginTop = ((double)event.getFrom().getMinute()/60)*hourHeight;
		Insets eventMargin = new Insets(marginTop, 0, 0, 0);
		GridPane.setMargin(eventRec, eventMargin);
		
		Insets textMargin = new Insets(marginTop, 0, 0, 3);
		GridPane.setMargin(eventName, textMargin);
		
		int dayOfWeek = event.getFrom().getDayOfWeek().getValue()-1;
		int startHour = event.getFrom().getHour();
		weekGrid.setGridLinesVisible(true);
		weekGrid.add(eventRec, dayOfWeek, startHour, 1, 1);
		weekGrid.add(eventName, dayOfWeek, startHour, 1, 1);
	}
	
	private ArrayList<Event> eventFormWeek(int i){
		return null;
	}
	
	private double getEventHeight(Event e){
		LocalDateTime from = e.getFrom();
		LocalDateTime to = e.getTo();
		double minDiff = (to.getHour()-from.getHour())*60 + (to.getMinute()-from.getMinute());
		return minDiff;
	}
	
	private void styleRectangle(Rectangle r){
		r.setArcHeight(3);
		r.setArcWidth(3);
		r.strokeProperty().set(Color.BLACK);
		r.strokeWidthProperty().set(1);
		r.fillProperty().set(Color.CHARTREUSE);
	}
	
	private void styleText(Text t){
		
	}

	private void openEvent(Event event){
		System.out.println(event.getName());
	}
}
