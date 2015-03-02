package socket;

import models.Calendar;
import models.Person;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by hegelstad on 24/02/15.
 */
public class SocketClient {
    public static void main(String[] args) {
        /** Define a host server */
        String host = "78.91.65.206";
        /** Define a port */
        int port = 25025;

        StringBuffer instr = new StringBuffer();
        String TimeStamp;
        System.out.println("SocketClient initialized");
        try {
            /** Obtain an address object of the server */
            InetAddress address = InetAddress.getByName(host);
            /** Establish a socket connetion */
            Socket connection = new Socket(address, port);
            /** Instantiate a BufferedOutputStream object */
            BufferedOutputStream bos = new BufferedOutputStream(connection.
                    getOutputStream());

            /** Instantiate an OutputStreamWriter object with the optional character
             * encoding.
             */
            OutputStreamWriter osw = new OutputStreamWriter(bos, "US-ASCII");

            TimeStamp = new java.util.Date().toString();
            String process = "Calling the Socket Server on "+ host + " port " + port +
                    " at " + TimeStamp +  (char) 13;

            String command = "getUserGroups";
            Person p = new Person("Sondre", "sondre", 2);
            ArrayList<Person> persons = new ArrayList<>();
            persons.add(p);
            persons.add(new Person("Pelle", "yolo", 3));
            persons.add(new Person("ss", "ss", 4));

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(connection.getOutputStream());
            //oos.writeObject("getUserGroups");
            oos.writeObject(p);
            
            osw.write(process);
            osw.flush();

            /** Instantiate a BufferedInputStream object for reading* 
             * incoming socket streams.
             */
            BufferedInputStream bis = new BufferedInputStream(connection.
                    getInputStream());
            
            /**Instantiate an InputStreamReader with the optional
             * character encoding.
             */
            InputStreamReader isr = new InputStreamReader(bis, "US-ASCII");

            RequestHandler s = new RequestHandler(connection);
            switch (command){
            	case "getUserGroups": System.out.println(s.getUserGroups());
            }
            //System.out.println(s.getUserGroups());

            /**Read the socket's InputStream and append to a StringBuffer */
            int c;
            while ((c = isr.read()) != 13)
                instr.append( (char) c);

            /** Close the socket connection. */

            connection.close();
            System.out.println(instr);
        }
        catch (IOException f) {
            System.out.println("IOException: " + f);
        }
        catch (Exception g) {
            System.out.println("Exception: " + g);
        }
    }
}