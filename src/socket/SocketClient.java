

package socket;

import models.Person;
import models.UserGroup;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class SocketClient {
    public static void main(String[] args) {
        /** Define a host server */

        String host = "78.91.73.10";

        /** Define a port */
        int port = 25025;

        System.out.println("SocketClient initialized");
        try {
            /** Obtain an address object of the server */
            InetAddress address = InetAddress.getByName(host);
            /** Establish a socket connetion */
            Socket connection = new Socket(address, port);
            
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
            
     
            Requester r = new Requester(connection);
            System.out.println("Requester initialized");
            //r.getPersons(ug);
            r.authenticate(p);

            connection.close();
        }
        catch (IOException f) {
            System.out.println("IOException: " + f);
        }
        catch (Exception g) {
            System.out.println("Exception: " + g);
        }
    }
}