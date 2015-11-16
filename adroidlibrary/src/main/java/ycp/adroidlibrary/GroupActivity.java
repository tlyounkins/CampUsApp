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
    String url = "http://192.168.172.59:3000";
    TextView groupName1, groupName2, groupName3, groupName4, groupName5;
    TextView groupDescription1, groupDescription2, groupDescription3, groupDescription4, groupDescription5;
    int id, count;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // For future use
        // Count number of pages
        count = 0;

        groupName1 = (TextView) findViewById(R.id.groupGroupName1);
        groupDescription1 = (TextView) findViewById(R.id.groupGroupDescription1);

        groupName2 = (TextView) findViewById(R.id.groupGroupName2);
        groupDescription2 = (TextView) findViewById(R.id.groupGroupDescription2);

        groupName3 = (TextView) findViewById(R.id.groupGroupName3);
        groupDescription3 = (TextView) findViewById(R.id.groupGroupDescription3);

        groupName4 = (TextView) findViewById(R.id.groupGroupName4);
        groupDescription4 = (TextView) findViewById(R.id.groupGroupDescription4);

        groupName5 = (TextView) findViewById(R.id.groupGroupName5);
        groupDescription5 = (TextView) findViewById(R.id.groupGroupDescription5);

        getGroupList();

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            username = extras.getString("username");
        }
    }

    public void onGroupCreatePress(View v){
        Intent intent = new Intent(GroupActivity.this, GroupRegisterActivity.class);
        startActivity(intent);
    }

    public void onGroup1Press(View v){
        // Start Group Activity
        Intent intent = new Intent(GroupActivity.this, GroupProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        intent.putExtra("group_id", 1);
        startActivity(intent);
    }

    public void onGroup2Press(View v){
        // Start Group Activity
        Intent intent = new Intent(GroupActivity.this, GroupProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        intent.putExtra("group_id", 2 + count);
        startActivity(intent);
    }

    public void onGroup3Press(View v){
        // Start Group Activity
        Intent intent = new Intent(GroupActivity.this, GroupProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        intent.putExtra("group_id", 3 + count);
        startActivity(intent);
    }

    public void onGroup4Press(View v){
        // Start Group Activity
        Intent intent = new Intent(GroupActivity.this, GroupProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("group_id", 4 + count);
        startActivity(intent);
    }

    public void onGroup5Press(View v){
        // Start Group Activity
        Intent intent = new Intent(GroupActivity.this, GroupProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        intent.putExtra("group_id", 5 + count);
        startActivity(intent);
    }

    private void getGroupList(){
        // Send JsonRequest to get 5 groups
        // First group
        JsonObjectRequest fieldsRequest1 = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(1 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName1.setText(response.get("groupName").toString());
                    groupDescription1.setText(response.get("description").toString());
                } catch (JSONException e){
                    // There was an error, print it
                    e.printStackTrace();
                    // Do not display text if error is thrown
                    groupName1.setText("");
                    groupDescription1.setText("");
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

        // Second group
        JsonObjectRequest fieldsRequest2 = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(2 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName2.setText(response.get("groupName").toString());
                    groupDescription2.setText(response.get("description").toString());
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
                groupName2.setText("");
                groupDescription2.setText("");
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest2);

        // Third group
        JsonObjectRequest fieldsRequest3 = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(3 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName3.setText(response.get("groupName").toString());
                    groupDescription3.setText(response.get("description").toString());
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
                groupName3.setText("");
                groupDescription3.setText("");
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest3);

        // Fourth group
        JsonObjectRequest fieldsRequest4 = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(4 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName4.setText(response.get("groupName").toString());
                    groupDescription4.setText(response.get("description").toString());
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
                groupName4.setText("");
                groupDescription4.setText("");
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest4);

        // Fifth group
        JsonObjectRequest fieldsRequest5 = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(5 + count)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName5.setText(response.get("groupName").toString());
                    groupDescription5.setText(response.get("description").toString());
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
                groupName5.setText("");
                groupDescription5.setText("");
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(fieldsRequest5);
    }

}
