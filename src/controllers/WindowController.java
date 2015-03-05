package controllers;

import models.Event;
import controllers.Main;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * WindowsController control all the windows
 * *author Gruppe 23
 */
public class WindowController {

	public static Stage thisStage;
	private static Main program;
	
	
	public static void setProgram(Main p){
		program = p;
	
}
	
	public static void setStage(Stage stage){
		thisStage=stage;
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
			replaceSceneContent("../views/SuperView.fxml", 1332 , 680);
		}
		catch (Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public static void goToEventView(Event event){
		try{
			Stage eventWindows = new Stage();
			Parent page;
			FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/EventView.fxml"), null, new JavaFXBuilderFactory());
			page = (Parent) loader.load();
			EventController controller = loader.getController();
			if (event != null){
				controller.openEvent(event);
			}
			controller.setStage(eventWindows);
			Scene scene = new Scene(page, 494, 712);
			eventWindows.setScene(scene);
			eventWindows.centerOnScreen();
			//Fix later: Only if event ==null	
			eventWindows.setTitle("Add a new event");
			eventWindows.setResizable(false);
			eventWindows.show();
		}
		catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
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
	
	public static void closeStage(){
		thisStage.close();
	}

	
}

