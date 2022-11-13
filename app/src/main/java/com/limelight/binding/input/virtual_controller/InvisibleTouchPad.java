package com.limelight.binding.input.virtual_controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;

import com.limelight.Game;
import com.limelight.binding.input.touch.MoveOnlyTouchContext;
import com.limelight.binding.input.touch.TouchContext;

import static com.limelight.Game.REFERENCE_HORIZ_RES;
import static com.limelight.Game.REFERENCE_VERT_RES;


public class InvisibleTouchPad extends VirtualControllerElement {

    TouchContext aTouchContextMap;

    private enum STICK_STATE {
        NO_MOVEMENT,
        MOVED_IN_DEAD_ZONE,
        MOVED_ACTIVE
    }

    /**
     * Click type states.
     */
    private enum CLICK_STATE {
        SINGLE,
        DOUBLE
    }

    private STICK_STATE stick_state = STICK_STATE.NO_MOVEMENT;
    private CLICK_STATE click_state = CLICK_STATE.SINGLE;

    protected InvisibleTouchPad(VirtualController controller, Context context, int elementId) {
        super(controller, context, elementId);
        aTouchContextMap = new MoveOnlyTouchContext(Game.getInstance().conn, 0, REFERENCE_HORIZ_RES, REFERENCE_VERT_RES, this);
    }

    @Override
    void setAlpha(int alpha) {

    }

    @Override
    protected void onElementDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onElementTouchEvent(MotionEvent event) {
        CLICK_STATE lastClickState = click_state;

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                aTouchContextMap.setPointerCount(event.getPointerCount());
                aTouchContextMap.touchDownEvent((int) event.getX(), (int) event.getY(), event.getEventTime(), true);
            }
            break;
            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getHistorySize(); i++) {
                    if (aTouchContextMap.getActionIndex() < event.getPointerCount()) {
                        aTouchContextMap.touchMoveEvent(
                                (int) event.getHistoricalX(aTouchContextMap.getActionIndex(), i),
                                (int) event.getHistoricalY(aTouchContextMap.getActionIndex(), i),
                                event.getHistoricalEventTime(i));
                    }
                }
                break;
        }

        return true;
    }
}
