package models;

import java.util.ArrayList;

public class Notification {
	private ArrayList<User> recievers;
	private String note;
	private User sender;
	private Event event;

	Notification(String note,User sender,ArrayList<User> recievers,Event event){
		this.recievers = recievers;
		this.sender = sender;
		this.note = note;
		this.event = event;
	}

	public ArrayList<User> getRecievers() {
		return recievers;
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
		return "Notification [recievers=" + recievers + ", note=" + note
				+ ", sender=" + sender + ", event=" + event + "]";
	}
	
	
}
