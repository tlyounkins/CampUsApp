package ycp.adroidlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConversationActivity extends AppCompatActivity {

    int id;
    String username, sender_username;
    //String url = "http://campus-app.herokuapp.com";
    String url = "http://192.168.172.72:3000";

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
                        messages.add(response.getString(i));
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

}
