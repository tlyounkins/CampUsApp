package ycp.adroidlibrary;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.Message;

public class MessageActivity extends AppCompatActivity {
    int user_id = 0;
    String username;
    //String url = "http://campus-app.herokuapp.com";
    String url = "http://192.168.172.72:3000";


    // Senders
    List<String> senders = new ArrayList<>();
    ArrayAdapter messageAdapter;
    ListView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle("Messages");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //creates message header
        TextView messageHeader = new TextView(MessageActivity.this);
        messageHeader.setText("Messages");

        //creates message list and adds them to the list
        messageList = (ListView) findViewById(R.id.messageList);
        messageAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, senders);
        messageList.setAdapter(messageAdapter);
        messageList.addHeaderView(messageHeader);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user_id = extras.getInt("id");
            username = extras.getString("username");
        }

        //function call
        getMessageList();

        messageList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get group id at position, start profile activity
                Intent intent = new Intent(MessageActivity.this, ConversationActivity.class);
                intent.putExtra("id", user_id);
                intent.putExtra("username", username);
                intent.putExtra("sender_username", messageList.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
    }

    //Function that goes to the Dashboard Activity
    public void onDashboardPress(View v){
        // Change view to Profile
        Intent intent = new Intent(MessageActivity.this, Dashboard.class);
        intent.putExtra("id", user_id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    //Function that goes to the Profile Activity
    public void onProfilePress(View v){
            // Change view to Profile
            Intent intent = new Intent(MessageActivity.this, ProfileActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            startActivity(intent);
    }

    //Function that goes to the Group/Friend/Edit Activity or Posts a status update
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int item_id = item.getItemId();

        if (item_id == R.id.action_Clubs){
            // Start Group Activity
            Intent intent = new Intent(MessageActivity.this, GroupActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(MessageActivity.this, FriendActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(MessageActivity.this, EditActivity.class);
            intent.putExtra("id", user_id);
            intent.putExtra("username", username);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_post){
            // Check if user is logged in
            if(user_id != 0) {
                // Create post dialog pop up
                final Dialog postDialog = new Dialog(MessageActivity.this);

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
                        Singleton.getInstance(MessageActivity.this).addToRequestQueue(postRequest);

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

    //Gets a message thread
    private void getMessageList(){
        // Get list of all groups
        JsonArrayRequest fieldsRequest1 = new JsonArrayRequest(url+"/private_messages/"+Integer.toString(user_id)+"/senders.json", new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                // Set the Text Fields to Acquired Information
                for (int i = 0; i < response.length(); i++){
                    try{
                        senders.add(response.getString(i));
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                messageAdapter.notifyDataSetChanged();
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

    //Function that handles a message being sent
    public void onSendMessagePress(View v){
        // Create post dialog pop up
        final Dialog messageDialog = new Dialog(MessageActivity.this);

        // Set dialog text
        messageDialog.setContentView(R.layout.dialog_message);
        messageDialog.setTitle("Send New Message");

        // Find button, text
        Button message_submit = (Button) messageDialog.findViewById(R.id.dMessageSendButton);
        final EditText message_text = (EditText) messageDialog.findViewById(R.id.dMessageBody);
        final EditText recipient_text = (EditText) messageDialog.findViewById(R.id.dMessageRecipient);

        message_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,String>  params = new HashMap<>();
                params.put("body",   message_text.getText().toString());
                params.put("recipient", recipient_text.getText().toString());

                JsonObjectRequest messageRequest = new JsonObjectRequest(Request.Method.POST, url + "/private_messages/"+ Integer.toString(user_id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("success").toString().equals("true")) {
                                // Display Successful message
                                Toast toast = Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            } else {
                                // Failure to Register User
                                Toast toast = Toast.makeText(getApplicationContext(), "Error Sending Message.", Toast.LENGTH_LONG);
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
                Singleton.getInstance(MessageActivity.this).addToRequestQueue(messageRequest);

                messageDialog.dismiss();
            }
        });

        // Create dialog
        messageDialog.show();
    }

}
