package com.android.pro1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;

    public DBHelper(Context context){
        super(context,"wheredb",null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String listSql="create table tb_checklist ("+
                "_id Integer primary key autoincrement,"+
                "checkplace not null,"+
                "checkaddress not null," +
                "checklatitude not null,"+
                "checklongitude not null,"+
                "checktime not null,"+
                "checkdistance)";
        String placeSql="create table tb_newlist ("+
                "_id Integer primary key autoincrement,"+
                "newaddress not null,"+
                "newlatitude not null,"+
                "newlongitude not null,"+
                "newDate,"+
                "newtime,"+
                "newtel)";
        db.execSQL(listSql);
        db.execSQL(placeSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion==DATABASE_VERSION){
            db.execSQL("drop table tb_checklist");
            db.execSQL("drop table tb_newlist");
            onCreate(db);
        }
    }
}
