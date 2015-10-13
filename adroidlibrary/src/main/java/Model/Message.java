package Model;

import java.util.Calendar;

/**
 * Created by Mike on 9/27/2015.
 *
 * Messages between users/groups
 *
 * contains data sent on message, sender, recipients
 */
public class Message {

    private int id;
    private String sender, recipient, body;
    Calendar date;
    String time;

    public Message(){

    }

    public void Create_Notification(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getSender(){
        return sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public String getRecipient(){
        return recipient;
    }

    public void setRecipient(String recipient){
        this.recipient = recipient;
    }

    public String getBody(){
        return body;
    }

    public void setBody(String body){
        this.body = body;
    }

    public String getTime(){
        return time;
    }

    public void setTime(){
       time = date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE);
    }
}
