package controllers;

import models.Person;
import socket.Requester;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

public class LoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Label status;
    boolean pressed = false;
    Requester connection;
    Person p;

    
    @FXML
    public void keyPressed(KeyEvent key){
    		            if (key.getCode().equals(KeyCode.ENTER))
    		            {
    		            	connection.authenticate(p);
    		                WindowController.goToCalendarView();
    		            }
    		            
    		            else if(key.getCode().equals(KeyCode.ESCAPE)){
    		            	WindowController.closeStage();
    		            }
    		        }

    @FXML
    private void initialize() {
        loginButton.setOnAction((event) -> {
            if (!pressed) {
            	status.setText("logging in");
            	Person p = new Person(username.getText(), password.getText());
            	connection.authenticate(p);
//                WindowController.goToCalendarView();
                
                pressed = true;

            } else {
                status.setText("not logging in");
                pressed = false;
            }
        });
        
        
    }
    
  
    

}
