package com.limelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.limelight.R;
import com.limelight.Game;
import com.limelight.UserData.HXSVmData;
import com.limelight.preferences.HXStreamViewPreference;
import com.limelight.utils.HXSIpParse;
import com.starvictory.micphonekit.AudioRecordUtil;

public class HXSSettingGroup extends RelativeLayout {
    private SeekBar seekBarAlpha;
    private SeekBar seekBarMouse;
    private TextView tvAlpha;
    private TextView tvMouseSpeed;
    private TextView tvDisableHandle;
    private HXSSwitchView switchOpenRs;
    private HXSSwitchView controllerSwitch;
    private HXSSwitchView useSourceKeyboard;
    private LinearLayout tvAdvancedController;
    private TextView tvHandle;
    private TextView tvAbsControl;
    private ImageView ivMouseHelp;
    private int touchType = Game.getInstance().getTouchType();

    public HXSSettingGroup(Context context) {
        super(context);
        initView(context);
    }

    public HXSSettingGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HXSSettingGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.setting_group, this);
        seekBarAlpha = findViewById(R.id.seekbar_alpha);
        seekBarMouse = findViewById(R.id.seekbar_mouse);
        tvAlpha = findViewById(R.id.tv_alpha);
        tvMouseSpeed = findViewById(R.id.tv_mouse_speed);
        tvAlpha.setText("" + Game.getInstance().alpha);
        tvMouseSpeed.setText("" + Game.getInstance().scrollerSpeed);
        seekBarMouse.setProgress(Game.getInstance().scrollerSpeed);
        tvHandle = findViewById(R.id.tv_handle);
        tvAbsControl = findViewById(R.id.tv_abs_control);
        ivMouseHelp = findViewById(R.id.iv_mouse_help);
        tvDisableHandle = findViewById(R.id.tv_disable_handle);
        useSourceKeyboard = findViewById(R.id.switch_use_source_keyboard);
        switchOpenRs = findViewById(R.id.switch_open_rs);
        switchOpenRs.setChecked(Game.getInstance().isTouchPadOpen);
        switchMouseVisible();
        HXSSwitchView switchMicPhone = findViewById(R.id.switch_mic_phone);
        switchMicPhone.setChecked(Game.getInstance().isMicPhoneOn);
        AudioRecordUtil.getInstance().initConnection(HXSIpParse.parseIp(HXSVmData.ip), 50054 + HXSVmData.portOffset, 50052 + HXSVmData.portOffset);
        switchMicPhone.setOnCheckedChangeListener(new HXSSwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(HXSSwitchView switchView, boolean isChecked) {
                if (isChecked) {
                    AudioRecordUtil.getInstance().start();
                } else {
                    AudioRecordUtil.getInstance().stop();
                }
                Game.getInstance().isMicPhoneOn = isChecked;
            }
        });
        useSourceKeyboard.setChecked(Game.getInstance().useSourceKeyboard);
        useSourceKeyboard.setOnCheckedChangeListener(new HXSSwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(HXSSwitchView switchView, boolean isChecked) {
                Game.getInstance().useSourceKeyboard = isChecked;
                HXStreamViewPreference.setUseSourceKeyboard(isChecked);
            }
        });
        tvHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                touchType = Game.TOUCH_TYPE_RELATIVE;
              Game.getInstance().setTouchType(touchType);
              switchMouseVisible();
            }
        });
        tvDisableHandle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                touchType = Game.TOUCH_TYPE_NON;
               Game.getInstance().setTouchType(touchType);
               switchMouseVisible();
            }
        });
        ivMouseHelp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tvAbsControl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                touchType = Game.TOUCH_TYPE_ABSOLUTE;
                Game.getInstance().setTouchType(touchType);
                switchMouseVisible();
            }
        });
        controllerSwitch = findViewById(R.id.switch_controller);
        controllerSwitch.setOnCheckedChangeListener(new HXSSwitchView.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(HXSSwitchView switchView, boolean isChecked) {
                seekBarAlpha.setEnabled(isChecked);
                Game.getInstance().setControllerVisible(isChecked);
                Game.getInstance().controllerVisible = isChecked;
            }
        });
        controllerSwitch.setChecked(Game.getInstance().controllerVisible);
        seekBarAlpha.setEnabled(Game.getInstance().controllerVisible);
        seekBarAlpha.setProgress(Game.getInstance().alpha);
        seekBarMouse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvMouseSpeed.setText("" + i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        switchOpenRs.setOnCheckedChangeListener(new HXSSwitchView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(HXSSwitchView switchView, boolean isChecked) {
                if (isChecked){
                    Game.getInstance().openTouchPad();
                }else {
                    Game.getInstance().openRS();
                }
                HXStreamViewPreference.setTouchPadOpen(isChecked);
            }
        });
        tvAdvancedController = findViewById(R.id.tv_advanced_controller);
        tvAdvancedController.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Game.getInstance().enterMoveButton();
            }
        });
        seekBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvAlpha.setText("" + i);
                Game.getInstance().setControllerAlpha(i * 255 / 50);
                Game.getInstance().alpha = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBarMouse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvMouseSpeed.setText("" + i);
                Game.getInstance().scrollerSpeed = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void switchMouseVisible() {
        switch (touchType) {
            case Game.TOUCH_TYPE_ABSOLUTE:
                tvHandle.setBackground(getResources().getDrawable(R.drawable.bg_mouse_unselected));
                tvAbsControl.setBackground(getResources().getDrawable(R.drawable.bg_mouse_selected));
                tvDisableHandle.setBackground(getResources().getDrawable(R.drawable.bg_mouse_unselected));

                break;
            case Game.TOUCH_TYPE_NON:
                tvDisableHandle.setBackground(getResources().getDrawable(R.drawable.bg_mouse_selected));
                tvHandle.setBackground(getResources().getDrawable(R.drawable.bg_mouse_unselected));
                tvAbsControl.setBackground(getResources().getDrawable(R.drawable.bg_mouse_unselected));
                break;
            case Game.TOUCH_TYPE_RELATIVE:
                tvAbsControl.setBackground(getResources().getDrawable(R.drawable.bg_mouse_unselected));
                tvHandle.setBackground(getResources().getDrawable(R.drawable.bg_mouse_selected));
                tvDisableHandle.setBackground(getResources().getDrawable(R.drawable.bg_mouse_unselected));
                break;
        }
    }

}
