/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;


import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Abinodh Thomas
 */
public class DatabaseConnection {
    
    private Connection connObject;
    private final static String studentNumber = "c0666528";
    
    DatabaseConnection() throws ClassNotFoundException, SQLException{
        Class.forName("org.apache.derby.jdbc.ClientDriver");
        connObject = DriverManager.getConnection("jdbc:derby://localhost:1527/SampleDB", "c0666528", "c0666528");   
    }
    
    public void addMessage(Message message) throws SQLException {
        PreparedStatement statement = connObject.prepareStatement("INSERT INTO messagetable(author, title, content, dateval) VALUES(?,?,?,?)");
        statement.setString(1, message.getAuthor());
        statement.setString(2, message.getTitle());
        statement.setString(3, message.getContent());
        statement.setDate(4, new java.sql.Date(message.getDate().getTime()));
        statement.execute();
    }
    
    public void updateMessage(Message message) throws SQLException {
        PreparedStatement statement = connObject.prepareStatement("UPDATE messagetable SET author=?, title=?, content=?, date=? WHERE id=?");
        statement.setString(1, message.getAuthor());
        statement.setString(2, message.getTitle());
        statement.setString(3, message.getContent());
        statement.setDate(4, new java.sql.Date(message.getDate().getTime()));
        statement.setInt(5, message.getId());
        statement.execute();
    }
    
    public void deleteMessage(Message message) throws SQLException {
        PreparedStatement statement = connObject.prepareStatement("DELETE FROM messagetable WHERE id=?");
        statement.setInt(1, message.getId());
        statement.execute();
    }
    
    public ArrayList<Message> selectAllMessage() throws SQLException {
        ArrayList allMessagesArray = new ArrayList<>();
        PreparedStatement statement = connObject.prepareStatement("SELECT * messagetable");
        ResultSet allMessages = statement.executeQuery();
        while(allMessages.next()){
            Message msg = new Message();
            msg.setAuthor(allMessages.getString("author"));
            msg.setTitle(allMessages.getString("title"));
            msg.setContent(allMessages.getString("content"));
            msg.setDate(allMessages.getDate("dateval"));
            allMessagesArray.add(msg);
        };
        return allMessagesArray;
    }
    
    public ArrayList<Message> selectMessage(int id) throws SQLException {
        ArrayList allMessagesArray = new ArrayList<>();
        PreparedStatement statement = connObject.prepareStatement("SELECT * messagetable WHERE id=?");
        statement.setInt(1, id);
        ResultSet allMessages = statement.executeQuery();
        while(allMessages.next()){
            Message msg = new Message();
            msg.setAuthor(allMessages.getString("author"));
            msg.setTitle(allMessages.getString("title"));
            msg.setContent(allMessages.getString("content"));
            msg.setDate(allMessages.getDate("dateval"));
            allMessagesArray.add(msg);
        };
        return allMessagesArray;
    }
}
