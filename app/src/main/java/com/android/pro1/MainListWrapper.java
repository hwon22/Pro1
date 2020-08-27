package com.android.pro1;

import android.view.View;
import android.widget.TextView;

public class MainListWrapper {
    public TextView nameView;
    public TextView dateView;
    public MainListWrapper(View root){
        nameView=(TextView)root.findViewById(R.id.main_item_name);
        dateView=(TextView)root.findViewById(R.id.main_item_date);
    }
}
