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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    String url = "http://192.168.172.59:3000";

    int user_id;
    String username;

    // Posts
    List<Map<String, String>>groups = new ArrayList<>();
    SimpleAdapter groupAdapter;
    ListView groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        groupList = (ListView) findViewById(R.id.groupList);
        groupAdapter= new SimpleAdapter(this, groups, android.R.layout.simple_list_item_2,
                new String[] {"Name", "Description"},
                new int[] {android.R.id.text1, android.R.id.text2});
        groupList.setAdapter(groupAdapter);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("id");
            username = extras.getString("username");
        }

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(user_id != 0) {
                    // Get group id at position, start profile activity
                    Intent intent = new Intent(GroupActivity.this, GroupProfileActivity.class);
                    intent.putExtra("id", user_id);
                    intent.putExtra("group_id", position + 1);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "You must log in to view a group profile", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        getGroupList();
    }

    public void onGroupCreatePress(View v){
        if(user_id != 0) {
            Intent intent = new Intent(GroupActivity.this, GroupRegisterActivity.class);
            startActivity(intent);
        } else{
            Toast toast = Toast.makeText(getApplicationContext(), "You must log in to create a group", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    private void getGroupList(){
        // Get list of all groups
        JsonArrayRequest fieldsRequest1 = new JsonArrayRequest(url+"/groups/getAll.json", new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                // Set the Text Fields to Acquired Information
                for (int i = 0; i < response.length(); i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        Map<String, String> datum = new HashMap<>(2);
                        datum.put("Name", obj.get("groupname").toString());
                        datum.put("Description", obj.get("description").toString());
                        groups.add(datum);
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                groupAdapter.notifyDataSetChanged();
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

}
