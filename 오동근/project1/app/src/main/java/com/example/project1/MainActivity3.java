package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity3 extends AppCompatActivity {
    private ListView list;
    private static final String Confident = MainActivity3.class.getSimpleName();

    String key = "9511747961";
    String type = "xml";
    String numOfRows = "100";
    String pageNo = "1";
    //휴게소 음식 메뉴 리스트 : http://data.ex.co.kr/openapi/basicinfo/openApiInfoM?apiId=0502&pn=-1
    String page = "http://data.ex.co.kr/openapi/restinfo/restBestfoodList?key=" + key + "&type=" + type + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().detectNetwork().build();
        StrictMode.setThreadPolicy(policy);

        list = (ListView) findViewById(R.id.list);

        List<String> data = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        list.setAdapter(adapter);

        Log.d(Confident,"This is");

        Button button1 = findViewById(R.id.source);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer html = new StringBuffer();
                try{
                    URL url = new URL(page);
                    InputStream inputStream = url.openStream();
                    TextView result = findViewById(R.id.textView1);
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    for(; ;){
                        String line = br.readLine();
                        if(line==null)
                            break;
                        html.append(line + '\n');
                    }String xml = html.toString();
                    result.setText(xml);
                    inputStream.close();
                }catch (IOException e){
                    Toast.makeText(v.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        //버튼
        Button button2 = findViewById(R.id.parse);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView result = findViewById(R.id.textView2);
                try{
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document doc = builder.parse(page);
                    NodeList cdList = doc.getElementsByTagName("list");
                    NodeList artistList = doc.getElementsByTagName("foodNm");
                    NodeList countryList = doc.getElementsByTagName("foodCost");
                    NodeList priceList = doc.getElementsByTagName("stdRestNm");
                    NodeList yearList = doc.getElementsByTagName("svarAddr");
                    //ect는 있을수도 없을수도 있어서 추가하면 충돌이 생김
                    for(int i = 0; i < cdList.getLength(); i++){
                        result.append("\n");
                        result.append("\n음식명 :  : ");
                        result.append(artistList.item(i).getFirstChild().getNodeValue() + "\n가격 : ");
                        result.append(countryList.item(i).getFirstChild().getNodeValue() + "\n휴게소명 : ");
                        result.append(priceList.item(i).getFirstChild().getNodeValue() + "\n주소 : ");
                        result.append(yearList.item(i).getFirstChild().getNodeValue() + "\n");
                        String value = yearList.item(i).getFirstChild().getNodeValue();
                        data.add(value);
                        result.append(cdList.getLength() + "\n");
                        result.append("-----------------------------\n");
                    }
                }catch (ParserConfigurationException e){
                    e.printStackTrace();
                }catch (SAXException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
        data.add("ddd");
    }
}