package ycp.adroidlibrary;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
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

import Model.Message;


public class Dashboard extends AppCompatActivity {
    int id = 0;
    String username = "";
    Button loginButton;
    TextView welcomeText;
    TextView postText;
    String url = "http://192.168.173.11:3000";

    // Posts
    List<String> posts = new ArrayList<>();
    ArrayAdapter<String> postAdapter;
    ListView postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Dashboard");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        loginButton = (Button) findViewById(R.id.tempLogin);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        postText = (TextView) findViewById(R.id.dashPostText);

        // Post List
        postList = (ListView) findViewById(R.id.dashPostList);
        postAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, posts);
        postList.setAdapter(postAdapter);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            username = extras.getString("username");
        }

        // If user is logged in, change button to log out/display name
        if(id != 0){
            String log = "Log Out";
            String hello = "Hello, ";
            loginButton.setText(log);
            welcomeText.setText(hello + username);
            getPostList();
        } else{
            postText.setVisibility(View.INVISIBLE);
            postList.setVisibility(View.INVISIBLE);
        }
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
            Intent intent = new Intent(Dashboard.this, GroupActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(Dashboard.this, FriendActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(Dashboard.this, EditActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_post){
            // Check if user is logged in
            if(id != 0) {
                // Create post dialog pop up
                final Dialog postDialog = new Dialog(Dashboard.this);

                // Set dialog text
                postDialog.setContentView(R.layout.dialog_post);
                postDialog.setTitle("Submit New Post");

                // Find button, text
                Button post_submit = (Button) postDialog.findViewById(R.id.dialogPostButton);
                final EditText post_text = (EditText) postDialog.findViewById(R.id.dialogPostBody);

                post_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String>  params = new HashMap<>();
                        params.put("content",   post_text.getText().toString());

                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + "/microposts/"+ Integer.toString(id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
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
                        Singleton.getInstance(Dashboard.this).addToRequestQueue(postRequest);

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

    public void onCalendarPress(View v){
        // Change view to Calendar
        Intent intent = new Intent(Dashboard.this, calendarActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onMessagePress(View v){
        // Change view to Calendar
        Intent intent = new Intent(Dashboard.this, MessageActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onProfilePress(View v){
        if(id !=0) {
            // Change view to Profile
            Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            startActivity(intent);
        } else{
            Toast toast = Toast.makeText(getApplicationContext(), "You must log in to view a profile", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void getPostList(){
            // Get Array of Posts
            JsonArrayRequest postsRequest = new JsonArrayRequest(url + "/microposts/" + Integer.toString(id) + "/posts.json", new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    // Set the Text Fields to Acquired Information
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            posts.add(obj.get("content").toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {
                    // Error communicating with server, print it
                    VolleyLog.e("Error: " + e.getMessage());
                }
            });

            // Add Request to Queue
            Singleton.getInstance(this).addToRequestQueue(postsRequest);
    }

    public void onDashLoginPress(View v){
        // No User is logged in, take them to log in screen
        if(id == 0) {
            Intent intent = new Intent(Dashboard.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        // A user is logged in, log them out
        else {
            // Pass User Id to server
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", Integer.toString(id));

            JsonObjectRequest passRequest = new JsonObjectRequest(Request.Method.DELETE, url+"/logout.json",new JSONObject(params), new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response){
                    try{
                        // Successful Logout
                        if(response.get("success").toString().equals("true")){
                            // Show success message
                            Toast toast = Toast.makeText(getApplicationContext(), "Log Out Successful", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            // Restart Activity
                            Intent intent = new Intent(Dashboard.this, Dashboard.class);
                            startActivity(intent);
                            finish();
                        }
                        // Failed. Somehow.
                        else{
                            // Show failure message
                            Toast toast = Toast.makeText(getApplicationContext(), "Log Out Failed", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            // Restart Activity
                            Intent intent = new Intent(Dashboard.this, Dashboard.class);
                            intent.putExtra("id", id);
                            intent.putExtra("username", username);
                            startActivity(intent);
                            finish();
                        }
                    } catch (JSONException e){
                        // There was an Error, print it
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    //error!
                    VolleyLog.e("Error: " + error.getMessage());
                }
            });

            // Add Request to Queue
            Singleton.getInstance(this).addToRequestQueue(passRequest);
        }
    }
}
