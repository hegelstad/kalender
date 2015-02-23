package database;

import com.sun.org.apache.xpath.internal.SourceTree;
import models.Notification;
import models.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by sondrehj on 23.02.2015.
 */
public class GetData {

    public static ArrayList<User> getUserGroup() throws SQLException {
        ArrayList<User> users = new ArrayList<>();
        Connection con = DBConnect.getConnection();
        //Execute query
        Statement stmt = con.createStatement();
        String sql = "SELECT UserGroupID, Name FROM UserGroup";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int UserGroupID = rs.getInt("UserGroupID");
            String Name = rs.getString("Name");
            users.add(new User(UserGroupID, Name));
        }
        rs.close();
        stmt.close();
        con.close();
        System.out.println(users);
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
    
    
    
