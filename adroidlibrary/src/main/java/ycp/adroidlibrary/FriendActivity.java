package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {
    String url = "http://192.168.172.59:3000";
    int user_id = 0;

    // Posts
    List<String> users = new ArrayList<>();
    ArrayAdapter<String> userAdapter;
    ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        // Post List
        userList = (ListView) findViewById(R.id.friendList);
        userAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, users);
        userList.setAdapter(userAdapter);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("id");
        }
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(user_id != 0) {
                    // Get group id at position, start profile activity
                    Intent intent = new Intent(FriendActivity.this, ProfileActivity.class);
                    intent.putExtra("id", user_id);
                    intent.putExtra("other_id", position + 1);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(), "You must log in to view a profile", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });

        getUserList();
    }
    public void getUserList(){
        // Get Array of Posts
        JsonArrayRequest postsRequest = new JsonArrayRequest(url+"/users/getAll.json",new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){

                // Set the Text Fields to Acquired Information
                for (int i = 0; i < response.length(); i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        users.add(obj.get("username").toString());
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e){
                // Error communicating with server, print it
                VolleyLog.e("Error: " + e.getMessage());
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(postsRequest);

    }

}
