package com.desktalk.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.activity.desktalkapp.R;
import com.desktalk.fragment.AcademicsFragment;
import com.desktalk.fragment.HomeFragment;
import com.desktalk.fragment.ProfileFragment;
import com.desktalk.util.Constants;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        AcademicsFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

    int int_value = 0;
    int selected_ID=0;
    private FloatingActionButton fab;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

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

        /*fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Chat coming soon....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

       /* DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();*/

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int userID = getIntent().getIntExtra("userID", 0);
        if (Constants.USER_ID == 0) {
            selected_ID=0;
            addTeacherMenuInNavMenuDrawer();
        } else if (Constants.USER_ID == 1) {
            selected_ID=1;
            addParentMenuInNavMenuDrawer();
        } else if (Constants.USER_ID == 2) {
            selected_ID=2;
            addStudentMenuInNavMenuDrawer();
        }
    }

    public void setToolbar(Toolbar toolbar, String title) {
        setSupportActionBar(toolbar);
        setActionBarToggle(toolbar);
        toolbar.setTitle(title);
    }

    private void setActionBarToggle(Toolbar toolbar) {
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
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Manage Timetable").setIcon(getDrawable(R.mipmap.ic_timetable)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_EXAM, 0, "Examination").setIcon(getDrawable(R.mipmap.ic_edit)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "Assignments").setIcon(getDrawable(R.mipmap.ic_assignment)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_STD_PERFORMANCE, 0, "Student Performance").setIcon(getDrawable(R.mipmap.ic_performance)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_EVENTS, 0, "Create Event").setIcon(getDrawable(R.mipmap.ic_event)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Approve Leave").setIcon(getDrawable(R.mipmap.ic_approveleave)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_suggestion)).setCheckable(true);


        navView.invalidate();
    }

    private void addParentMenuInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Timetable").setIcon(getDrawable(R.mipmap.ic_timetable)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_NEWS, 0, "View Newsletter").setIcon(getDrawable(R.mipmap.ic_news)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "View Assignments").setIcon(getDrawable(R.mipmap.ic_assignment)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Leave Application").setIcon(getDrawable(R.mipmap.ic_leave)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_suggestion)).setCheckable(true);


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
                HomeFragment fragment = new HomeFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout, fragment).commit();
            } else
                super.onBackPressed();
        }
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
            finish();
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
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
            /*Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            *//*Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(DashboardActivity.this,
                    android.R.anim.fade_in, android.R.anim.fade_out).toBundle();*//*
            startActivity(intent);
            overridePendingTransition(0, 0);*/
            fragmentClass = ProfileFragment.class;

        } else if (id == R.id.nav_academics) {
            fragmentClass = AcademicsFragment.class;
//            fab.setImageResource(android.R.drawable.ic_input_add);
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
            fragmentManager.beginTransaction().replace(R.id.main_framelayout, fragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
