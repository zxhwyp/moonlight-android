package com.limelight.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.limelight.Game;
import com.limelight.R;

public class HXSMouseHelpDialog extends Dialog {
    private int helpType = 0;
    public HXSMouseHelpDialog(int type,@NonNull Context context) {
        super(context);
        helpType = type;
    }

    public HXSMouseHelpDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HXSMouseHelpDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (helpType==0){
            setContentView(R.layout.rl_mouse_help);
        }else {
            setContentView(R.layout.rl_touch_help);
        }
        initView();
    }
    private void initView(){
        ImageView ivClose = findViewById(R.id.iv_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        Game.getInstance().openSettingDialog();
    }
}
