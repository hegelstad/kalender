package database;

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

    public static ArrayList<User> getUsers() throws SQLException {
        ArrayList users = new ArrayList<User>();
        Statement stmt = null;
        Connection con = DBConnect.getConnection();
        //Execute query
        stmt = con.createStatement();
        String sql;
        sql = "SELECT UserID, Name, Username, Password FROM User";
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("--- Users ---");
        while (rs.next()) {
            //Retrieve by column name
            int UserID = rs.getInt("UserID");
            String Name = rs.getString("Name");
            String Username = rs.getString("Username");
            String Password = rs.getString("Password");
            //Display values
            System.out.print("UserId: " + UserID + "\n");
            System.out.print("Name: " + Name + "\n");
            System.out.println("Username: " + Username);
        }
        return users;
    }

}
    
    
    
