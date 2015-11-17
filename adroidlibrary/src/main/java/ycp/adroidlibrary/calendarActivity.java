package ycp.adroidlibrary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class calendarActivity extends AppCompatActivity {
    int id = 0;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        // Check for extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            username = extras.getString("username");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calendar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onDashboardPress(View v){
        // Change view to Dashboard
        Intent intent = new Intent(calendarActivity.this, Dashboard.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onProfilePressCalendar(View v){
        // Change view to Profile
        Intent intent = new Intent(calendarActivity.this, ProfileActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
