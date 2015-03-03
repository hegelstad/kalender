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
	final double indentMargin = 15;
	@FXML
	GridPane weekGrid;
	Calendar cal = new Calendar(1, "SuperKalender", null);
	Event event = new Event(1, "Møte", null, LocalDateTime.now(), LocalDateTime.now().plusHours(2) , cal);
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
		//Lager testdata
		ArrayList<Event> events = new ArrayList<Event>();
		for(int i = 0;i<7;i++){
			Event e = new Event(i, "Dag :"+i, null, LocalDateTime.of(2015, 3, 2+i, 10, 0)
					, LocalDateTime.of(2015, 3, 2+i, 15, 0), cal);
			events.add(e);
		}
		events.add(event);events.add(event2);
		//drawEvent(event);
		//drawEvent(event2);
		for(Event drawSubject : events){
			drawEvent(drawSubject);
		}
		
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
			openEvent(event);				
		});

		double eventIndentMargin = indentMargin*getIndentIndex(event);
		
		double marginTop = ((double)event.getFrom().getMinute()/60)*hourHeight;
		Insets eventMargin = new Insets(marginTop, 0, 0, eventIndentMargin);
		GridPane.setMargin(eventRec, eventMargin);
		
		Insets textMargin = new Insets(marginTop, 0, 0, 3 + eventIndentMargin);
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
		double height = minDiff/60.0 * 65.5;
		System.out.println(height);
		return height;
	}
	
	private void styleRectangle(Rectangle r){
		r.setArcHeight(3);
		r.setArcWidth(3);
		r.strokeProperty().set(Color.BLACK);
		r.strokeWidthProperty().set(1);
		r.fillProperty().set(Color.CHARTREUSE);
		r.opacityProperty().set(0.5);
	}
	
	private void styleText(Text t){
		
	}

	private int getIndentIndex(Event event){
//		if(event.getEventID()==1){
//			return 0;
//		}
//		else{
//			return 1;
//		}
		return 0;
	}
	
	private void openEvent(Event event){
		System.out.println(event.getName());
	}
}
