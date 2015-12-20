package tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.TextView;

import ycp.adroidlibrary.Dashboard;
import ycp.adroidlibrary.R;


// Created by Juan on 11/19/2015.

public class DashboardActivityTest extends ActivityInstrumentationTestCase2<Dashboard> {
    private Dashboard dashboard;
    private Button loginButton;
    private TextView welcomeText;
    private TextView postText;
    public DashboardActivityTest(){super(Dashboard.class);}

    @Override
    protected void setUp() throws Exception{
       super.setUp();

        dashboard = getActivity();
        loginButton = (Button) dashboard.findViewById(R.id.tempLogin);
        welcomeText = (TextView) dashboard.findViewById(R.id.welcomeText);
        postText = (TextView) dashboard.findViewById(R.id.dashPostText);
    }

    public void testPreconditions(){
        assertNotNull("main not null", dashboard);
    }
}
