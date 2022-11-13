/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;


import com.limelight.Game;
import com.limelight.Infrastructure.common.HXSConstant;
import com.limelight.preferences.HXSControllerPosition;
import com.limelight.preferences.HXSControllerPositionPreference;
import com.limelight.ui.dialog.HXSResizeDialog;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class VirtualControllerElement extends View {
    protected static boolean _PRINT_DEBUG_INFORMATION = false;

    public static final int EID_DPAD = 1;
    public static final int EID_LT = 2;
    public static final int EID_RT = 3;
    public static final int EID_LB = 4;
    public static final int EID_RB = 5;
    public static final int EID_A = 6;
    public static final int EID_B = 7;
    public static final int EID_X = 8;
    public static final int EID_Y = 9;
    public static final int EID_BACK = 10;
    public static final int EID_START = 11;
    public static final int EID_LS = 12;
    public static final int EID_RS = 13;
    public static final int EID_LSB = 14;
    public static final int EID_RSB = 15;
    public static final int EID_MOUSE_LEFT = 16;
    public static final int EID_MOUSE_MIDDLE = 17;
    public static final int EID_MOUSE_RIGHT = 18;
    public static final int EID_INVISIBLE_TOUCH_PAD = 19;

    protected VirtualController virtualController;
    protected final int elementId;

    private final Paint paint = new Paint();

    private int normalColor = 0x00888888;
    protected int pressedColor = 0x000000FF;
    private int configMoveColor = 0x00FF0000;
    private int configResizeColor = 0x00FF00FF;
    private int configSelectedColor = 0x0000FF00;

    protected int startSize_x;
    protected int startSize_y;

    float position_pressed_x = 0;
    float position_pressed_y = 0;

    public double width;
    public double height;
    protected int index = -1;

    boolean isMoved = false;


    private enum Mode {
        Normal,
        Resize,
        Move
    }

    private Mode currentMode = Mode.Normal;

    protected VirtualControllerElement(VirtualController controller, Context context, int elementId) {
        super(context);

        this.virtualController = controller;
        this.elementId = elementId;
    }

    protected void moveElement(int pressed_x, int pressed_y, int x, int y) {
        int newPos_x = (int) getX() + x - pressed_x;
        int newPos_y = (int) getY() + y - pressed_y;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

        layoutParams.leftMargin = newPos_x > 0 ? newPos_x : 0;
        layoutParams.topMargin = newPos_y > 0 ? newPos_y : 0;
        layoutParams.rightMargin = 0;
        layoutParams.bottomMargin = 0;

        requestLayout();
        HXSControllerPosition position = null;
        position = getPosition();
        if (position == null) {
            return;
        }
        DisplayMetrics screen = HXSConstant.App.getResources().getDisplayMetrics();
        int height = screen.heightPixels;
        int width = screen.widthPixels;
        position.width = ((double) layoutParams.leftMargin) / width;
        position.height = ((double) layoutParams.topMargin) / height;
    }
    public static HXSControllerPosition getPosition(int id){
        switch (id){
            case VirtualControllerElement.EID_A:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_A);
            case VirtualControllerElement.EID_B:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_B);
            case VirtualControllerElement.EID_X:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_X);
            case VirtualControllerElement.EID_Y:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_Y);
            case VirtualControllerElement.EID_DPAD:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_PAD);
            case VirtualControllerElement.EID_BACK:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_BACK);
            case VirtualControllerElement.EID_START:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_START);
            case VirtualControllerElement.EID_LB:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_LB);
            case VirtualControllerElement.EID_LS:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_LS);
            case VirtualControllerElement.EID_RT:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_RT);
            case VirtualControllerElement.EID_LT:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_LT);
            case VirtualControllerElement.EID_RB:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_RB);
            case VirtualControllerElement.EID_RS:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_RS);
            case VirtualControllerElement.EID_MOUSE_LEFT:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_MOUSE_LEFT);
            case VirtualControllerElement.EID_MOUSE_MIDDLE:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_MOUSE_MIDDLE);
            case VirtualControllerElement.EID_MOUSE_RIGHT:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_MOUSE_RIGHT);
            case VirtualControllerElement.EID_INVISIBLE_TOUCH_PAD:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_INVISIBLE_PAD);
            case  VirtualControllerElement.EID_LSB:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_LSB);
            case VirtualControllerElement.EID_RSB:
                return HXSControllerPositionPreference.getInstance().positionHashMap.get(HXSControllerPositionPreference.KEY_RSB);
        }
        return null;
    }
    public HXSControllerPosition getPosition() {

        return getPosition(elementId);
    }

    protected void resizeElement(int pressed_x, int pressed_y, int width, int height) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

        int newHeight = height + (startSize_y - pressed_y);
        int newWidth = width + (startSize_x - pressed_x);

        layoutParams.height = newHeight > 20 ? newHeight : 20;
        layoutParams.width = newWidth > 20 ? newWidth : 20;

        requestLayout();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        onElementDraw(canvas);

        if (currentMode != Mode.Normal) {
            paint.setColor(configSelectedColor);
            paint.setStrokeWidth(getDefaultStrokeWidth());
            paint.setStyle(Paint.Style.STROKE);

            canvas.drawRect(paint.getStrokeWidth(), paint.getStrokeWidth(),
                    getWidth() - paint.getStrokeWidth(), getHeight() - paint.getStrokeWidth(),
                    paint);
        }

        super.onDraw(canvas);
    }

    /*
    protected void actionShowNormalColorChooser() {
        AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), normalColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog)
            {}

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                normalColor = color;
                invalidate();
            }
        });
        colorDialog.show();
    }

    protected void actionShowPressedColorChooser() {
        AmbilWarnaDialog colorDialog = new AmbilWarnaDialog(getContext(), normalColor, true, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                pressedColor = color;
                invalidate();
            }
        });
        colorDialog.show();
    }
    */
    abstract void setAlpha(int alpha);

    protected void actionEnableMove() {
        currentMode = Mode.Move;
    }

    protected void actionEnableResize() {
        currentMode = Mode.Resize;
    }

    protected void actionCancel() {
        currentMode = Mode.Normal;
        invalidate();
    }

    protected int getDefaultColor() {
        if (virtualController.getControllerMode() == VirtualController.ControllerMode.MoveButtons)
            return configMoveColor;
        else if (virtualController.getControllerMode() == VirtualController.ControllerMode.ResizeButtons)
            return configResizeColor;
        else
            return normalColor;
    }

    protected int getDefaultStrokeWidth() {
        DisplayMetrics screen = getResources().getDisplayMetrics();
        return (int) (screen.heightPixels * 0.004f);
    }

    protected void showConfigurationDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());

        alertBuilder.setTitle("Configuration");

        CharSequence functions[] = new CharSequence[]{
                "Move",
                "Resize",
                /*election
                "Set n
                Disable color sormal color",
                "Set pressed color",
                */
                "Cancel"
        };

        alertBuilder.setItems(functions, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: { // move
                        actionEnableMove();
                        break;
                    }
                    case 1: { // resize
                        actionEnableResize();
                        break;
                    }
                /*
                case 2: { // set default color
                    actionShowNormalColorChooser();
                    break;
                }
                case 3: { // set pressed color
                    actionShowPressedColorChooser();
                    break;
                }
                */
                    default: { // cancel
                        actionCancel();
                        break;
                    }
                }
            }
        });
        AlertDialog alert = alertBuilder.create();
        // show menu
        alert.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (virtualController.getControllerMode() == VirtualController.ControllerMode.Active) {

            return onElementTouchEvent(event);
        }
        if(elementId == EID_INVISIBLE_TOUCH_PAD){
            //            透明触摸板不接受自定义  拦截事件分发
            return true;
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                index = event.getActionIndex();
            case MotionEvent.ACTION_POINTER_DOWN: {
                position_pressed_x = event.getX();
                position_pressed_y = event.getY();
                startSize_x = getWidth();
                startSize_y = getHeight();

                if (virtualController.getControllerMode() == VirtualController.ControllerMode.MoveButtons)
                    actionEnableMove();
                else if (virtualController.getControllerMode() == VirtualController.ControllerMode.ResizeButtons)
                    actionEnableResize();

                return true;
            }
            case MotionEvent.ACTION_MOVE: {

                switch (currentMode) {
                    case Move: {
                        if (Math.abs(position_pressed_x - event.getX()) < 7 && Math.abs(position_pressed_y - event.getY()) < 7) {
                            break;
                        }
                        isMoved = true;
                        moveElement(
                                (int) position_pressed_x,
                                (int) position_pressed_y,
                                (int) event.getX(),
                                (int) event.getY());
                        break;
                    }
                    case Resize: {
                        //                        resizeElement(
                        //                                (int) position_pressed_x,
                        //                                (int) position_pressed_y,
                        //                                (int) event.getX(),
                        //                                (int) event.getY());
                        break;
                    }
                    case Normal: {
                        break;
                    }
                }
                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:

                if (index != event.getActionIndex()){
                    return false;
                }
            {
                if (currentMode == Mode.Move) {
                    if (!isMoved) {
                        HXSResizeDialog dialog = new HXSResizeDialog(Game.getInstance(), this);
                        dialog.show();
                        WindowManager windowManager = Game.getInstance().getWindowManager();
                        Display display = windowManager.getDefaultDisplay();
                        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                        lp.width = (int) (display.getWidth() * 0.65);
                        lp.height = (int) (display.getHeight() * 0.9);
                        dialog.getWindow().setAttributes(lp);
                        dialog.setCancelable(false);
                    }
                    isMoved = false;
                }
                actionCancel();
                return true;
            }
            default: {
            }
        }
        return true;
    }

    abstract protected void onElementDraw(Canvas canvas);

    abstract public boolean onElementTouchEvent(MotionEvent event);

    protected static final void _DBG(String text) {
        if (_PRINT_DEBUG_INFORMATION) {
            System.out.println(text);
        }
    }

    public void setColors(int normalColor, int pressedColor) {
        this.normalColor = normalColor;
        this.pressedColor = pressedColor;

        invalidate();
    }


    public void setOpacity(int opacity) {
        int hexOpacity = opacity * 255 / 100;
        this.normalColor = (hexOpacity << 24) | (normalColor & 0x00FFFFFF);
        this.pressedColor = (hexOpacity << 24) | (pressedColor & 0x00FFFFFF);

        invalidate();
    }

    protected final float getPercent(float value, float percent) {
        return value / 100 * percent;
    }

    protected final int getCorrectWidth() {
        return getWidth() > getHeight() ? getHeight() : getWidth();
    }


    public JSONObject getConfiguration() throws JSONException {
        JSONObject configuration = new JSONObject();

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();

        configuration.put("LEFT", layoutParams.leftMargin);
        configuration.put("TOP", layoutParams.topMargin);

        return configuration;
    }

    public void loadConfiguration(HXSControllerPosition configuration) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getLayoutParams();
        DisplayMetrics screen = HXSConstant.App.getResources().getDisplayMetrics();
        int height = screen.heightPixels;
        int width = screen.widthPixels;
        layoutParams.leftMargin = (int) (configuration.width * width);
        layoutParams.topMargin = (int) (configuration.height * height);
        layoutParams.height = (int) (configuration.size * this.height);
        layoutParams.width = (int) (configuration.size * this.width);
        requestLayout();
        if (configuration.visitable) {
            this.setVisibility(VISIBLE);
        } else {
            this.setVisibility(GONE);
        }
    }
}
