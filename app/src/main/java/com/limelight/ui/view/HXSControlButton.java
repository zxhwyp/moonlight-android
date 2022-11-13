package com.limelight.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.limelight.HXSLog;
import com.limelight.R;

public class HXSControlButton extends LinearLayout {
    private ImageView ivIcon;
    private TextView ivMessage;

    public HXSControlButton(Context context) {
        super(context);
        initView(context,null);

    }

    public HXSControlButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);

    }

    public HXSControlButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }


    private void initView(Context context,AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ll_ic_tv_btn, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HXSControlButton);
        int iconImage = typedArray.getResourceId(R.styleable.HXSControlButton_HXSB_src, 0);
        String srcId = typedArray.getString(R.styleable.HXSControlButton_HXSB_message);
        HXSLog.info("imageId" + iconImage + "Message" + srcId);
        ivIcon = findViewById(R.id.iv_icon);
        ivMessage = findViewById(R.id.iv_message);
        if (iconImage != 0) {
            ivIcon.setImageResource(iconImage);
        }
        ivMessage.setText(srcId);
    }
}
