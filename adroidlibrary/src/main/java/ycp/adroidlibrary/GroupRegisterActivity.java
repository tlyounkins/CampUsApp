package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GroupRegisterActivity extends AppCompatActivity {
    String url = "http://192.168.172.255:3000";
    EditText name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_register);

        name = (EditText) findViewById(R.id.gregisterName);
        description = (EditText) findViewById(R.id.gregisterDescription);
    }

    public void onGroupRegisterPress(View v){

        // Place new group information into hash to be sent
        HashMap<String, String> params = new HashMap<>();
        params.put("groupname", name.getText().toString());
        params.put("description", description.getText().toString());

        // Send Parameters to Server
        // Send JSON request to server to add to database
        JsonObjectRequest groupRequest = new JsonObjectRequest(Request.Method.POST, url + "/groups.json", new JSONObject(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.get("success").toString().equals("true")) {
                        // Display Successful message
                        Toast toast = Toast.makeText(getApplicationContext(), "Successful Registration.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        Notify.notify(GroupRegisterActivity.this, "Group" + name.getText().toString() + " created.", 1);

                        // Bring user back to Dashboard
                        Intent intent = new Intent(GroupRegisterActivity.this, Dashboard.class);
                        startActivity(intent);
                        finish();
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
