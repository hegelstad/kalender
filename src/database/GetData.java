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
        ArrayList<User> users = new ArrayList<>();
        Connection con = DBConnect.getConnection();
        //Execute query
        Statement stmt = con.createStatement();
        String sql;
        sql = "SELECT UserID, Name FROM User";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            int UserID = rs.getInt("UserID");
            String Name = rs.getString("Name");
            users.add(new User(UserID, Name));
        }
        System.out.println(users);
        return users;
    }

}
    
    
    
