package ycp.adroidlibrary;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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

public class GroupActivity extends AppCompatActivity {
    String url = "http://192.168.172.234:3000";
    TextView groupName, groupDescription, groupMember;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupName = (TextView) findViewById(R.id.groupGroupName1);
        groupDescription = (TextView) findViewById(R.id.groupGroupDescription1);
        groupMember = (TextView) findViewById(R.id.groupMember1);

        // Send JsonRequest to get fields
        JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(1)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName.setText(response.get("groupName").toString());
                    groupDescription.setText(response.get("description").toString());
                    groupMember.setText(response.get("username").toString());
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

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
    }

    public void onGroupCreatePress(View v){
        Intent intent = new Intent(GroupActivity.this, GroupRegisterActivity.class);
        startActivity(intent);
    }

    public void onJoinPress(View v){
        if(id != 0){

            // Send JsonRequest to get fields
            JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(1)+"/join/" + Integer.toString(id)+".json", null, new Response.Listener<JSONObject>(){
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
            Singleton.getInstance(this).addToRequestQueue(fieldsRequest);
        } else{
            // Display Error Message
            Toast toast = Toast.makeText(getApplicationContext(), "You must be logged in to join a group", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
