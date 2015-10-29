package ycp.adroidlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import Model.User;

public class EditActivity extends AppCompatActivity {
    String url = "http://192.168.172.185:3000";
    int id;
    EditText hometown, major, bio, gender, firstname, lastname;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize Texts
        hometown = (EditText) findViewById(R.id.editHome);
        major = (EditText) findViewById(R.id.editMajor);
        bio = (EditText) findViewById(R.id.editBio);
        gender = (EditText) findViewById(R.id.editGender);
        firstname = (EditText) findViewById(R.id.editFirst);
        lastname = (EditText) findViewById(R.id.editLast);
        username = (TextView) findViewById(R.id.editUserText);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            username.setText(extras.getString("username"));
        }

        // Send JsonRequest to get fields
        JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url+"/users/"+Integer.toString(id)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    hometown.setText(response.getString("hometown"));
                    major.setText(response.getString("major"));
                    bio.setText(response.getString("bio"));
                    gender.setText(response.getString("gender"));
                    firstname.setText(response.getString("firstname"));
                    lastname.setText(response.getString("lastname"));
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
    }

    public void onConfirmPress(View v){
        // Place Entries into a Hash to be sent to server
        HashMap<String, String> params = new HashMap<>();
        params.put("hometown", hometown.getText().toString());
        params.put("major", major.getText().toString());
        params.put("bio", bio.getText().toString());
        params.put("gender", gender.getText().toString());
        params.put("firstname", firstname.getText().toString());
        params.put("lasname", lastname.getText().toString());

        // Send changes to server
        JsonObjectRequest changesRequest = new JsonObjectRequest(Request.Method.PATCH, url+"/users/"+Integer.toString(id)+".json", new JSONObject(params), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try{
                    if(response.get("success").toString().equals("true")){
                        // Profile updated successfully, show success message
                        Toast toast = Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        // Close activity
                        finish();
                    } else {
                        // Profile updated failed, show failure message
                        Toast toast = Toast.makeText(getApplicationContext(), "Profile updated failed, try again", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                } catch (JSONException e){
                    // Print JSONError
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e){
                // Print the Volley Error
                VolleyLog.e("Error: " + e.getMessage());
            }
        });
    }
}
