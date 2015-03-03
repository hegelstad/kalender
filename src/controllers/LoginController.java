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
  private void keyPressed(KeyEvent key){
	  if (key.getCode().equals(KeyCode.ENTER)){
		  if (fielsdAreSet()){
			  if (connection.authenticate(p)){
				  WindowController.goToCalendarView();
			  }
		  }
	  }
	  
  }
    
   @FXML
   private void loginButtonOnAction(){
	   if (fielsdAreSet()){
		   status.setText("logging in");
		   if(authenticateUser()){
			   WindowController.goToCalendarView();
		   }
		   status.setText("Incorrect username or password");
	   }
	   else{
	   status.setText("Fill in both username and password");}
	 
   }
   
   private boolean fielsdAreSet(){
	   if(username.getText().isEmpty() || password.getText().isEmpty()){
		   return false;
	   }
	   return true;
   }
   
   private boolean authenticateUser(){
	   connection = new Requester();
	   p = new Person(username.getText(), password.getText());
	   boolean status = connection.authenticate(p);
	   connection.closeConnection();
	   return status;
	   
   }

}


   
