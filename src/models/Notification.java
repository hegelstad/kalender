package models;

import java.util.ArrayList;

public class Notification {
	private ArrayList<UserGroup> recievers;
	private String note;
	private UserGroup sender;
	private Event event;

	Notification(String note,UserGroup sender,ArrayList<UserGroup> recievers,Event event){
		this.recievers = recievers;
		this.sender = sender;
		this.note = note;
		this.event = event;
	}

	public ArrayList<UserGroup> getReceivers() {
		return recievers;
	}

	public String getNote() {
		return note;
	}

	public UserGroup getSender() {
		return sender;
	}

	public Event getEvent() {
		return event;
	}

	@Override
	public String toString() {
		return "Notification [recievers=" + recievers + ", note=" + note
				+ ", sender=" + sender + ", event=" + event + "]";
	}
	
	
}
