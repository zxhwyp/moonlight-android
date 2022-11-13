package com.limelight.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.limelight.R;

public class HXSAlterDialog extends Dialog {

    TextView tvMessage;
    TextView tvRecommendBtn;
    TextView tvMiddleBtn;
    TextView tvRightBtn;

    public HXSAlterDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public HXSAlterDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    protected HXSAlterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private void initView(){
        setContentView(R.layout.rl_alter_dialog);
        tvMessage=findViewById(R.id.tv_message);
        tvRecommendBtn=findViewById(R.id.tv_recommend_btn);
        tvMiddleBtn=findViewById(R.id.tv_middle_btn);
        tvRightBtn=findViewById(R.id.tv_right_btn);
        this.getWindow().setBackgroundDrawable(null);
    }
    public HXSAlterDialog setTvMessage(String message){
        tvMessage.setText(message);
        return this;
    }
    public HXSAlterDialog setTvRecommendBtn(String text){
        tvRecommendBtn.setText(text);
        return this;
    }
    public HXSAlterDialog setTvMiddleBtn(String text){
        if (text == null){
           tvMiddleBtn.setVisibility(View.GONE);
            return this;
        }
        if (text.equals("")){
            tvMiddleBtn.setVisibility(View.GONE);
            return this;
        }
        tvMiddleBtn.setVisibility(View.VISIBLE);
        tvMiddleBtn.setText(text);
        return this;
    }
    public HXSAlterDialog setTvRightBtn(String text){
        tvRightBtn.setText(text);
        return this;
    }
    public HXSAlterDialog setTvRecommendBtnListener(View.OnClickListener listener){
        tvRecommendBtn.setOnClickListener(listener);
        return this;
    }
    public HXSAlterDialog setTvMiddleBtnListener(View.OnClickListener listener){
        tvMiddleBtn.setOnClickListener(listener);
        return this;
    }
    public HXSAlterDialog setTvRightBtnListener(View.OnClickListener listener){
        tvRightBtn.setOnClickListener(listener);
        return this;
    }
}
