package socket;

import models.Attendant;
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
import java.util.ArrayList;

/**
 * Created by sondrehj on 02.03.2015.
 */
public class Requester {
	
    Socket con;
    /**
     * Requester oppretter en connection med servereren, og etter det kan man kjøre metoder mot serveren.
     * IP TIL SERVER MÅ SETTES HER!
     */
    public Requester (){
    	String host = "localhost";
        /** Define a port */
        int port = 25025;

        try {
            /** Obtain an address object of the server */
            InetAddress address = InetAddress.getByName(host);
            /** Establish a socket connetion */
            con = new Socket(address, port);
            con.setSoTimeout(6500);
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
    /**
     * Tar inn en person og returnerer alle userGroups som personen er medlem i.
     * @param p
     * @return
     */
    public ArrayList<UserGroup> getUserGroups(Person p){
    	Command cmd = new Command("getUserGroups-person");
    	ArrayList<UserGroup> userGroups = null;
        try {
            ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(p);
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
    
    /**
     * Tar inn en liste med userGroups og returnerer personer som er medlem i disse.
     * @param userGroups
     * @return
     */
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

    /**
     * Tar inn en liste med kalendere og returnerer alle userGroups som hører til kalenderene.
     * @param cal
     * @return
     */
    public ArrayList<UserGroup> getUserGroups(ArrayList<Calendar> cal){
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

    /**
     * Tar in en liste med userGroups og sletter de fra databasen.
     * @param userGroups
     */
    public void deleteUserGroups(ArrayList<UserGroup> userGroups){
    	Command cmd = new Command("deleteUserGroups-usergroups");
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
    
    /**
     * Tar inn en userGroup og oppretter den i databasen.
     * @param ug
     */
    public UserGroup createUserGroup(UserGroup ug){
    	Command cmd = new Command("createUserGroup-usergroup");
    	UserGroup userGroup = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(ug);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            userGroup = (UserGroup) o;
            System.out.println(userGroup);

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
    	return userGroup;
    }
    
    /**
     * Tar inn en userGroup, og oppdaterer userGroup i databasen.
     * @param ug
     */
    public void addUsers(UserGroup ug){
    	Command cmd = new Command("addUsers-usergroup");
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
    
    /**
     * Henter ut alle private userGroups fra databasen.
     * @return
     */
    public ArrayList<UserGroup> getPrivateUserGroups(){
    	Command cmd = new Command("getPrivateUserGroups");
    	ArrayList<UserGroup> userGroups = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            userGroups = (ArrayList<UserGroup>) o;
            for (UserGroup u : userGroups){
                System.out.println(u);
            }

    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }catch(ClassNotFoundException e){
             System.out.println(e);
         }catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    	return userGroups;
    }
    
    /**
     * Henter den personlige userGroupen for en Person.
     * @param p
     * @return
     */
    public UserGroup getPersonalUserGroup(Person p){
    	Command cmd = new Command("getPersonalUserGroup-person");
    	UserGroup userGroup = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(p);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            userGroup = (UserGroup) o;
            System.out.println(userGroup);

    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }catch(ClassNotFoundException e){
             System.out.println(e);
         }catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    	return userGroup;
    }

    /**
     * Henter saltet til et brukernavn
     * @param p
     * @return
     */
    public UserGroup getSalt(String p){
        Command cmd = new Command("getSalt");
        String salt = null;
        try{
            ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(p);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            salt = (String) o;
            System.out.println(salt);

        }  catch (ClassCastException e) {
            System.out.println(e);
        }catch(ClassNotFoundException e){
            System.out.println(e);
        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return salt;
    }

    
    //CALENDAR
    /**
     * Tar inn en usergroup og henter alle kalendere usergroupen tilhører.
     * @param ug
     * @return
     */
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
    public void addUserGroup(Calendar cal, UserGroup ug){
    	Command cmd = new Command("addUserGroup-calendar");
    	Calendar calendar = null;
    	try{
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
    
    /**
     * Tar inn et kalenderobjekt, oppretter kalenderen i databasen, og returnerer kalender med ny id.
     * @param cal
     * @return
     */
    public Calendar createCalendar(Calendar cal){
    	Command cmd = new Command("createCalendar-calendar");
    	Calendar calendar = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(cal);
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
     * Tar inn et kalenderobjekt og fjerner kalenderen fra databasen.
     * @param cal
     */
    public void deleteCalendar(Calendar cal){
    	Command cmd = new Command("deleteCalendar-calendar");
     	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(cal);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    
    //EVENTS
    /**
     * Tar inn en arraylist med kalendere og returnerer alle Events som hører til kalenderene
     * @param cal
     * @return
     */
    public ArrayList<Event> getEvents(ArrayList<Calendar> cal){
    	Command cmd = new Command("getEvents-calendars");
    	ArrayList<Event> events = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(cal);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            events = (ArrayList<Event>) o;
            for (Event event : events){
                System.out.println(event);
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
    	return events;
    }
    
    /**
     * Denne må returnere et Event og oppdatere eventet.
     * Tar inn et event og et rom, oppretter eventet, booker rom og returnerer eventet.
     * @param event
     */
    public Event createEvent(Event event){
    	Command cmd = new Command("createEvent-event");
    	Event ev = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(event);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            ev = (Event) o;
            System.out.println(ev);

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
    	return ev;
    }
    
    /**
     * Oppdaterer event.
     * @param event
     */
    public void editEvent(Event event){
    	Command cmd = new Command("editEvent-event");
    	Event ev = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(event);
    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }
         catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    }
    
    /**
     * Tar inn et event og sletter eventet fra databasen.
     * @param event
     */
    public void deleteEvent(Event event){
    	Command cmd = new Command("deleteEvent-event");
     	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(event);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    
    //NOTIFICATION
    /**
     * Denne metoden tar inn en person og returnerer alle notifications for personen.
     * @param p
     * @return
     */
    public ArrayList<Notification> getNotifications(UserGroup ug){
    	Command cmd = new Command("getNotifications-person");
    	ArrayList<Notification> n = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(ug);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            n = (ArrayList<Notification>) o;
            for (Notification not : n){
                System.out.println(not);
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
    	return n;
    }
    
    /**
     * Tar inn en notification og oppretter notification i databasen.
     * @param n
     * @return
     */
    public void setNotification(Notification n){
    	Command cmd = new Command("setNotification-notification");
    	Notification notification = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(n);
    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }
         catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    }
    
    /**
     * Tar inn en notification og en person og oppdaterer notification til "read" i databasen.
     * @param n
     * @param p
     */
    public void setRead(Notification n, UserGroup ug){
    	Command cmd = new Command("setRead-notification-person");
     	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(n);
    		oos.writeObject(ug);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    
    //PERSON
    /**
     * Tar inn en person og returnerer "hele" personen med id, navn etc. (nok å ta inn brukernavn og passord i personen)
     * @param p
     * @return
     */
    public Person getPerson(Person p){
    	Command cmd = new Command("getPerson-person");
    	Person person = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(p);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            person = (Person) o;
            System.out.println(person);

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
    	return person;
    }
    
    /**
     * Tar inn en person, og oppretter personen i databasen.
     * @param p
     * @return
     */
    public void createPerson(Person p){
    	Command cmd = new Command("createPerson-person");
    	Person person = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(p);
    	 }  catch (ClassCastException e) {
             System.out.println(e);
         }
         catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    }
    
    /**
     * Tar inn en person og sletter personen fra databasen
     * @param p
     */
    public void deletePerson(Person p){
    	Command cmd = new Command("deletePerson-person");
    	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(p);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }

    /**
     *
     * LogIn må returnere en person og sette navn etc.
     * @param person
     */
    public Person authenticate(Person person){
        Command cmd = new Command("authenticate-username-pass");
        Person p = null;
        try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(person);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            p = (Person) o;
            System.out.println(p);

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
    	return p;
    }

    
    //ROOM
    /**
     * Returnerer en liste med alle rom.
     * @return
     */
    public ArrayList<Room> getRooms(){
    	Command cmd = new Command("getRooms");
    	ArrayList<Room> r = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            r = (ArrayList<Room>) o;
            for (Room room : r){
                System.out.println(room);
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
    	return r;
    }
    
    /**
     * Tar inn et event og returnerer tilgjengelige rom for eventet.
     * @param e
     * @return
     */
    public ArrayList<Room> getAvailableRooms(Event ev){
    	Command cmd = new Command("getAvailableRooms-event");
    	ArrayList<Room> r = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(ev);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            r = (ArrayList<Room>) o;
            for (Room room : r){
                System.out.println(room);
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
    	return r;
    }
    
    /**
     * Tar inn et event og et rom og oppretter rombooking. Om et event eksisterer med rombooking fra før vil denne
     * booking bli slettet, og en ny opprettet.
     * @param ev
     * @param rm
     */
    public void bookRoom(Event ev, Room rm){
    	Command cmd = new Command("bookRoom-event-room");
    	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(ev);
    		oos.writeObject(rm);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    /**
     * Tar inn et event og returnerer eventuell roombooking for eventet.
     * @param ev
     * @return
     */
    public Room getEventRoom(Event ev){
    	Command cmd = new Command("getEventRoom-event");
    	Room r = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(ev);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            r = (Room) o;
            System.out.println(r);
            }catch (ClassCastException e) {
             System.out.println(e);
         }
         catch(ClassNotFoundException e){
             System.out.println(e);
         }
         catch (IOException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }
    	return r;
    }
    
    
    //Attends
    /**
     * Updates person to mark if he is attending or not in the database.
     * @param ev
     * @param p
     * @param s
     */
    public void updateAttends(Event ev, Attendant a){
    	Command cmd = new Command("updateAttends-event-attendant");
    	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(ev);
    		oos.writeObject(a);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    /**
     * Inserts an attendant to Attends
     * @param ev
     * @param p
     * @param s
     */
    public void setAttends(Event ev, ArrayList<UserGroup> ug){
    	Command cmd = new Command("setAttends-event-attendants");
    	try {
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
    		oos.writeObject(cmd);
    		oos.writeObject(ev);
    		oos.writeObject(ug);
    	}  catch (ClassCastException e) {
    		System.out.println(e);
    	}
    	catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    }
    
    /**
     * Henter alle inviterte deltakere til et event.
     * @param ev
     * @return
     */
    public ArrayList<Attendant> getAttendands(Event ev){
    	Command cmd = new Command("getAttendands-event");
    	ArrayList<Attendant> attendants = null;
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            oos.writeObject(cmd);
            oos.writeObject(ev);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            attendants = (ArrayList<Attendant>) o;
            for (Attendant a : attendants){
                System.out.println(a);
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
    	return attendants;
    }
    
    /**
     * Metode for å lukke connection med server.
     * @return
     */
    public boolean closeConnection(){
    	try {
			con.close();
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
}
