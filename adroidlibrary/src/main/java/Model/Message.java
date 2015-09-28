package Model;

import java.util.Date;

/**
 * Created by Mike on 9/27/2015.
 *
 * Messages between users/groups
 *
 * contains data sent on message, sender, recipients
 */
public class Message {

    private String sender, recipient, body;
    Date date;

    public Message(){

    }

    public void Create_Notification(){

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

    public Date getDate(){
        return date;
    }

    public void setDate(){
    }
}
