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
        for (int pageNum = 1; pageNum < 4; pageNum++) {
            String numOfRows = String.valueOf(100);
            String pageNo = String.valueOf(pageNum); //1~44
            String page = "http://data.ex.co.kr/openapi/locationinfo/locationinfoRest?key=" + key + "&type=" + type + "&numOfRows=" + numOfRows + "&pageNo=" + pageNo;

            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(page);
                NodeList menuList = doc.getElementsByTagName("list");
                NodeList nameList = doc.getElementsByTagName("unitName");
                NodeList addrList = doc.getElementsByTagName("routeName");

                for (int i = 0; i < menuList.getLength(); i++) {
                    //restN[i] = nameList.item(i).getFirstChild().getNodeValue();
                    User user = new User();
                    user.restName = nameList.item(i).getFirstChild().getNodeValue();
                    user.location = addrList.item(i).getFirstChild().getNodeValue();
                    user.lastMsgTime = String.valueOf(cnt);
                    userArrayList.add(user);
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


        ListAdapter listAdapter = new ListAdapter(ThisActivity.this,userArrayList);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ThisActivity.this,ListActivity.class);
                i.putExtra("restName",userArrayList.get(position).restName);
                i.putExtra("number",userArrayList.get(position).lastMsgTime);
                startActivity(i);

            }
        });

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
    public void searching(String val) {

        ArrayList<User> nmData = new ArrayList<User>();

        for (int i =0; i<userArrayList.size();i++){
            if(userArrayList.get(i).restName.contains(val)){
                User user = new User();
                user.restName = userArrayList.get(i).restName;
                user.location = userArrayList.get(i).location;
                user.lastMsgTime = userArrayList.get(i).lastMsgTime;
                nmData.add(user);
            }
        }
        ListAdapter listAdapter = new ListAdapter(ThisActivity.this,nmData);

        binding.listview.setAdapter(listAdapter);
        binding.listview.setClickable(true);
        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(ThisActivity.this,ListActivity.class);
                i.putExtra("restName",userArrayList.get(position).restName);
                i.putExtra("number",userArrayList.get(position).lastMsgTime);
                startActivity(i);

            }
        });
    }
}