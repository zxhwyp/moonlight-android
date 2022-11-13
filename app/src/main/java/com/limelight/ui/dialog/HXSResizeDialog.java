package com.limelight.ui.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.limelight.Game;
import com.limelight.R;
import com.limelight.binding.input.virtual_controller.VirtualControllerElement;


public class HXSResizeDialog extends Dialog {
    VirtualControllerElement element;
    SeekBar sizeSeekBar;
    Bitmap bitmap;
    double size = 1;
    ImageView ivElement;
    TextView tvSize;
    TextView tvSaveClose;
    TextView tvReset;
    TextView tvDelete;
    public HXSResizeDialog(@NonNull Context context, VirtualControllerElement element) {
        super(context);
        this.element = element;
    }

    public HXSResizeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HXSResizeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_resize);
        initView();
    }
    private void initView(){
        tvSize = findViewById(R.id.tv_size);
        sizeSeekBar = findViewById(R.id.seekbar_resize);
        ivElement = findViewById(R.id.iv_element);
        size = element.getPosition().size;
        element.setDrawingCacheEnabled(true);
        element.buildDrawingCache();
        bitmap = element.getDrawingCache();
        ivElement.setImageBitmap(bitmap);
        tvSize.setText(""+(int)(size * 50)+"%");
        sizeSeekBar.setProgress((int)(size * 50));
        tvSaveClose = findViewById(R.id.tv_save_close);
        tvDelete = findViewById(R.id.tv_delete);
        tvReset = findViewById(R.id.tv_reset);
        tvSaveClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Game.getInstance().virtualController.refreshLayout();
                dismiss();
            }
        });
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeSeekBar.setProgress(50);
            }
        });
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                element.getPosition().visitable = false;
                Game.getInstance().virtualController.refreshLayout();
                dismiss();
            }
        });
//        element.destroyDrawingCache();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ivElement.getLayoutParams());
        params.width = (int) (element.width * size);
        params.height = (int) (element.height * size);
        params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
        ivElement.setLayoutParams(params);

        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvSize.setText(""+i+"%");
                size = (double)i /50;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ivElement.getLayoutParams());
                params.width = (int) (element.width * size);
                params.height = (int) (element.height * size);
                params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
                ivElement.setLayoutParams(params);
                element.getPosition().size = size;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

}
