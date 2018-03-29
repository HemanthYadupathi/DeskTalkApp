package com.desktalk.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.adapter.Leave_Adapter;
import com.desktalk.fragment.AcademicsFragment;
import com.desktalk.fragment.AttendanceFragment;
import com.desktalk.fragment.AttendanceHistoryFragment;
import com.desktalk.fragment.AttendanceMainFragment;
import com.desktalk.fragment.HomeFragment;
import com.desktalk.fragment.LeaveFragment;
import com.desktalk.fragment.MapFragment;
import com.desktalk.fragment.ProfileFragment;
import com.desktalk.util.Connectivity;
import com.desktalk.util.Constants;
import com.desktalk.util.PublicMethods;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        AcademicsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener, AttendanceMainFragment.OnFragmentInteractionListener, AttendanceFragment.OnFragmentInteractionListener, AttendanceHistoryFragment.OnListFragmentInteractionListener, LeaveFragment.OnLeaveFragmentBackPress {

    int int_value = 0;
    int selected_ID = 0;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    private final String TAG = DashboardActivity.class.getSimpleName();
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE); //1
        editor = sharedpreferences.edit();
        token = sharedpreferences.getString(Constants.PREFERENCE_KEY_TOKEN, "");

        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = null;
            fragmentClass = HomeFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, fragment).commit();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        View headerview = navigationView.getHeaderView(0);

        TextView nav_user = (TextView) headerview.findViewById(R.id.text_name);
        sharedpreferences = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_LOGIN_DETAILS, Context.MODE_PRIVATE);
        String userData = sharedpreferences.getString(Constants.PREFERENCE_KEY_USERDATA, "");
        if (!userData.contentEquals("") || userData != null) {
            try {
                JsonObject userDataObject = new JsonParser().parse(userData).getAsJsonObject();
                nav_user.setText(userDataObject.get("fname").getAsString() + " " + userDataObject.get("lname").getAsString());

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        } else {
            Log.e(TAG, "userData is null");
        }
        /*TextView profilename = (TextView) headerview.findViewById(R.id.textViewEdit);

        profilename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawers();
                Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });*/
        navigationView.setCheckedItem(R.id.nav_home);
        if (Constants.USER_ID == Constants.USER_TEACHER) {
            selected_ID = 0;
            addTeacherMenuInNavMenuDrawer();
        } else if (Constants.USER_ID == Constants.USER_PARENT) {
            selected_ID = 1;
            addParentMenuInNavMenuDrawer();
            if (!token.contentEquals("") || token != null) {
                if (Connectivity.isConnected(getApplicationContext())) {
                    PublicMethods.getStudentList(DashboardActivity.this, TAG, token);
                } else {
                    Log.d(TAG, getString(R.string.check_connection));
                }
            } else {
                Log.d(TAG, "Token null");
            }
        } else if (Constants.USER_ID == Constants.USER_STUDENT) {
            selected_ID = 2;
            addStudentMenuInNavMenuDrawer();
        }

    }

    public void setToolbar(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        setActionBarToggle(toolbar);
        toolbar.setTitle(title);
    }

    public void setActionBarToggle(Toolbar toolbar) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void addTeacherMenuInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ATTENDENCE, 0, "Attendance").setIcon(getDrawable(R.mipmap.ic_attend)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Manage Timetable").setIcon(getDrawable(R.mipmap.ic_timetable)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_EXAM, 0, "Examination").setIcon(getDrawable(R.mipmap.ic_edit)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "Assignments").setIcon(getDrawable(R.mipmap.ic_assignment)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_STD_PERFORMANCE, 0, "Student Performance").setIcon(getDrawable(R.mipmap.ic_performance)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_EVENTS, 0, "Create Event").setIcon(getDrawable(R.mipmap.ic_event)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Approve Leave").setIcon(getDrawable(R.mipmap.ic_approveleave)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_suggestion)).setCheckable(true);


        navView.invalidate();
    }

    private void addParentMenuInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_BUS_TRACKING, 0, "Bus Tracking").setIcon(getDrawable(R.mipmap.ic_bus)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Timetable").setIcon(getDrawable(R.mipmap.ic_timetable)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_NEWS, 0, "View Newsletter").setIcon(getDrawable(R.mipmap.ic_news)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "View Assignments").setIcon(getDrawable(R.mipmap.ic_assignment)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Leave Application").setIcon(getDrawable(R.mipmap.ic_leave)).setCheckable(true);
        //menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_suggestion)).setCheckable(true);


        navView.invalidate();
    }


    private void addStudentMenuInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Timetable").setIcon(getDrawable(R.mipmap.ic_timetable)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_NEWS, 0, "View Newsletter").setIcon(getDrawable(R.mipmap.ic_news)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "View Assignments").setIcon(getDrawable(R.mipmap.ic_assignment)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Leave Status").setIcon(getDrawable(R.mipmap.ic_leave)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_suggestion)).setCheckable(true);


        navView.invalidate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (checkNavigationMenuItem() != 0) {
                navigationView.setCheckedItem(R.id.nav_home);
                //List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
                //if (fragmentList.get(0).getTargetFragment() instanceof HomeFragment) {
                HomeFragment fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();
                //}
            } else
                super.onBackPressed();
        }

        OnLeaveFragmentBackPress();
    }


    private int checkNavigationMenuItem() {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).isChecked())
                return i;
        }
        return -1;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            if (!token.contentEquals("") || token != null) {
                if (Connectivity.isConnected(getApplicationContext())) {
                    Constants.logout(DashboardActivity.this, token);
                } else {
                    Log.d(TAG, getString(R.string.check_connection));
                }
            } else {
                Log.d(TAG, "Token null");
            }
            Constants.clearSharedPreferenceData(sharedpreferences, TAG);
            finish();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_home) {
            fragmentClass = HomeFragment.class;
        } else if (id == R.id.nav_profile) {
            fragmentClass = ProfileFragment.class;

        } else if (id == R.id.nav_academics) {
            fragmentClass = AcademicsFragment.class;
        } else if (id == Constants.NAV_MENU_ITEM_ATTENDENCE) {
            fragmentClass = AttendanceMainFragment.class;
        } else if (id == Constants.NAV_MENU_ITEM_STD_PERFORMANCE) {
            //startActivity(new Intent(DashboardActivity.this, BusTrackMapActivity.class));
        } else if (id == Constants.NAV_MENU_ITEM_BUS_TRACKING) {
            //TODO: Implement
            fragmentClass = MapFragment.class;
            //startActivity(new Intent(DashboardActivity.this, BusTrackMapActivity.class));
        } else if (id == Constants.NAV_MENU_ITEM_LEAVES) {
            fragmentClass = LeaveFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Bundle arguments = new Bundle();
            arguments.putInt("selected_ID", selected_ID);
            fragment.setArguments(arguments);
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, fragment).addToBackStack(null).commit();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void OnLeaveFragmentBackPress() {
        if (LeaveFragment.pendingLeavesList != null) {
            Log.d(TAG, "OnLeaveFragmentBackPress clear arraylist");
            LeaveFragment.pendingLeavesList.clear();
            LeaveFragment.rejectList.clear();
            LeaveFragment.approvedList.clear();
        }
    }
}
