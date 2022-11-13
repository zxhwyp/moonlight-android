package com.limelight.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.limelight.Game;
import com.limelight.R;
import com.limelight.constant.HXSKeycodePackage;
import com.limelight.nvstream.input.KeyboardPacket;

public class HXSKeyboardNum extends RelativeLayout{

    public HXSKeyboardNum(Context context) {
        super(context);
        initView(context);
    }

    public HXSKeyboardNum(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HXSKeyboardNum(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.rl_keyboard_num, this);
        HXSKeyButton keyButton0 = findViewById(R.id.key_0);
        HXSKeyButton keyButton1 = findViewById(R.id.key_1);
        View viewRight = findViewById(R.id.view_background_right);
        viewRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        View viewLeft = findViewById(R.id.view_background_left);
        viewLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        keyButton0.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_0, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_0, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        keyButton1.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_1, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_1, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton2 = findViewById(R.id.key_2);
        keyButton2.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_2, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_2, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton3 = findViewById(R.id.key_3);
        keyButton3.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_3, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_3, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton4 = findViewById(R.id.key_4);
        keyButton4.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_4, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_4, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton5 = findViewById(R.id.key_5);
        keyButton5.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_5, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_5, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton6 = findViewById(R.id.key_6);
        keyButton6.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_6, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_6, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton7 = findViewById(R.id.key_7);
        keyButton7.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_7, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_7, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton8 = findViewById(R.id.key_8);
        keyButton8.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_8, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_8, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton9 = findViewById(R.id.key_9);
        keyButton9.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_9, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_9, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButtonBack = findViewById(R.id.key_back);
        keyButtonBack.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BACK_SPACE,KeyboardPacket.KEY_DOWN,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BACK_SPACE,KeyboardPacket.KEY_UP,(byte)0x00);
            }
        });
        HXSKeyButton keyButtonJing = findViewById(R.id.key_jing);
        keyButtonJing.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_3,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_3,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonSanjiao = findViewById(R.id.key_sanjiao);
        keyButtonSanjiao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_6,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_6,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonXiaoyu = findViewById(R.id.key_xiaoyu);
        keyButtonXiaoyu.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_DOUHAO,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_DOUHAO,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonDayu = findViewById(R.id.key_dayu);
        keyButtonDayu.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JVHAO,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JVHAO,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonDakuohao = findViewById(R.id.key_dakuohao);
        keyButtonDakuohao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_LEFT_ZHONGKUOHAO,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_LEFT_ZHONGKUOHAO,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonDakuohaoRight = findViewById(R.id.key_dakuohao_right);
        keyButtonDakuohaoRight.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_RIGHT_ZHONGKUOHAO,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_RIGHT_ZHONGKUOHAO,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonShift = findViewById(R.id.key_shift);
        keyButtonShift.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SHIFT,KeyboardPacket.KEY_DOWN,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SHIFT,KeyboardPacket.KEY_UP,(byte)0x00);
            }
        });
        HXSKeyButton keyButtonBolang = findViewById(R.id.key_bolang);
        keyButtonBolang.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BOLANG,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BOLANG,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonDouhao = findViewById(R.id.key_douhao);
        keyButtonDouhao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_DOUHAO,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_DOUHAO,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonDunhao = findViewById(R.id.key_dunhao);
        keyButtonDunhao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BOLANG,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BOLANG,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonABC = findViewById(R.id.key_abc);
        keyButtonABC.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.getInstance().showKeyboardABC();
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonAt = findViewById(R.id.key_at);
        keyButtonAt.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_2,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_2,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonDot = findViewById(R.id.key_dot);
        keyButtonDot.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JVHAO,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JVHAO,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonSpace = findViewById(R.id.key_space);
        keyButtonSpace.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE,KeyboardPacket.KEY_DOWN,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE,KeyboardPacket.KEY_UP,(byte)0x00);
            }
        });
        HXSKeyButton keyButtonFanxie = findViewById(R.id.key_fanxie);
        keyButtonFanxie.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_AND,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_AND,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonBaifenhao = findViewById(R.id.key_baifenhao);
        keyButtonBaifenhao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_5,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_5,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonChu = findViewById(R.id.key_chu);
        keyButtonChu.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_WENHAO,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_WENHAO,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonCheng = findViewById(R.id.key_cheng);
        keyButtonCheng.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_8,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_8,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonJia = findViewById(R.id.key_jia);
        keyButtonJia.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIAHAO,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIAHAO,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonJian = findViewById(R.id.key_jian);
        keyButtonJian.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIANHAO,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIANHAO,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonUnder = findViewById(R.id.key_under);
        keyButtonUnder.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIANHAO,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIANHAO,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonZhongkuohao = findViewById(R.id.key_zhongkuohao);
        keyButtonZhongkuohao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_RIGHT_ZHONGKUOHAO,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_RIGHT_ZHONGKUOHAO,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonZhongKuohaoLeft = findViewById(R.id.key_zhongkuohao_left);
        keyButtonZhongKuohaoLeft.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_LEFT_ZHONGKUOHAO,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_LEFT_ZHONGKUOHAO,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonDeng = findViewById(R.id.key_deng);
        keyButtonDeng.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIAHAO,KeyboardPacket.KEY_DOWN,(byte)0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JIAHAO,KeyboardPacket.KEY_UP,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonWenhao = findViewById(R.id.key_wenhao);
        keyButtonWenhao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_WENHAO,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_WENHAO,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonTanhao = findViewById(R.id.key_tanhao);
        keyButtonTanhao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_1,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_1,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonEnter = findViewById(R.id.key_enter);
        keyButtonEnter.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_ENTER,KeyboardPacket.KEY_DOWN,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_ENTER,KeyboardPacket.KEY_UP,(byte)0x00);
            }
        });
        HXSKeyButton keyButtonSpace2 = findViewById(R.id.key_space2);
        keyButtonSpace2.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE,KeyboardPacket.KEY_DOWN,(byte)0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE,KeyboardPacket.KEY_UP,(byte)0x00);
            }
        });
        HXSKeyButton keyButtonAnd = findViewById(R.id.key_and);
        keyButtonAnd.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_AND,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_AND,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonKuohaoLeft = findViewById(R.id.key_kuohao_left);
        keyButtonKuohaoLeft.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_9,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_9,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonKuohao = findViewById(R.id.key_kuohao);
        keyButtonKuohao.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_0,KeyboardPacket.KEY_DOWN,(byte)0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_0,KeyboardPacket.KEY_UP,(byte)0x01);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonHide = findViewById(R.id.key_hide);
        keyButtonHide.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.getInstance().hideKeyboards();
            }

            @Override
            public void onKeyReleased() {

            }
        });
    }
}
