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

    String key = "9511747961";
    String type = "xml";
    //휴게소 음식 메뉴 리스트 : http://data.ex.co.kr/openapi/basicinfo/openApiInfoM?apiId=0502&pn=-1

    ListView mListView = null;
    BaseAdapters mAdapter = null;
    ArrayList<Datalist> mData = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().detectNetwork().build();
        StrictMode.setThreadPolicy(policy);

        Intent intent = this.getIntent();

        String val = intent.getStringExtra("restName"); // 리스트 클릭하여 가져온 데이터
        String num = intent.getStringExtra("number");

        init(val, num);

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

        Button button3 = findViewById(R.id.exit);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void init(String val, String num) {
        mListView = null;
        mAdapter = null;
        mData = null;

        mData = new ArrayList<Datalist>();
        if(Integer.valueOf(num)<110){
            for (int pageNum = 1; pageNum < 45; pageNum++) {
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
                    //NodeList addrList = doc.getElementsByTagName("svarAddr");
                    //ect는 있을수도 없을수도 있어서 추가하면 충돌이 생김
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
    public void searching(String val) {
        mListView = null;
        mAdapter = null;
        ArrayList<Datalist> nmData = new ArrayList<>();

        for (int i =0; i<mData.size();i++){
            if(mData.get(i).mName.contains(val)){
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
    public void reset(){
        mListView = null;
        mAdapter = null;
        mAdapter = new BaseAdapters ( this, mData );
        mListView = (ListView) findViewById( R.id.list_view );
        mListView.setAdapter( mAdapter );
    }
}