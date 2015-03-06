package models;

import java.util.ArrayList;

import models.Calendar;
import models.Event;
import models.Notification;
import models.Person;
import models.UserGroup;

public class PersonInfo {

	public static PersonInfo personInfo;
	private Person person;
	private UserGroup personalUserGroup;
	private ArrayList<UserGroup> usergroups;
	private ArrayList<Calendar> calendars;
	private ArrayList<Event> events;
	private ArrayList<Notification> notifications;
	
	public Person getPerson() {
		return person;
	}
	
	public UserGroup getPersonalUserGroup() {
		return personalUserGroup;
	}
	
	public ArrayList<UserGroup> getUsergroups() {
		return usergroups;
	}
	
	public ArrayList<Calendar> getCalendars() {
		return calendars;
	}
	
	public ArrayList<Event> getEvents() {
		return events;
	}
	
	public ArrayList<Notification> getNotifications() {
		return notifications;
	}
	
	public void setPerson(Person person) {
		this.person = person;
	}
	
	public void setPersonalUserGroup(UserGroup personalUserGroup) {
		this.personalUserGroup = personalUserGroup;
	}
	
	public void setUsergroups(ArrayList<UserGroup> usergroups) {
		this.usergroups = usergroups;
	}
	
	public void setCalendars(ArrayList<Calendar> calendars) {
		this.calendars = calendars;
	}
	
	public void setEvents(ArrayList<Event> events) {
		this.events = events;
	}
	
	public void setNotifications(ArrayList<Notification> notifications) {
		this.notifications = notifications;
	}
	
	public static PersonInfo getPersonInfo(){
		return personInfo;
	}
	
	public static void setPersonInfo(PersonInfo p){
		personInfo = p;
	}
}
