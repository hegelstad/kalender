package controllers;

import java.util.ArrayList;

import com.sun.org.apache.bcel.internal.generic.NEW;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import models.Calendar;
import models.PersonInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import models.CalendarCell;
import socket.Requester;

public class SidebarController {

	private static SidebarController controller;
	
	@FXML private ListView<Calendar> calendarList;
	@FXML private Pane userManagmentPaneButton;
	@FXML private TextField searchCalendar;
	@FXML private ListView<Calendar> subscribeCalendarList;
	
	ObservableList<Calendar> calendars = FXCollections.observableArrayList(new ArrayList<Calendar>());
	
	@FXML private void initialize(){
		controller = this;
		Rectangle rectangle = new Rectangle(1,1);
		searchCalendar.setShape(rectangle);
		updateCalendarList();
		initSearchCalendar();
		for(Calendar cal : PersonInfo.getPersonInfo().getAllCalendars()){
			System.out.println(cal);
		}
		userManagmentPaneButton.setOnMouseClicked( (mouseEvent) -> {
			{
				WindowController.goToManageUsersView();
			}
		});
		
	}
	
	@FXML
    public void openNewCalendar() {
        Scene scene = WindowController.thisStage.getScene();
        Pane newCalendarWindow = (Pane) scene.lookup("#newCalendarWindow");
        TextField newCalendarTextField = (TextField) scene.lookup("#newCalendarTextField");
        if (newCalendarWindow.isVisible()){
            newCalendarWindow.setVisible(false);
            newCalendarTextField.setText("");
        } 
        
        else {
            newCalendarWindow.setVisible(true);
            newCalendarTextField.requestFocus();
            
        }
    }
	
	public void updateCalendarList(){
		calendarList.setItems(calendars);
		calendarList.setCellFactory( (list) -> {
			return new CalendarCell();
		});
	}
	
	public void addCalendar(Calendar cal){
		calendars.add(cal);
		/* Must add new button and label to scene here */
	}
	public void addCalendars(ArrayList<Calendar> cals){
		calendars.clear();
		calendars.addAll(cals);
	}
	
	public void weekInit(){
		ArrayList<Calendar> incomingCals = PersonInfo.getPersonInfo().getAllCalendars();
		addCalendars(incomingCals);
	}
	
	public static SidebarController getController(){
		return controller;
	}
	
	public ListView<Calendar> getListView(){
		return calendarList;
	}
	
	public void initSearchCalendar (){
		//Henter alle kalendere og legger de til i en observable list
		Requester r = new Requester();
		ArrayList<Calendar> allCalendars = r.getAllCalendars();
		r.closeConnection();
		searchCalendar.setStyle("-fx-text-fill: #000000");
		ObservableList<Calendar> masterData = FXCollections.observableArrayList(allCalendars);
		r.closeConnection();
		//Setter CellFactory
		subscribeCalendarList.setCellFactory((list) -> {
			return new CalendarCell();
		});
		//ChangeListener som lytter på endringer i tekstfeltet og finner kalendere som inneholder
		//teksten
		searchCalendar.textProperty().addListener((observable, oldValue, newValue) -> {
			ArrayList<Calendar> filteredCalendars = new ArrayList<>();
			ObservableList<Calendar> filteredData = FXCollections.observableArrayList();
            for (Calendar cal : masterData){
				if(cal.getName().contains(searchCalendar.getText())){
					filteredCalendars.add(cal);
				}
			}
			//Fjerner alle søkeresultater om tekstfeltet er tomt
			if(searchCalendar.getText().length() == 0){
				subscribeCalendarList.setItems(null);
			} else {
				//Setter innholder av listviewt til resultatet vi har filtrert ut
				filteredData = FXCollections.observableArrayList(filteredCalendars);
				subscribeCalendarList.setItems(filteredData);
			}
		});
	}
	
	

	
}
