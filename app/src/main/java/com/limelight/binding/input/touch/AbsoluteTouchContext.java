package com.limelight.binding.input.touch;

import android.os.SystemClock;
import android.view.View;

import com.limelight.nvstream.NvConnection;
import com.limelight.nvstream.input.MouseButtonPacket;

import java.util.Timer;
import java.util.TimerTask;

public class AbsoluteTouchContext implements TouchContext {
    private int lastTouchDownX = 0;
    private int lastTouchDownY = 0;
    private long lastTouchDownTime = 0;
    private int lastTouchUpX = 0;
    private int lastTouchUpY = 0;
    private long lastTouchUpTime = 0;
    private int lastTouchLocationX = 0;
    private int lastTouchLocationY = 0;
    private boolean cancelled;
    private boolean confirmedLongPress;
    private boolean confirmedTap;
    private Timer longPressTimer;
    private Timer tapDownTimer;
    private float accumulatedScrollDelta;

    private final NvConnection conn;
    private final int actionIndex;
    private final View targetView;

    private static final int SCROLL_SPEED_DIVISOR = 20;

    private static final int LONG_PRESS_TIME_THRESHOLD = 650;
    private static final int LONG_PRESS_DISTANCE_THRESHOLD = 30;

    private static final int DOUBLE_TAP_TIME_THRESHOLD = 250;
    private static final int DOUBLE_TAP_DISTANCE_THRESHOLD = 60;

    private static final int TOUCH_DOWN_DEAD_ZONE_TIME_THRESHOLD = 100;
    private static final int TOUCH_DOWN_DEAD_ZONE_DISTANCE_THRESHOLD = 20;

    public AbsoluteTouchContext(NvConnection conn, int actionIndex, View view) {
        this.conn = conn;
        this.actionIndex = actionIndex;
        this.targetView = view;
    }

    @Override
    public int getActionIndex() {
        return actionIndex;
    }

    @Override
    public boolean touchDownEvent(int eventX, int eventY, long time, boolean isNewFinger) {
        if (!isNewFinger) {
            // We don't handle finger transitions for absolute mode
            return true;
        }

        lastTouchLocationX = lastTouchDownX = eventX;
        lastTouchLocationY = lastTouchDownY = eventY;
        lastTouchDownTime = SystemClock.uptimeMillis();
        cancelled = confirmedTap = confirmedLongPress = false;
        accumulatedScrollDelta = 0;

        if (actionIndex == 0) {
            // Start the timers
            startTapDownTimer();
            startLongPressTimer();
        }

        return true;
    }

    private boolean distanceExceeds(int deltaX, int deltaY, double limit) {
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2)) > limit;
    }

    private void updatePosition(int eventX, int eventY) {
        // We may get values slightly outside our view region on ACTION_HOVER_ENTER and ACTION_HOVER_EXIT.
        // Normalize these to the view size. We can't just drop them because we won't always get an event
        // right at the boundary of the view, so dropping them would result in our cursor never really
        // reaching the sides of the screen.
        eventX = Math.min(Math.max(eventX, 0), targetView.getWidth());
        eventY = Math.min(Math.max(eventY, 0), targetView.getHeight());

        conn.sendMousePosition((short) eventX, (short) eventY, (short) targetView.getWidth(), (short) targetView.getHeight());
    }

    @Override
    public void touchUpEvent(int eventX, int eventY, long time) {
        if (cancelled) {
            return;
        }

        if (actionIndex == 0) {
            // Cancel the timers
            cancelLongPressTimer();
            cancelTapDownTimer();

            // Raise the mouse buttons that we currently have down
            if (confirmedLongPress) {
                conn.sendMouseButtonUp(MouseButtonPacket.BUTTON_RIGHT);
            } else if (confirmedTap) {
                conn.sendMouseButtonUp(MouseButtonPacket.BUTTON_LEFT);
            } else {
                // If we get here, this means that the tap completed within the touch down
                // deadzone time. We'll need to send the touch down and up events now at the
                // original touch down position.
                tapConfirmed();
                try {
                    // FIXME: Sleeping on the main thread sucks
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
                conn.sendMouseButtonUp(MouseButtonPacket.BUTTON_LEFT);
            }
        }

        lastTouchLocationX = lastTouchUpX = eventX;
        lastTouchLocationY = lastTouchUpY = eventY;
        lastTouchUpTime = SystemClock.uptimeMillis();
    }

    private synchronized void startLongPressTimer() {
        longPressTimer = new Timer(true);
        longPressTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (AbsoluteTouchContext.this) {
                    // Check if someone cancelled us
                    if (longPressTimer == null) {
                        return;
                    }

                    // Uncancellable now
                    longPressTimer = null;

                    // This timer should have already expired, but cancel it just in case
                    cancelTapDownTimer();

                    // Switch from a left click to a right click after a long press
                    confirmedLongPress = true;
                    if (confirmedTap) {
                        conn.sendMouseButtonUp(MouseButtonPacket.BUTTON_LEFT);
                    }
                    conn.sendMouseButtonDown(MouseButtonPacket.BUTTON_RIGHT);
                }
            }
        }, LONG_PRESS_TIME_THRESHOLD);
    }

    private synchronized void cancelLongPressTimer() {
        if (longPressTimer != null) {
            longPressTimer.cancel();
            longPressTimer = null;
        }
    }

    private synchronized void startTapDownTimer() {
        tapDownTimer = new Timer(true);
        tapDownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (AbsoluteTouchContext.this) {
                    // Check if someone cancelled us
                    if (tapDownTimer == null) {
                        return;
                    }

                    // Uncancellable now
                    tapDownTimer = null;

                    // Start our tap
                    tapConfirmed();
                }
            }
        }, TOUCH_DOWN_DEAD_ZONE_TIME_THRESHOLD);
    }

    private synchronized void cancelTapDownTimer() {
        if (tapDownTimer != null) {
            tapDownTimer.cancel();
            tapDownTimer = null;
        }
    }

    private void tapConfirmed() {
        if (confirmedTap || confirmedLongPress) {
            return;
        }

        confirmedTap = true;
        cancelTapDownTimer();

        // Left button down at original position
        if (lastTouchDownTime - lastTouchUpTime > DOUBLE_TAP_TIME_THRESHOLD ||
                distanceExceeds(lastTouchDownX - lastTouchUpX, lastTouchDownY - lastTouchUpY, DOUBLE_TAP_DISTANCE_THRESHOLD)) {
            // Don't reposition for finger down events within the deadzone. This makes double-clicking easier.
            updatePosition(lastTouchDownX, lastTouchDownY);
        }
        conn.sendMouseButtonDown(MouseButtonPacket.BUTTON_LEFT);
    }

    @Override
    public boolean touchMoveEvent(int eventX, int eventY, long time) {
        if (cancelled) {
            return true;
        }

        if (actionIndex == 0) {
            if (distanceExceeds(eventX - lastTouchDownX, eventY - lastTouchDownY, LONG_PRESS_DISTANCE_THRESHOLD)) {
                // Moved too far since touch down. Cancel the long press timer.
                cancelLongPressTimer();
            }

            // Ignore motion within the deadzone period after touch down
            if (confirmedTap || distanceExceeds(eventX - lastTouchDownX, eventY - lastTouchDownY, TOUCH_DOWN_DEAD_ZONE_DISTANCE_THRESHOLD)) {
                tapConfirmed();
                updatePosition(eventX, eventY);
            }
        } else if (actionIndex == 1) {
            accumulatedScrollDelta += (eventY - lastTouchLocationY) / (float) SCROLL_SPEED_DIVISOR;
            if ((short) accumulatedScrollDelta != 0) {
                conn.sendMouseHighResScroll((short) accumulatedScrollDelta);
                accumulatedScrollDelta -= (short) accumulatedScrollDelta;
            }
        }

        lastTouchLocationX = eventX;
        lastTouchLocationY = eventY;

        return true;
    }

    @Override
    public void cancelTouch() {
        cancelled = true;

        // Cancel the timers
        cancelLongPressTimer();
        cancelTapDownTimer();

        // Raise the mouse buttons
        if (confirmedLongPress) {
            conn.sendMouseButtonUp(MouseButtonPacket.BUTTON_RIGHT);
        } else if (confirmedTap) {
            conn.sendMouseButtonUp(MouseButtonPacket.BUTTON_LEFT);
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setPointerCount(int pointerCount) {
        if (actionIndex == 0 && pointerCount > 1) {
            cancelTouch();
        }
    }
}
