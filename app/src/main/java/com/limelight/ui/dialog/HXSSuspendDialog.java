package com.limelight.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.limelight.HXSLog;
import com.limelight.R;
import com.hxstream.operation.HXSConnection;
import com.limelight.ui.view.PickerView;

import java.util.ArrayList;


public class HXSSuspendDialog extends Dialog {
    public HXSSuspendDialog(@NonNull Context context) {
        super(context);
    }

    public HXSSuspendDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected HXSSuspendDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    class SuspendData {
        public String text;
        public int minutes;

        public SuspendData(String text, int minutes) {
            this.minutes = minutes;
            this.text = text;
        }
    }

    private ArrayList<SuspendData> suspendDataList = new ArrayList<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rl_suspend_dialog);
        PickerView pickerView = findViewById(R.id.pickView);

        suspendDataList.add(new SuspendData("0.5 小时", 30));
        suspendDataList.add(new SuspendData("1.0 小时", 60));
        suspendDataList.add(new SuspendData("6 小时", 6 * 60));
        suspendDataList.add(new SuspendData("12 小时", 12 * 60));

        PickerView.Adapter adapter = new PickerView.Adapter() {

            @Override
            public int getItemCount() {
                return suspendDataList.size();
            }

            @Override
            public PickerView.PickerItem getItem(int index) {
                return new PickerView.PickerItem() {
                    @Override
                    public String getText() {
                        return "" + index;
                    }

                    @Override
                    public int getIndex() {
                        return index;
                    }
                };
            }

            @Override
            public String getText(int index) {
                return suspendDataList.get(index).text;
            }
        };
        pickerView.setAdapter(adapter);
        Button btnPositive = findViewById(R.id.btn_postive);
        btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int minutes = 0;
                minutes = suspendDataList.get(pickerView.getSelectedItemPosition()).minutes;
                HXSLog.info("挂机时间" + minutes);
                HXSConnection.getInstance().chooseSuspend(minutes);
                dismiss();
            }
        });
        Button btnNegative = findViewById(R.id.btn_negative);
        btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        this.getWindow().setBackgroundDrawable(null);
    }
}
