package models;

import java.time.LocalDateTime;

import controllers.WeekController;
<<<<<<< HEAD
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
=======
import controllers.WindowController;
import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
>>>>>>> Lagt til delvis støtte for høyreklikk på event, og satt en OS check ved startup som vurderer om about-menyen kan vises (fungerer kun i OSX)
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
	
	public final double fullEventWidth = 155;
	public final double fullEventWidthPrecise = 153; 
	public final double hourHeight = 66;
	public final double hourHeightPrecise = 66; 
	public final double indentMargin = 15.0;
	
	public EventDrawing(Event event, int dayOfEvent, WeekController controller){
		this.event = event;
		this.dayOfDrawing= dayOfEvent;
		this.controller = controller;
	}
	
	public void draw(){
		drawEvent();
		setMouseListeners();
	}
	
	public void toFront(){
		eventRectangle.toFront();
		eventName.toFront();
		statusCircle.toFront();
	}
	
	private void setMouseListeners(){
		eventRectangle.setOnMouseEntered( enterEvent -> {
			controller.setMouseOverEvent(true);
			toFront();
			eventRectangle.opacityProperty().set(0.9);
			eventRectangle.strokeProperty().set(Color.CADETBLUE);
		});
		
		eventRectangle.setOnMouseExited( exitEvent -> {
			controller.setMouseOverEvent(false);
			//eventRec.setWidth(controller.fullEventWidth-3);
			if(!isExpanded){
				eventRectangle.opacityProperty().set(0.5);				
			}
			eventRectangle.strokeProperty().set(Color.BLACK);
		});
		
		eventRectangle.setOnMouseClicked(clickEvent -> {
			if(clickEvent.getClickCount() == 2){
				controller.openEvent(event);
				return;
			}
			else if(clickEvent.getButton() == MouseButton.SECONDARY){
					final ContextMenu contextMenu = new ContextMenu();
					MenuItem item1 = new MenuItem("Edit");
					MenuItem item2 = new MenuItem("Delete");
					contextMenu.getItems().addAll(item1, item2);
					Scene scene = WindowController.thisStage.getScene();
					AnchorPane root = (AnchorPane) scene.lookup("#root");
					contextMenu.show(root, clickEvent.getScreenX(), clickEvent.getScreenY());
				
			}
//			
//			if(isExpanded)
//			{
//				isExpanded = false;
//				ScaleTransition animation = new ScaleTransition(Duration.millis(100),eventRec);
//				animation.setFromX(1.5);
//				animation.setToX(1.0);
//				animation.play();				
//			}
//			else
//			{
//				isExpanded = true;
//				ScaleTransition animation = new ScaleTransition(Duration.millis(100),eventRec);
//				animation.setFromX(1.0);
//				animation.setToX(1.5);
//				animation.play();				
//			}
			//eventRec.setWidth(controller.fullEventWidth*1.5);
		});

	}
	
	private boolean isOverMidnight(Event event){
		LocalDateTime midnight = event.getFrom().withHour(0).withMinute(0);
		return event.getTo().isAfter(midnight);
	}
	
	public void drawEvent(){
		for (Calendar cal : PersonInfo.getPersonInfo().getAllCalendars()){
			if (cal.getCalendarID() == event.getCal().getCalendarID()){
				event.getCal().setColorID(cal.getColorID());
			}
		}
		//System.out.println("Drawing : "+event.getName() + " id:" + event.getEventID() + " with colorID : " +event.getCal().getColorID());
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
		
		double marginTop = 0.0;						
		/* If event is at day 0 draw according to start , else on top */
		if(dayOfDrawing== 0){
			marginTop = ((double)event.getFrom().getMinute()/60)*hourHeight;
		}
		
		Insets eventMargin = new Insets(marginTop, 0, 0, eventIndentMargin);
		GridPane.setMargin(eventRec, eventMargin);
		
		Insets textMargin = new Insets(marginTop, 0, 0, 3 + eventIndentMargin);
		GridPane.setMargin(eventName, textMargin);
		
		double rightMargin = fullEventWidth-(double)reverseIndent*indentMargin
				+(double)indent*indentMargin-13;
		//System.out.println("reverse indent : "+reverseIndent+" rightMargin circle : "+rightMargin );
		Insets circleMargin = new Insets(marginTop+3, 0,0,rightMargin);
		GridPane.setMargin(statusCircle,circleMargin);
		
		int dayOfWeek = event.getFrom().getDayOfWeek().getValue()-1+dayOfDrawing;
		//System.out.println(event.getName()+ " ukedag :"+dayOfWeek);
		int startHour =0;
		if(dayOfDrawing==0)
		{
			startHour = event.getFrom().getHour();			
		}
		//weekGrid.setGridLinesVisible(true);
		controller.weekGrid.add(eventRec, dayOfWeek, startHour, 1, 1);
		controller.weekGrid.add(statusCircle, dayOfWeek, startHour, 1,1);
		controller.weekGrid.add(eventName, dayOfWeek, startHour, 1, 1);
		//System.out.println(eventRec);
		this.eventRectangle = eventRec;
		this.eventName = eventName;
		this.statusCircle = statusCircle;
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
	
	private double getEventHeight(Event e){
		LocalDateTime from = e.getFrom();
		LocalDateTime to = e.getTo();
		if(dayOfDrawing!=0&&dayOfDrawing+from.getDayOfWeek().getValue()!=to.getDayOfWeek().getValue()){
			to = from.withHour(23).withMinute(59);
			from = to.withHour(0).withMinute(0);
		}
		if(to.isAfter(from.withHour(23).withMinute(59))){
			to = from.withHour(23).withMinute(59);
		}
		double minDiff = (to.getHour()-from.getHour())*60 + (to.getMinute()-from.getMinute());
		double height = (minDiff/60.0) * 66;
		System.out.println("HEIGHT :    " + height);
		return height;
	}
	
	public void remove(){
		controller.weekGrid.getChildren().remove(eventRectangle);
		controller.weekGrid.getChildren().remove(statusCircle);
		controller.weekGrid.getChildren().remove(eventName);
	}
	
	public Event getEvent(){
		return event;
	}
}
