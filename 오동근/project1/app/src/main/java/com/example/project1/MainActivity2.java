package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity2 extends AppCompatActivity {

    private TextView tv_sub;
    private ListView list;
    private Button btn_move2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageView test;

        tv_sub = findViewById(R.id.tv_sub);

        Intent intent = getIntent();
        String str = intent.getStringExtra("str");
        tv_sub.setText(str);
        //이동버튼
        btn_move2 = findViewById(R.id.btn_move2);
        btn_move2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        // 여기부터 리스트뷰

        list = (ListView) findViewById(R.id.list);

        List<String> data = new ArrayList<>();

        // 리스트뷰 참조 및 Adapter달기

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        for(int i=0;i<10;i++){
            data.add("아진짜..." + i);
        }
        data.add("서울만남(부산)휴게소");
        data.add("죽전(서울)휴게소");
        data.add("기흥(부산)휴게소");
        data.add("안성(부산)휴게소");

        /*list.setOnItemClickListener({parent, view, position, id ->
            textView1.text = list.getItemAtPosition(position) as CharSequence
        });*/

        adapter.notifyDataSetChanged();
    }
}