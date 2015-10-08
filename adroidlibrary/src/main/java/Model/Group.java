package Model;

/**
 * Created by Tyler on 9/27/15.
 */
public class Group {

    private String admin;
    private String boardmember;
    private String bio;
    private String member;
    private int num_groupmembers;

    public Group() {

    }

    public Group(String groupname, String admin, String boardmember, String bio, String member,
                 int num_groupmembers) {
        this.admin = admin;
        this.boardmember = boardmember;
        this.bio = bio;
        this.member = member;
        this.num_groupmembers = num_groupmembers;
        this.groupname = groupname;
    }

    public void add_member(String member) {

    }
    public void remove_member(String member) {

    }
    public void add_boardmember(String member) {

    }
    public void remove_boardmember(String boardmember) {

    }
    // Post a message to wall
    public void post() {

    }
    // Send a Message to specified recipient(s)
    public void sendMessage(){

    }

    // Getters and Setters
    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    private String groupname;

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getBoardmember() {
        return boardmember;
    }

    public void setBoardmember(String boardmember) {
        this.boardmember = boardmember;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getNum_groupmembers() {
        return num_groupmembers;
    }

    public void setNum_groupmembers(int num_groupmembers) {
        this.num_groupmembers = num_groupmembers;
    }

}
