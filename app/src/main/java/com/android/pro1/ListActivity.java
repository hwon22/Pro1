package com.android.pro1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    ArrayList<ListVO> datas;
    long mNow=System.currentTimeMillis();
    Date mDate=new Date(mNow);
    SimpleDateFormat mFormat = new SimpleDateFormat("YYYY.MM.dd");
    String newDate = mFormat.format(mDate);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=(ListView)findViewById(R.id.main_list);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
        TextView todayDate = (TextView)findViewById(R.id.textView3);
        todayDate.setText(newDate);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from tb_checklist order by _id",null);

        datas =new ArrayList<>();
        while(cursor.moveToNext()){
            ListVO vo = new ListVO();
            vo.id = cursor.getInt(0);
            vo.checkplace=cursor.getString(1);
            vo.checkaddress=cursor.getString(2);
            vo.checktime=cursor.getString(5);
            datas.add(vo);
        }
        db.close();
        MainListAdapter adapter = new MainListAdapter(this,R.layout.main_list_item,datas);
        listView.setAdapter(adapter);
    }


    public void backBtnClick2(View view) {
        //뒤로가기 기능
        if(view.getId()==R.id.list_backBtn){
            finish();
        }
    }
}
