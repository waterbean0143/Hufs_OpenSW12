package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btn_to;
    private EditText et_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //번들에 저장된 savedInstanceState가 있으면 이 state로 만든다.
        setContentView(R.layout.activity_main);

        et_test = findViewById(R.id.et_test);

        //종료버튼
        Button exitButton = (Button) findViewById(R.id.exitButton);
        exitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("정말로 종료하시겠습니까?");
                builder.setTitle("종료 알림창")  //종료 알림창 Yes or No
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                finish();
                            }
                            //yes 클릭시 프로그램 종료
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.cancel();
                            }
                            //no 클릭시 dialog 취소
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("종료 알림창");
                alert.show();
            }
        });

        //버튼1 : 지도를 띄우는 액티비티로 가는 intent
        btn_to = findViewById(R.id.btn_to);
        btn_to.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, MapActivity1.class);
                startActivity(intent2);
            }
        });
        //버튼2 : 리스트뷰와 검색기능을 활용한 액티비티로 가는 intent
        btn_to = findViewById(R.id.btn_con);
        btn_to.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, ThisActivity.class);
                startActivity(intent3);
            }
        });
    }
}