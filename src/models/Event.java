package models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    ArrayList<User> participants;
    LocalDate date;
    LocalTime to;
    LocalTime from;

    Event(ArrayList<User> participants, LocalDate date, LocalTime to, LocalTime from) {

        this.participants = participants;
        this.date = date;
        this.to = to;
        this.from = from;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
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
                ", to=" + to +
                ", from=" + from +
                '}';
    }
}
