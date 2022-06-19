package com.example.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class ListActivity extends AppCompatActivity {

    String key = "9511747961"; // API 인증키
    String type = "xml"; // 데이터 타입
    //휴게소 음식 메뉴 리스트 : http://data.ex.co.kr/openapi/basicinfo/openApiInfoM?apiId=0502&pn=-1

    ListView mListView = null; //ListView 객체
    BaseAdapters mAdapter = null; //ListView 추가를 위한 Adapter
    ArrayList<Datalist> mData = null; // Data의 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        
        
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().detectNetwork().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = this.getIntent();

        String val = intent.getStringExtra("restName"); // 리스트 클릭하여 가져온 데이터
        String num = intent.getStringExtra("number"); // 리스트의 넘버

        init(val, num); // 초기 값 세팅 ( 공공데이터 파싱 후 Arraylist 추가 )

        
        // EditText에 값이 바뀌는 것이 체크되었을 때 검색하는 기능
        EditText editText = findViewById(R.id.item_index_edit);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searching(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        
        // 이전으로 이동하는 기능
        Button button3 = findViewById(R.id.exit);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //세팅 함수
    public void init(String val, String num) {
        mListView = null;
        mAdapter = null;
        mData = null;

        mData = new ArrayList<Datalist>(); // Datalist 배열을 새로생성
        if(Integer.valueOf(num)<110){ // 데이터의 양이 많기에 절반으로 나눠 찾는 방향을 다르게 주었습니다.
            for (int pageNum = 1; pageNum < 45; pageNum++) {
                String numOfRows = String.valueOf(100);
                String pageNo = String.valueOf(pageNum); //1~44
                String page = "http://data.ex.co.kr/openapi/restinfo/restBestfoodList?key=" + key + "&type=" + type + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo;

                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //Dom 파싱
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(page);
                    // 리스트와 그 외 다른 목록 가져오기
                    NodeList menuList = doc.getElementsByTagName("list");
                    NodeList nameList = doc.getElementsByTagName("foodNm");
                    NodeList costList = doc.getElementsByTagName("foodCost");
                    NodeList restList = doc.getElementsByTagName("stdRestNm");
                    //NodeList addrList = doc.getElementsByTagName("svarAddr");
                    //ect는 있을수도 없을수도 있어서 추가하면 충돌이 생김
                    boolean flags = false; // 목록을 다 찾게 되면 플래그가 올라가 바로 data를 불러오는 것을 stop함
                    for (int i = 0; i < menuList.getLength(); i++) {
                        // 리스트에서 가져온 휴게소 명과 같은 값이 체크되었을 때 arraylist 추가
                        if (val.equals(restList.item(i).getFirstChild().getNodeValue())) { 
                            flags = true;
                            Datalist datalist = new Datalist();

                            datalist.mName = nameList.item(i).getFirstChild().getNodeValue();
                            datalist.mNumber = costList.item(i).getFirstChild().getNodeValue()+"원";
                            datalist.mDepartment = restList.item(i).getFirstChild().getNodeValue();
                            mData.add(datalist);
                            continue;
                        }
                        if (flags) {
                            break;
                        }
                    }
                    if(flags){
                        break;
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            for (int pageNum = 44; pageNum > 0; pageNum--) {
                String numOfRows = String.valueOf(100);
                String pageNo = String.valueOf(pageNum); //1~44
                String page = "http://data.ex.co.kr/openapi/restinfo/restBestfoodList?key=" + key + "&type=" + type + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo;

                try {
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(page);
                    NodeList menuList = doc.getElementsByTagName("list");
                    NodeList nameList = doc.getElementsByTagName("foodNm");
                    NodeList costList = doc.getElementsByTagName("foodCost");
                    NodeList restList = doc.getElementsByTagName("stdRestNm");

                    boolean flags = false;
                    for (int i = 0; i < menuList.getLength(); i++) {
                        if (val.equals(restList.item(i).getFirstChild().getNodeValue())) {
                            flags = true;
                            Datalist datalist = new Datalist();

                            datalist.mName = nameList.item(i).getFirstChild().getNodeValue();
                            datalist.mNumber = costList.item(i).getFirstChild().getNodeValue()+"원";
                            datalist.mDepartment = restList.item(i).getFirstChild().getNodeValue();
                            mData.add(datalist);
                            continue;
                        }
                        if (flags) {
                            break;
                        }
                    }
                    if(flags){
                        break;
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (SAXException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        mAdapter = new BaseAdapters(this, mData);
        mListView = (ListView) (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
    }
    // 텍스트의 바뀐 값을 활용하여 Arraylist 안에 있는 값 분류 후 시각화
    public void searching(String val) {
        mListView = null;
        mAdapter = null;
        ArrayList<Datalist> nmData = new ArrayList<>();

        for (int i =0; i<mData.size();i++){
            if(mData.get(i).mName.contains(val)){ // 텍스트의 음식 이름 중 한글자라도 똑같은 글자가 있을경우 
                Datalist datalist = new Datalist();
                datalist.mName = mData.get(i).mName;
                datalist.mNumber = mData.get(i).mNumber;
                datalist.mDepartment = mData.get(i).mDepartment;
                nmData.add(datalist);
            }
        }
        mAdapter = new BaseAdapters ( this, nmData );
        mListView = (ListView) findViewById( R.id.list_view );
        mListView.setAdapter( mAdapter );
    }
}
