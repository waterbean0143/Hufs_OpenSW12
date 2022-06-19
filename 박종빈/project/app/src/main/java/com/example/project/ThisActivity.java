package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.databinding.ActivityThisBinding;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ThisActivity extends AppCompatActivity {

    ActivityThisBinding binding;

    String key = "9511747961";
    String type = "xml";

    ArrayList<User> userArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityThisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().detectNetwork().build();
        StrictMode.setThreadPolicy(policy);


        //String[] restN = new String[300];
        int cnt = 1;
        // 휴게소 이름 리스트를 가져오는 기능
        for (int pageNum = 1; pageNum < 4; pageNum++) {
            
            String numOfRows = String.valueOf(100);
            String pageNo = String.valueOf(pageNum); //1~44
            String page = "http://data.ex.co.kr/openapi/locationinfo/locationinfoRest?key=" + key + "&type=" + type + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo;

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); //Dom 파싱
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(page);
                NodeList menuList = doc.getElementsByTagName("list");
                NodeList nameList = doc.getElementsByTagName("unitName");
                NodeList addrList = doc.getElementsByTagName("routeName");

                for (int i = 0; i < menuList.getLength(); i++) {
                    //restN[i] = nameList.item(i).getFirstChild().getNodeValue();
                    User user = new User(); //User class를 새로 생성 후 이름과 위치를 가져옴
                    user.restName = nameList.item(i).getFirstChild().getNodeValue();
                    user.location = addrList.item(i).getFirstChild().getNodeValue();
                    user.lastMsgTime = String.valueOf(cnt);
                    userArrayList.add(user); // 가져온 값을 Arraylist에 추가
                    cnt++;
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        int[] imageId = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e,
                R.drawable.f,R.drawable.g};
        //image를 보내기 위함, 현재는 쓰지 않음


        ListAdapter listAdapter = new ListAdapter(ThisActivity.this,userArrayList);

        binding.listview.setAdapter(listAdapter); //listAdapter 아이템들을 바인딩한다.
        binding.listview.setClickable(true); //아이템 하나하나를 클릭할 수 있도록 한다.
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //클릭이벤트를 생성한다.
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ThisActivity.this,ListActivity.class);
                i.putExtra("restName",userArrayList.get(position).restName); //ListActivity에 보낼 때 추가하는 휴게소 이름
                i.putExtra("number",userArrayList.get(position).lastMsgTime);
                startActivity(i);

            }
        });

        
        // 텍스트에 글자를 입력하면 원하는 값만 추출하는 기능 (자동완성)
        EditText editText = findViewById(R.id.item_index);

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

    }
    // 텍스트를 확인 후 원하는 값을 추출하는 함수
    public void searching(String val) {

        ArrayList<User> nmData = new ArrayList<User>();

        for (int i =0; i<userArrayList.size();i++){
            if(userArrayList.get(i).restName.contains(val)){ // 가져온 텍스트 값과 휴게소 명 비교 후 한글자라도 같은 글자가 있을시 arraylist 추가
                User user = new User();
                user.restName = userArrayList.get(i).restName;
                user.location = userArrayList.get(i).location;
                user.lastMsgTime = userArrayList.get(i).lastMsgTime;
                nmData.add(user);
            }
        }
        
        ListAdapter listAdapter = new ListAdapter(ThisActivity.this,nmData);

        binding.listview.setAdapter(listAdapter);  //listAdapter binding
        binding.listview.setClickable(true);    //선택가능하도록
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() { //클릭이벤트 구현
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ThisActivity.this,ListActivity.class);   //ListActivity로 인텐트
                i.putExtra("restName",userArrayList.get(position).restName);
                i.putExtra("number",userArrayList.get(position).lastMsgTime);
                startActivity(i);

            }
        });
    }
}
