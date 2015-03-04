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
  private void keyPressed(KeyEvent key) throws InterruptedException{
	  if (key.getCode().equals(KeyCode.ENTER)){
		  if (fieldsAreSet()){
			  if (authenticateUser()){
				  WindowController.goToCalendarView();
			  }
			  status.setText("Incorrect username or password");
		  }
		  else{
			  status.setText("Fill in both username and password");
		  }
	  }
  }
    
   @FXML
   private void loginButtonOnAction(){
	   if (fieldsAreSet()){
		   status.setText("logging in");
		   if(authenticateUser()){
			   WindowController.goToCalendarView();
		   }
		   status.setText("Incorrect username or password");
	   }
	   else{
	   status.setText("Fill in both username and password");}
	 
   }
   
   private boolean fieldsAreSet(){
	   if(username.getText().isEmpty() || password.getText().isEmpty()){
		   return false;
	   }
	   return true;
   }
   
   private boolean authenticateUser(){
	   connection = new Requester();
	   p = new Person(username.getText(), password.getText());
	   Person p2  = connection.authenticate(p);
	   if (p2 == null){
		   connection.closeConnection();
		   return false; 
	   }
	   this.p=p2;
	   PersonInfo personInfo = new PersonInfo();
	   personInfo.setPerson(p);
	   connection.closeConnection();
	   return true;
   }
}


   
