package ycp.adroidlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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

import Model.User;

public class EditActivity extends AppCompatActivity {
    String url = "http://192.168.173.11:3000";
    int id;
    String logged_in_user;
    EditText hometown, major, bio, gender, firstname, lastname;
    View editView, progressView;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //changing title of Activity
        setTitle("Profile Editor");

        setContentView(R.layout.activity_edit);

        // Initialize Texts
        hometown = (EditText) findViewById(R.id.editHome);
        major = (EditText) findViewById(R.id.editMajor);
        bio = (EditText) findViewById(R.id.editBio);
        gender = (EditText) findViewById(R.id.editGender);
        firstname = (EditText) findViewById(R.id.editFirst);
        lastname = (EditText) findViewById(R.id.editLast);
        username = (TextView) findViewById(R.id.editUserText);
        editView = findViewById(R.id.editUserForm);
        progressView = findViewById(R.id.loadUserProgress);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            logged_in_user = extras.getString("username");
        }

        // Show a progress spinner
        showProgress(true);

        // Send JsonRequest to get fields
        JsonObjectRequest fieldsRequest = new JsonObjectRequest(Request.Method.GET, url+"/users/"+Integer.toString(id)+".json", null, new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    // Set the Text Fields to Acquired Information
                    username.setText(response.get("username").toString());
                    hometown.setText(response.get("hometown").toString());
                    major.setText(response.get("major").toString());
                    bio.setText(response.get("bio").toString());
                    gender.setText(response.get("gender").toString());
                    firstname.setText(response.get("firstname").toString());
                    lastname.setText(response.get("lastname").toString());
                    showProgress(false);
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
            Intent intent = new Intent(EditActivity.this, GroupActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", logged_in_user);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_search){
            //start list of all users
            Intent intent = new Intent(EditActivity.this, FriendActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", logged_in_user);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_Account){
            // Start edit activity
            Intent intent = new Intent(EditActivity.this, EditActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("username", logged_in_user);
            startActivity(intent);
            return true;
        }

        if(item_id == R.id.action_post){
            // Check if user is logged in
            if(id != 0) {
                // Create post dialog pop up
                final Dialog postDialog = new Dialog(EditActivity.this);

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
                        Singleton.getInstance(EditActivity.this).addToRequestQueue(postRequest);

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

    public void onConfirmPress(View v){
        // Place Entries into a Hash to be sent to server
        HashMap<String, String> params = new HashMap<>();
        params.put("id", Integer.toString(id));
        params.put("hometown", hometown.getText().toString());
        params.put("major", major.getText().toString());
        params.put("bio", bio.getText().toString());
        params.put("gender", gender.getText().toString());
        params.put("firstname", firstname.getText().toString());
        params.put("lastname", lastname.getText().toString());

        showProgress(true);

        // Send changes to server
        JsonObjectRequest changesRequest = new JsonObjectRequest(Request.Method.PUT, url+"/users/"+Integer.toString(id)+".json", new JSONObject(params), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try{
                    if(response.get("success").toString().equals("true")){
                        // Profile updated successfully, show success message
                        Toast toast = Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        // Close activity
                        Intent intent = new Intent(EditActivity.this, ProfileActivity.class);
                        intent.putExtra("id", id);
                        intent.putExtra("username", logged_in_user);
                        startActivity(intent);
                        finish();
                    } else {
                        // Profile updated failed, show failure message
                        Toast toast = Toast.makeText(getApplicationContext(), "Profile updated failed, try again", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        showProgress(false);
                    }
                } catch (JSONException e){
                    // Print JSONError
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError e){
                // Print the Volley Error
                VolleyLog.e("Error: " + e.getMessage());
            }
        });

        // Add Request to Queue
        Singleton.getInstance(this).addToRequestQueue(changesRequest);
    }

    /**
     * Shows the progress UI and hides the edit form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            editView.setVisibility(show ? View.GONE : View.VISIBLE);
            editView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    editView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            progressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressView.setVisibility(show ? View.VISIBLE : View.GONE);
            editView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
