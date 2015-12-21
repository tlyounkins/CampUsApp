package ycp.adroidlibrary;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class ProfileActivity extends AppCompatActivity {
    String url = "http://campus-app.herokuapp.com";
    //String url = "http://192.168.172.23:3000";
    int id = 0;
    String logged_in_user, school;
    int other_id = 0;

    //user text views
    TextView username, firstname, lastname, age, gender, major, hometown, bio;

    // Posts
    List<String> posts = new ArrayList<>();
    ArrayAdapter<String> postAdapter;
    ListView postList;

    //profile text views
    ImageButton profileEditButton, addButton, messageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //changing title of Activity
        setTitle("Profile");

        TextView postHeader = new TextView(ProfileActivity.this);
        postHeader.setText("Your Recent Posts");

        setContentView(R.layout.activity_profile);

        //user description
        username = (TextView) findViewById(R.id.profileUsername);
        firstname = (TextView) findViewById(R.id.profileFirstName);
        lastname = (TextView) findViewById(R.id.profileLastName);
        age = (TextView) findViewById(R.id.profileAge);
        gender = (TextView) findViewById(R.id.profileGender);
        major = (TextView) findViewById(R.id.profileMajor);
        hometown = (TextView) findViewById(R.id.profileHometown);
        bio = (TextView) findViewById(R.id.profileBio);
        profileEditButton = (ImageButton) findViewById(R.id.profileEditButton);
        addButton = (ImageButton) findViewById(R.id.profileAddButton);
        messageButton = (ImageButton) findViewById(R.id.profileMessageButton);

        age.setVisibility(View.INVISIBLE);

        // Post List
        postList = (ListView) findViewById(R.id.profilePostList);
        postAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, posts);
        postList.setAdapter(postAdapter);
        postList.addHeaderView(postHeader);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            other_id = extras.getInt("other_id");
            logged_in_user = extras.getString("username");
            school = extras.getString("school");
        }

        // Hide edit button if viewing other profile
        if(other_id != 0 && other_id != id){
            profileEditButton.setVisibility(View.GONE);
        } else{
            // Hide add friend button if viewing own profile, change to
            // create post button
            addButton.setImageResource(android.R.drawable.ic_menu_edit);
            messageButton.setVisibility(View.GONE);
        }



        if (id != 0) {
            if(other_id != 0 && other_id != id) {
                // Send JsonRequest to get fields
                JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url + "/users/" + Integer.toString(other_id) + ".json", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Set the Text Fields to Acquired Information
                            username.setText(response.get("username").toString());
                            firstname.setText(response.get("firstname").toString());
                            lastname.setText(response.get("lastname").toString());
                            age.setText(response.get("age").toString());
                            if(response.get("gender").toString().equals("null")){
                                gender.setText("");
                            } else{
                                gender.setText(response.get("gender").toString());
                            }
                            if(response.get("major").toString().equals("null")){
                                major.setText("");
                            } else{
                                major.setText(response.get("major").toString());
                            }
                            if(response.get("hometown").toString().equals("null")){
                                hometown.setText("");
                            } else {
                                hometown.setText(response.get("hometown").toString());
                            }
                            if(response.get("bio").toString().equals("null")){
                                bio.setText("");
                            } else {
                                bio.setText(response.get("bio").toString());
                            }
                        } catch (JSONException e) {
                            // There was an error, print it
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Error communicating with server, print it
                        VolleyLog.e("Error: " + e.getMessage());
                    }
                });

                // Add Request to Queue
                Singleton.getInstance(this).addToRequestQueue(fieldsRequest);
            }
            else {
                // Send JsonRequest to get fields
                JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url + "/users/" + Integer.toString(id) + ".json", null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Set the Text Fields to Acquired Information
                            username.setText(response.get("username").toString());
                            firstname.setText(response.get("firstname").toString());
                            lastname.setText(response.get("lastname").toString());
                            age.setText(response.get("age").toString());
                            if(response.get("gender").toString().equals("null")){
                                gender.setText("");
                            } else{
                                gender.setText(response.get("gender").toString());
                            }
                            if(response.get("major").toString().equals("null")){
                                major.setText("");
                            } else{
                                major.setText(response.get("major").toString());
                            }
                            if(response.get("hometown").toString().equals("null")){
                                hometown.setText("");
                            } else {
                                hometown.setText(response.get("hometown").toString());
                            }
                            if(response.get("bio").toString().equals("null")){
                                bio.setText("");
                            } else {
                                bio.setText(response.get("bio").toString());
                            }
                        } catch (JSONException e) {
                            // There was an error, print it
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Error communicating with server, print it
                        VolleyLog.e("Error: " + e.getMessage());
                    }
                });

                // Add Request to Queue
                Singleton.getInstance(this).addToRequestQueue(fieldsRequest);
            }

            //send JSON request for posts
            getPostList();
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
            Intent intent = new Intent(ProfileActivity.this, GroupActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", logged_in_user);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(ProfileActivity.this, FriendActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", logged_in_user);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", logged_in_user);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_post){
            // Check if user is logged in
            if(id != 0) {
                // Create post dialog pop up
                final Dialog postDialog = new Dialog(ProfileActivity.this);

                // Set dialog text
                postDialog.setContentView(R.layout.dialog_post);
                postDialog.setTitle("Submit New Post");

                // Find button, text
                Button post_submit = (Button) postDialog.findViewById(R.id.dialogPostButton);
                final EditText post_text = (EditText) postDialog.findViewById(R.id.dialogPostBody);

                post_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final HashMap<String,String>  params = new HashMap<>();
                        params.put("content",   post_text.getText().toString());

                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + "/microposts/"+ Integer.toString(id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    if (response.get("success").toString().equals("true")) {
                                        // Display Successful message
                                        Toast toast = Toast.makeText(getApplicationContext(), "Post Successful", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER, 0, 0);
                                        toast.show();
                                        postAdapter.add(params.get("content"));
                                        postAdapter.notifyDataSetChanged();
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
                        Singleton.getInstance(ProfileActivity.this).addToRequestQueue(postRequest);

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

    public void onDashboardPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this,Dashboard.class);
        intent.putExtra("id", id);
        intent.putExtra("username", logged_in_user);
        intent.putExtra("school", school);
        startActivity(intent);
    }

    public void onCalenderPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this,calendarActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", logged_in_user);
        intent.putExtra("school", school);
        startActivity(intent);
    }

    public void onMessagePress(View v){
        // Change view to Calendar
        Intent intent = new Intent(ProfileActivity.this, MessageActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", logged_in_user);
        intent.putExtra("school", school);
        startActivity(intent);
    }
    public void onProfileEditPress(View v){
        Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", logged_in_user);
        intent.putExtra("school", school);
        startActivity(intent);
    }

   /*public void onProfileAddButton(View V) {
       if (other_id != 0 && other_id != id) {
           // Get Array of Posts
           JsonArrayRequest followRequest = new JsonArrayRequest(url + "/followers/" + Integer.toString(id) + ".json", null , new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {
                   try {
                       // Set the Text Fields to Acquired Information

                   } catch (JSONException e) {
                       // There was an error, print it
                       e.printStackTrace();
                   }
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError e) {
                   // Error communicating with server, print it
                   VolleyLog.e("Error: " + e.getMessage());
               }
           });

           // Add Request to Queue
           Singleton.getInstance(this).addToRequestQueue(followRequest);
       } else {
           JsonArrayRequest followRequest = new JsonArrayRequest(url + "/followers/" + Integer.toString(id) + ".json", null, new Response.Listener<JSONObject>() {
               @Override
               public void onResponse(JSONObject response) {
                   try {
                       // Set the Text Fields to Acquired Information

                   } catch (JSONException e) {
                       // There was an error, print it
                       e.printStackTrace();
                   }
               }
           }, new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError e) {
                   // Error communicating with server, print it
                   VolleyLog.e("Error: " + e.getMessage());
               }
           });

           // Add Request to Queue
           Singleton.getInstance(this).addToRequestQueue(followRequest);
       }

       //send JSON request for posts
       getPostList();
   }*/


    public void getPostList(){
        if(other_id != 0 && other_id != id) {
            // Get Array of Posts
            JsonArrayRequest postsRequest = new JsonArrayRequest(url + "/microposts/" + Integer.toString(other_id) + "/posts.json", new Response.Listener<JSONArray>() {
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
        } else{
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

    }

    public void onAddPress(View v){
        if(other_id != 0 && id != other_id){
            // Viewing other profile, add friend
            // TODO
            Toast toast = Toast.makeText(getApplicationContext(), "Feature coming soon", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else{
            // Viewing own profile, change button to create
            // post button, create post button dialog
            // Create post dialog pop up
            final Dialog postDialog = new Dialog(ProfileActivity.this);

            // Set dialog text
            postDialog.setContentView(R.layout.dialog_post);
            postDialog.setTitle("Submit New Post");

            // Find button, text
            Button post_submit = (Button) postDialog.findViewById(R.id.dialogPostButton);
            final EditText post_text = (EditText) postDialog.findViewById(R.id.dialogPostBody);

            post_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   final HashMap<String,String>  params = new HashMap<>();
                    params.put("content",   post_text.getText().toString());

                    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + "/microposts/"+ Integer.toString(id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.get("success").toString().equals("true")) {
                                    // Display Successful message
                                    Toast toast = Toast.makeText(getApplicationContext(), "Post Successful", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();

                                    postAdapter.add(params.get("content"));
                                    postAdapter.notifyDataSetChanged();
                                } else {
                                    // Failure to Post Message
                                    Toast toast = Toast.makeText(getApplicationContext(), "Error posting, try again later", Toast.LENGTH_SHORT);
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
                    Singleton.getInstance(ProfileActivity.this).addToRequestQueue(postRequest);

                    postDialog.dismiss();
                }
            });

            // Create dialog
            postDialog.show();
        }
    }

    public void onProfileMessagePress(View v){
        // Create post dialog pop up
        final Dialog messageDialog = new Dialog(ProfileActivity.this);

        // Set dialog text
        messageDialog.setContentView(R.layout.dialog_message);
        messageDialog.setTitle("Send New Message");

        // Find button, text
        Button message_submit = (Button) messageDialog.findViewById(R.id.dMessageSendButton);
        final EditText message_text = (EditText) messageDialog.findViewById(R.id.dMessageBody);
        final EditText recipient_text = (EditText) messageDialog.findViewById(R.id.dMessageRecipient);
        recipient_text.setText(username.getText());

        message_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String>  params = new HashMap<>();
                params.put("body",   message_text.getText().toString());
                params.put("recipient", recipient_text.getText().toString());

                JsonObjectRequest messageRequest = new JsonObjectRequest(Request.Method.POST, url + "/private_messages/"+ Integer.toString(id) + ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("success").toString().equals("true")) {
                                // Display Successful message
                                Toast toast = Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                // Failure to Register User
                                Toast toast = Toast.makeText(getApplicationContext(), "Error Sending Message.", Toast.LENGTH_LONG);
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
                Singleton.getInstance(ProfileActivity.this).addToRequestQueue(messageRequest);

                messageDialog.dismiss();
            }
        });

        // Create dialog
        messageDialog.show();
    }


}
