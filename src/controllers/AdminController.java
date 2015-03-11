package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import models.Person;
import models.UserGroup;
import socket.Requester;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AdminController {

    @FXML private ListView<UserGroup> userListView;
    private ObservableList<UserGroup> userListData;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button exitButton;
    private UserGroup selectedUserGroup;

    @FXML public void initialize() {

        Requester r = new Requester();
        ArrayList<UserGroup> privateUsers = r.getPrivateUserGroups();
        r.closeConnection();

//        r = new Requester();
//        ArrayList<Person> persons = r.getPersons(privateUsers);
//        r.closeConnection();

        userListData = FXCollections.observableArrayList(privateUsers);
        userListView.setItems(userListData);
        userListView.setCellFactory((list) -> {
            return new ListCell<UserGroup>() {
                @Override
                protected void updateItem(UserGroup item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };
        });

        userListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedUserGroup = newValue;
        });

        removeButton.setOnAction((event) -> {
            UserGroup userGroupToDelete = selectedUserGroup;
            selectedUserGroup = null;
            userListData.remove(userGroupToDelete);
            Requester requester = new Requester();
            Person personToBeDeleted = new Person(userGroupToDelete.getName(), null, null);
            requester.deletePerson(personToBeDeleted);
            requester.closeConnection();
        });
    }
}