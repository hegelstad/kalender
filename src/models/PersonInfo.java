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
	private ArrayList<Calendar> allCalendars;
	private ArrayList<Calendar> calendarsInUse;
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
	
	public ArrayList<Calendar> getAllCalendars() {
		return allCalendars;
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
	
	public void setAllCalendars(ArrayList<Calendar> allCalendars) {
		this.allCalendars = allCalendars;
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
	
	public ArrayList<Calendar> getCalendarsInUse(){
		return calendarsInUse;
	}
	
	public void setCalendarsInUse(ArrayList<Calendar> calendarsInUse){
		this.calendarsInUse = calendarsInUse;
	}
	
	public ArrayList<Event> getEventsForWeek(int weekNumber){
		ArrayList<Event> events = new ArrayList<Event>();
		for(Calendar cal: calendarsInUse){
			
		}
		
		
		
		return events;
	}
	
}
