package com.limelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.limelight.Game;
import com.limelight.Infrastructure.httpUtils.HXSHttpRequestCenter;
import com.limelight.R;

public class HXSFeedbackGroup extends RelativeLayout implements View.OnClickListener {

    private TextView tvProblem;
    private TextView tvClientAdvice;
    private TextView tvGameAdvice;
    private TextView tvOther;
    private TextView textView;
    private EditText etFeedbackMessage;
    private String feedbackType = "问题";
    private int drawableId= R.drawable.bg_btn_feedback;
    private TextView tvCommitFeedback;

    public HXSFeedbackGroup(Context context) {
        super(context);
        initView(context);
    }

    public HXSFeedbackGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HXSFeedbackGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }
    private void initView(Context context){
        LayoutInflater.from(context).inflate(R.layout.rl_feedback, this);
        tvProblem = findViewById(R.id.tv_problem);
        tvClientAdvice = findViewById(R.id.tv_client_advice);
        tvGameAdvice = findViewById(R.id.tv_game_advice);
        tvOther = findViewById(R.id.tv_other);
        tvCommitFeedback = findViewById(R.id.tv_commit_feedback);
        etFeedbackMessage = findViewById(R.id.et_feedback_message);
        tvProblem.setOnClickListener(this);
        tvClientAdvice.setOnClickListener(this);
        tvGameAdvice.setOnClickListener(this);
        tvOther.setOnClickListener(this);
        setTypeSelected(tvProblem);
        tvCommitFeedback.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(etFeedbackMessage.getText().toString())){
                    Toast.makeText(Game.getInstance(),"发送反馈成功,感谢您的反馈",Toast.LENGTH_SHORT).show();
                    HXSHttpRequestCenter.requestSendFeedback(null,feedbackType,etFeedbackMessage.getText().toString(),5);
                    if (Game.getInstance().dialog!=null){
                        if (Game.getInstance().dialog.isShowing()){
                            Game.getInstance().dialog.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(Game.getInstance(),"请输入问题描述",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        textView = (TextView) view;
        setTypeSelected(textView);
        feedbackType = textView.getText().toString().trim();
    }
    private void setTypeSelected(TextView textView){
        tvOther.setBackground(null);
        tvGameAdvice.setBackground(null);
        tvClientAdvice.setBackground(null);
        tvProblem.setBackground(null);
        tvOther.setTextColor(0xFFFFFFFF);
        tvGameAdvice.setTextColor(0xFFFFFFFF);
        tvClientAdvice.setTextColor(0xFFFFFFFF);
        tvProblem.setTextColor(0xFFFFFFFF);
        textView.setTextColor(0xFF000000);
        textView.setBackground(getResources().getDrawable(drawableId));
    }
}
