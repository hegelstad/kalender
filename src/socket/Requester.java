package socket;

import models.Calendar;
import models.Event;
import models.Notification;
import models.Person;
import models.Room;
import models.UserGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by sondrehj on 02.03.2015.
 */
public class Requester {

    Socket con;
    
    public Requester (){
    	String host = "78.91.64.114";

        /** Define a port */
        int port = 25025;

        try {
            /** Obtain an address object of the server */
            InetAddress address = InetAddress.getByName(host);
            /** Establish a socket connetion */
            con = new Socket(address, port);
            System.out.println("SocketClient initialized");
            //connection.close();
        }catch (IOException f) {
            System.out.println("IOException: " + f);
        }
        catch (Exception g) {
            System.out.println("Exception: " + g);
        }
    }

    //USER GROUPS
    public ArrayList<UserGroup> getUserGroups(Person p){
    	Command cmd = new Command("getUserGroups-person");
    	ArrayList<UserGroup> userGroups = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(userGroups);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            userGroups = (ArrayList<UserGroup>) o;
            for (UserGroup ug : userGroups){
                System.out.println(ug);
            }

        }  catch (ClassCastException e) {
            System.out.println(e);
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userGroups;
    }
    
    public ArrayList<Person> getPersons(ArrayList<UserGroup> userGroups){
        ArrayList<Person> persons = null;
        Command cmd = new Command("getPersons-usergroup");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(userGroups);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            persons = (ArrayList<Person>) o;
            for (Person person : persons){
                System.out.println(person);
            }

        }  catch (ClassCastException e) {
            System.out.println(e);
        }
        catch(ClassNotFoundException e){
            System.out.println(e);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return persons;
    }

    public ArrayList<UserGroup> getUserGroups(Calendar cal){
    	Command cmd = new Command("getUserGroups-calendar");
    	ArrayList<UserGroup> userGroups = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(cal);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            userGroups = (ArrayList<UserGroup>) o;
            for (UserGroup ug : userGroups){
                System.out.println(ug);
            }

    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }
         catch(ClassNotFoundException e){
             System.out.println(e);
         }
         catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    	return userGroups;
    }

    public void deleteUserGroups(ArrayList<UserGroup> userGroups){
    	Command cmd = new Command("deleteUserGroups-userGroups");
    	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(userGroups);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    public void createUserGroups(UserGroup ug){
    	Command cmd = new Command("createUserGroup-string");
    	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(ug);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    public void addUsers(UserGroup ug){
    	Command cmd = new Command("addUsers-userGroup");
    	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(ug);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    //CALENDAR
    public ArrayList<Calendar> getCalendars(UserGroup ug){
    	Command cmd = new Command("getCalendars-usergroup");
    	ArrayList<Calendar> calendars = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(ug);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            calendars = (ArrayList<Calendar>) o;
            for (Calendar cal : calendars){
                System.out.println(cal);
            }

    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }
         catch(ClassNotFoundException e){
             System.out.println(e);
         }
         catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    	return calendars;
    }

    /**
     * Tar inn kalender og userGroup, oppdaterer kalender med de nye usergroupene og returnerer kalender.
     * @param cal
     * @param ug
     * @return
     */
    public Calendar addUserGroup(Calendar cal, UserGroup ug){
    	Command cmd = new Command("addUserGroup-calendar");
    	Calendar calendar = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(cal);
            oos.writeObject(ug);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            calendar = (Calendar) o;
            System.out.println(calendar);

    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }
         catch(ClassNotFoundException e){
             System.out.println(e);
         }
         catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    	return calendar;
    }
    
    /**
     * Tar inn kalender og UserGroup og fjerner usergroup fra kalender
     * @param cal
     * @param ug
     */
    public void removeUserGroup(Calendar cal, UserGroup ug){
    	Command cmd = new Command("removeUserGroup-calendar");
     	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(cal);
    		oos.writeObject(ug);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    public Calendar createCalendar(Calendar cal){
    	Command cmd = new Command("createCalendar-calendar");
    	return null;
    }
    
    public void deleteCalendar(Calendar cal){
    	Command cmd = new Command("deleteCalendar-calendar");
    }
    
    //EVENT
    public ArrayList<Event> getEvents(Calendar cal){
    	Command cmd = new Command("getEvents-calendars");
    	return null;
    }
    
    /**
     * denne må returnere et Event og oppdatere eventet.
     * @param event
     */
    public Event createEvent(Event event){
    	Command cmd = new Command("createEvent-event");
    	return null;
    }
    
    /**
     * Denne må returnere et Event og oppdatere eventet.
     * @param event
     */
    public Event editEvent(Event event){
    	Command cmd = new Command("editEvent-event");
    	return null;
    }
    
    public void deleteEvent(Event event){
    	Command cmd = new Command("deleteEvent-event");
    }
    
    //NOTIFICATION
    public Notification getNotification(Person p){
    	Command cmd = new Command("getNotification-person");
    	return null;
    }
    
    public Notification setNotification(Notification n){
    	Command cmd = new Command("setNotification-notification");
    	return null;
    }
    
    public void setRead(Notification n, Person p){
    	Command cmd = new Command("setRead-notification-person");
    }
    
    //PERSON
    public Person getPerson(Person p){
    	Command cmd = new Command("getPerson-person");
    	return null;
    }
    
    public Person createPerson(Person p){
    	Command cmd = new Command("createPerson-person");
    	return null;
    }
    
    public void deletePerson(Person p){
    	Command cmd = new Command("deletePerson-person");
    }

    /**
     *
     * LogIn må returnere en person og sette navn etc.
     * @param person
     */
    public void authenticate(Person person){
        Command cmd = new Command("authenticate-username-pass");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(person);
        }  catch (ClassCastException e) {
            System.out.println(e);
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //ROOM
    public ArrayList<Room> getRooms(){
    	Command cmd = new Command("getRooms");
    	return null;
    }
    
    public ArrayList<Room> getAvailableRooms(Event e){
    	Command cmd = new Command("getAvailableRooms-event");
    	return null;
    }

 
}
