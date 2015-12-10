package ycp.adroidlibrary;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupActivity extends AppCompatActivity {
    String url = "http://campus-app.herokuapp.com";
    //String url = "http://192.168.172.105:3000";

    int user_id;
    String username, school;

    // Groups
    List<Map<String, String>>groups = new ArrayList<>();
    SimpleAdapter groupAdapter;
    ListView groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //changing title of Activity
        setTitle("Group List");

        setContentView(R.layout.activity_group);

        TextView groupHeader = new TextView(GroupActivity.this);
        groupHeader.setText("Available groups");

        groupList = (ListView) findViewById(R.id.groupList);
        groupAdapter= new SimpleAdapter(this, groups, android.R.layout.simple_list_item_2,
                new String[] {"Name", "Description"},
                new int[] {android.R.id.text1, android.R.id.text2});
        groupList.setAdapter(groupAdapter);
        groupList.addHeaderView(groupHeader);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("id");
            username = extras.getString("username");
            school = extras.getString("school");
        }

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (user_id != 0) {
                    // Get ID from server
                    Map<String, String> obj =  groups.get(position-1);
                    obj.put("Name", obj.get("Name").replaceAll(" ","_"));
                    JsonObjectRequest idRequest = new JsonObjectRequest(Request.Method.GET, url + "/groups/findId/" + obj.get("Name") + ".json", null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                int group_id = Integer.parseInt(response.get("id").toString());
                                // Get group id at position, start profile activity
                                Intent intent = new Intent(GroupActivity.this, GroupProfileActivity.class);
                                intent.putExtra("id", user_id);
                                intent.putExtra("school", school);
                                intent.putExtra("username", username);
                                intent.putExtra("group_id", group_id);
                                startActivity(intent);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            VolleyLog.e("Error: " + e.getMessage());
                        }
                    });

                    Singleton.getInstance(getApplicationContext()).addToRequestQueue(idRequest);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "You must log in to view a group profile", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        getGroupList();
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
            Intent intent = new Intent(GroupActivity.this, GroupActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(GroupActivity.this, FriendActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(GroupActivity.this, EditActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_post){
            // Check if user is logged in
            if(user_id != 0) {
                // Create post dialog pop up
                final Dialog postDialog = new Dialog(GroupActivity.this);

                // Set dialog text
                postDialog.setContentView(R.layout.dialog_post);
                postDialog.setTitle("Submit New Post");

                // Find button, text
                Button post_submit = (Button) postDialog.findViewById(R.id.dialogPostButton);
                final EditText post_text = (EditText) postDialog.findViewById(R.id.dialogPostBody);

                post_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,String>  params = new HashMap<>();
                        params.put("content",   post_text.getText().toString());

                        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url + "/microposts/"+ Integer.toString(user_id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
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
                        Singleton.getInstance(GroupActivity.this).addToRequestQueue(postRequest);

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

    public void onGroupCreatePress(View v){
        if(user_id != 0) {
            Intent intent = new Intent(GroupActivity.this, GroupRegisterActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            intent.putExtra("school", school);
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
                        if(school.equals(obj.get("school").toString())) {
                            datum.put("Name", obj.get("groupname").toString());
                            datum.put("Description", obj.get("description").toString());
                            groups.add(datum);
                        }
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
