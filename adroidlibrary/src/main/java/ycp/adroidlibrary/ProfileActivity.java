package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {
    String url = "http://192.168.172.255:3000";
    int id = 0;
    int count = 0;

    //user text views
    TextView username, firstname, lastname, age, gender, major, hometown, bio;

    //profile text views
    TextView profilePost1, profilePost2, profilePost3;
    TextView profilePost1Description, profilePost2Description, profilePost3Description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //user posts and post descriptions
        profilePost1 = (TextView) findViewById(R.id.profilePost1);
        profilePost1Description = (TextView) findViewById(R.id.profilePost1Description);

        profilePost2 = (TextView) findViewById(R.id.profilePost2);
        profilePost2Description = (TextView) findViewById(R.id.profilePost2Description);

        profilePost3 = (TextView) findViewById(R.id.profilePost3);
        profilePost3Description = (TextView) findViewById(R.id.profilePost3Description);

        //send JSON request for 3 posts
        getPostList();

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        if (id != 0) {
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
                        gender.setText(response.get("gender").toString());
                        major.setText(response.get("major").toString());
                        hometown.setText(response.get("hometown").toString());
                        bio.setText(response.get("bio").toString());
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

    }

    public void onDashboardPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this,Dashboard.class);
        startActivity(intent);
    }

    public void onCalenderPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this,calendarActivity.class);
        startActivity(intent);
    }
    public void onProfileEditPress(View v){
        Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getPostList(){
        // Send JsonRequest to get 3 posts
        // First post
        //TODO change url "/profile/"
        JsonObjectRequest fieldsRequest1 = new JsonObjectRequest(Request.Method.GET, url+"/profile/"+Integer.toString(1 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    //TODO change "groupname"
                    //TODO change "description"
                    // Set the Text Fields to Acquired Information
                    profilePost1.setText(response.get("groupName").toString());
                    profilePost1Description.setText(response.get("description").toString());
                } catch (JSONException e){
                    // There was an error, print it
                    e.printStackTrace();
                    // Do not display text if error is thrown
                    profilePost1.setText("");
                    profilePost1Description.setText("");
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
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest1);

        // Second Post
        //TODO change url "/profile/"
        JsonObjectRequest fieldsRequest2 = new JsonObjectRequest(Request.Method.GET, url+"/profile/"+Integer.toString(2 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    //TODO change "groupname"
                    //TODO change "description"
                    profilePost2.setText(response.get("groupName").toString());
                    profilePost2Description.setText(response.get("description").toString());
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
                // Do not display text if error is thrown
                profilePost2.setText("");
                profilePost2Description.setText("");
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest2);

        // Third Post
        //TODO change url "/profile/"
        JsonObjectRequest fieldsRequest3 = new JsonObjectRequest(Request.Method.GET, url+"/profile/"+Integer.toString(3 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    //TODO change "groupname"
                    //TODO change "description"
                    profilePost3.setText(response.get("groupName").toString());
                    profilePost3Description.setText(response.get("description").toString());
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
                // Do not display text if error is thrown
                profilePost3.setText("");
                profilePost3Description.setText("");
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest3);
    }

}
