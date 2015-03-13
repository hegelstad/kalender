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
	public final double fullEventWidthPrecise = 153; 
	public final double hourHeight = 66;
	public final double hourHeightPrecise = 66; 
	public final double indentMargin = 15.0;
	boolean mouseOverEvent = false;
	ArrayList<EventDrawing> eventDrawings = new ArrayList<>();
	ArrayList<Event> allEvents = new ArrayList<>();
	private ArrayList<Rectangle> allRecs= new ArrayList<>();
	private ArrayList<Text> allTexts = new ArrayList<>();
	private ArrayList<Circle> allCircs = new ArrayList<Circle>();
	public @FXML GridPane weekGrid;
	@FXML ScrollPane WeekView;
	
	/* Making some testdata */  /*
	Calendar cal = new Calendar(1, "SuperKalender", null);
	Event event = new Event(1, "Møte",null, null, LocalDateTime.now(), LocalDateTime.now().plusHours(2) , cal);
	Event event2 = new Event(2, "Annet Møte",null, null, LocalDateTime.now().plusMinutes(15)
			, LocalDateTime.now().plusHours(1).plusMinutes(15) , cal);

	Event event3 = new Event(3, "Siste m�te",null, null, LocalDateTime.now().plusHours(1)
			, LocalDateTime.now().plusHours(2) , cal);*/
	/* End of making testdata */


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
			if(mouseOverEvent || WindowController.getEventWindowIsOpenOrClosed()){
				return;
			}
			double y = mouseEvent.getY();
			double x = mouseEvent.getX();
			int column = (int) (x/fullEventWidthPrecise);
			int r1 = (int) (y/hourHeightPrecise);
			int row = r1;
			System.out.println("COLUMN " + column);
			System.out.println("ROW    "  + row);
			LocalDateTime from = HeaderController.getController().getDateForColumn(column, row);
			LocalDateTime to = from.plusMinutes(60);
			Event clickEvent = new Event(0, "New event", null, null, from, to, PersonInfo.getPersonInfo().getSelectedCalendar());
			/* Tegner ny event, den vil blir erstatted av den ferdigredigerte hendelsen eller 
			 * fjernet ved omtegning av eventer som blir gjort ved � g� ut av eventEdit*/
			drawEvent(clickEvent,0,0);
			openEvent(clickEvent);
		});
		System.out.println("WeekController inited");
		HeaderController.getController().weekInit();
		SidebarController.getController().weekInit();
	}

	private void drawEvent(Event event,int indent, int reverseIndent){
		for (Calendar cal : PersonInfo.getPersonInfo().getAllCalendars()){
			if (cal.getCalendarID() == event.getCal().getCalendarID()){
				event.getCal().setColorID(cal.getColorID());
			}
		}
		System.out.println("Drawing : "+event.getName() + " id:" + event.getEventID() + " with colorID : " +event.getCal().getColorID());
		Rectangle eventRec = new Rectangle(fullEventWidth-3-(indentMargin*reverseIndent), getEventHeight(event));
		styleRectangle(eventRec, event);
		Text eventName = new Text(event.getName());
		styleText(eventName);
		Circle statusCircle = new Circle(4);
		switch (event.getAttends()){
		case 0:	
			statusCircle.setFill(Color.GOLDENROD);
			break;
		case 1:
			statusCircle.setFill(Color.DARKGREEN);
			break;
		default:
			statusCircle.setFill(Color.BROWN);
			break;
		}
		
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
		double height = (minDiff/60.0) * 66;
//		System.out.println("HEIGHT :    " + height);
		if(height<0){
			return -height;
		}
		return height;
	}
	
	private void styleRectangle(Rectangle r, Event e){
		r.setArcHeight(3);
		r.setArcWidth(3);
		r.strokeProperty().set(Color.BLACK);
		r.strokeWidthProperty().set(1);
		
		switch (e.getCal().getColorID()){
			case 0: r.fillProperty().set(Color.LIGHTSKYBLUE);
					break;
			case 1: r.fillProperty().set(Color.LIGHTSALMON);
					break;
			case 2: r.fillProperty().set(Color.LIGHTGREEN);
					break;
			case 3: r.fillProperty().set(Color.YELLOW);
					break;
			case 4: r.fillProperty().set(Color.LIGHTPINK);
					break;
			case 5: r.fillProperty().set(Color.AQUAMARINE);
					break;
			case 6: r.fillProperty().set(Color.LIGHTCORAL);
					break;
			case 7: r.fillProperty().set(Color.THISTLE);
					break;
			case 8: r.fillProperty().set(Color.BEIGE);
					break;
			case 9: r.fillProperty().set(Color.SILVER);
					break;
			default: r.fillProperty().set(Color.CORNFLOWERBLUE);
					break;
		}
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
			events = new ArrayList<Event>();
			System.out.println(sortedEvents);
			ArrayList<Event> tempList = new ArrayList<>(sortedEvents);
			Collections.copy(events, tempList);
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
			
			//Gå nedover til det ikke overlapper og sett reverseIndent
			
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
			/* Siste event kommer seg gjennom siste løkken uten å sjekkes (i= k+1) */
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
	
	public void setVvalue(){
		WeekView.setVvalue(0.5);
	}
	
}
