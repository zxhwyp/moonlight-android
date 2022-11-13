package com.limelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.limelight.Game;
import com.limelight.R;


public class HXSMoveTopTab extends LinearLayout {

    public HXSMoveTopTab(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HXSMoveTopTab(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    public void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.controller_move_tab, this);
        TextView tvReset = findViewById(R.id.tv_reset);
        TextView tvSave = findViewById(R.id.tv_save);
        tvReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Game.getInstance().resetViewController();
            }
        });
        tvSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Game.getInstance().removeTab();
            }
        });
    }
}
