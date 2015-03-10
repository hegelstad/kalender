package controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import models.Calendar;
import models.Event;
import models.EventDrawing;
import models.PersonInfo;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class WeekController {

	public static WeekController controller;
	public final double fullEventWidth = 155;
	public final double fullEventWidthPrecise = 155; 
	public final double hourHeight = 66;
	public final double hourHeightPrecise = 65.5; 
	public final double indentMargin = 15.0;
	boolean mouseOverEvent = false;
	ArrayList<EventDrawing> eventDrawings = new ArrayList<>();
	ArrayList<Event> allEvents = new ArrayList<>();
	private ArrayList<Rectangle> allRecs= new ArrayList<>();
	private ArrayList<Text> allTexts = new ArrayList<>();
	private ArrayList<Circle> allCircs = new ArrayList<Circle>();
	@FXML
	GridPane weekGrid;
	@FXML 
	ScrollPane WeekView;
	
	/* Making some testdata */  /*
	Calendar cal = new Calendar(1, "SuperKalender", null);
	Event event = new Event(1, "Møte",null, null, LocalDateTime.now(), LocalDateTime.now().plusHours(2) , cal);
	Event event2 = new Event(2, "Annet Møte",null, null, LocalDateTime.now().plusMinutes(15)
			, LocalDateTime.now().plusHours(1).plusMinutes(15) , cal);

	Event event3 = new Event(3, "Siste m�te",null, null, LocalDateTime.now().plusHours(1)
			, LocalDateTime.now().plusHours(2) , cal);*/
	/* End of making testdata */

	public WeekController(){
		
	}
	
	@FXML
	private void initialize(){
		controller = this;
		for(RowConstraints r :weekGrid.getRowConstraints()){
			r.setValignment(VPos.TOP);
		}
		for(ColumnConstraints r :weekGrid.getColumnConstraints()){
			r.setHalignment(HPos.LEFT);
		
		}
		weekGrid.setGridLinesVisible(true);
		
		/*Lager testdata */
//		for(int i = 0;i<7;i++){
//			Event e = new Event(i, "Dag :"+i,null, null, LocalDateTime.of(2015, 3, 2+i, 10, 0)
//					, LocalDateTime.of(2015, 3, 2+i, 15, 0), cal);
//			drawnEvents.add(e);
//		}
//		drawnEvents.add(event);drawnEvents.add(event2);drawnEvents.add(event3);
		//drawEvent(event);
		//drawEvent(event2);
		
		weekGrid.setOnMouseClicked( (mouseEvent) -> {
			if(mouseOverEvent){
				return;
			}
			double y = mouseEvent.getY();
			double x = mouseEvent.getX();
			int row = (int) (x/fullEventWidthPrecise);
			int column = (int) (y/hourHeightPrecise); 
			//System.out.println("Row :"+row+" Col: "+column);				
			LocalDateTime from = LocalDateTime.of(2015, 3, 2+row, column, 0);
			LocalDateTime to = LocalDateTime.of(2015, 3, 2+row, column+1, 0);
			Event clickEvent = new Event(0, "Ny hendelse",null, null, from, to, null);
			/* Tegner ny event, den vil blir erstatted av den ferdigredigerte hendelsen eller 
			 * fjernet ved omtegning av eventer som blir gjort ved � g� ut av eventEdit*/
			drawEvent(clickEvent,0,0);
			openEvent(clickEvent);
		});
		
		System.out.println("WeekController inited");
		HeaderController.getController().weekInit();
		SidebarController.getController().weekInit();
		WeekView.setVvalue(8000);
	}
	
	private void drawEvent(Event event,int indent, int reverseIndent){
		System.out.println("Drawing : "+event.getName() + " id:" + event.getEventID());
		
		Rectangle eventRec = new Rectangle(fullEventWidth-3-(indentMargin*reverseIndent), getEventHeight(event));
		styleRectangle(eventRec);
		Text eventName = new Text(event.getName());
		styleText(eventName);
		Circle statusCircle = new Circle(4);
		statusCircle.setFill(Color.BROWN);
		
		
		double eventIndentMargin = indentMargin*indent;
		//double reverseIndentMargin = indentMargin*reverseIndent;
		double marginTop = ((double)event.getFrom().getMinute()/60)*hourHeight;
		
		Insets eventMargin = new Insets(marginTop, 0, 0, eventIndentMargin);
		GridPane.setMargin(eventRec, eventMargin);
		
		Insets textMargin = new Insets(marginTop, 0, 0, 3 + eventIndentMargin);
		GridPane.setMargin(eventName, textMargin);
		
		double rightMargin = fullEventWidth-(double)reverseIndent*indentMargin
				+(double)indent*indentMargin-13;
		//System.out.println("reverse indent : "+reverseIndent+" rightMargin circle : "+rightMargin );
		Insets circleMargin = new Insets(marginTop+3, 0,0,rightMargin);
		GridPane.setMargin(statusCircle,circleMargin);
		
		int dayOfWeek = event.getFrom().getDayOfWeek().getValue()-1;
		//System.out.println(event.getName()+ " ukedag :"+dayOfWeek);
		int startHour = event.getFrom().getHour();
		//weekGrid.setGridLinesVisible(true);
		weekGrid.add(eventRec, dayOfWeek, startHour, 1, 1);
		weekGrid.add(statusCircle, dayOfWeek, startHour, 1,1);
		//System.out.println(eventRec);
		weekGrid.add(eventName, dayOfWeek, startHour, 1, 1);
		eventDrawings.add(new EventDrawing(eventRec, eventName, event,statusCircle, this));
		allRecs.add(eventRec);
		allTexts.add(eventName);
		allCircs.add(statusCircle);
		
	}
	
	private double getEventHeight(Event e){
		LocalDateTime from = e.getFrom();
		LocalDateTime to = e.getTo();
		double minDiff = (to.getHour()-from.getHour())*60 + (to.getMinute()-from.getMinute());
		double height = minDiff/60.0 * 65.5;
		//System.out.println(height);
		if(height<0){
			return -height;
		}
		return height;
	}
	
	private void styleRectangle(Rectangle r){
		r.setArcHeight(3);
		r.setArcWidth(3);
		r.strokeProperty().set(Color.BLACK);
		r.strokeWidthProperty().set(1);
		r.fillProperty().set(Color.LIGHTSKYBLUE);
		r.opacityProperty().set(0.5);
	}
	
	private void styleText(Text t){
		
	}
	
	public void openEvent(Event event){
		WindowController.goToEventView(event);
		//System.out.println(event.getName());
	}
	
	/*
	 * Replaces all currently drawn events with incoming events
	 */
	public void drawEvents(Collection<Event> sortedEvents){
		
		if(sortedEvents == null){
			System.out.println("Ingen faktiske skikkelige eventer hentet fra databasen");
			return;
		}
		
		removeAllDrawings();
		
		ArrayList<Event> events;
		
		if(sortedEvents instanceof TreeSet && sortedEvents.size()!=0){
			
			System.out.println("\n\n");
			System.out.println("Finner data");
			System.out.println(sortedEvents);
			events = new ArrayList<Event>(sortedEvents);
		}
		else if(sortedEvents.size()==0)
		{
			System.out.println("Tom eventliste");
			return;
		}
		else
		{
			/* Kun testdata finnes */
			events = (ArrayList<Event>) sortedEvents;
			Collections.sort(events, (e1,e2)->{
				return e1.getFrom().isBefore(e2.getFrom()) ? -1: e1.getFrom().isEqual(e2.getFrom())?0:1;
			});
		}
		
		fixEventsOverADay(events);
		
		int indent = 0;
		int reverseIndent = 0;
		for(int k =0; k<events.size(); k++){
			Event currentEvent = events.get(k);
			
			//G� oppover til det ikke overlapper og sett indent
			
			for(int i=k-1; i>-1; i--){
				
				if(currentEvent.getFrom().isBefore(events.get(i).getTo())){
					indent += 1;
					//System.out.println(currentEvent.getName()+ " har oppoverlapp med " + events.get(i).getName());
				}
				else{
					break;
				}
			}
			
			//G� nedover til det ikke overlapper og sett reverseIndent
			
			for(int i=k+1; i<events.size(); i++){
				if(currentEvent.getTo().isAfter(events.get(i).getFrom())){
					reverseIndent += 1;
					//System.out.println(currentEvent.getName()+ " har nedoverlapp");
					/* Om flere eventer slutter sist og samtidig */
					if(i==events.size()-1){
						drawEvent(currentEvent, indent, reverseIndent+indent);
						reverseIndent = 0;
						indent = 0;
					}
				}
				else
				{
					drawEvent(currentEvent, indent, reverseIndent+indent);
					reverseIndent = 0;
					indent = 0;
					break;
				}
			}
			
			//System.out.println(currentEvent.getName()+" indent: "+indent+" reverseIndent: "+reverseIndent+indent);
			/* Siste event kommer seg gjennom siste l�kken uten � sjekkes (i= k+1) */
			if(k == events.size()-1){
				drawEvent(currentEvent,indent,reverseIndent+indent);
			}
			
			
		}
		
	}
	
	private void fixEventsOverADay(ArrayList<Event> events) {
		
		
	}

//	public void removeCalendarEvents(Calendar cal){
//		allEvents.removeAll(cal.getEvents());
//		removeAllDrawings();
//		drawEvents(allEvents);
//	}
//	
//	public void addCalendarEvents(Calendar cal){
//		allEvents.addAll(cal.getEvents());
//		removeAllDrawings();
//		drawEvents(allEvents);
//	}
	
	public void setMouseOverEvent(boolean isOver){
		mouseOverEvent = isOver;
	}

	public void removeAllDrawings(){
		weekGrid.getChildren().removeAll(allRecs);
		weekGrid.getChildren().removeAll(allTexts);
		weekGrid.getChildren().removeAll(allCircs);
	}
	
	public static WeekController getController(){
		return controller;
	}
	
	
}
