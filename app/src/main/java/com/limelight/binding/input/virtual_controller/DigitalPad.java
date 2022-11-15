/**
 * Created by Karim Mreisi.
 */

package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import com.limelight.R;

import java.util.ArrayList;
import java.util.List;

public class DigitalPad extends VirtualControllerElement {
    public final static int DIGITAL_PAD_DIRECTION_NO_DIRECTION = 0;
    int direction = DIGITAL_PAD_DIRECTION_NO_DIRECTION;
    public final static int DIGITAL_PAD_DIRECTION_LEFT = 1;
    public final static int DIGITAL_PAD_DIRECTION_UP = 2;
    public final static int DIGITAL_PAD_DIRECTION_RIGHT = 4;
    public final static int DIGITAL_PAD_DIRECTION_DOWN = 8;

    private int iconLeftNormal = R.mipmap.ic_left_normal;
    private int iconRightNormal = R.mipmap.ic_right_normal;
    private int iconUpNormal = R.mipmap.ic_up_normal;
    private int iconDownNormal = R.mipmap.ic_down_normal;
    private int iconLeftPressed = R.mipmap.ic_left_pressed;
    private int iconRightPressed = R.mipmap.ic_right_pressed;
    private int iconUpPressed = R.mipmap.ic_up_pressed;
    private int iconDownPressed = R.mipmap.ic_down_pressed;
    private int alpha = 255;
    private int buttonMargin = 20;
    List<DigitalPadListener> listeners = new ArrayList<>();

    private static final int DPAD_MARGIN = 5;

    private final Paint paint = new Paint();

    public DigitalPad(VirtualController controller, Context context) {
        super(controller, context, EID_DPAD);
    }

    public void addDigitalPadListener(DigitalPadListener listener) {
        listeners.add(listener);
    }

    @Override
    protected void onElementDraw(Canvas canvas) {
        if (direction == DIGITAL_PAD_DIRECTION_NO_DIRECTION) {
            drawDigitalPad(iconUpNormal, iconDownNormal, iconLeftNormal, iconRightNormal, canvas);
            return;
        }

        if ((
                (direction & DIGITAL_PAD_DIRECTION_LEFT) > 0 &&
                        (direction & DIGITAL_PAD_DIRECTION_UP) > 0
        )) {
            drawDigitalPad(iconUpPressed, iconDownNormal, iconLeftPressed, iconRightNormal, canvas);
            return;
        }
        if ((
                (direction & DIGITAL_PAD_DIRECTION_UP) > 0 &&
                        (direction & DIGITAL_PAD_DIRECTION_RIGHT) > 0
        )) {
            drawDigitalPad(iconUpPressed, iconDownNormal, iconLeftNormal, iconRightPressed, canvas);
            return;
        }
        if ((
                (direction & DIGITAL_PAD_DIRECTION_RIGHT) > 0 &&
                        (direction & DIGITAL_PAD_DIRECTION_DOWN) > 0
        )) {
            drawDigitalPad(iconUpNormal, iconDownPressed, iconLeftNormal, iconRightPressed, canvas);
            return;
        }
        if ((
                (direction & DIGITAL_PAD_DIRECTION_DOWN) > 0 &&
                        (direction & DIGITAL_PAD_DIRECTION_LEFT) > 0
        )) {
            drawDigitalPad(iconUpNormal, iconDownPressed, iconLeftPressed, iconRightNormal, canvas);
            return;
        }

        if ((direction & DIGITAL_PAD_DIRECTION_LEFT) > 0) {
            drawDigitalPad(iconUpNormal, iconDownNormal, iconLeftPressed, iconRightNormal, canvas);
            return;
        }

        if ((direction & DIGITAL_PAD_DIRECTION_UP) > 0) {
            drawDigitalPad(iconUpPressed, iconDownNormal, iconLeftNormal, iconRightNormal, canvas);
            return;
        }

        if ((direction & DIGITAL_PAD_DIRECTION_RIGHT) > 0) {
            drawDigitalPad(iconUpNormal, iconDownNormal, iconLeftNormal, iconRightPressed, canvas);
            return;
        }
        if ((direction & DIGITAL_PAD_DIRECTION_DOWN) > 0) {
            drawDigitalPad(iconUpNormal, iconDownPressed, iconLeftNormal, iconRightNormal, canvas);
            return;
        }


    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        invalidate();
    }

    private void drawDigitalPad(int upIcon, int downIcon, int leftIcon, int rightIcon, Canvas canvas) {
        Drawable drawableUp = getResources().getDrawable(upIcon);
        drawableUp.setBounds((int) getPercent(getWidth(), 33), 0, (int) getPercent(getWidth(), 66), getHeight() / 2 - getHeight() / buttonMargin);
        drawableUp.setAlpha(alpha);
        drawableUp.draw(canvas);
        Drawable drawableDown = getResources().getDrawable(downIcon);
        drawableDown.setBounds((int) getPercent(getWidth(), 33), getHeight() / 2 + getHeight() / buttonMargin, (int) getPercent(getWidth(), 66), getHeight());
        drawableDown.setAlpha(alpha);
        drawableDown.draw(canvas);
        Drawable drawableLeft = getResources().getDrawable(leftIcon);
        drawableLeft.setBounds(0, (int) getPercent(getHeight(), 33), getWidth() / 2 - getWidth() / buttonMargin, (int) getPercent(getHeight(), 66));
        drawableLeft.setAlpha(alpha);
        drawableLeft.draw(canvas);
        Drawable drawableRight = getResources().getDrawable(rightIcon);
        drawableRight.setBounds(getWidth() / 2 + getWidth() / buttonMargin, (int) getPercent(getHeight(), 33), getWidth(), (int) getPercent(getHeight(), 66));
        drawableRight.setAlpha(alpha);
        drawableRight.draw(canvas);
    }

    private void newDirectionCallback(int direction) {
        _DBG("direction: " + direction);

        // notify listeners
        for (DigitalPadListener listener : listeners) {
            listener.onDirectionChange(direction);
        }
    }

    @Override
    public boolean onElementTouchEvent(MotionEvent event) {
        // get masked (not specific to a pointer) action
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE: {
                direction = 0;

                if (event.getX() < getPercent(getWidth(), 33)) {
                    direction |= DIGITAL_PAD_DIRECTION_LEFT;
                }
                if (event.getX() > getPercent(getWidth(), 66)) {
                    direction |= DIGITAL_PAD_DIRECTION_RIGHT;
                }
                if (event.getY() > getPercent(getHeight(), 66)) {
                    direction |= DIGITAL_PAD_DIRECTION_DOWN;
                }
                if (event.getY() < getPercent(getHeight(), 33)) {
                    direction |= DIGITAL_PAD_DIRECTION_UP;
                }
                newDirectionCallback(direction);
                invalidate();

                return true;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            {
                direction = 0;
                newDirectionCallback(direction);
                invalidate();

                return true;
            }
            case MotionEvent.ACTION_POINTER_UP:
            default: {
            }
        }

        return true;
    }

    public interface DigitalPadListener {
        void onDirectionChange(int direction);
    }
}
