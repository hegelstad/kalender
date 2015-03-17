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
	public @FXML GridPane weekGrid;
	@FXML ScrollPane WeekView;

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

		weekGrid.setOnMouseClicked( (mouseEvent) -> {
			if(mouseOverEvent || WindowController.getEventWindowIsOpenOrClosed()){
				return;
			}
			else if(mouseEvent.getClickCount() ==2){
				double y = mouseEvent.getY();
				double x = mouseEvent.getX();
				int column = (int) (x/fullEventWidthPrecise);
				int r1 = (int) (y/hourHeightPrecise);
				int row = r1;
				LocalDateTime from = HeaderController.getController().getDateForColumn(column, row);
				LocalDateTime to = from.plusMinutes(60);
				Event clickEvent = new Event(0, "New event", null, null, from, to, PersonInfo.getPersonInfo().getSelectedCalendar());
				/* Tegner ny event, den vil blir erstatted av den ferdigredigerte hendelsen eller 
				 * fjernet ved omtegning av eventer som blir gjort ved � g� ut av eventEdit*/
				/* TODO: draw new clickevent */
				
				openEvent(clickEvent);
			}});
		System.out.println("WeekController inited");
		HeaderController.getController().weekInit();
		SidebarController.getController().weekInit();
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
//			System.out.println("Ingen faktiske skikkelige eventer hentet fra databasen");
			removeAllDrawings();
			return;
		}

		removeAllDrawings();
		ArrayList<ArrayList<EventDrawing>> weekOfDrawings;		

		if(sortedEvents instanceof TreeSet && sortedEvents.size()!=0){	
//			System.out.println("\n\n");
//			System.out.println("Finner data");
//			System.out.println(sortedEvents);
			weekOfDrawings = sortIntoDays(sortedEvents);
		}
		else if(sortedEvents.size()==0)
		{
			System.out.println("Tom eventliste");
			return;
		}
		else{
			weekOfDrawings = new ArrayList<ArrayList<EventDrawing>>();
		}

		for(ArrayList<EventDrawing> dayOfDrawings : weekOfDrawings){

			for( int k = 0; k<dayOfDrawings.size(); k++){

				int indent = 0;
				int reverseIndent = 0;
				Event currentEvent = dayOfDrawings.get(k).getEvent();
				EventDrawing currentDrawing = dayOfDrawings.get(k);

				//G� oppover til det ikke overlapper og sett indent
				System.out.println(currentEvent.getName());

				for(int i=k-1; i>-1; i--){
					if(currentEvent.getName().equals("TDT shots")){
						System.out.println("*-------------*");
						System.out.println(currentEvent.getFrom());
						System.out.println(dayOfDrawings.get(i).getEvent().getName());
						System.out.println(dayOfDrawings.get(i).getEvent().getTo());
					}
					if(currentEvent.getFrom().isBefore(dayOfDrawings.get(i).getEvent().getTo())){
						indent += 1;
						System.out.println(currentEvent.getName()+ " har oppoverlapp med " + currentEvent.getName());
					}
					else{
						
					}
				}

				//Gå nedover til det ikke overlapper og sett reverseIndent

				for(int i=k+1; i<dayOfDrawings.size(); i++){
					if(currentEvent.getTo().isAfter(dayOfDrawings.get(i).getEvent().getFrom())){
						reverseIndent += 1;
						System.out.println(currentEvent.getName()+ " har nedoverlapp");
						/* Om flere eventer slutter sist og samtidig */
						if(i==dayOfDrawings.size()-1){
							currentDrawing.indent = indent;
							currentDrawing.reverseIndent = (reverseIndent+indent);
							currentDrawing.draw();
							eventDrawings.add(currentDrawing);
							//drawEvent(currentEvent, indent, reverseIndent+indent);
							reverseIndent = 0;
							indent = 0;
						}
					}
					else
					{
						currentDrawing.indent = indent;
						currentDrawing.reverseIndent = (reverseIndent+indent);
						currentDrawing.draw();
						eventDrawings.add(currentDrawing);
						//drawEvent(currentEvent, indent, reverseIndent+indent);
						reverseIndent = 0;
						indent = 0;
						break;
					}
				}

				//System.out.println(currentEvent.getName()+" indent: "+indent+" reverseIndent: "+reverseIndent+indent);
				/* Siste event kommer seg gjennom siste løkken uten å sjekkes (i= k+1) */
				if(k == dayOfDrawings.size()-1){
					currentDrawing.indent = indent;
					currentDrawing.reverseIndent = (reverseIndent+indent);
					currentDrawing.draw();
					eventDrawings.add(currentDrawing);
					//drawEvent(currentEvent,indent,reverseIndent+indent);
				}



			}
		}

	}

	public void setMouseOverEvent(boolean isOver){
		mouseOverEvent = isOver;
	}

	public void removeAllDrawings(){
		for(EventDrawing drawing : eventDrawings){
			drawing.remove();
		}
		eventDrawings.clear();
	}

	public static WeekController getController(){
		return controller;
	}

	/**
	 * Set scrollbar to a resonable value , not starting from 00.00 o clock
	 */
	public void setVvalue(){
		WeekView.setVvalue(0.5);
	}

	private ArrayList<ArrayList<EventDrawing>> sortIntoDays(Collection<Event> events){
		ArrayList<ArrayList<EventDrawing>> weekEvents = new ArrayList<ArrayList<EventDrawing>>();

		/* Initialise arrayLists */
		for(int i=0; i<7;i++){
			weekEvents.add(new ArrayList<EventDrawing>());
		}

		/* Put corresponding EventDrawing into correct day */
		for(int i=0; i<7;i++){
			for(Event event : events){
				/* Finnes en event som startet minst dagen f�r og slutter idag eller senere */
				if(startedBeforeToday(event,i)&&endsTodayOrAfter(event,i))
				{
					System.out.println("Lager eventDrawing som g�r over flere dager : "+event.getName());
					weekEvents.get(i).add(new EventDrawing(event,getDaysFromStart(event,i),this));
				}
				else if(startsAndEndsToday(event,i)||startsToday(event,i))
				{
					/* Event starter denne dagen */ 
					weekEvents.get(i).add(new EventDrawing(event,0,this));
				}
				
			}

		}
		return weekEvents;
	}

	private boolean isOverMidnight(Event event){
		LocalDateTime midnight = event.getFrom().withHour(0).withMinute(0);
		return event.getTo().isAfter(midnight);
	}

	private boolean startedBeforeToday(Event event, int weekDay){
		int dayOfWeek = event.getFrom().getDayOfWeek().getValue();
		return dayOfWeek < weekDay; 
	}
	private boolean endsTodayOrAfter(Event event, int weekDay){
		int dayOfWeek = event.getTo().getDayOfWeek().getValue();
		return dayOfWeek >= weekDay;
	}
	private boolean startsAndEndsToday(Event event , int weekDay){
		int startDayOfWeek = event.getTo().getDayOfWeek().getValue();
		int endDayOfWeek = event.getFrom().getDayOfWeek().getValue();
		return (startDayOfWeek == weekDay) && (startDayOfWeek == endDayOfWeek);
	}
	private boolean startsToday(Event event, int weekDay){
		int dayOfWeek = event.getFrom().getDayOfWeek().getValue();
		return dayOfWeek == weekDay;
	}
	private int getDaysFromStart(Event event, int weekDay){
		/* Antar ny event er i riktig veke */ 
		return weekDay - event.getFrom().getDayOfWeek().getValue();
	}
}
