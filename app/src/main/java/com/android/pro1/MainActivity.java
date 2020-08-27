package com.android.pro1;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_CODE_PERMISSIONS = 1000;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    long mNow=System.currentTimeMillis();
    Date mDate=new Date(mNow);
    SimpleDateFormat mFormat = new SimpleDateFormat("HH시 mm분");
    String formatDate = mFormat.format(mDate);

    int id=0;
    String newaddress="";
    String newlatitude ="";
    String newlongitude ="";
    String newtime="";
    String newtel="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLastLocationButtonClicked(view);
            }
        });
    }

    public void onLastLocationButtonClicked(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSIONS);
            return;
        }
        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location!=null){
                    LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.addMarker(new MarkerOptions()
                            .position(myLocation)
                            .title("현재 위치"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));
                    onNewPlace();
                }
            }
        });
    }

    public void markUp(String a){
        String name = a;
        if(name==null || name.equals("")){
            Toast t=Toast.makeText(this,"실패- 장소가 입력되지 않았습니다.",Toast.LENGTH_SHORT);
            t.show();
        }
        else{
            DBHelper helper = new DBHelper(this);
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into tb_list (placename,date) values (?,?)",
                    new String[] {name,formatDate});
            db.close();
            Toast t=Toast.makeText(this,"성공- 장소가 저장되었습니다.",Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void onNewPlace(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("새로운 위치 도착!");
        alert.setMessage("여기는 어딘가요?");
        final EditText place = new EditText(this);
        alert.setView(place);
        alert.setPositiveButton("체크인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String placename = place.getText().toString(); markUp(placename);
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
           Intent intent = new Intent(getApplicationContext(),ListActivity.class);
           startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from tb_newlist order by _id",null);



        while(cursor.moveToNext()){

            id = cursor.getInt(0);
            newaddress=cursor.getString(1);
            newlatitude=cursor.getString(2);
            newlongitude=cursor.getString(3);
            newtime=cursor.getString(4);
            newtel=cursor.getString(5);
        }
        db.close();

        LatLng seoul = new LatLng(Double.parseDouble(newlatitude), Double.parseDouble(newlongitude));
        mMap.addMarker(new MarkerOptions().position(seoul).title(newaddress).snippet("클릭하여 전화하기")).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul,16));
      //  mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+newtel));
                try{
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_PERMISSIONS:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"권한 체크 거부 됨",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this,"권한 허용 버튼 재클릭",Toast.LENGTH_SHORT).show();
        }
    }
}