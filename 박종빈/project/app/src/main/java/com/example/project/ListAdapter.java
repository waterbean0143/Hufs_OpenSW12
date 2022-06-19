package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<User> {


    public ListAdapter(Context context, ArrayList<User> userArrayList){

        super(context,R.layout.list_item,userArrayList);
        //ListAdapter를 설정한다.
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //List 배열에서 가져오기
        User user = getItem(position); //listView Position(index)에 위치한 정보를 가져온다

        if (convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
            //inflate으로 객체를 부풀려서 전달받는다.
        }

        ImageView imageView = convertView.findViewById(R.id.profile_pic);
        TextView userName = convertView.findViewById(R.id.areaName);
        TextView lastMsg = convertView.findViewById(R.id.location);
        TextView time = convertView.findViewById(R.id.msgtime);
        //View값들을 layout에 저장된 xml파일 id에 맞춰서 받는다.

        imageView.setImageResource(user.imageId);
        userName.setText(user.restName);
        lastMsg.setText(user.location);
        time.setText(user.lastMsgTime);
        //받은 값들을 user 객체에 저장한다.

        return convertView;
    }
}
