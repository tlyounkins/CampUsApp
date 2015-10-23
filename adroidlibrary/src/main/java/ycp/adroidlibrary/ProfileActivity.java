package ycp.adroidlibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void dashPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void calendarPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this, calendarActivity.class);
        startActivity(intent);
        finish();
    }
}
