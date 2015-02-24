package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements Serializable{
	ArrayList<User> participants;
	LocalDate date;
	LocalTime to;
	LocalTime from;

    public ArrayList<User> getParticipants(){
        return participants;
    }

    @Override
    public String toString() {
        return "Event(" +
                ", date: " + date +
                ", to: " + to +
                ", from: " + from +
                ')' ;
    }
}
