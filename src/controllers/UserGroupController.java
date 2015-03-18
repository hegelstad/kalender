package controllers;

import java.util.ArrayList;

import socket.Requester;
import models.Person;
import models.PersonInfo;
import models.UserGroup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

public class UserGroupController {

	@FXML Button btnSave;
	@FXML Button btnCancel;
	@FXML Button btnAdd;
	@FXML Button btnRemove;
	@FXML Button btnAddGroup;
	@FXML ListView<Person> outUsersView;
	@FXML ListView<Person> inUsersView;
	@FXML ComboBox<UserGroup> userGroupCombo;
	
	ObservableList<UserGroup> inUsersList;
	ObservableList<UserGroup> outUsersList;
	ObservableList<UserGroup> userGroupsList;
	
	@FXML
	private void initialize(){
		ArrayList<Person> persons;
		ArrayList<UserGroup> userGroups = new ArrayList<UserGroup>();
		ArrayList<UserGroup> allGroups = PersonInfo.getPersonInfo().getUsergroups();
		
		userGroupsList = FXCollections.observableArrayList(userGroups);
		//outUsersList = FXCollections.observableArrayList(persons);
		inUsersList = FXCollections.observableArrayList(new ArrayList<UserGroup>());
		
		//userGroupCombo.setItems(userGroupsList);
		//outUsersView.setItems(outUsersList);
		//inUsersView.setItems(inUsersList);
	}
	
	private void setUserGroup(UserGroup ug){
		
		for(Person person : ug.getUsers()) {

        }
	}
}
