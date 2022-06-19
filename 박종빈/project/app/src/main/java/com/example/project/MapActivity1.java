package com.example.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MapActivity1 extends AppCompatActivity implements MapView.MapViewEventListener,
        MapView.CurrentLocationEventListener, MapView.POIItemEventListener{
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkLocation()){  // gps가 꺼져있다면 gps를 켜는 함수를 호출
            onGPS();
        }
        OnCheckPermission();
        setContentView(R.layout.activity_map1);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().detectNetwork().build();
        StrictMode.setThreadPolicy(policy);
        MapView mapView = new MapView(this);
        ArrayList<rests > name = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<>();
        ArrayList<MapPOIItem> markerArr = new ArrayList<>();
        for(int i=1;i<4;i++){
            makelist t = new makelist(i,name);
            t.start();
            threads.add(t);
        }
        for(Thread t:threads){
            try {
                t.join();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        Log.d("SIZE: ",name.get(1).getName()+name.get(1).getX().toString());
        for(rests rest:name){
            MapPOIItem marker = new MapPOIItem();
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(rest.getY(),rest.getX()));
            marker.setItemName(rest.getName());
            markerArr.add(marker);
        }
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setZoomLevel(7,true);
        mapView.addPOIItems(markerArr.toArray(new MapPOIItem[markerArr.size()]));
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);
    }
    class makelist extends Thread{  // 쓰레드를 사용하여 휴게소목록을 api를 통해 호출한다. 
        int numOfPage;
        ArrayList<rests > list;
        public makelist(int numOfPage, ArrayList<rests > list){
            this.list=list;
            this.numOfPage=numOfPage;
        }

        @Override
        public void run() {
            super.run();
            String page = "http://data.ex.co.kr/openapi/locationinfo/locationinfoRest?key=9511747961&type=json&numOfRows=100&pageNo="+numOfPage;
            try {
                StringBuffer sb = new StringBuffer();
                URL url = new URL(page);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("content-type","application/json");
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                for(;;){
                    String line = br.readLine();
                    if(line==null) break;
                    sb.append(line);
                }
                con.disconnect();
                br.close();
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(sb.toString());
                JSONArray jsonArray = (JSONArray) jsonObject.get("list");
                for(Object o:jsonArray){
                    rests rest = new rests();
                    JSONObject item = (JSONObject) o;
                    rest.setName(item.get("unitName").toString());
                    rest.setX(Double.valueOf(item.get("xValue").toString()));
                    rest.setY(Double.valueOf(item.get("yValue").toString()));
                    list.add(rest);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private void onGPS(){  // gps를 활성화하는 함수
        AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity1.this);
        builder.setTitle("위치 서비스 활성화");  
        builder.setMessage("위치 서비스를 활성화 해야합니다.");
        builder.setCancelable(true);
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("GPS설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {  // 클릭 시 설정에서 직접 gps를 켤 수 있게 만들었다. 
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                startActivity(intent);
            }
        });
        builder.create().show();
    }
    public void OnCheckPermission(){  // 현재 어플리케이션이 위치정보를 허용했는지 확인한다. 
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        if(permissionCheck == PackageManager.PERMISSION_DENIED){ //위치 권한 확인

            //위치 권한 요청
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, 0);
        }
    }
    public boolean checkLocation(){ // LocationManager를 통해 현재 GPS기능이 꺼져있는지 확인한다. 
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }
   
    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }
    // 지도의 말풍선을 클릭 시 호출 함수
    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {
        Log.d("Touched: ",mapPOIItem.getItemName());
        Intent i = new Intent(MapActivity1.this,ListActivity.class);
        i.putExtra("restName",mapPOIItem.getItemName()); // 선택한 마커의 이름을 intent로 전송한다. 
        i.putExtra("number","1");
        startActivity(i);
    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}
