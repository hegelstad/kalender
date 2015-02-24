package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
	
    ArrayList<UserGroup> participants;
    LocalDate date;
    LocalTime from;
    int duration;

    Event(ArrayList<UserGroup> participants, LocalDate date,LocalTime from, int duration) {

        this.participants = participants;
        this.date = date;
        this.from = from;
        this.duration = duration;
    }

    public ArrayList<UserGroup> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<UserGroup> participants) {
        this.participants = participants;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return "Event{" +
                "participants=" + participants +
                ", date=" + date +
                ", duration=" + duration +
                ", from=" + from +
                '}';
    }
}
