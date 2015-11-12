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

    TextView username;
    TextView firstname;
    TextView lastname;
    TextView age;
    TextView gender;
    TextView major;
    TextView hometown;
    TextView bio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = (TextView) findViewById(R.id.profileUsername);
        firstname = (TextView) findViewById(R.id.profileFirstName);
        lastname = (TextView) findViewById(R.id.profileLastName);
        age = (TextView) findViewById(R.id.profileAge);
        gender = (TextView) findViewById(R.id.profileGender);
        major = (TextView) findViewById(R.id.profileMajor);
        hometown = (TextView) findViewById(R.id.profileHometown);
        bio = (TextView) findViewById(R.id.profileBio);

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

}
