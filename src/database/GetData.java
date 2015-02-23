package database;

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
        String sql = "SELECT UserID, Name FROM UserGroup";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int UserID = rs.getInt("UserID");
            String Name = rs.getString("Name");
            users.add(new User(UserID, Name));
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
        String y = "";
        while (rs.next()) {
            System.out.println(rs.toString());
        }
        rs.close();
        stmt.close();
        con.close();
       
        return notifications;
    }
    
    
}
    
    
    
