/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.util.DisplayMetrics;

import com.limelight.Game;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.R;
import com.limelight.nvstream.input.ControllerPacket;
import com.limelight.nvstream.input.MouseButtonPacket;
import com.limelight.preferences.HXSControllerPosition;
import com.limelight.preferences.HXSControllerPositionPreference;
import com.limelight.preferences.PreferenceConfiguration;

public class VirtualControllerConfigurationLoader {
    public static final String OSC_PREFERENCE = "OSC";

    private static int getPercent(
            int percent,
            int total) {
        return (int) (((float) total / (float) 100) * (float) percent);
    }

    // The default controls are specified using a grid of 128*72 cells at 16:9
    private static int screenScale(int units, int height) {
        return (int) (((float) height / (float) 72) * (float) units);
    }

    private static DigitalPad createDigitalPad(
            final VirtualController controller,
            final Context context) {

        DigitalPad digitalPad = new DigitalPad(controller, context);
        digitalPad.addDigitalPadListener(new DigitalPad.DigitalPadListener() {
            @Override
            public void onDirectionChange(int direction) {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();

                if (direction == DigitalPad.DIGITAL_PAD_DIRECTION_NO_DIRECTION) {
                    inputContext.inputMap &= ~ControllerPacket.LEFT_FLAG;
                    inputContext.inputMap &= ~ControllerPacket.RIGHT_FLAG;
                    inputContext.inputMap &= ~ControllerPacket.UP_FLAG;
                    inputContext.inputMap &= ~ControllerPacket.DOWN_FLAG;

                    controller.sendControllerInputContext();
                    return;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_LEFT) > 0) {
                    inputContext.inputMap |= ControllerPacket.LEFT_FLAG;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_RIGHT) > 0) {
                    inputContext.inputMap |= ControllerPacket.RIGHT_FLAG;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_UP) > 0) {
                    inputContext.inputMap |= ControllerPacket.UP_FLAG;
                }
                if ((direction & DigitalPad.DIGITAL_PAD_DIRECTION_DOWN) > 0) {
                    inputContext.inputMap |= ControllerPacket.DOWN_FLAG;
                }
                controller.sendControllerInputContext();
            }
        });

        return digitalPad;
    }

    private static StarVictoryDigitalButton createStarVictoryDigitalButton(
            final int elementId,
            final int keyShort,
            final int keyLong,
            final int layer,
            final String text,
            final int icon,
            final int iconPressed,
            final VirtualController controller,
            final Context context) {
        StarVictoryDigitalButton button = new StarVictoryDigitalButton(controller, elementId, layer, context);
        button.setText(text);
        button.setIcon(icon);
        button.setIconPressed(iconPressed);

        button.addDigitalButtonListener(new StarVictoryDigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap |= keyShort;

                controller.sendControllerInputContext();
            }

            @Override
            public void onLongClick() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap |= keyLong;

                controller.sendControllerInputContext();
            }

            @Override
            public void onRelease() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap &= ~keyShort;
                inputContext.inputMap &= ~keyLong;

                controller.sendControllerInputContext();
            }
        });

        return button;
    }

    private static StarVictoryDigitalButton createStarvictoryLeftTrigger(
            final int layer,
            final String text,
            final int icon,
            final int iconPressed,
            final VirtualController controller,
            final Context context) {
        StarVictoryLeftTrigger button = new StarVictoryLeftTrigger(controller, layer, context);
        button.setText(text);
        button.setIcon(icon);
        button.setIconPressed(iconPressed);
        return button;
    }

    private static StarVictoryDigitalButton createStarVictoryRightTrigger(
            final int layer,
            final String text,
            final int icon,
            final int iconPressed,
            final VirtualController controller,
            final Context context) {
        StarVictoryRightTrigger button = new StarVictoryRightTrigger(controller, layer, context);
        button.setText(text);
        button.setIcon(icon);
        button.setIconPressed(iconPressed);
        return button;
    }

    private static DigitalButton createDigitalButton(
            final int elementId,
            final int keyShort,
            final int keyLong,
            final int layer,
            final String text,
            final int icon,
            final VirtualController controller,
            final Context context) {
        DigitalButton button = new DigitalButton(controller, elementId, layer, context);
        button.setText(text);
        button.setIcon(icon);

        button.addDigitalButtonListener(new DigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap |= keyShort;

                controller.sendControllerInputContext();
            }

            @Override
            public void onLongClick() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap |= keyLong;

                controller.sendControllerInputContext();
            }

            @Override
            public void onRelease() {
                VirtualController.ControllerInputContext inputContext =
                        controller.getControllerInputContext();
                inputContext.inputMap &= ~keyShort;
                inputContext.inputMap &= ~keyLong;

                controller.sendControllerInputContext();
            }
        });

        return button;
    }

    private static StarVictoryDigitalButton createMouseButton(
            final int elementId,
            final byte keyShort,
            final int keyLong,
            final int layer,
            final String text,
            final int icon,
            final int iconPressed,
            final VirtualController controller,
            final Context context) {
        StarVictoryDigitalButton button = new StarVictoryDigitalButton(controller, elementId, layer, context);
        button.setText(text);
        button.setIcon(icon);
        button.setIconPressed(iconPressed);

        button.addDigitalButtonListener(new StarVictoryDigitalButton.DigitalButtonListener() {
            @Override
            public void onClick() {
                Game.connection.sendMouseButtonDown(keyShort);
                Game.connection.sendMouseButtonUp(keyShort);
            }

            @Override
            public void onLongClick() {

            }

            @Override
            public void onRelease() {

            }
        });

        return button;
    }

    private static AnalogStick createLeftStick(
            final VirtualController controller,
            final Context context) {
        return new LeftAnalogStick(controller, context);
    }

    private static AnalogStickRight createRightStick(
            final VirtualController controller,
            final Context context) {
        return new RightAnalogStick(controller, context);
    }


    private static final int TRIGGER_L_BASE_X = 1;
    private static final int TRIGGER_R_BASE_X = 92;
    private static final int TRIGGER_DISTANCE = 23;
    private static final int TRIGGER_BASE_Y = 31;
    private static final int TRIGGER_WIDTH = 11;
    private static final int TRIGGER_HEIGHT = 11;

    // Face buttons are defined based on the Y button (button number 9)
    private static final int BUTTON_BASE_X = 106;
    private static final int BUTTON_BASE_Y = 1;
    private static final int BUTTON_SIZE = 10;
    private static final int BUTTON_WIDTH = getPercent(30, 33);
    private static final int BUTTON_HEIGHT = getPercent(40, 33);
    private static final int DPAD_BASE_X = 4;
    private static final int DPAD_BASE_Y = 41;
    private static final int DPAD_SIZE = 23;

    private static final int ANALOG_L_BASE_X = 6;
    private static final int ANALOG_L_BASE_Y = 4;
    private static final int ANALOG_R_BASE_X = 98;
    private static final int ANALOG_R_BASE_Y = 42;
    private static final int ANALOG_SIZE = 20;

    private static final int L3_R3_BASE_Y = 60;

    private static final int START_X = 83;
    private static final int BACK_X = 34;
    private static final int START_BACK_Y = 64;
    private static final int START_BACK_WIDTH = 12;
    private static final int START_BACK_HEIGHT = 7;

    public static void createDefaultLayout(final VirtualController controller, final Context context) {

        DisplayMetrics screen = context.getResources().getDisplayMetrics();
        PreferenceConfiguration config = PreferenceConfiguration.readPreferences(context);
        HXSControllerPositionPreference.getInstance();

        // Displace controls on the right by this amount of pixels to account for different aspect ratios
        int rightDisplacement = screen.widthPixels - screen.heightPixels * 16 / 9;

        int height = screen.heightPixels;

        // NOTE: Some of these getPercent() expressions seem like they can be combined
        // into a single call. Due to floating point rounding, this isn't actually possible.

        if (!config.onlyL3R3) {
            controller.addElement(new InvisibleTouchPad(controller, context, VirtualControllerElement.EID_INVISIBLE_TOUCH_PAD)
                    , 0,
                    0,
                    HXSConstant.App.getResources().getDisplayMetrics().widthPixels,
                    HXSConstant.App.getResources().getDisplayMetrics().heightPixels);
            controller.addElement(createDigitalPad(controller, context),
                    screenScale(DPAD_BASE_X, height),
                    screenScale(DPAD_BASE_Y, height),
                    screenScale(DPAD_SIZE, height),
                    screenScale(DPAD_SIZE, height)
            );
            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_A,
                            !config.flipFaceButtons ? ControllerPacket.A_FLAG : ControllerPacket.B_FLAG, 0, 1,
                            !config.flipFaceButtons ? "A" : "B", R.mipmap.a_normal, R.mipmap.a_pressed, controller, context),
                    screenScale(BUTTON_BASE_X, height) + rightDisplacement,
                    screenScale(BUTTON_BASE_Y + 2 * BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height)
            );

            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_B,
                            config.flipFaceButtons ? ControllerPacket.A_FLAG : ControllerPacket.B_FLAG, 0, 1,
                            config.flipFaceButtons ? "A" : "B", R.mipmap.b_normal, R.mipmap.b_pressed, controller, context),
                    screenScale(BUTTON_BASE_X + BUTTON_SIZE, height) + rightDisplacement,
                    screenScale(BUTTON_BASE_Y + BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height)
            );

            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_X,
                            !config.flipFaceButtons ? ControllerPacket.X_FLAG : ControllerPacket.Y_FLAG, 0, 1,
                            !config.flipFaceButtons ? "X" : "Y", R.mipmap.x_normal, R.mipmap.x_pressed, controller, context),
                    screenScale(BUTTON_BASE_X - BUTTON_SIZE, height) + rightDisplacement,
                    screenScale(BUTTON_BASE_Y + BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height)
            );

            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_Y,
                            config.flipFaceButtons ? ControllerPacket.X_FLAG : ControllerPacket.Y_FLAG, 0, 1,
                            config.flipFaceButtons ? "X" : "Y", R.mipmap.y_normal, R.mipmap.y_pressed, controller, context),
                    screenScale(BUTTON_BASE_X, height) + rightDisplacement,
                    screenScale(BUTTON_BASE_Y, height),
                    screenScale(BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height)
            );

            controller.addElement(createStarvictoryLeftTrigger(
                            1, "LT", R.mipmap.ic_button_lt, R.mipmap.ic_button_lt_pressed, controller, context),
                    screenScale(TRIGGER_L_BASE_X, height),
                    screenScale(TRIGGER_BASE_Y, height),
                    screenScale(TRIGGER_WIDTH, height),
                    screenScale(TRIGGER_HEIGHT, height)
            );

            controller.addElement(createStarVictoryRightTrigger(
                            1, "RT", R.mipmap.ic_button_rt, R.mipmap.ic_button_rt_pressed, controller, context),
                    screenScale(TRIGGER_R_BASE_X + TRIGGER_DISTANCE, height) + rightDisplacement,
                    screenScale(TRIGGER_BASE_Y, height),
                    screenScale(TRIGGER_WIDTH, height),
                    screenScale(TRIGGER_HEIGHT, height)
            );

            controller.addElement(createStarVictoryDigitalButton(VirtualControllerElement.EID_LB,
                            ControllerPacket.LB_FLAG, 0, 1, "LB",
                            R.mipmap.ic_button_lb, R.mipmap.ic_button_lb_pressed, controller, context),
                    screenScale(TRIGGER_L_BASE_X + TRIGGER_DISTANCE, height),
                    screenScale(TRIGGER_BASE_Y, height),
                    screenScale(TRIGGER_WIDTH, height),
                    screenScale(TRIGGER_HEIGHT, height)
            );

            controller.addElement(createStarVictoryDigitalButton(VirtualControllerElement.EID_RB,
                            ControllerPacket.RB_FLAG, 0, 1, "RB",
                            R.mipmap.ic_button_rb, R.mipmap.ic_button_rb_pressed, controller, context),
                    screenScale(TRIGGER_R_BASE_X, height) + rightDisplacement,
                    screenScale(TRIGGER_BASE_Y, height),
                    screenScale(TRIGGER_WIDTH, height),
                    screenScale(TRIGGER_HEIGHT, height)
            );

            controller.addElement(createLeftStick(controller, context),
                    screenScale(ANALOG_L_BASE_X, height),
                    screenScale(ANALOG_L_BASE_Y, height),
                    screenScale(ANALOG_SIZE, height),
                    screenScale(ANALOG_SIZE, height)
            );

            controller.addElement(createRightStick(controller, context),
                    screenScale(ANALOG_R_BASE_X, height) + rightDisplacement,
                    screenScale(ANALOG_R_BASE_Y, height),
                    screenScale(ANALOG_SIZE, height),
                    screenScale(ANALOG_SIZE, height)
            );

            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_BACK,
                            ControllerPacket.BACK_FLAG, 0, 2, "BACK", R.mipmap.ic_button_back, R.mipmap.ic_button_back_pressed, controller, context),
                    screenScale(BACK_X, height),
                    screenScale(START_BACK_Y, height),
                    screenScale(START_BACK_WIDTH, height),
                    screenScale(START_BACK_HEIGHT, height)
            );

            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_START,
                            ControllerPacket.PLAY_FLAG, 0, 3, "START", R.mipmap.ic_button_start, R.mipmap.ic_button_start_pressed, controller, context),
                    screenScale(START_X, height) + rightDisplacement,
                    screenScale(START_BACK_Y, height),
                    screenScale(START_BACK_WIDTH, height),
                    screenScale(START_BACK_HEIGHT, height)
            );
            controller.addElement(createMouseButton(
                            VirtualControllerElement.EID_MOUSE_LEFT,
                            MouseButtonPacket.BUTTON_LEFT, 0, 1, "left", R.mipmap.ic_left_click, R.mipmap.ic_left_clicked, controller, context),
                    screenScale(BACK_X, height),
                    screenScale(START_BACK_Y, height),
                    screenScale(BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height)
            );
            controller.addElement(createMouseButton(
                            VirtualControllerElement.EID_MOUSE_MIDDLE,
                            MouseButtonPacket.BUTTON_MIDDLE, 0, 1, "middle", R.mipmap.ic_middle_click, R.mipmap.ic_middle_clicked, controller, context),
                    screenScale(BACK_X, height),
                    screenScale(START_BACK_Y, height),
                    screenScale(BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height)
            );
            controller.addElement(createMouseButton(
                            VirtualControllerElement.EID_MOUSE_RIGHT,
                            MouseButtonPacket.BUTTON_RIGHT, 0, 1, "right", R.mipmap.ic_right_click, R.mipmap.ic_right_clicked, controller, context),
                    screenScale(BACK_X, height),
                    screenScale(START_BACK_Y, height),
                    screenScale(BUTTON_SIZE, height),
                    screenScale(BUTTON_SIZE, height)
            );
            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_LSB,
                            ControllerPacket.LS_CLK_FLAG, 0, 1, "L3", R.mipmap.ic_ls, R.mipmap.ic_ls_clicked, controller, context),
                    screenScale(TRIGGER_L_BASE_X, height),
                    screenScale(L3_R3_BASE_Y, height),
                    screenScale(TRIGGER_WIDTH, height),
                    screenScale(TRIGGER_HEIGHT, height)
            );

            controller.addElement(createStarVictoryDigitalButton(
                            VirtualControllerElement.EID_RSB,
                            ControllerPacket.RS_CLK_FLAG, 0, 1, "R3", R.mipmap.ic_rs, R.mipmap.ic_rs_clicked, controller, context),
                    screenScale(TRIGGER_R_BASE_X + TRIGGER_DISTANCE, height) + rightDisplacement,
                    screenScale(L3_R3_BASE_Y, height),
                    screenScale(TRIGGER_WIDTH, height),
                    screenScale(TRIGGER_HEIGHT, height)
            );

        } else {

        }

        controller.setOpacity(config.oscOpacity);
    }

    public static void saveProfile() {
        HXSControllerPositionPreference.getInstance().savePositions();
    }

    public static void loadFromPreferences(final VirtualController controller, final Context context) {

        for (VirtualControllerElement element : controller.getElements()) {
            HXSControllerPosition position = null;
            position = element.getPosition();
            if (position == null) {
                return;
            }
            element.loadConfiguration(position);
        }

        // Remove the corrupt element from the preferences


    }
}
