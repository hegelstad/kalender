package models;

import java.util.ArrayList;

public class Notification {
	private UserGroup recievers;
	private String note;
	private Person sender;
	private Event event;

	Notification(String note,Person sender,UserGroup recievers,Event event){
		this.recievers = recievers;
		this.sender = sender;
		this.note = note;
		this.event = event;
	}

	public UserGroup getReceivers() {
		return recievers;
	}

	public String getNote() {
		return note;
	}

	public Person getSender() {
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
