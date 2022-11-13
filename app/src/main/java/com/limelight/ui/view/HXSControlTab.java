package com.limelight.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import androidx.core.view.MotionEventCompat;

import com.limelight.Game;
import com.limelight.R;

public class HXSControlTab extends RelativeLayout {

    private RelativeLayout controlPad;
    private HXSControlButton btnBack;
    private HXSControlButton btnSetting;
    private HXSControlButton btnKeyboard;
    private HXSControlButton btnShutdown;
    private HXSControlButton btnDesktop;
    private boolean isControlPadShow = false;
    private Button btnHide;
    private Handler handler = new Handler();
    private HXSTabbar.IDragViewClicked listener;
    public HXSControlTab(Context context) {
        super(context);
        initView(context);
    }

    public HXSControlTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public HXSControlTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.rl_control_tab, this);
        controlPad = findViewById(R.id.control_pad);
        btnBack = findViewById(R.id.btn_back);
        btnSetting = findViewById(R.id.btn_setting);
        btnKeyboard = findViewById(R.id.btn_keyboard);
        btnShutdown = findViewById(R.id.btn_shutdown);
        btnDesktop = findViewById(R.id.btn_desktop);
        btnHide = findViewById(R.id.btn_hide);
        btnBack.setAlpha(1f);
        btnSetting.setAlpha(1f);
        btnShutdown.setAlpha(1f);
        btnKeyboard.setAlpha(1f);
        btnDesktop.setAlpha(1f);
        btnHide.setAlpha(0.4f);
        setUpEvents();
    }

    public void setListener(HXSTabbar.IDragViewClicked listener) {
        this.listener = listener;
    }
    public void removeListener(){
        this.listener = null;
    }

    private void setUpEvents(){
        btnHide.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
               switchControlPad();
               if (Game.isKeyboardShown){
                   if (listener!=null){
                       listener.onKeyboardClicked();
                   }
               }
            }
        });
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onBackClicked();
                    switchControlPad();
                }
            }
        });
        btnSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onSettingClicked();
                    switchControlPad();
                }
            }
        });
        btnDesktop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onDesktopClicked();
                    switchControlPad();
                }
            }
        });
        btnShutdown.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onShutdownClicked();
                    switchControlPad();
                }
            }
        });
        btnKeyboard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener!=null){
                    listener.onKeyboardClicked();
                    switchControlPad();
                }
            }
        });
        setTouchListener(btnKeyboard);
        setTouchListener(btnShutdown);
        setTouchListener(btnDesktop);
        setTouchListener(btnSetting);
        setTouchListener(btnBack);
    }
    private void setTouchListener(View view){
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = MotionEventCompat.getActionMasked(motionEvent);
                if (action == MotionEvent.ACTION_DOWN) {
                    view.setAlpha(0.7f);
                }
                if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                    view.setAlpha(1f);
                }
                return false;
            }
        });
    }
    private void switchControlPad(){
        if (isControlPadShow){
            controlPad.setVisibility(GONE);
            isControlPadShow = false;
            btnHide.setAlpha(0.4f);
        }else {
            controlPad.setVisibility(VISIBLE);
            btnHide.setAlpha(1f);
            isControlPadShow = true;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        sleep(5000);
//                       handler.post(new Runnable() {
//                           @Override
//                           public void run() {
//                               if (isControlPadShow){
//                                   switchControlPad();
//                               }
//                           }
//                       });
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
        }
    }
}
