package models;

import controllers.CalendarEventsViewController;
import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class EventDrawing {
	Rectangle eventRectangle;
	Text eventName;
	Event event;
	Circle statusCircle;
	int indent;
	int reverserIndent;
	boolean isExpanded = false;
	CalendarEventsViewController controller;
	
	public EventDrawing(Rectangle eventRectangle,Text eventName,Event event
			,Circle statusCircle ,CalendarEventsViewController controller){
		this.eventRectangle = eventRectangle;
		this.eventName = eventName;
		this.statusCircle = statusCircle;
		this.event = event;
		this.controller = controller;
		setMouseListeners(eventRectangle, eventName);
	}
	
	public void toFront(){
		eventRectangle.toFront();
		eventName.toFront();
		statusCircle.toFront();
	}
	
	private void setMouseListeners(Rectangle eventRec,Text eventName){
		eventRec.setOnMouseEntered( enterEvent -> {
			controller.setMouseOverEvent(true);
			toFront();
			eventRec.opacityProperty().set(0.9);
			eventRec.strokeProperty().set(Color.CADETBLUE);
		});
		
		eventRec.setOnMouseExited( exitEvent -> {
			controller.setMouseOverEvent(false);
			//eventRec.setWidth(controller.fullEventWidth-3);
			eventRec.opacityProperty().set(0.5);
			eventRec.strokeProperty().set(Color.BLACK);
		});
		
		eventRec.setOnMouseClicked(clickEvent -> {
			if(isExpanded)
			{
				isExpanded = false;
				ScaleTransition animation = new ScaleTransition(Duration.millis(150),eventRec);
				animation.setFromX(1.5);
				animation.setToX(1.0);
				animation.play();				
			}
			else
			{
				isExpanded = true;
				ScaleTransition animation = new ScaleTransition(Duration.millis(150),eventRec);
				animation.setFromX(1.0);
				animation.setToX(1.5);
				animation.play();				
			}
			//eventRec.setWidth(controller.fullEventWidth*1.5);
			controller.openEvent(event);				
		});

	}
	
	
	
}
