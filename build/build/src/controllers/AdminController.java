package controllers;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import models.Person;
import models.UserGroup;
import socket.Requester;

public class AdminController {

    private Stage stage;
    @FXML private ListView<UserGroup> userListView;
    private ObservableList<UserGroup> userListData;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    @FXML private TextField nameField;
    @FXML private CheckBox checkbox;
    @FXML private Label statusLabel;
    @FXML private Button addButton;
    @FXML private Button removeButton;
    @FXML private Button exitButton;
    private UserGroup selectedUserGroup;

    @FXML public void initialize() {
        addButton.setDisable(true);
        statusLabel.setText("");
        Shape shape = new Rectangle(1,1);
        usernameField.setShape(shape);
        passwordField.setShape(shape);
        nameField.setShape(shape);
        Requester r = new Requester();
        ArrayList<UserGroup> privateUsers = r.getPrivateUserGroups();
        r.closeConnection();

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
            statusLabel.setText("Deleted!");
        });

        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            usernameField.setText(newValue.toLowerCase());
            checkField();
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            checkField();
        });

        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            checkField();
        });

        addButton.setOnAction((event) -> {

            /* Create salt */
            final Random random = new SecureRandom();
            byte[] salt = new byte[32];
            random.nextBytes(salt);
            String flag;
            if (checkbox.isSelected()) {
                flag = "a";
            } else {
                flag = "u";
            }
            statusLabel.setText("Adding...."); //virker ikke av en eller annen grunn...
            Person personToBeAdded = new Person(usernameField.getText(),
                    passwordField.getText(), salt.toString(), nameField.getText(), flag);
            Requester requester = new Requester();
            requester.createPerson(personToBeAdded);
            requester.closeConnection();
            UserGroup userGroupToAdd = null;
            try {
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            requester = new Requester();
            ArrayList<UserGroup> privateUserGroups = requester.getPrivateUserGroups();
            requester.closeConnection();
            for (UserGroup ug : privateUserGroups) {
                if (ug.getName().equals(nameField.getText())) {
                    userGroupToAdd = ug;
                }
            }
            userListData.add(userGroupToAdd);
            statusLabel.setText("Added!");
            usernameField.setText("");
            passwordField.setText("");
            nameField.setText("");
        });

        exitButton.setOnAction((event) -> {
            WindowController.setAdminWindowIsOpen(false);
            stage.close();
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        usernameField.requestFocus();
    }

    private void checkField() {
        if(fieldsAreSet()){
            addButton.setDisable(false);
        } else {
            addButton.setDisable(true);
        }
    }

    private boolean fieldsAreSet() {
        for (UserGroup ug : userListData) {
            if (nameField.getText().equals(ug.getName())) {
                return false;
            }
        }
        return !(usernameField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty());
    }
}