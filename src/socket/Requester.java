package socket;

import models.Person;
import models.UserGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;

/**
 * Created by sondrehj on 02.03.2015.
 */
public class Requester {

    Socket con;
    public Requester (Socket con){
        this.con = con;
    }

    public ArrayList<Person> getPersons(ArrayList<UserGroup> userGroups){
        ArrayList<Person> persons = null;
        Command cmd = new Command("getPersons-usergroup");
        try {
            ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            System.out.println("HEY");
            oos.writeObject(cmd);
            System.out.println("Sending command");
            oos.writeObject(userGroups);
            InputStream is = con.getInputStream();
            ObjectInputStream os = new ObjectInputStream(is);
            Object o = os.readObject();
            persons = (ArrayList<Person>) o;
            System.out.println("Reading objects");
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


}
