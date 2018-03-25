package com.confortapp.leon.confortapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.confortapp.leon.confortapp.ExpandableList.Info;
import com.confortapp.leon.confortapp.ExpandableList.InfoData;
import com.confortapp.leon.confortapp.ExpandableList.InfoListAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ContactsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbarContacts = (Toolbar) findViewById(R.id.toolbarContacts);
        toolbarContacts.setTitle("Contacte");
        setSupportActionBar(toolbarContacts);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbarContacts, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView infoRecyclerView = findViewById(R.id.recyclerview_user_list);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        InfoListAdapter adapter = new InfoListAdapter();
        infoRecyclerView.setAdapter(adapter);

        InfoData infoData = new InfoData();
        List<Info> infoList = infoData.getInfoList();
        List<String> infoTypeList = infoData.getInfoTypeList();
        adapter.seInfoListAndType(infoList, infoTypeList);
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
        getMenuInflater().inflate(R.menu.contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_menu) {
            intent = new Intent(this, Home.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_about) {
            intent = new Intent(this, About.class);
            startActivity(intent);

        } else if (id == R.id.nav_contacts) {
            intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_log_out) {
            finish();
            moveTaskToBack(true);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
