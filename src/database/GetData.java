package database;
import models.Event;
import models.Notification;
import models.UserGroup;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * Created by sondrehj on 23.02.2015.
 */

public class GetData {

    public static ArrayList<UserGroup> getUserGroup() throws SQLException {
        ArrayList<UserGroup> users = new ArrayList<>();
        Connection con = DBConnect.getConnection();
        //Execute query
        Statement stmt = con.createStatement();
        String sql = "SELECT UserGroupID, Name FROM UserGroup";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int UserGroupID = rs.getInt("UserGroupID");
            String Name = rs.getString("Name");
            //users.add(new UserGroup(UserGroupID, Name));
        }
        rs.close();
        stmt.close();
        con.close();
        return users;
    }

    public static ArrayList<Notification> getNotifications() throws SQLException {
        ArrayList<Notification> notifications = new ArrayList<>();
        Connection con = DBConnect.getConnection();
        //Execute query
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM Notification";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.toString());
        }
        rs.close();
        stmt.close();
        con.close();
        return notifications;
    }

    public static ArrayList<Event> getEvents() throws SQLException {
        ArrayList<Event> events = new ArrayList<>();
        Connection con = DBConnect.getConnection();
        //Execute query
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM Event";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            int EventID = rs.getInt("EventID");
            String Name = rs.getString("Name");
            ArrayList<User> participants = new ArrayList<>();
            LocalDate date = LocalDate.parse(rs.getString("Date"));
            LocalTime from = LocalTime.parse(rs.getString("From"));
            int duration = rs.getInt("Duration");
            events.add(new Event(EventID, Name, participants, date, from, duration));
        }
        rs.close();
        stmt.close();
        con.close();
        return events;
    }

    public static ArrayList<User> getEventParticipants(int EventID) throws SQLException {
        ArrayList<User> participants = new ArrayList<>();
        Connection con = DBConnect.getConnection();
        //Execute query
        Statement stmt = con.createStatement();
        String sql =
                "SELECT p.PersonID, p.Name, a.EventID\n"
                        + "FROM Attends AS a JOIN Person AS p\n"
                        + "ON a.PersonID = p.PersonID\n"
                        + "WHERE a.Attends = \'1\' AND a.EventID = \'" + EventID + "\';";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String Name = rs.getString("Name");
            LocalDate date = LocalDate.parse(rs.getString("Date"));
            LocalTime from = LocalTime.parse(rs.getString("From"));
            int duration = rs.getInt("Duration");
        }
        rs.close();
        stmt.close();
        con.close();
        return participants;
    }



    public static String getPassword(String username) throws SQLException {

        Connection con = DBConnect.getConnection();
        //Execute query
        String pass;
        Statement stmt = con.createStatement();
        String sql = "SELECT * FROM `Person` WHERE Username = \'" + username + "\'";
        ResultSet rs = stmt.executeQuery(sql);
        try {
            rs.next();
            pass = rs.getString("Password");
        } catch(Exception e) {
            throw new IllegalArgumentException("Username doesn't exist'");
        }
        rs.close();
        stmt.close();
        con.close();
        return pass;
    }
}


