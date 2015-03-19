package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.sun.media.jfxmedia.locator.Locator;

import models.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image; 
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
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
	private static double userGroupXOffset = 0;
	private static double userGroupYOffset = 0;
	private static boolean eventWindowIsOpen = false;
	private static boolean adminWindowIsOpen = false;
	private static boolean aboutScreenIsOpen = false;
	private static boolean userGroupWindowIsOpen = false;
	private static boolean osIsOSX;

	public static void setProgram(Main p){
		program = p;
}
	
	public static void setStage(Stage stage){
		thisStage=stage;
		thisStage.getIcons().add(new Image("calicon-1.png"));
		
	}
	
	public static void setEventWindowIsOpen(boolean b){
		eventWindowIsOpen = b;
	}
	
	public static void setAdminWindowIsOpen(boolean b){
		adminWindowIsOpen = b;
	}
	
	public static void setUserGroupWindowIsOpen(boolean b){
		userGroupWindowIsOpen = b;
	}
	
	public static void setAboutWindowIsOpen(boolean b){
		aboutScreenIsOpen = b;
	}
	
	public static boolean getEventWindowIsOpenOrClosed(){
		return eventWindowIsOpen;
	}



	
	public static void logOff() {
		setEventWindowIsOpen(false);
		setAdminWindowIsOpen(false);
		setUserGroupWindowIsOpen(false);
		setAboutWindowIsOpen(false);
		HeaderController.getController().scheduler.cancel();
		HeaderController.getController().timer.cancel();
		thisStage.close();
		goToLogin();
	}
	
	
	public static void goToLogin(){
		String title ="";
		Parent page;
		try {
			page = (Parent) FXMLLoader.load(program.getClass().getResource("../views/LoginView.fxml"));
			Scene scene = new Scene(page, 300, 400);
			scene.getStylesheets().add("/css/login.css");
			title = "Login";
			thisStage.setScene(scene);
			thisStage.setTitle(title);
			thisStage.centerOnScreen();
			thisStage.setResizable(false);
			thisStage.getIcons().add(new Image("calicon-1.png"));
			if(osIsOSX){
				Pane root = null;
				root = (Pane) scene.lookup("#background");
				final Menu menu1 = new Menu("Calify");
				MenuBar menuBar = new MenuBar();
				MenuItem menu12 = new MenuItem("Om Calify");
				menu12.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						openAboutScreen();
					}
				});
				menu1.getItems().add(menu12);
				menuBar.getMenus().add(menu1);
				root.getChildren().add(menuBar);
				menuBar.setUseSystemMenuBar(true);
			}
			thisStage.show();
		} catch (IOException e1) {
			System.out.println("Couldnt load LoginView.fxml");
		}
	}
	
	public static void goToCalendarView(){
		Stage superWindow = new Stage();
		thisStage=superWindow;
		String title ="";
		Parent page;
		try {
			page = (Parent) FXMLLoader.load(
					program.getClass().getResource("../views/SuperView.fxml"));
			Scene scene;
			if (osIsOSX){
				scene = new Scene(page, 1333, 701);				
			}
			else{
				scene = new Scene(page, 1322, 690);
			}
			scene.getStylesheets().add("/css/main.css");
			title = "Calify";
			thisStage.setScene(scene);
			thisStage.setTitle(title);
			thisStage.centerOnScreen();
			thisStage.setResizable(false);
			thisStage.getScene().setRoot(page);
			thisStage.getIcons().add(new Image("calicon-1.png"));
			thisStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent we) {
					if (HeaderController.getController() != null){
						HeaderController.getController().scheduler.cancel();
						HeaderController.getController().timer.cancel();
					}
				}
			});
			if(osIsOSX){
				AnchorPane root = (AnchorPane) scene.lookup("#root");			
				final Menu menu1 = new Menu("Calify");
				MenuBar menuBar = new MenuBar();
				MenuItem menu12 = new MenuItem("Om Calify");
				menu12.setOnAction(new EventHandler<ActionEvent>() {
					@Override public void handle(ActionEvent e) {
						openAboutScreen();
					}
				});
				menu1.getItems().add(menu12);
				menuBar.getMenus().add(menu1);
				root.getChildren().add(menuBar);
				menuBar.setUseSystemMenuBar(true);
			}
			thisStage.show();
			WeekController.getController().setVvalue();
		} catch (IOException e1) {
			System.out.println("Couldnt load SuperView.fxml");
		}
	}
	
	public static void goToEventView(Event event){
		Stage eventWindows = new Stage();
		if (eventWindowIsOpen){
			System.out.println("You cannot open more then one event window at once");
			warning("There is already an Event window open");
		} else {
            try {
                eventWindows.initStyle(StageStyle.UNDECORATED);
                Parent page;
                FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/EventView.fxml"));
                page = (Parent) loader.load();
                EventController controller = loader.getController();
                if (event.getEventID() != -1337) {
                    controller.openEvent(event);
                }else{
                	controller.setBarColor(event);
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
                controller.setStage(eventWindows);
                Scene scene = new Scene(page, 410, 570);
                scene.getStylesheets().add("/css/event.css");
                eventWindows.setScene(scene);
                eventWindows.initOwner(thisStage);
                eventWindows.centerOnScreen();
                eventWindows.setResizable(false);
                eventWindows.show();
                eventWindowIsOpen = true;
            } catch(Exception e) {
               System.out.println("Couldnt load EventView.fxml");
               e.printStackTrace();
            }
        }
	}
	
	public static void goToManageUsersView() {
		if (adminWindowIsOpen) {
			System.out.println("You cannot open more then one management window at once");
			warning("There is already an Admin window open");
		} else {
			try {
				Stage adminView = new Stage();
                adminView.initStyle(StageStyle.UNDECORATED);
				Parent page;
                FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/AdminView.fxml"));
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
                adminView.initOwner(thisStage);
                adminView.setScene(scene);
				adminView.centerOnScreen();
				adminView.setResizable(false);
                controller.setStage(adminView);
                adminWindowIsOpen = true;
				adminView.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        adminView.close();
                        setAdminWindowIsOpen(false);
                        System.out.println("Closing management window");
                    }
                });
				adminView.show();
			} catch(Exception e) {
				System.out.println("Couldnt load AdminView.fxml");
			}
		}
	}
	
	public static void goToUserGroupView(){
		if (userGroupWindowIsOpen){
			System.out.println("You cannot open more then one UserGroup Window at once");
			warning("There is already a User group managment window open.");
		}
		else{
			try {
	         	Stage userGroupView = new Stage();
	         	userGroupView.initStyle(StageStyle.UNDECORATED);
	             Parent page;
	             FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/UserGroupView.fxml"));
	             page = (Parent) loader.load();
	             page.setOnMousePressed(new EventHandler<MouseEvent>() {
	                 @Override
	                 public void handle(MouseEvent event) {
	                    userGroupXOffset = event.getSceneX();
	                    userGroupYOffset = event.getSceneY();
	                 }
	             });
	             page.setOnMouseDragged(new EventHandler<MouseEvent>() {
	                 @Override
	                 public void handle(MouseEvent event) {
	                     userGroupView.setX(event.getScreenX() - userGroupXOffset);
	                     userGroupView.setY(event.getScreenY() - userGroupYOffset);
	                 }
	             });
	             Object o = loader.getController();
	             UserGroupController controller = (UserGroupController) o ;
	             controller.setStage(userGroupView);
	             Scene scene = new Scene(page);
	             userGroupView.initOwner(thisStage);
	             scene.getStylesheets().add("/css/userGroup.css");
	             userGroupView.setScene(scene);
	             userGroupView.centerOnScreen();
	             userGroupView.setResizable(false);
	             userGroupWindowIsOpen=true;
	             userGroupView.show();
	         } catch(Exception e) {
	            System.out.println("Couldnt load UserGroupView.fxml");
	         }
		}
     }
	
	
	
	public static void openAboutScreen(){
		Stage aboutScreen = new Stage();
		FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/AboutView.fxml"));
		Parent page;
		if (aboutScreenIsOpen){
			System.out.println("You cannot open more then one about-window at once");
		}
		else{
			try {
				page = (Parent) loader.load();
				Scene scene = new Scene(page);
				scene.getStylesheets().add("/css/about.css");
				aboutScreen.setScene(scene);
				aboutScreen.initOwner(thisStage);
				aboutScreen.centerOnScreen();
				aboutScreen.setResizable(false);
				aboutScreen.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent we) {
                        aboutScreen.close();
                        aboutScreenIsOpen=false;
                        System.out.println("Closing management window");
                    }
                });
				aboutScreenIsOpen=true;
				aboutScreen.show();
			} catch (IOException e) {
				System.out.println("Couldn't load AboutView.fxml");
			}
			
		}
	}

	private static void goToDialogWindows(String messageToUser){
            try {
            	Stage dialogWindow = new Stage();
                Parent page;
                FXMLLoader loader = new FXMLLoader(program.getClass().getResource("../views/UserDialogView.fxml"), null, new JavaFXBuilderFactory());
                page = (Parent) loader.load();
                Scene scene = new Scene(page);
                Pane textWrappPane = (Pane) scene.lookup("#textWrappPane");
                GridPane gridPane = (GridPane) scene.lookup("#userDialogGridPane");
                final double w = gridPane.getPrefWidth();
                System.out.println("Width : " + w);
                final double h = gridPane.getPrefHeight();
                System.out.println("Height: " + h);
                Text messageToUserText = new Text(messageToUser);
                messageToUserText.setTextAlignment(TextAlignment.CENTER);
                messageToUserText.setWrappingWidth(w*0.9);
                messageToUserText.setFill(Color.WHITE);
                textWrappPane.getChildren().add(messageToUserText);
                messageToUserText.setLayoutY(h/2-30);
                messageToUserText.setLayoutX(w / 2 - messageToUserText.getLayoutBounds().getWidth() / 2);
                scene.getStylesheets().add("/css/UserDialogWindow.css");
                dialogWindow.initOwner(thisStage);
                dialogWindow.initModality(Modality.APPLICATION_MODAL);
                dialogWindow.setScene(scene);
                dialogWindow.centerOnScreen();
                dialogWindow.setResizable(false);
                dialogWindow.show();
            } catch(Exception e) {
               System.out.println("Couldnt load UserDialogView.fxml");
            }
        }

	public static void checkOsVersion() {
		if(System.getProperty("os.name").toLowerCase().startsWith("mac os x")) {
			System.out.println("OS is OSX!");
			osIsOSX=true;
		} else {
            System.out.println("OS is not OSX");
			osIsOSX = false;
			}
	}
	
	public static void warning(String message){
		System.out.println(message);
		LoginController.getLoginController().setStatus(message);
		goToDialogWindows(message);
	}
}
