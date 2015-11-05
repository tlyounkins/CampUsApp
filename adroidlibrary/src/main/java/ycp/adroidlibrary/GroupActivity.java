package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class GroupActivity extends AppCompatActivity {
    String url = "http://192.168.172.48:3000";
    TextView groupName, groupDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupName = (TextView) findViewById(R.id.groupGroupName1);
        groupDescription = (TextView) findViewById(R.id.groupGroupDescription1);

        // Send JsonRequest to get fields
        JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url+"/groups/"+Integer.toString(1)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    groupName.setText(response.get("groupName").toString());
                    groupDescription.setText(response.get("description").toString());
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

    public void onGroupCreatePress(View v){
        Intent intent = new Intent(GroupActivity.this, GroupRegisterActivity.class);
        startActivity(intent);
    }
}
