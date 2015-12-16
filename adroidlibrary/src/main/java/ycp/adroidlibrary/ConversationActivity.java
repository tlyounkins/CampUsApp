package ycp.adroidlibrary;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class ConversationActivity extends AppCompatActivity {

    int id;
    String username, sender_username, school;
    String url = "http://campus-app.herokuapp.com";
    //String url = "http://192.168.172.105:3000";
    // Messages
    List<String> messages = new ArrayList<>();
    ArrayAdapter messageAdapter;
    ListView messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        //creates message header
        TextView messageHeader = new TextView(ConversationActivity.this);


        messageList = (ListView) findViewById(R.id.conversationList);
        messageAdapter= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messages);
        messageList.setAdapter(messageAdapter);


        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
            username = extras.getString("username");
            sender_username = extras.getString("sender_username");
            school = extras.getString("school");
        }

        messageHeader.setText("Conversation with " + sender_username);
        messageList.addHeaderView(messageHeader);

        getMessages();
    }

    public void getMessages(){

        // Get list of all messages
        JsonArrayRequest messageRequest = new JsonArrayRequest(url+"/private_messages/"+Integer.toString(id)+"/"+sender_username+".json", new Response.Listener<JSONArray>(){
            @Override
            public void onResponse(JSONArray response){
                // Set the Text Fields to Acquired Information
                for (int i = 0; i < response.length(); i++){
                    try{
                        JSONObject obj = response.getJSONObject(i);
                        messages.add(obj.get("body").toString());
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
        Singleton.getInstance(this).addToRequestQueue(messageRequest);
    }

    public void onReplyPress(View v){
        // Create post dialog pop up
        final Dialog messageDialog = new Dialog(ConversationActivity.this);

        // Set dialog text
        messageDialog.setContentView(R.layout.dialog_message);
        messageDialog.setTitle("Send Reply");

        // Find button, text
        Button message_submit = (Button) messageDialog.findViewById(R.id.dMessageSendButton);
        final EditText message_text = (EditText) messageDialog.findViewById(R.id.dMessageBody);
        final EditText recipient_text = (EditText) messageDialog.findViewById(R.id.dMessageRecipient);

        recipient_text.setText(sender_username);

        message_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            final HashMap<String,String>  params = new HashMap<>();
                params.put("body",   message_text.getText().toString());
                params.put("recipient", recipient_text.getText().toString());

                JsonObjectRequest messageRequest = new JsonObjectRequest(Request.Method.POST, url + "/private_messages/"+ Integer.toString(id)+ ".json", new JSONObject(params), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.get("success").toString().equals("true")) {
                                // Display Successful message
                                Toast toast = Toast.makeText(getApplicationContext(), "Message Sent", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                messageAdapter.add(params.get("body") );
                                messageAdapter.notifyDataSetChanged();
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
                Singleton.getInstance(ConversationActivity.this).addToRequestQueue(messageRequest);

                messageDialog.dismiss();
            }
        });

        // Create dialog
        messageDialog.show();
    }

}
