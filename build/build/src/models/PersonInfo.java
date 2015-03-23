package models;

import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TreeSet;

import controllers.HeaderController;
import controllers.SidebarController;

public class PersonInfo {

	public static PersonInfo personInfo;
	private Person person;
	private UserGroup personalUserGroup;
	private ArrayList<UserGroup> usergroups;
	private ArrayList<Calendar> allCalendars;
	private ArrayList<Calendar> calendarsInUse;
	private ArrayList<Event> events;
	private ArrayList<Notification> notifications;
	private Calendar selectedCalendar;
	private ArrayList<Calendar> subscribedCalendars = new ArrayList<>();
	private Calendar personalCalendar;
	
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

	public void setSubscribedCalendars(ArrayList<Calendar> subscribedCalendars) {
		this.subscribedCalendars = subscribedCalendars;
	}
	
	public void setPersonalCalendar(Calendar cal) { this.personalCalendar = cal; }
	
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
	
	public Calendar getSelectedCalendar() {
		return selectedCalendar;
	}
	
	public Calendar getPersonalCalendar() { return personalCalendar; }

	public ArrayList<Calendar> getSubscribedCalendars() {
		return subscribedCalendars;
	}

	public void setSelectedCalendar(Calendar selectedCalendar) {
		this.selectedCalendar = selectedCalendar;
	}

	public TreeSet<Event> getEventsForWeek(int weekNumber){
		TreeSet<Event> events = new TreeSet<Event>( (e1,e2) ->{
			/* Event is sorted on their starting time */
			return e1.getFrom().isBefore(e2.getFrom()) ? -1: 1;
		});
		
		TemporalField woy = WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear();
		if(calendarsInUse == null){
			System.out.println("Ingen brukerkalendere registrert");
			return null;
		}
		for(Calendar cal: calendarsInUse){
			System.out.println("Adding events for " + cal.getName());
			for(Event event : cal.getEvents()){
				if(event.getFrom().get(woy)==weekNumber){
					events.add(event);
				}
			}
		}
		if(events==null || events.size() == 0){
			System.out.println("Ingen events på denne uka");
		}
		return events;
	}
	
	public void setUp(){
		for(Calendar cal: allCalendars){
			cal.setEvents(new ArrayList<Event>());
		}
		for(Event event : events){
			for(Calendar cal : allCalendars){
				if(event.getCal().getCalendarID()==cal.getCalendarID()){
					cal.getEvents().add(event);
				}
			}
		}
	}
	
	public boolean addCalendarInUse(Calendar cal){
		boolean changed = calendarsInUse.add(cal);
		HeaderController.getController().drawEventsForWeek();
		return changed;
	}
	
	public boolean removeCalendarInUse(Calendar cal){
		boolean changed = calendarsInUse.remove(cal);
		HeaderController.getController().drawEventsForWeek();
		return changed;
	}
	
	public void addCalendar(Calendar cal){
		//allCalendars.add(cal);
		//calendarsInUse.add(cal);
		SidebarController.getController().getCalendars().add(cal);
	}

}
