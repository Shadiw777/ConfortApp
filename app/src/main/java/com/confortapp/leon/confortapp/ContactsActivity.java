package com.confortapp.leon.confortapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.confortapp.leon.confortapp.ExpandableList.Info;
import com.confortapp.leon.confortapp.ExpandableList.InfoData;
import com.confortapp.leon.confortapp.ExpandableList.InfoListAdapter;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        RecyclerView userRecyclerView = findViewById(R.id.recyclerview_user_list);
        userRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        InfoListAdapter adapter = new InfoListAdapter();
        userRecyclerView.setAdapter(adapter);

        InfoData usersData = new InfoData();
        List<Info> usersList = usersData.getInfoList();
        List<String> userTypeList = usersData.getInfoTypeList();
        adapter.seInfoListAndType(usersList, userTypeList);

    }
}
