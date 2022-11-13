package com.limelight.ui.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.R;

public class HXSToast extends Toast {
    public HXSToast(Context context, String msg) {
        super(context);
        View toastView = LayoutInflater.from(context).inflate(R.layout.rl_toast, null);
        TextView message = toastView.findViewById(R.id.tv_message);
        message.setText(msg);
        setView(toastView);
        setGravity(Gravity.CENTER,0,0);
    }

    @Override
    public void show() {
        super.show();
    }
}
