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
import javafx.util.StringConverter;

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
		
		userGroupCombo.setEditable(true);
		userGroupCombo.setConverter(new StringConverter<UserGroup>(){

			@Override
			public UserGroup fromString(String groupName) {
				for(UserGroup ug : userGroupsList){
					if(ug.getName().equals(groupName)){
						return ug;
					}
				}
				return null;
			}

			@Override
			public String toString(UserGroup ug) {
				if(ug==null||ug.getName()==null){
					return "";
				}
				return ug.getName();					
			}
			
		});
		
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
		if(ug==null){
			return;
		}
		/* Add all users that belong in group */
		inUsersList.addAll(ug.getUsers());
		outUsersList.removeAll(ug.getUsers());
		
		outUsersView.getSelectionModel().select(0);
		
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
		if(!isValid()){
			WindowController.warning("Choose usergroup ");
			return;
		}
		UserGroup oldGroup = userGroupCombo.getSelectionModel().getSelectedItem();
		if(oldGroup == null){
			WindowController.warning("Usergroup does not exist ");
			return;
		}
		UserGroup newGroup = new UserGroup(oldGroup.getUserGroupID(), oldGroup.getName(), null, 1);
		ArrayList<Person> newPersons = new ArrayList<Person>();
		for(Person person : inUsersList){
			newPersons.add(person);
		}
		newGroup.setUsers(newPersons);
		
		Requester r = new Requester();
		boolean ok = r.editUserGroup(newGroup);
		r.closeConnection();
		if(!ok){
			WindowController.warning("Editen failet");
		}
		else{
			WindowController.setUserGroupWindowIsOpen(false); 
			stage.close();
		}
		
	}
	
	@FXML
	private void cancel(){
		WindowController.setUserGroupWindowIsOpen(false);
		stage.close();
	}
	
	@FXML
	private void addNewGroup(){
		String newGroupName = userGroupCombo.getEditor().getText();
		UserGroup newGroup = new UserGroup(0, newGroupName, new ArrayList<Person>(), 1);
		Requester r = new Requester();
		newGroup = r.createUserGroup(newGroup);
		r.closeConnection();
		if(inUsersList.size()!=0){
			ArrayList<Person> newUserList = new ArrayList<Person>();
			for(Person person : inUsersList){
				newUserList.add(person);
			}
			newGroup.setUsers(newUserList);
			Requester editReq = new Requester();
			editReq.editUserGroup(newGroup);
			r.closeConnection();
		}
		//System.out.println("gruppenavn:"+newGroupName+": id:"+newGroup.getUserGroupID());
		userGroupCombo.setValue(null);
		userGroupCombo.getEditor().setText("");
		userGroupsList.add(newGroup);
		PersonInfo.getPersonInfo().getUsergroups().add(newGroup);
		
	}
	
	public void setStage(Stage stage){
		this.stage = stage;
	}
	
	private void setComboListener(){
		userGroupCombo.valueProperty().addListener( (ug,newVal,oldVal)-> {
			setUserGroup(userGroupCombo.getSelectionModel().getSelectedItem());
		});
	}
	// asdfasdfjkj
	private boolean isValid(){
		if(userGroupCombo.getSelectionModel().getSelectedItem() == null){
			return false;
		}
		return true;
	}
	
	
	
	
	
}
