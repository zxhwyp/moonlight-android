package com.limelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.limelight.R;

public class HXSTabbar extends LinearLayout {

    private TextView tvShutdown;
    private TextView tvBack;
    private TextView tvSetting;
    private TextView tvKeyboard;
    private ImageView ivShutdown;
    private ImageView ivBack;
    private ImageView ivSetting;
    private ImageView ivKeyboard;
    private ImageView ivCloseTab;
    private IDragViewClicked listener;

    public HXSTabbar(Context context, boolean left,IDragViewClicked listener) {
        super(context);
        initView(context, left);
        this.listener = listener;
    }

    public HXSTabbar(Context context, @Nullable AttributeSet attrs, boolean left,IDragViewClicked listener) {
        super(context, attrs);
        initView(context, left);
    }

    public HXSTabbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, boolean left,IDragViewClicked listener) {
        super(context, attrs, defStyleAttr);
        initView(context, left);
    }

    private void initView(Context context, boolean left) {
        View view;
        if (left) {
            view = LayoutInflater.from(context).inflate(R.layout.ll_tab_bar_left, this);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.ll_tab_bar, this);
        }
        tvBack = view.findViewById(R.id.tv_back);
        ivBack = view.findViewById(R.id.iv_back);
        tvShutdown = view.findViewById(R.id.tv_shutdown);
        ivShutdown = view.findViewById(R.id.iv_shutdown);
        tvKeyboard = view.findViewById(R.id.tv_keyboard);
        ivKeyboard = view.findViewById(R.id.iv_keyboard);
        tvSetting = view.findViewById(R.id.tv_setting);
        ivSetting = view.findViewById(R.id.iv_setting);
        ivCloseTab = view.findViewById(R.id.iv_close_tab);
        tvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onBackClicked();
                }
            }
        });
        ivBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onBackClicked();
                }
            }
        });
        ivShutdown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onShutdownClicked();
                }
            }
        });
        tvShutdown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onShutdownClicked();
                }
            }
        });
        ivCloseTab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onCloseClicked();
                }
            }
        });
        ivKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onKeyboardClicked();
                }
            }
        });
        tvKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onKeyboardClicked();
                }
            }
        });
        tvSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onSettingClicked();
                }
            }
        });
        ivSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onSettingClicked();
                }
            }
        });
    }

    public void setListener(IDragViewClicked listener) {
        this.listener = listener;
    }

    public interface IDragViewClicked{
        void onBackClicked();
        void onCloseClicked();
        void onSettingClicked();
        void onShutdownClicked();
        void onKeyboardClicked();
        void onDesktopClicked();
    }
}
