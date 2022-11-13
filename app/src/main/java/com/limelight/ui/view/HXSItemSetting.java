package com.limelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.limelight.R;

public class HXSItemSetting extends RelativeLayout {

    private TextView tvItemText;
    private View viewYellow;
    private ImageView ivTriangle;

    public HXSItemSetting(Context context) {
        super(context);
        initView(context);
    }

    public HXSItemSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HXSItemSetting(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.item_on_streamview,this);
        tvItemText = findViewById(R.id.tv_item_text);
        viewYellow = findViewById(R.id.view_yellow);
        ivTriangle = findViewById(R.id.iv_triangle);
    }
    public void setSelected(boolean selected){
        if (selected){
            viewYellow.setVisibility(VISIBLE);
            ivTriangle.setVisibility(VISIBLE);
        }else {
            viewYellow.setVisibility(GONE);
            ivTriangle.setVisibility(GONE);
        }
    }
    public void setText(String text){
        tvItemText.setText(text);
    }
}
