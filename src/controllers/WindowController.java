package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Event;
import controllers.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * WindowsController control all the windows
 * *author Gruppe 23
 */
public class WindowController {

	public static Stage thisStage;
	private static Main program;
	private static double xOffset = 0;
	private static double yOffset = 0;
	private static boolean eventWindowIsOpenOrClosed=false;
	private static boolean manageUsersWindowIsOpenOrClosed=false;
	private static List<Stage> stages = new ArrayList<Stage>();
	
	
	public static void setProgram(Main p){
		program = p;
	
}
	
	public static void setStage(Stage stage){
		thisStage=stage;
	}
	
	public static void setEventWindowIsOpenOrClosed(boolean b){
		eventWindowIsOpenOrClosed = b;
	}
	
	public static void setManageUsersWindowIsOpenOrClosed(boolean b){
		manageUsersWindowIsOpenOrClosed = b;
	}
	
	public static boolean getEventWindowIsOpenOrClosed(){
		return eventWindowIsOpenOrClosed;
	}

	
	public static void showStage(){
		thisStage.centerOnScreen();
		thisStage.setResizable(false);
		thisStage.show();
	}
	
	private static Parent replaceSceneContent(String fxml, int width, int height) throws Exception{
		String title ="";
		Parent page = (Parent) FXMLLoader.load(
				program.getClass().getResource(fxml), null, 
				new JavaFXBuilderFactory());
		Scene scene = new Scene(page, width, height);
		if (fxml.equalsIgnoreCase("../views/LoginView.fxml")){
			scene.getStylesheets().add("/css/login.css");
			title = "Login";
		}
		else if (fxml.equalsIgnoreCase("../views/SuperView.fxml")) {
            scene.getStylesheets().add("/css/main.css");
            title = "Calendar";
        }
		thisStage.hide();
		thisStage.setScene(scene);
		thisStage.setTitle(title);
		thisStage.centerOnScreen();
		thisStage.setResizable(false);
		thisStage.show();
		if(scene == null){
			scene = new Scene(page);
			thisStage.setScene(scene);
		}
		else{
			thisStage.getScene().setRoot(page);
		}
		return page;
	}
	
	public static void goToLogin(){
		try {
			replaceSceneContent("../views/LoginView.fxml", 600, 450 );
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			
		}
	}
	
	public static void goToCalendarView(){
		try{
			replaceSceneContent("../views/SuperView.fxml", 1333 , 701);
			WeekController.getController().setVvalue();
		}
		catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public static void goToEventView(Event event){
		Stage eventWindows = new Stage();
		if (eventWindowIsOpenOrClosed){
			System.out.println("You cannot open more then one event window at once");
			stages.remove(eventWindows);
		}
		else{
		try{
			stages.add(eventWindows);
			eventWindows.initStyle(StageStyle.UNDECORATED);
			Parent page;
			FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/EventView.fxml"), null, new JavaFXBuilderFactory());
			page = (Parent) loader.load();
			EventController controller = loader.getController();
			if (event != null){
				controller.openEvent(event);
			}
			page.setOnMousePressed(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	               xOffset = event.getSceneX();
	               yOffset = event.getSceneY();
	            }
	        });
	        page.setOnMouseDragged(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                eventWindows.setX(event.getScreenX() - xOffset);
	                eventWindows.setY(event.getScreenY() - yOffset);
	            }
	        });
			eventWindows.setAlwaysOnTop(true);
			controller.setStage(eventWindows);
			Scene scene = new Scene(page, 410, 570);
			scene.getStylesheets().add("/css/event.css");
			eventWindows.setScene(scene);
			eventWindows.centerOnScreen();
			eventWindows.setResizable(false);
			eventWindows.show();
			eventWindowIsOpenOrClosed = true;
		}
		catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	}

	public static void goToNotificationView(){
		try{
			Stage notificationWindows = new Stage();
			Parent page;
			FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/NotificationView.fxml"), null, new JavaFXBuilderFactory());
			page = (Parent) loader.load();
			NotificationController controller = loader.getController();
			Scene scene = new Scene(page, 494, 712);
			notificationWindows.setScene(scene);
			notificationWindows.centerOnScreen();
			notificationWindows.setResizable(false);
			notificationWindows.show();
		}
		catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public static void goToManageUsersView(){
		Stage manageUsersView = new Stage();
		if (manageUsersWindowIsOpenOrClosed){
			System.out.println("You cannot open more then one managment window at once");
			stages.remove(manageUsersView);
		}
		else{
			
			try{
				Parent page;
				FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/ManageUsersView.fxml"), null, new JavaFXBuilderFactory());
				page = (Parent) loader.load();
				ManageUsersController controller = loader.getController();
				Scene scene = new Scene(page);
				manageUsersView.setScene(scene);
				manageUsersView.setX(300);
				manageUsersView.setResizable(false);
				manageUsersView.setAlwaysOnTop(true);
				manageUsersWindowIsOpenOrClosed = true;
				manageUsersView.setOnCloseRequest(new EventHandler<WindowEvent>() {
		            public void handle(WindowEvent we) {
		            	manageUsersView.close();
		                setManageUsersWindowIsOpenOrClosed(false);
		                System.out.println("Closing managment window");
		            }
		        }); 
				stages.add(manageUsersView);
				manageUsersView.show();
			}
			catch(Exception e){
				System.out.println(e);
				e.printStackTrace();
			}
			
		}
		}
	
	
	public static void logOff(){
			for (Stage s: stages){
				s.close();
			}
			setEventWindowIsOpenOrClosed(false);
			setManageUsersWindowIsOpenOrClosed(false);
		goToLogin();
		
	}
}

