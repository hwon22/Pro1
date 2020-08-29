package com.android.pro1;

import android.view.View;
import android.widget.TextView;

public class MainListWrapper {
    public TextView addView;
    public TextView addPlusView;
    public TextView dateView;
    public MainListWrapper(View root){
        addView=(TextView)root.findViewById(R.id.main_item_address);
        addPlusView=(TextView)root.findViewById(R.id.main_item_plus_address);
        dateView=(TextView)root.findViewById(R.id.main_item_time);
    }
}
