package ycp.adroidlibrary;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;

public class calendarActivity extends AppCompatActivity {
    int id = 0;
    String username;
    //String url = "http://campus-app.herokuapp.com";
    String url = "http://192.168.172.72:3000";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            username = extras.getString("username");
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
            Intent intent = new Intent(calendarActivity.this, GroupActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(calendarActivity.this, FriendActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(calendarActivity.this, EditActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_post){
            // Check if user is logged in
            if(id != 0) {
                // Create post dialog pop up
                final Dialog postDialog = new Dialog(calendarActivity.this);

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
                        Singleton.getInstance(calendarActivity.this).addToRequestQueue(postRequest);

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

    public void onMessagePress(View v){
        // Change view to Calendar
        Intent intent = new Intent(calendarActivity.this, MessageActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onDashboardPress(View v){
        // Change view to Dashboard
        Intent intent = new Intent(calendarActivity.this, Dashboard.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onProfilePressCalendar(View v){
        // Change view to Profile
        Intent intent = new Intent(calendarActivity.this, ProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
