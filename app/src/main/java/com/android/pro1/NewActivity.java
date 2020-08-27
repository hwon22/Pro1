package com.android.pro1;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.util.List;

public class NewActivity extends AppCompatActivity {
    private Geocoder geocoder; //지오코딩
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        geocoder = new Geocoder(this);
        editText1 = (EditText)findViewById(R.id.addressText);
        editText2=(EditText)findViewById(R.id.timeText);
        editText3= (EditText)findViewById(R.id.telText);
    }

    public void startBtnClick(View view) {
        String newAdd = editText1.getText().toString();
        String newTime = editText2.getText().toString();
        String newTel = editText3.getText().toString();

            if(view.getId()==R.id.startBtn) {
                //모든 입력을 받았는가?
                if(newAdd==null || newAdd.equals("") || newTime==null || newTime.equals("")||newTel==null||newTel.equals("")) {
                    Toast t = Toast.makeText(this, "값을 모두 입력해주세요.",Toast.LENGTH_SHORT);
                    t.show();
                }
                else {
                    //입력한 주소를 지오코딩으로 변환
                    List<Address> addressList = null;
                    try {
                        addressList = geocoder.getFromLocationName(newAdd, 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(addressList.get(0).toString());
                    String[] splitStr = addressList.get(0).toString().split(",");
                    String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1, splitStr[0].length() - 2);
                    String newLatitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1);
                    String newLongitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1);

                    //DB에 검색한 장소이름, point값, 시간, 전번을 저장함
                    DBHelper helper = new DBHelper(this);
                    SQLiteDatabase db = helper.getWritableDatabase();
                    db.execSQL("insert into tb_newlist (newaddress,newlatitude,newlongitude,newtime,newtel) values (?,?,?,?,?)",
                            new String[]{newAdd, newLatitude, newLongitude, newTime, newTel});
                    db.close();
                    Toast t = Toast.makeText(this, "목적지지정성공", Toast.LENGTH_SHORT);
                    t.show();

                    //MainActivity로 이동
                    Intent intent = new Intent(NewActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
    }

    public void backBtnClick(View view) {
        if(view.getId()==R.id.backBtn){
            finish();
        }
    }
}
