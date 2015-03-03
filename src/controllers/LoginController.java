package controllers;

import socket.Requester;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Text status;
    boolean pressed = false;

    
    private void initialize() {
    	Requester connection = new Requester();
        loginButton.setOnAction((event) -> {
            if (!pressed) {
                status.setText("logger inn");
                WindowController.goToCalendarView();
                
                pressed = true;

            } else {
                status.setText("logger ikke inn");
                pressed = false;
            }
        });
        
        
    }
    
    

}
