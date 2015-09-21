/**
 * Created by Mike on 9/18/2015.
 *
 * Contains all information stored in a users Profile
 */
public class Profile {
   private String username, email, password, location, school;
    private String[] clubs, friends;
   private int age, dob, friend_count, club_count;

    // TODO: Walls, posts, messages

    // Add to lists
    // TODO: Change to other classes when implemented String Club->Club/Group
    public Profile(){

    }

    public Profile(String username, String email, String password, String location, String school, String[] clubs, String[] friends, int age, int dob, int friend_count, int club_count){
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
        this.school = school;
        this.clubs = clubs;
        this.friends = friends;
        this.age = age;
        this.dob = dob;
        this.friend_count = friend_count;
        this.club_count = club_count;
    }

    public void addFriend(String friend){
        this.friends[friend_count] = friend;
        this.friend_count++;
    }

    public void addClub(String club){
        this.clubs[club_count] = club;
        this.club_count++;
    }

    // Getters/Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school){
        this.school = school;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public int getDob(){
        return dob;
    }

    public void setDob(int dob){
        this.dob = dob;
    }

    public String[] getClubs() {
        return clubs;
    }

    public void setClubs(String[] clubs) {
        this.clubs = clubs;
    }

    public String[] getFriends(){
        return friends;
    }

    public void setFriends(String[] friends){
        this.friends = friends;
    }

}
