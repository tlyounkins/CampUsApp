package Model;

public class Event{
    private int id;
    private long start_time, end_time, date ;
    private String location, description, participants;

    public Event() {

    }

    public Event(int id,long start_time, long end_time, long date,String location, String description, String participants){
        this.id = id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.date = date;
        this.location = location;
        this.description = description;
        this.participants = participants;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(long end_time) {
        this.end_time = end_time;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

}