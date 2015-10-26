package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    String url = "http://192.168.172.90:3000";
    EditText pass;
    EditText email;
    EditText confirm;
    EditText username;
    EditText firstname;
    EditText lastname;
    EditText bio;
    EditText major;
    EditText hometown;
    EditText gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pass = (EditText) findViewById(R.id.registerPassword);
        confirm = (EditText) findViewById(R.id.registerConfirm);
        email = (EditText) findViewById(R.id.registerEmail);
        username = (EditText) findViewById(R.id.registerUserName);
        firstname = (EditText) findViewById(R.id.registerFirstName);
        lastname = (EditText) findViewById(R.id.registerLastName);
        bio = (EditText) findViewById(R.id.registerBio);
        major = (EditText) findViewById(R.id.registerMajor);
        hometown = (EditText) findViewById(R.id.registerHometown);
        gender = (EditText) findViewById(R.id.registerGender);


    }

    public void onRegisterClick(View v){
        // Place information entered by User into Hashmap
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email.getText().toString());
        params.put("password", pass.getText().toString());
        params.put("username", username.getText().toString());
        params.put("password_confirmation", confirm.getText().toString());
        params.put("firstname",firstname.getText().toString());
        params.put("lastname",lastname.getText().toString());
        params.put("bio",bio.getText().toString());
        params.put("major",major.getText().toString());
        params.put("hometown",hometown.getText().toString());
        params.put("gender",gender.getText().toString());

        // Send JSON request to server to add to database
        JsonObjectRequest passRequest = new JsonObjectRequest(url+"/users/new.json", new JSONObject(params), new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try{
                    if(response.get("success").toString().equals("true")){
                        // Display Successful message
                        Toast toast = Toast.makeText(getApplicationContext(), "Successful Registration.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        // Bring user back to Dashboard
                        Intent intent = new Intent(RegisterActivity.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                    } else{
                        // Failure to Register User
                        Toast toast = Toast.makeText(getApplicationContext(), "Error Registering.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        // Bring User back to Login
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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
