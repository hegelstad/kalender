package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

public class SuperController {
	@FXML private TextField newCalendarTextField;
	private Scene scene;
	
	
	@FXML
	private void initialize(){
		Rectangle shape = new Rectangle(1, 1);
		newCalendarTextField.setShape(shape);
		scene = WindowController.thisStage.getScene();
		scene.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				System.out.println("mouse click detected! "+mouseEvent.getSource());
			}
		});
	}
	
	@FXML
	public void keyPressed(KeyEvent key){
		if(key.getCode().equals(KeyCode.ESCAPE)){
			WindowController.logOff();
		}
	}
	
}
