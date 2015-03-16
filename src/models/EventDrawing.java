package models;

import controllers.WeekController;
import controllers.WindowController;
import javafx.animation.ScaleTransition;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
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
	WeekController controller;
	
	public EventDrawing(Rectangle eventRectangle,Text eventName,Event event
			,Circle statusCircle ,WeekController controller){
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
			if(!isExpanded){
				eventRec.opacityProperty().set(0.5);				
			}
			eventRec.strokeProperty().set(Color.BLACK);
		});
		
		eventRec.setOnMouseClicked(clickEvent -> {
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
	
	
	
}
