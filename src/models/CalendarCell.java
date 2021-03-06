package models;
import java.util.ArrayList;

import socket.Requester;
import controllers.HeaderController;
import controllers.SidebarController;
import controllers.WeekController;
import controllers.WindowController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
public class CalendarCell extends ListCell<Calendar> {
	
	public Label label;
	public CheckBox checkbox;
	
	public CalendarCell(){

	}

	private void init(Calendar cal){
		Pane pane = new Pane();
		checkbox = new CheckBox();
		if (PersonInfo.getPersonInfo().getCalendarsInUse().contains(cal)) checkbox.setSelected(true);
		checkbox.setLayoutY(2);
		label = new Label(cal.getName());
		label.setTooltip(new Tooltip("Owner: " + cal.getUserGroups().get(0)));
		label.setLayoutX(25);
		label.setLayoutY(2);
		label.setTextFill(Color.web("#0076a3"));
		SidebarController.getController().addLabel(label);
		if(cal.getName().length() > 20){
			label.setPrefWidth(100);
			label.setPrefHeight(40);
			label.setWrapText(true);
		}
		switch (cal.getColorID()){
		case 0: 
			pane.getStyleClass().add(0,"lightskyblue");
			break;
		case 1: 
			pane.getStyleClass().add(0,"lightsalmon");
			break;
		case 2: 
			pane.getStyleClass().add(0,"lightgreen");
			break;
		case 3:
			pane.getStyleClass().add(0,"yellow");
			break;
		case 4: 
			pane.getStyleClass().add(0,"lightpink");
			break;
		case 5: 
			pane.getStyleClass().add(0,"aquamarine");
			break;
		case 6: 
			pane.getStyleClass().add(0,"lightcoral");
			break;
		case 7: 
			pane.getStyleClass().add(0,"thistle");
			break;
		case 8:
			pane.getStyleClass().add(0,"beige");
			break;
		case 9: 
			pane.getStyleClass().add(0,"silver");
			break;
		default: 
			pane.getStyleClass().add(0,"cornflowerblue");
			break;
		}
		
		pane.getChildren().addAll(checkbox,label);
		setId("calendar-cell");
		checkbox.selectedProperty().addListener( (ob,oldVal,newVal) -> {
			if(newVal)
			{	
				PersonInfo.getPersonInfo().addCalendarInUse(cal);
			}
			else
			{
				PersonInfo.getPersonInfo().removeCalendarInUse(cal);
			}
		});
		
		label.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 2){
		                PersonInfo.getPersonInfo().setSelectedCalendar(cal);
		                if (! checkbox.isSelected()) checkbox.setSelected(true);
		                System.out.println("Selected calendar: " + PersonInfo.getPersonInfo().getSelectedCalendar());
		                SidebarController.updateLabels();
		                label.setFont(Font.font(null, FontWeight.BOLD, 13));
		            }
		        }else if(mouseEvent.getButton() == MouseButton.SECONDARY){
					if(! cal.getName().equals(PersonInfo.getPersonInfo().getPersonalUserGroup().getName())){
						final ContextMenu contextMenu = new ContextMenu();
						MenuItem item = new MenuItem("Delete");
						item.setOnAction(new EventHandler<ActionEvent>() {
						    @Override public void handle(ActionEvent e) {
							       Requester requester = new Requester();
							       requester.deleteCalendar(cal);
							       requester.closeConnection();
							       PersonInfo.getPersonInfo().getAllCalendars().remove(cal);
							       if (PersonInfo.getPersonInfo().getCalendarsInUse().contains(cal)){
							    	   PersonInfo.getPersonInfo().getCalendarsInUse().remove(cal);
							       }
							       if (PersonInfo.getPersonInfo().getSelectedCalendar().equals(cal)){
							    	   PersonInfo.getPersonInfo().setSelectedCalendar(PersonInfo.getPersonInfo().getAllCalendars().get(0));
							       }
							       HeaderController.getController().drawEventsForWeek();
							       SidebarController.getController().calendars.remove(cal);
							    }
							});
						contextMenu.getItems().addAll(item);
						setContextMenu(contextMenu);
					}else{
						System.out.println("Can not delete personal calendar.");
					}	
				}
		    }
		});
		
			
		
		if (PersonInfo.getPersonInfo().getSelectedCalendar().equals(cal)){
			label.setFont(Font.font(null, FontWeight.BOLD, 13));	
		}else{
			label.setFont(Font.font(null, FontWeight.NORMAL, 13));;
		}
		setGraphic(pane);
	}

	@Override
	public void updateItem(Calendar cal, boolean empty){
		super.updateItem(cal, empty);		
		if(cal!=null&&!empty){
			init(cal);			
		}
		else{
			setGraphic(null);
		}
	}
}
