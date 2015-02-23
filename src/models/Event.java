package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
	ArrayList<User> participants;
	LocalDate date;
	LocalTime to;
	LocalTime from;
	
	public ArrayList<User> getParticipants(){
		return participants;
	}
}
