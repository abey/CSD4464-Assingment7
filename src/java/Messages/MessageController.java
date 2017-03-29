/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Messages;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author Abinodh Thomas (Student ID: c0666528)
 * 
 */
    
@ApplicationScoped
@Named
public class MessageController {
    private final List<Message> messages = new ArrayList<>();
    private Message message = new Message();
    private DatabaseConnection dbconn;
    

    public MessageController() throws ClassNotFoundException, SQLException {
        populateList();
        dbconn = new DatabaseConnection();
    }

    private void populateList() {
        Message message = new Message();
        message.setAuthor("Double Double Mafia");
        message.setContent("Team Members: Abinodh, Gabriel & Preethi");
        message.setId(messages.size() + 1);
        message.setDate(new Date());
        message.setTitle("Our Team");
        messages.add(message);  
    }
    
    public ArrayList<Message> getMessageById(int id) throws SQLException {
        return dbconn.selectMessage(id);
    }
    
    public List<Message> getMessages() throws SQLException { 
        return dbconn.selectAllMessage();
    }
    
    public void addMessage() throws SQLException, ClassNotFoundException {
        dbconn.addMessage(message);
    }
    
    public ArrayList<Message> getMessagesOnDateRange(Date minDate, Date maxDate) {
        ArrayList<Message> messagesOnRange = new ArrayList<>();

        for (Message message : messages) {
            if (message.getDate().after(minDate) && message.getDate().before(maxDate)) {
                messagesOnRange.add(message);
            }
        }

        return messagesOnRange;
    }
    
    public void removeMessage(Message message) throws SQLException {
        dbconn.deleteMessage(message);
    }

    public void edit(Message m) throws SQLException {
        dbconn.updateMessage(m);
    }

    
    //---------------------------------------- Getters and Setters ---------------------------------------------------

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    
}