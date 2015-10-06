/**
 * Created by Mike on 9/18/2015.
 *
 * Contains all information stored in a users User
 */

package Model;

public class User {
   private String username, email, password, location, school;
    private String[] clubs, friends;
   private int age, month, day, year, friend_count, club_count;

    // TODO: Walls, posts, messages
    public User(){

    }

    public User(String username, String email, String password, String location, String school, String[] clubs, String[] friends, int age, int month, int day, int year, int friend_count, int club_count){
        this.username = username;
        this.email = email;
        this.password = password;
        this.location = location;
        this.school = school;
        this.clubs = clubs;
        this.friends = friends;
        this.age = age;
        this.day = day;
        this.month = month;
        this.year = year;

        this.friend_count = friend_count;
        this.club_count = club_count;
    }

    // Send a Message to specified recipient(s)
    public void sendMessage(){

    }

    // Send a request to a Group for membership
    public void groupRequest(){

    }

    // Send a friend request to another user
    public void sendFriendRequest(){

    }

    // Post a message to wall
    public void post(){

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

    public int getDobYear(){
        return year;
    }

    public int getDobMonth(){
        return month;
    }

    public int getDobDay(){
        return day;
    }

    public void setDob(int month, int day, int year){
        this.month = month;
        this.day = day;
        this.year = year;
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

