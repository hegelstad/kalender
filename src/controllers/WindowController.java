package controllers;

import java.util.ArrayList;
import java.util.List;

import models.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class WindowController {

	public static Stage thisStage;
	private static Main program;
	private static double xOffset = 0;
	private static double yOffset = 0;
	private static double adminXOffset = 0;
	private static double adminYOffset = 0;
	private static boolean eventWindowIsOpen = false;
	private static boolean adminWindowIsOpen = false;
	private static List<Stage> stages = new ArrayList<Stage>();

	public static void setProgram(Main p){
		program = p;
}
	
	public static void setStage(Stage stage){
		thisStage=stage;
	}
	
	public static void setEventWindowIsOpen(boolean b){
		eventWindowIsOpen = b;
	}
	
	public static void setAdminWindowIsOpen(boolean b){
		adminWindowIsOpen = b;
	}
	
	public static boolean getEventWindowIsOpenOrClosed(){
		return eventWindowIsOpen;
	}

	
	public static void showStage(){
		thisStage.centerOnScreen();
		thisStage.setResizable(false);
		thisStage.show();
	}
	
	private static Parent replaceSceneContent(String fxml, int width, int height) throws Exception {
		String title ="";
		Parent page = (Parent) FXMLLoader.load(
				program.getClass().getResource(fxml), null, 
				new JavaFXBuilderFactory());
		Scene scene = new Scene(page, width, height);
		if (fxml.equalsIgnoreCase("../views/LoginView.fxml")) {
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
		thisStage.getScene().setRoot(page);
		return page;
	}
	
	public static void goToLogin(){
		try {
			replaceSceneContent("../views/LoginView.fxml", 600, 450);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void goToCalendarView(){
		try {
			replaceSceneContent("../views/SuperView.fxml", 1333 , 701);
			WeekController.getController().setVvalue();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void goToEventView(Event event){
		Stage eventWindows = new Stage();
		if (eventWindowIsOpen){
			System.out.println("You cannot open more then one event window at once");
			stages.remove(eventWindows);
		} else {
            try {
                stages.add(eventWindows);
                eventWindows.initStyle(StageStyle.UNDECORATED);
                Parent page;
                FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/EventView.fxml"), null, new JavaFXBuilderFactory());
                page = (Parent) loader.load();
                EventController controller = loader.getController();
                if (event != null) {
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
                eventWindowIsOpen = true;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
	}
	
	public static void goToManageUsersView() {
		Stage adminView = new Stage();
		if (adminWindowIsOpen) {
			System.out.println("You cannot open more then one management window at once");
			stages.remove(adminView);
		} else {
			try {
                adminView.initStyle(StageStyle.UNDECORATED);
				Parent page;
                FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/AdminView.fxml"), null, new JavaFXBuilderFactory());
                page = (Parent) loader.load();
                page.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                       adminXOffset = event.getSceneX();
                       adminYOffset = event.getSceneY();
                    }
                });
                page.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        adminView.setX(event.getScreenX() - adminXOffset);
                        adminView.setY(event.getScreenY() - adminYOffset);
                    }
                });
                AdminController controller = loader.getController();
                Scene scene = new Scene(page);
                scene.getStylesheets().add("/css/manageUsers.css");
                adminView.setScene(scene);
				adminView.centerOnScreen();
				adminView.setResizable(false);
				adminView.setAlwaysOnTop(true);
                controller.setStage(adminView);
                adminWindowIsOpen = true;
				adminView.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        adminView.close();
                        setAdminWindowIsOpen(false);
                        System.out.println("Closing management window");
                    }
                });
				stages.add(adminView);
				adminView.show();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static void logOff() {
		for (Stage s: stages) {
			s.close();
		}
		setEventWindowIsOpen(false);
		setAdminWindowIsOpen(false);
		goToLogin();
	}
}

