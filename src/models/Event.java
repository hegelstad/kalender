package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
	private ArrayList<UserGroup> participants;
	private LocalDate date;
	private LocalTime to;
	private int duration;
	
	public ArrayList<UserGroup> getParticipants(){
		return participants;
	}

	public Event(ArrayList<UserGroup> participants, LocalDate date,
			LocalTime to, LocalTime from) {
		super();
		this.participants = participants;
		this.date = date;
		this.to = to;
		this.duration = duration;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTo() {
		return to;
	}

	public void setTo(LocalTime to) {
		this.to = to;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setParticipants(ArrayList<UserGroup> participants) {
		this.participants = participants;
	}
	
	
}
