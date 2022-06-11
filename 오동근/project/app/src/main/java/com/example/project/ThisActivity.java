package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.databinding.ActivityThisBinding;

import java.util.ArrayList;

public class ThisActivity extends AppCompatActivity {

    ActivityThisBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int[] imageId = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,
                R.drawable.f,R.drawable.g};
        String[] restName = {"서울만남(부산)휴게소","죽전(서울)휴게소","기흥(부산)휴게소","안성(부산)휴게소","입장거봉포도(서울)휴게소",
            "망향(부산)휴게소","천안호두(부산)휴게소"};
        String[] location = {"서울 서초구","경기 죽전시","경기 용인시","경기 안성시","충남 천안시","충남 천안시","충남 천안시"};
        String[] lastmsgTime = {"#1","#2","#3","#4","#5","#6","#7"};

        ArrayList<User> userArrayList = new ArrayList<>();

        for(int i = 0;i< restName.length;i++){

            User user = new User(restName[i],location[i],lastmsgTime[i],imageId[i]);
            userArrayList.add(user);

        }



        ListAdapter listAdapter = new ListAdapter(ThisActivity.this,userArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ThisActivity.this,ListActivity.class);
                i.putExtra("restName",restName[position]);
                i.putExtra("imageid",imageId[position]);
                startActivity(i);

            }
        });

    }
}