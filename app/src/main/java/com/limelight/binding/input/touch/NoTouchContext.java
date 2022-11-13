package com.limelight.binding.input.touch;

public class NoTouchContext implements TouchContext {
    @Override
    public int getActionIndex() {
        return 0;
    }

    @Override
    public void setPointerCount(int pointerCount) {

    }

    @Override
    public boolean touchDownEvent(int eventX, int eventY, long eventTime, boolean isNewFinger) {
        return false;
    }

    @Override
    public boolean touchMoveEvent(int eventX, int eventY, long eventTime) {
        return false;
    }

    @Override
    public void touchUpEvent(int eventX, int eventY, long eventTime) {

    }

    @Override
    public void cancelTouch() {

    }

    @Override
    public boolean isCancelled() {
        return false;
    }
}
