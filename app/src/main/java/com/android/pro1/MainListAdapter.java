package com.android.pro1;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MainListAdapter extends ArrayAdapter<ListVO> {
    Context context;
    ArrayList<ListVO> datas;
    int resId;
    public MainListAdapter(Context context,int resId, ArrayList<ListVO> datas){
        super(context,resId);
        this.context=context;
        this.datas=datas;
        this.resId=resId;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(resId,null);

            MainListWrapper wrapper = new MainListWrapper(convertView);
            convertView.setTag(wrapper);
        }
        MainListWrapper wrapper = (MainListWrapper)convertView.getTag(); //태그획득
        TextView nameView = wrapper.nameView; //view 얻어오기
        TextView dateView = wrapper.dateView;

        final ListVO vo = datas.get(position);
        nameView.setText(vo.placename);
        dateView.setText(vo.placetime);
        return convertView;
    }
}
