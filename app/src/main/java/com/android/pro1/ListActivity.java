package com.android.pro1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView listView;
    ArrayList<ListVO> datas;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        listView=(ListView)findViewById(R.id.main_list);
        listView.setOnItemClickListener(this);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return true;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        DBHelper helper = new DBHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from tb_list order by _id",null);

        datas =new ArrayList<>();
        while(cursor.moveToNext()){
            ListVO vo = new ListVO();
            vo.id = cursor.getInt(0);
            vo.placename=cursor.getString(1);
            vo.placetime=cursor.getString(2);
            datas.add(vo);
        }
        db.close();
        MainListAdapter adapter = new MainListAdapter(this,R.layout.main_list_item,datas);
        listView.setAdapter(adapter);
    }
}
