package models;

import java.util.ArrayList;

public class Notification {
	private ArrayList<User> receivers;
	private String note;
	private User sender;
	private Event event;

	Notification(String note,User sender,ArrayList<User> recievers,Event event){
		this.receivers = receivers;
		this.sender = sender;
		this.note = note;
		this.event = event;
	}

	public ArrayList<User> getReceivers() {
		return receivers;
	}

	public String getNote() {
		return note;
	}

	public User getSender() {
		return sender;
	}

	public Event getEvent() {
		return event;
	}

	@Override
	public String toString() {
		return "Notification [recievers=" + receivers + ", note=" + note
				+ ", sender=" + sender + ", event=" + event + "]";
	}
	
	
}
