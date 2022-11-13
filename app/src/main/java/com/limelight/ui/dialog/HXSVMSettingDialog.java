package com.limelight.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import com.limelight.R;
import com.limelight.Game;
import com.limelight.UserData.HXSUserModule;
import com.limelight.preferences.HXStreamViewPreference;
import com.limelight.ui.view.HXSFeedbackGroup;
import com.limelight.ui.view.HXSItemSetting;
import com.limelight.ui.view.HXSSettingGroup;

public class HXSVMSettingDialog extends Dialog {
    private HXSItemSetting itemControllSetting;
    private HXSItemSetting itemFeedback;
    private ImageView ivClose;
    private TextView tvBalance;
    private ImageView ivMouseHelp;
    private ImageView ivTouchHelp;
    private HXSSettingGroup settingGroup;
    private HXSFeedbackGroup feedbackGroup;

    public HXSVMSettingDialog(@NonNull Context context) {
        super(context);
    }

    public HXSVMSettingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_dialog);
        initView();
    }

    public void initView() {
        settingGroup = findViewById(R.id.setting_group);
        feedbackGroup = findViewById(R.id.feedback_group);
        ivTouchHelp = findViewById(R.id.iv_touch_help);
        ivTouchHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                HXSMouseHelpDialog dialog = new HXSMouseHelpDialog(1, Game.getInstance());
                dialog.show();
                WindowManager windowManager = Game.getInstance().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth() * 0.8);
                lp.height = (int) (display.getHeight() * 0.7);
                dialog.getWindow().setAttributes(lp);
                dialog.setCancelable(false);
            }
        });
        ivMouseHelp = findViewById(R.id.iv_mouse_help);
        ivMouseHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                HXSMouseHelpDialog dialog = new HXSMouseHelpDialog(0, Game.getInstance());
                dialog.show();
                WindowManager windowManager = Game.getInstance().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth() * 0.8);
                lp.height = (int) (display.getHeight() * 0.7);
                dialog.getWindow().setAttributes(lp);
                dialog.setCancelable(false);
            }
        });
        tvBalance = findViewById(R.id.tv_balance);
        if (HXSUserModule.getInstance().bonus != null && HXSUserModule.getInstance().balance != null) {
            if (!"".equals(HXSUserModule.getInstance().bonus) && !"".equals(HXSUserModule.getInstance().balance)) {
                tvBalance.setText("海星点：" + HXSUserModule.getInstance().balance + "  赠点：" + HXSUserModule.getInstance().bonus);
            }
        }
        this.getWindow().setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.dialog_bg));
        ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HXSVMSettingDialog.this.dismiss();
            }
        });
        itemControllSetting = findViewById(R.id.item_setting);
        itemFeedback = findViewById(R.id.item_feedback);
        itemFeedback.setText("问题反馈");
        onItemSettingClicked();
        itemFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemFeedbackClicked();
            }
        });
        itemControllSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemSettingClicked();
            }
        });
    }

    @Override
    public void show() {
        super.show();
    }

    public void onItemFeedbackClicked() {
        itemFeedback.setSelected(true);
        itemControllSetting.setSelected(false);
        itemFeedback.setBackgroundColor(0xff24314c);
        itemControllSetting.setBackground(null);
        feedbackGroup.setVisibility(View.VISIBLE);
        settingGroup.setVisibility(View.GONE);
    }

    public void onItemSettingClicked() {
        itemFeedback.setSelected(false);
        itemControllSetting.setSelected(true);
        itemControllSetting.setBackgroundColor(0xff24314c);
        itemFeedback.setBackground(null);
        feedbackGroup.setVisibility(View.GONE);
        settingGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismiss() {
        if (Game.getInstance() != null) {
            HXStreamViewPreference.setSeekAlpha(Game.getInstance().alpha);
            HXStreamViewPreference.setSeekSpeed(Game.getInstance().scrollerSpeed);
            HXStreamViewPreference.setSwitchControllerVisible(Game.getInstance().controllerVisible);
            HXStreamViewPreference.setMouseVisible(Game.getInstance().mouseShow);
        }
        super.dismiss();
    }
}
