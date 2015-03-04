package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Notification implements Serializable{

	private int noteID;
	private ArrayList<UserGroup> receivers;
	private String note;
	private UserGroup sender;
	private Event event;

    public Notification(int noteID,String note, UserGroup sender, ArrayList<UserGroup> receivers,Event event){
    	this.noteID = noteID;
		this.receivers = receivers;
		this.sender = sender;
		this.note = note;
		this.event = event;
	}
    
    public int getNoteID(){
    	return noteID;
    }

	public ArrayList<UserGroup> getReceivers() {
		return receivers;
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
	

	public void setNoteID(int noteID) {
		this.noteID = noteID;
	}

	public void setReceivers(ArrayList<UserGroup> receivers) {
		this.receivers = receivers;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public void setSender(UserGroup sender) {
		this.sender = sender;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "Notification(receivers: " + receivers + ", note: " + note
				+ ", sender: " + sender + ", event: " + event + ")";
	}
}
