package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
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

public class ProfileActivity extends AppCompatActivity {
    String url = "http://192.168.172.59:3000";
    int id = 0;
    int count = 0;

    //user text views
    TextView username, firstname, lastname, age, gender, major, hometown, bio;

    // Posts
    List<String> posts = new ArrayList<>();
    ArrayAdapter<String> postAdapter;
    ListView postList;

    //profile text views
    TextView profileCreateText;
    EditText profileCreatePostText;

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


        profileCreateText = (TextView) findViewById(R.id.profileCreateText);
        profileCreatePostText = (EditText) findViewById(R.id.profileCreatePostText);

        // Post List
        postList = (ListView) findViewById(R.id.profilePostList);
        postAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, posts);
        postList.setAdapter(postAdapter);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        //send JSON request for 3 posts
        getPostList();

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
        intent.putExtra("id", id);
        intent.putExtra("username", username.getText());
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

    public void getPostList(){
        // Get Array of Posts
        JsonArrayRequest postsRequest = new JsonArrayRequest(url+"/microposts/"+Integer.toString(id)+"/posts.json",new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){

                    // Set the Text Fields to Acquired Information
                   for (int i = 0; i < response.length(); i++){
                       try{
                           JSONObject obj = response.getJSONObject(i);
                           posts.add(obj.get("content").toString());
                       } catch(JSONException e){
                           e.printStackTrace();
                       }
                   }
                postAdapter.notifyDataSetChanged();
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

    public void onSubmitRequest(View v){

        if(id != 0){
            HashMap<String,String>  params = new HashMap<>();
            params.put("content",profileCreatePostText.getText().toString());

            JsonObjectRequest groupRequest = new JsonObjectRequest(Request.Method.POST, url + "/microposts/"+ Integer.toString(id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.get("success").toString().equals("true")) {
                            // Display Successful message
                            Toast toast = Toast.makeText(getApplicationContext(), "Post post: Post posted", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            Notify.notify(ProfileActivity.this, username.getText().toString() + " created a post", 1);

                            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                            intent.putExtra("id", id);
                            startActivity(intent);
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
            Singleton.getInstance(this).addToRequestQueue(groupRequest);


        }
    }

}
