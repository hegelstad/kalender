package models;

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
		t.setText("\n" + "Note: " + note.getNote() + "\n" + "From " + note.getEvent().getName());
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
							s.setRead(note, PersonInfo.getPerson());
							s.closeConnection();
							Requester q = new Requester();
							
							System.out.println("Notification flagged as read & accepted");

							/* Removes the notification from ListView */
							list.remove(note);
							setGraphic(new Text("tom celle"));
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
