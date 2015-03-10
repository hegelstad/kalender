package controllers;

import java.util.ArrayList;

import socket.Requester;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Notification;
import models.Person;
import models.PersonInfo;
import models.UserGroup;

public class ManageUsersController {

	@FXML ListView<UserGroup> usersList;
    ObservableList<UserGroup> users;
    
    @FXML
    private void initialize(){
    	Requester r = new Requester();
    	ArrayList<UserGroup> persons = r.getPrivateUserGroups();
        users = FXCollections.observableArrayList(persons);
        usersList.setItems(users);
    }
	
}
