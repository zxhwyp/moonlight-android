package com.limelight.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.MotionEventCompat;

import com.limelight.R;

public class HXSKeyButton extends RelativeLayout {

    private TextView tvKeyText;
    private ImageView ivKeyIcon;
    private IOnKeyboardClicked listener;

    public HXSKeyButton(Context context) {
        this(context, null);
    }

    public HXSKeyButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HXSKeyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.rl_key_button, this);
        HXSKeyButton.this.setBackground(getResources().getDrawable(R.drawable.bg_key_button_normal));
        tvKeyText = findViewById(R.id.tv_key_text);
        ivKeyIcon = findViewById(R.id.iv_key_icon);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HXSKeyButton);
        int imgSrc = typedArray.getResourceId(R.styleable.HXSKeyButton_src, -1);
        if (imgSrc < 0) {
            ivKeyIcon.setVisibility(GONE);
            ivKeyIcon = null;
            tvKeyText.setText(typedArray.getNonResourceString(R.styleable.HXSKeyButton_text));
        } else {
            tvKeyText.setVisibility(GONE);
            tvKeyText = null;
            ivKeyIcon.setImageResource(imgSrc);
            int widthId = typedArray.getDimensionPixelSize(R.styleable.HXSKeyButton_imgWidth, -1);
            int heightId = typedArray.getDimensionPixelSize(R.styleable.HXSKeyButton_imgHeight, -1);
            if (widthId > 0 && heightId > 0) {
                LayoutParams params = (LayoutParams) ivKeyIcon.getLayoutParams();
                params.width = widthId;
                params.height = heightId;
                ivKeyIcon.setLayoutParams(params);
            }
        }
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = MotionEventCompat.getActionMasked(motionEvent);
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if (listener != null) {
                            listener.onKeyPressed();
                        }
                        HXSKeyButton.this.setBackground(getResources().getDrawable(R.drawable.bg_key_button_selected));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        if (listener != null) {
                            listener.onKeyReleased();
                        }
                        HXSKeyButton.this.setBackground(getResources().getDrawable(R.drawable.bg_key_button_normal));
                        break;
                }
                return true;
            }
        });

    }
    public void setIvKeyIcon(Drawable drawable){
        if (ivKeyIcon == null){
            return;
        }
        ivKeyIcon.setImageDrawable(drawable);
    }

    public void setOnKeyboardClickedListener(IOnKeyboardClicked listener) {
        this.listener = listener;
    }

    public interface IOnKeyboardClicked {
        public void onKeyPressed();

        public void onKeyReleased();
    }
}
