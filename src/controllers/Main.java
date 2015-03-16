package controllers;

import javafx.application.Application;
import controllers.WindowController;
import javafx.stage.Stage;

public class Main extends Application {
	private Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {
    	stage = primaryStage;
    	WindowController.checkOsVersion();
    	WindowController.setStage(stage);
    	WindowController.setProgram(this);
    	WindowController.goToLogin();
    	WindowController.showStage();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
