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

public class FriendActivity extends AppCompatActivity {
    String url = "http://campus-app.herokuapp.com";
    //String url = "http://192.168.172.105:3000";
    String username, school;
    int user_id = 0;

    // Posts
    List<String> users = new ArrayList<>();
    ArrayAdapter<String> userAdapter;
    ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //changing title of Activity
        setTitle("Users List");

        setContentView(R.layout.activity_friend);

        // Post List
        userList = (ListView) findViewById(R.id.friendList);
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        userList.setAdapter(userAdapter);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("id");
            username = extras.getString("username");
            school = extras.getString("school");
        }
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(user_id != 0) {
                    // Get group id at position, start profile activity
                    // Get ID from server
                    JsonObjectRequest idRequest = new JsonObjectRequest(Request.Method.GET, url + "/users/findId/" + userList.getItemAtPosition(position).toString() + ".json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int userId = Integer.parseInt(response.get("id").toString());
                                Intent intent = new Intent(FriendActivity.this, ProfileActivity.class);
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
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "You must log in to view a profile", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
        getUserList();
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
            Intent intent = new Intent(FriendActivity.this, GroupActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(FriendActivity.this, FriendActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(FriendActivity.this, EditActivity.class);
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
                final Dialog postDialog = new Dialog(FriendActivity.this);

                // Set dialog text
                postDialog.setContentView(R.layout.dialog_post);
                postDialog.setTitle("Submit New Post");

                // Find button, text
                Button post_submit = (Button) postDialog.findViewById(R.id.dialogPostButton);
                final EditText post_text = (EditText) postDialog.findViewById(R.id.dialogPostBody);

                post_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String> params = new HashMap<>();
                        params.put("content",   post_text.getText().toString());

                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + "/microposts/"+ Integer.toString(user_id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
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
                                        Toast toast = Toast.makeText(getApplicationContext(), "Error Registering.", Toast.LENGTH_LONG);
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
                        Singleton.getInstance(FriendActivity.this).addToRequestQueue(postRequest);

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

    public void getUserList(){
        // Get Array of Posts
        JsonArrayRequest postsRequest = new JsonArrayRequest(url+"/users/getAll.json",new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){

                // Set the Text Fields to Acquired Information
                for (int i = 0; i < response.length(); i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        if(school.equals(obj.get("school").toString())) {
                            users.add(obj.get("username").toString());
                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e){
                // Error communicating with server, print it
                VolleyLog.e("Error: " + e.getMessage());
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(postsRequest);
    }
}
