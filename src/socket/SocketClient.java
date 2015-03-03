

package socket;

import models.Person;
import models.UserGroup;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient {

	public static void main(String[] args) {
    	Person p = new Person("Sondre", "yolo");
        ArrayList<Person> persons = new ArrayList<>();
        UserGroup u = new UserGroup(7, "Sondre", null);
        UserGroup u1 = new UserGroup(6, "Sondre", null);
        ArrayList<UserGroup> ug = new ArrayList<>();
        ug.add(u);
        ug.add(u1);
        persons.add(p);
        persons.add(new Person("Pelle", "yolo", 3));
        persons.add(new Person("ss", "ss", 4));
        
 
        Requester r = new Requester();
        System.out.println("Requester initialized");
        //r.getPersons(ug);
        r.authenticate(p);

    }
}