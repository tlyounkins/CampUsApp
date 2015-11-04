package ycp.adroidlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
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


public class Dashboard extends AppCompatActivity {
    int id = 0;
    String username = "";
    Button loginButton;
    TextView welcomeText;
    String url = "http://192.168.172.48:3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        loginButton = (Button) findViewById(R.id.tempLogin);
        welcomeText = (TextView) findViewById(R.id.welcomeText);

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCalendarPress(View v){
        // Change view to Calendar
        Intent intent = new Intent(Dashboard.this, calendarActivity.class);
        startActivity(intent);
    }

    public void onProfilePress(View v){
        // Change view to Profile
        Intent intent = new Intent(Dashboard.this, ProfileActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void onDashLoginPress(View v){
        // No User is logged in, take them to log in screen
        if(id == 0) {
            Intent intent = new Intent(Dashboard.this, LoginActivity.class);
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
