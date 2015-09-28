package Model;

/**
 * Created by Tyler on 9/27/15.
 */
public class Group {
    private String groupname,admin, boardmember, bio, member;
    private int num_groupmembers;

    public Group(String groupname, int num_groupmembers){
        this.groupname = groupname;
        this.num_groupmembers = num_groupmembers;
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
}
