package ycp.adroidlibrary;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupProfileActivity extends AppCompatActivity {
    int user_id, group_id;
    TextView groupName, groupDescription;
    List<String> members = new ArrayList<>();
    ArrayAdapter<String> memberAdapter;
    ListView memberList;
    String url = "http://campus-app.herokuapp.com";
    //String url = "http://192.168.172.105:3000";
    String username, school;

    // Posts
    List<String> posts = new ArrayList<>();
    ArrayAdapter<String> postAdapter;
    ListView postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //changing title of Activity
        setTitle("Group Profile");
        setContentView(R.layout.activity_group_profile);

        TextView memberHeader = new TextView(GroupProfileActivity.this);
        TextView postHeader = new TextView(GroupProfileActivity.this);

        memberHeader.setText("List of Members");
        postHeader.setText("Group Posts");

        groupName = (TextView) findViewById(R.id.gProfileGroupName);
        groupDescription = (TextView) findViewById(R.id.gProfileGroupDescription);
        memberList = (ListView) findViewById(R.id.gMemberView);
        memberAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, members);
        memberList.setAdapter(memberAdapter);

        postList = (ListView) findViewById(R.id.gProfileGroupPosts);
        postAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, posts);
        postList.setAdapter(postAdapter);

        memberList.addHeaderView(memberHeader);
        postList.addHeaderView(postHeader);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("id");
            group_id = extras.getInt("group_id");
            username = extras.getString("username");
            school = extras.getString("school");
        }

       // When an item is clicked, take the user to that profile
        memberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String name = memberList.getItemAtPosition(position).toString();
                if (name != null) {
                    final Intent intent = new Intent(GroupProfileActivity.this, ProfileActivity.class);
                    intent.putExtra("id", user_id);
                    intent.putExtra("username", username);
                    intent.putExtra("school", school);
                    // Get ID from server
                    JsonObjectRequest idRequest = new JsonObjectRequest(Request.Method.GET, url + "/users/findId/" + name + ".json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int userId = Integer.parseInt(response.get("id").toString());
                                intent.putExtra("id", user_id);
                                intent.putExtra("other_id", userId);
                                intent.putExtra("username", username);
                                intent.putExtra("school", school);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            VolleyLog.e("Error: " + e.getMessage());
                        }
                    });

                    Singleton.getInstance(getApplicationContext()).addToRequestQueue(idRequest);
                }
            }
        });

        // Send JsonRequest to get fields
        JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(group_id)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName.setText(response.get("groupName").toString());
                    groupDescription.setText(response.get("description").toString());
                    //groupMember.setText(response.get("username").toString());
                } catch (JSONException e){
                    // There was an error, print it
                    e.printStackTrace();
                }
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e){
                // Error communicating with server, print it
                VolleyLog.e("Error: " + e.getMessage());
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest);

        // Send request to get list of members
        JsonArrayRequest memberRequest = new JsonArrayRequest(url + "/groups/" + Integer.toString(group_id) + "/members.json", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        members.add(obj.get("username").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                memberAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(error.getMessage());
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(memberRequest);

        // Send request to get posts
        JsonArrayRequest postRequest = new JsonArrayRequest(url + "/group_microposts/" + Integer.toString(group_id) +"/posts.json", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        posts.add(obj.get("content").toString());
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                postAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(error.getMessage());
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(postRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int item_id = item.getItemId();

        if (item_id == R.id.action_Clubs){
            // Start Group Activity
            Intent intent = new Intent(GroupProfileActivity.this, GroupActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(GroupProfileActivity.this, FriendActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(GroupProfileActivity.this, EditActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_post){
            // Check if user is logged in
            if(user_id != 0) {
                // Create post dialog pop up
                final Dialog postDialog = new Dialog(GroupProfileActivity.this);

                // Set dialog text
                postDialog.setContentView(R.layout.dialog_post);
                postDialog.setTitle("Submit New Group Post");

                // Find button, text
                Button post_submit = (Button) postDialog.findViewById(R.id.dialogPostButton);
                final EditText post_text = (EditText) postDialog.findViewById(R.id.dialogPostBody);

                post_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String>  params = new HashMap<>();
                        params.put("content",   post_text.getText().toString());

                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + "/group_microposts/"+ Integer.toString(group_id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.get("success").toString().equals("true")) {
                                        // Display Successful message
                                        Toast toast = Toast.makeText(getApplicationContext(), "Post post: Post posted", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    } else {
                                        // Failure to Register User
                                        Toast toast = Toast.makeText(getApplicationContext(), "Error Posting.", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                    }

                                } catch (JSONException e) {
                                    // There was an Error, print it
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //error!
                                VolleyLog.e("Error: " + error.getMessage());
                            }
                        });

                        // Add Request to Queue
                        Singleton.getInstance(GroupProfileActivity.this).addToRequestQueue(postRequest);

                        postDialog.dismiss();
                    }
                });

                // Create dialog
                postDialog.show();
            } else{
                Toast toast = Toast.makeText(getApplicationContext(), "You must log in to create a post", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void onEventPress(View v){
        Intent intent = new Intent(GroupProfileActivity.this, EventActivity.class);
        intent.putExtra("id", user_id);
        intent.putExtra("group_id", group_id);
        intent.putExtra("username", username);
        intent.putExtra("school", school);
        startActivity(intent);
    }

    public void onApplyPress(View v){
        // Check to make sure user is logged in
        if(user_id != 0){

            // Send JsonRequest to get fields
            JsonObjectRequest joinRequest = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(group_id)+"/join/" + Integer.toString(user_id)+".json", null, new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response){
                    try {
                        if (response.get("Success").toString().equals("true")){
                            // Display Success Message
                            Toast toast = Toast.makeText(getApplicationContext(), "Successfully Joined Group", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else{
                            // Display Error Message
                            Toast toast = Toast.makeText(getApplicationContext(), "Error Joining Group", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } catch (JSONException e){
                        // There was an error, print it
                        e.printStackTrace();
                    }
                }
            },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError e){
                    // Error communicating with server, print it
                    VolleyLog.e("Error: " + e.getMessage());
                }
            });

            // Add Request to Queue
            Singleton.getInstance(this).addToRequestQueue(joinRequest);
        } else{
            // Display Error Message
            Toast toast = Toast.makeText(getApplicationContext(), "You must be logged in to join a group", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
