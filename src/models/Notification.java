package models;

import com.sun.tools.corba.se.idl.constExpr.Not;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Notification implements Serializable{
	private ArrayList<User> receivers;
	private String note;
	private User sender;
	private Event event;

	Notification(User sender, ArrayList<User> receivers, String note, Event event){
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
		return "Notification (sender: " + sender + ", receivers: " + receivers
				+ ", note: '" + note + "', event: " + event + ")";
	}

    public static void main(String[] args) {
        User u1 = new User(1, "karl");
        User u2 = new User(2, "pelle");
        User u3 = new User(3, "banan");
        ArrayList<User> userlist = new ArrayList<User>();
        Event e = new Event();
        userlist.add(u1);
        userlist.add(u2);
        userlist.add(u3);
        Notification n = new Notification(u2, userlist, "hallo", e);
        System.out.println(n);
    }
}
