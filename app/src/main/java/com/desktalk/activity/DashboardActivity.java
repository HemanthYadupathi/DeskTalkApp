package com.desktalk.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.FrameLayout;
import android.widget.Toast;

import com.activity.desktalkapp.R;
import com.desktalk.fragment.AcademicsFragment;
import com.desktalk.fragment.HomeFragment;
import com.desktalk.fragment.ProfileFragment;
import com.desktalk.util.Constants;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, AcademicsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Chat coming soon....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        int userID = getIntent().getIntExtra("userID", 0);
        if (userID == 0)
            addTeacherMenuInNavMenuDrawer();
        else if (userID == 1)
            addParentMenuInNavMenuDrawer();
        else if (userID == 2)
            addStudentMenuInNavMenuDrawer();
    }

    private void addTeacherMenuInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ATTENDENCE, 0, "Attendance").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Manage Timetable").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_EXAM, 0, "Examination").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "Assignments").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_STD_PERFORMANCE, 0, "Student Performance").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_EVENTS, 0, "Create Event").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Approve Leave").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);


        navView.invalidate();
    }

    private void addParentMenuInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Timetable").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_NEWS, 0, "View Newsletter").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "View Assignments").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Leave Application").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);


        navView.invalidate();
    }


    private void addStudentMenuInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_TIMETABLE, 0, "Timetable").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_NEWS, 0, "View Newsletter").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_ASSIGN, 0, "View Assignments").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_LEAVES, 0, "Leave Status").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);
        menu.add(R.id.main, Constants.NAV_MENU_ITEM_SUGGESTION, 0, "Suggestion Box").setIcon(getDrawable(R.mipmap.ic_launcher)).setCheckable(true);


        navView.invalidate();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
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