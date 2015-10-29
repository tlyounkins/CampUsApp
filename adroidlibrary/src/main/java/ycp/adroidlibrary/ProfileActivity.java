package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
        }
    }

    public void onDashboardPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this,Dashboard.class);
        startActivity(intent);
    }

    public void onCalenderPressProfile(View v){
        Intent intent = new Intent(ProfileActivity.this,calendarActivity.class);
        startActivity(intent);
    }
    public void onProfileEditPress(View v){
        Intent intent = new Intent(ProfileActivity.this, EditActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

}
