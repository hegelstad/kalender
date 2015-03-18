package controllers;

import java.util.ArrayList;

import socket.Requester;
import models.Person;
import models.PersonInfo;
import models.UserGroup;
import models.PersonCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class UserGroupController {


	private Stage stage;
	@FXML
	Button btnSave;
	@FXML
	Button btnCancel;
	@FXML
	Button btnAdd;
	@FXML
	Button btnRemove;
	@FXML
	Button btnAddGroup;
	@FXML
	ListView<Person> outUsersView;
	@FXML
	ListView<Person> inUsersView;
	@FXML
	ComboBox<UserGroup> userGroupCombo;
	
	ObservableList<Person> inUsersList;
	ObservableList<Person> outUsersList;
	ObservableList<UserGroup> userGroupsList;
	
	@FXML
	private void initialize(){
		Requester r = new Requester();
		ArrayList<Person> persons = r.getAllPersons();
		r.closeConnection();
		/* Get all non private user groups */
		ArrayList<UserGroup> allUserGroups = PersonInfo.getPersonInfo().getUsergroups();
		ArrayList<UserGroup> userGroups = new ArrayList<UserGroup>();
		for(UserGroup ug : allUserGroups){
			System.out.println(ug);
			if(!ug.isPrivate()){
				userGroups.add(ug);
			}
		}
		
		userGroupsList = FXCollections.observableArrayList(userGroups);

		outUsersList = FXCollections.observableArrayList(persons);
		inUsersList = FXCollections.observableArrayList(new ArrayList<Person>());
		

		userGroupCombo.setItems(userGroupsList);
		outUsersView.setItems(outUsersList);
		inUsersView.setItems(inUsersList);
		
		outUsersView.setCellFactory( (list) -> {
			return new PersonCell();
		});
		
		inUsersView.setCellFactory( (list) -> {
			return new PersonCell();
		});
		
		
		setComboListener();
	}
	
	private void setUserGroup(UserGroup ug){
		/* Setter alle personer tilbake i ut-liste */
		for(Person p : inUsersList){
			if(!outUsersList.contains(p)){
				outUsersList.add(p);
			}
		}
		inUsersList.clear();
		/* Add all users that belong in group */
		inUsersList.addAll(ug.getUsers());
		outUsersList.removeAll(ug.getUsers());
		
	}

	
	@FXML
	private void addUser(){
		Person selectedPerson = outUsersView.getSelectionModel().getSelectedItem();
		if(selectedPerson!=null){
			inUsersList.add(selectedPerson);
			outUsersList.remove(selectedPerson);
		}
	}
	
	@FXML
	private void removeUser(){
		Person selectedPerson = inUsersView.getSelectionModel().getSelectedItem();
		if(selectedPerson != null){
			outUsersList.add(selectedPerson);
			inUsersList.remove(selectedPerson);			
		}
	}
	
	@FXML
	private void save(){
		UserGroup oldGroup = userGroupCombo.getSelectionModel().getSelectedItem();
		UserGroup newGroup = new UserGroup(oldGroup.getUserGroupID(), oldGroup.getName(), null, 1);
		UserGroup toBeDeleted = new UserGroup(oldGroup.getUserGroupID(), oldGroup.getName(), null, 1);
		
		ArrayList<Person> usersToBeDeleted = new ArrayList<Person>();
		ArrayList<Person> usersAdded = new ArrayList<Person>();
		/* Copy old list */
		for(Person person : oldGroup.getUsers()){
			usersAdded.add(person);
			usersToBeDeleted.add(person);
		}
		/* Add new users/ Remove fromm delete list*/
		for(Person person : inUsersList){
			if(!usersAdded.contains(person)){
				usersAdded.add(person);
			}
			usersToBeDeleted.remove(person);
		}
		
		if(usersToBeDeleted.size()!=0){
			toBeDeleted.setUsers(usersToBeDeleted);
			Requester deleteReq = new Requester();
			
		}
		
	}
	
	@FXML
	private void cancel(){
		
		stage.close();
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	private void setComboListener(){
		userGroupCombo.valueProperty().addListener( (ug,newVal,oldVal)-> {
			//System.out.println("dasfasdfasdfsdfas");
			setUserGroup(userGroupCombo.getSelectionModel().getSelectedItem());
		});
	}
	
	
	
	
	
}
