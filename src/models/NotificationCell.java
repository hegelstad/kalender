package models;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import controllers.HeaderController;
import socket.Requester;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class NotificationCell extends ListCell<Notification> {
	
	ObservableList<Notification> list;
	Notification note;
	
	public NotificationCell(ObservableList<Notification> list){
		this.list = list;
	}
	
	@Override
	public void updateItem(Notification note, boolean empty){
		super.updateItem(note, empty);
		this.note = note;
		if(note!=null&&!empty){
			init();			
		}
		else{
			setGraphic(null);
		}
	}
	
	public void init(){
		Pane content = new Pane();
		Text t = new Text();
		t.setWrappingWidth(250.00);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("HH:mm");
		t.setText("\n" + note.getNote() + "\n" + "From " + note.getSender().getName() + "\n" + note.getEvent().getFrom().format(formatter) + " kl."+ note.getEvent().getFrom().format(formatter2) + " - " + note.getEvent().getTo().format(formatter)+ " kl."+ note.getEvent().getTo().format(formatter2));
		content.getChildren().add(t);

		content.setOnMouseClicked(new EventHandler<MouseEvent>() {
			Boolean is_expanded = false;


			@Override
			public void handle(MouseEvent mouseEvent) {
				if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

					if (!is_expanded) {
						content.setPrefHeight(content.getHeight() + 50);

						/* Creates accept and decline button inside the pane */
						Button decline = new Button("Decline");
						Button accept = new Button("Accept");
						accept.setPrefWidth(133);
						accept.setLayoutY(content.getHeight() + 20);
						decline.setLayoutY(content.getHeight() + 20);
						decline.setLayoutX(138);
						decline.setPrefWidth(133);

						content.getChildren().addAll(accept, decline);
						is_expanded = true;

						accept.setOnAction(event -> {

							/* Replace new Person med PersonInfo.getPerson() */
							//System.out.println(n);
							//System.out.println(n.getNoteID());

							/* Set the notification as HasRead */
							Requester s = new Requester();
							s.setRead(note, PersonInfo.getPersonInfo().getPersonalUserGroup());
							s.closeConnection();
							s = new Requester();
							Attendant at = new Attendant(PersonInfo.getPersonInfo().getPersonalUserGroup().getUserGroupID(), PersonInfo.getPersonInfo().getPersonalUserGroup().getName(), 1);
							s.updateAttends(note.getEvent(), at);
							s.closeConnection();
							System.out.println("Notification flagged as read & accepted");

							s = new Requester();
							PersonInfo.personInfo.setNotifications(s.getNotifications(PersonInfo.getPersonInfo().getPersonalUserGroup()));
							s.closeConnection();
							/* Removes the notification from ListView */
							list.remove(note);
							setGraphic(null);
							HeaderController.getController().updateNotificationButton(PersonInfo.getPersonInfo().getNotifications().size());
							//notifications.remove(n);
							//notifications =
							//System.out.println(notifications);
						});

					} else {
						//content.getChildren().remove(1, 2);
						content.setPrefHeight(content.getHeight() - 50);
						is_expanded = false;
					}
				}
			}
		});
		if (list.contains(note)) {
			setGraphic(content);
		}
	}


}
