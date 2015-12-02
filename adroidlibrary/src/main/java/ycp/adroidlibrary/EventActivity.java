package ycp.adroidlibrary;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.Map;

public class EventActivity extends AppCompatActivity {
    int id, group_id;
    String username;

    String url = "http://192.168.172.83:3000";

    // Events
    List<Map<String, String>> events = new ArrayList<>();
    SimpleAdapter eventAdapter;
    ListView eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //changing title of Activity
        setTitle("Events");

        setContentView(R.layout.activity_event);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            group_id = extras.getInt("group_id");
            username = extras.getString("username");
        }

        eventList = (ListView) findViewById(R.id.eEventList);
        eventAdapter= new SimpleAdapter(this, events, android.R.layout.simple_list_item_2,
                new String[] {"Name", "Description"},
                new int[] {android.R.id.text1, android.R.id.text2});
        eventList.setAdapter(eventAdapter);

        // Get group event list
        // Get list of all group events
        JsonArrayRequest fieldsRequest1 = new JsonArrayRequest(url+"/group_event/getGroup/" + Integer.toString(group_id)+ ".json", new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                // Set the Text Fields to Acquired Information
                for (int i = 0; i < response.length(); i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        Map<String, String> datum = new HashMap<>(2);
                        datum.put("Name", obj.get("name").toString());
                        datum.put("Description", obj.get("description").toString());
                        events.add(datum);
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                eventAdapter.notifyDataSetChanged();
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
    }


    public void onEventCreatePress(View v){
        // Create post dialog pop up
        final Dialog eventDialog = new Dialog(EventActivity.this);

        // Set dialog text
        eventDialog.setContentView(R.layout.dialog_event);
        eventDialog.setTitle("Create New Event");

        // Find button, text
        Button create_button = (Button) eventDialog.findViewById(R.id.eventCreateButton);
        final EditText event_name = (EditText) eventDialog.findViewById(R.id.eventName);
        final EditText event_description = (EditText) eventDialog.findViewById(R.id.eventDescription);
        final EditText event_date = (EditText) eventDialog.findViewById(R.id.eventDate);
        final EditText event_start = (EditText) eventDialog.findViewById(R.id.eventStart);
        final EditText event_end = (EditText) eventDialog.findViewById(R.id.eventEnd);

        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String>  params = new HashMap<>();
                params.put("name",   event_name.getText().toString());
                params.put("description", event_description.getText().toString());
                params.put("date", event_date.getText().toString());
                params.put("start", event_start.getText().toString());
                params.put("end", event_end.getText().toString());

                JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + "/group_event/"+ Integer.toString(group_id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("success").toString().equals("true")) {
                                // Display Successful message
                                Toast toast = Toast.makeText(getApplicationContext(), "Event Posted", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                // Failure to Register User
                                Toast toast = Toast.makeText(getApplicationContext(), "Error Creating Event", Toast.LENGTH_LONG);
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
                Singleton.getInstance(EventActivity.this).addToRequestQueue(postRequest);

                eventDialog.dismiss();
            }
        });

        // Create dialog
        eventDialog.show();
    }
}
