package com.limelight.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.limelight.Game;
import com.limelight.R;
import com.limelight.constant.HXSKeycodePackage;
import com.limelight.nvstream.input.KeyboardPacket;


public class HXSKeyboard extends RelativeLayout {
    private boolean capOn = false;
    private byte innerFlag = 0x00;

    public HXSKeyboard(Context context) {
        super(context);
        initView(context);
    }

    public HXSKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HXSKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.rl_keyboard, this);
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
        HXSKeyButton keyButtonQ = findViewById(R.id.key_q);
        keyButtonQ.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_Q, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_Q, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonW = findViewById(R.id.key_w);
        keyButtonW.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_W, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_W, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonE = findViewById(R.id.key_e);
        keyButtonE.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_E, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_E, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonR = findViewById(R.id.key_r);
        keyButtonR.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_R, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_R, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonT = findViewById(R.id.key_t);
        keyButtonT.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_T, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_T, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonY = findViewById(R.id.key_y);
        keyButtonY.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_Y, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_Y, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonI = findViewById(R.id.key_i);
        keyButtonI.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_I, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_I, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonO = findViewById(R.id.key_o);
        keyButtonO.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_O, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_O, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonP = findViewById(R.id.key_p);
        keyButtonP.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_P, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_P, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonA = findViewById(R.id.key_a);
        keyButtonA.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_A, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_A, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonS = findViewById(R.id.key_s);
        keyButtonS.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_S, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_S, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonD = findViewById(R.id.key_d);
        keyButtonD.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_D, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_D, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonF = findViewById(R.id.key_f);
        keyButtonF.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_F, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_F, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonG = findViewById(R.id.key_g);
        keyButtonG.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_G, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_G, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonH = findViewById(R.id.key_h);
        keyButtonH.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_H, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_H, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonJ = findViewById(R.id.key_j);
        keyButtonJ.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_J, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_J, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonK = findViewById(R.id.key_k);
        keyButtonK.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_K, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_K, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonL = findViewById(R.id.key_l);
        keyButtonL.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_L, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_L, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonCap = findViewById(R.id.key_cap);
        keyButtonCap.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                if (capOn) {
                    innerFlag = 0x00;
                    keyButtonCap.setIvKeyIcon(getResources().getDrawable(R.drawable.ic_key_shift));
                } else {
                    innerFlag = 0x01;
                    keyButtonCap.setIvKeyIcon(getResources().getDrawable(R.drawable.ic_key_capelock));
                }
                capOn = !capOn;
            }

            @Override
            public void onKeyReleased() {
            }
        });
        HXSKeyButton keyButtonBack = findViewById(R.id.key_back);
        keyButtonBack.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BACK_SPACE, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_BACK_SPACE, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButtonEnter = findViewById(R.id.key_enter);
        keyButtonEnter.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_ENTER, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_ENTER, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButtonZ = findViewById(R.id.key_z);
        keyButtonZ.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_Z, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_Z, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonU = findViewById(R.id.key_u);
        keyButtonU.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_U, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_U, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonEsc = findViewById(R.id.key_esc);
        keyButtonEsc.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_ESCAPE,KeyboardPacket.KEY_DOWN,innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_ESCAPE, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonX = findViewById(R.id.key_x);
        keyButtonX.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_X, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_X, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonC = findViewById(R.id.key_c);
        keyButtonC.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_C, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_C, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonV = findViewById(R.id.key_v);
        keyButtonV.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_V, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_V, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonB = findViewById(R.id.key_b);
        keyButtonB.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_B, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_B, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonN = findViewById(R.id.key_n);
        keyButtonN.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_N, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_N, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonM = findViewById(R.id.key_m);
        keyButtonM.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_M, KeyboardPacket.KEY_DOWN, innerFlag);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_M, KeyboardPacket.KEY_UP, innerFlag);
            }
        });
        HXSKeyButton keyButtonShift = findViewById(R.id.key_shift);
        keyButtonShift.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SHIFT, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SHIFT, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButton123 = findViewById(R.id.key_123);
        keyButton123.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.showKeyboardNum();
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonAt = findViewById(R.id.key_at);
        keyButtonAt.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_2, KeyboardPacket.KEY_DOWN, (byte) 0x01);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_KEY_2, KeyboardPacket.KEY_UP, (byte) 0x01);
            }

            @Override
            public void onKeyReleased() {
            }
        });
        HXSKeyButton keyButtonDot = findViewById(R.id.key_dot);
        keyButtonDot.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JVHAO, KeyboardPacket.KEY_DOWN, (byte) 0x00);
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_JVHAO, KeyboardPacket.KEY_UP, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {

            }
        });
        HXSKeyButton keyButtonSpace = findViewById(R.id.key_space);
        keyButtonSpace.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE, KeyboardPacket.KEY_UP, (byte) 0x00);
            }
        });
        HXSKeyButton keyButtonSpace2 = findViewById(R.id.key_space2);
        keyButtonSpace2.setOnKeyboardClickedListener(new HXSKeyButton.IOnKeyboardClicked() {
            @Override
            public void onKeyPressed() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE, KeyboardPacket.KEY_DOWN, (byte) 0x00);
            }

            @Override
            public void onKeyReleased() {
                Game.connection.sendKeyboardInput(HXSKeycodePackage.WIN_SPACE, KeyboardPacket.KEY_UP, (byte) 0x00);
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
