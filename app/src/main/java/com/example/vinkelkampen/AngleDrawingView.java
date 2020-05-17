package com.example.vinkelkampen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class AngleDrawingView extends View {
    private static boolean initiated = false;
    private static boolean hideAngle = false;
    private static boolean touchDisabled = false;
    private static boolean oppositeAngle = false;

    private static int ICON_HALF_SIZE = 200;

    private static int centerX;
    private static int centerY;

    private static final String TAG = "CirclesDrawingView";

    private Rect mMeasuredRect = new Rect(0, 0, 1, 1);
    private Rect iconRect = new Rect(centerX-ICON_HALF_SIZE, centerY-ICON_HALF_SIZE, centerX+ICON_HALF_SIZE, centerY+ICON_HALF_SIZE);
    private RectF arcRect = new RectF(0, 0, 0, 0);

    /** Stores data about one circle */
    static class CircleArea {
        int radius;
        int centerX;
        int centerY;

        CircleArea(int centerX, int centerY, int radius) {
            this.radius = radius;
            this.centerX = centerX;
            this.centerY = centerY;
        }

    }

    /** Stores data about lines connecting circles **/
    static class CircleLine {
        int startX;
        int startY;
        int endX;
        int endY;

        CircleLine(int startX, int startY, int endX, int endY) {
            this.startX = startX;
            this.startY = startY;
            this.endX = endX;
            this.endY = endY;
        }

    }

    /** Paint to draw circles */
    private Paint mCirclePaint;
    /** Paint to draw lines */
    private Paint mLinePaint;

    private Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground);

    /** Default circle radius */
    private final static int DEFAULT_RADIUS = 40;

    /** Number of circles used */
    private static final int CIRCLES_LIMIT = 3;

    /** All available circles */
    private static HashMap<Integer, CircleArea> mCircles = new HashMap<Integer, CircleArea>(CIRCLES_LIMIT);
    private Integer activeCircle;

    /** Lines between circles **/
    private static HashMap<Integer, CircleLine> mCircleLines = new HashMap<Integer, CircleLine>(CIRCLES_LIMIT-1);

    /** Constructor **/
    public AngleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.GRAY);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mLinePaint = new Paint();
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(10);
        mLinePaint.setStyle(Paint.Style.STROKE);

    }

    /**
     * Get the current angle between the lines, if the lines exists
     * @return angle
     */
    public double getCurrentAngle() {
        double angle = 0;
        if (mCircleLines.size() == 2) {
            angle = angleBetween2Lines(mCircleLines.get(0),mCircleLines.get(1));
        }
        return angle;
    }

    /**
     * Function for calculating the angle of a line
     * @param line line that the angle should be returned for
     * @return The angle in degrees
     */
    private static double getAngleLine(CircleLine line, boolean invertedY) {
        double angle;
        if (invertedY) {
            angle = Math.atan2(line.startY - line.endY, line.endX - line.startX);
        }
        else {
            angle = Math.atan2(line.endY-line.startY, line.endX - line.startX);
        }
        return Math.toDegrees(angle);
    }

    /**
     * Calculate the smallest angle between two lines.
     * The y-positions are flipped since the y-axis is flipped in the view.
     * @param line1 first line
     * @param line2 second line
     * @return the angle in radians
     */
    private static double angleBetween2Lines(CircleLine line1, CircleLine line2)
    {
        double angle1 = getAngleLine(line1, true);
        double angle2 = getAngleLine(line2, true);

        return Math.abs(angle1-angle2) < 180 ? Math.abs(angle1-angle2) : 360 - Math.abs(angle1-angle2);

    }

    public void hideAngle(boolean hide) {
        hideAngle = hide;
        invalidate();
    }

    public void toggleOppositeAngle() {
        oppositeAngle = !oppositeAngle;
        invalidate();
    }

    public void touchDisabled(boolean disabled) {
        touchDisabled = disabled;
        if (touchDisabled) {
            mCirclePaint.setAlpha(0);
        }
        else {
            mCirclePaint.setAlpha(1);
        }
        invalidate();
    }

    /**
     * Initiates the canvas with three circles connected with lines.
     */
    private void initCircles() {
        mCircles.clear();
        mCircleLines.clear();
        CircleArea newCircle = new CircleArea(centerX+100, centerY-100, DEFAULT_RADIUS);
        mCircles.put(0, newCircle);

        newCircle = new CircleArea(centerX, centerY, DEFAULT_RADIUS);
        mCircles.put(1, newCircle);

        newCircle = new CircleArea(centerX+100, centerY+100, DEFAULT_RADIUS);
        mCircles.put(2, newCircle);

        mCircleLines.put(mCircleLines.size(), new CircleLine(centerX, centerY, centerX+100, centerY-100));
        mCircleLines.put(mCircleLines.size(), new CircleLine(centerX, centerY, centerX+100, centerY+100));

        initiated = true;
    }

    private void drawArc(final Canvas canv, CircleArea center, CircleLine line1, CircleLine line2) {
        arcRect.set(center.centerX-DEFAULT_RADIUS*2, center.centerY-DEFAULT_RADIUS*2,
                   center.centerX+DEFAULT_RADIUS*2, center.centerY+DEFAULT_RADIUS*2);

        float startAngle = (float) getAngleLine(line1, false);
        float endAngle = (float) getAngleLine(line2, false);
        float betweenAngle = (float) getCurrentAngle();
        float sweepAngle = 0 < endAngle-startAngle && endAngle-startAngle < 180 ?  betweenAngle : -betweenAngle;

        if (startAngle > 0 && endAngle < 0 && Math.abs(endAngle-startAngle) > 180) {
            sweepAngle = betweenAngle;
        }

        if (oppositeAngle) {
            sweepAngle = sweepAngle < 0 ? sweepAngle + 360 : sweepAngle-360;
        }
        canv.drawArc(arcRect, startAngle, sweepAngle, false, mLinePaint);
    }

    @Override
    public void onDraw(final Canvas canv) {
        // background bitmap to cover all area
        if (hideAngle && bitmapIcon != null) {
            canv.drawColor(Color.DKGRAY);
            canv.drawBitmap(bitmapIcon, null, iconRect, null);
        }
        else {
            for (Integer key : mCircles.keySet()) {
                CircleArea circle = mCircles.get(key);
                if (circle != null) {
                    canv.drawCircle(circle.centerX, circle.centerY, circle.radius, mCirclePaint);
                }
            }

            for (Integer key : mCircleLines.keySet()) {
                CircleLine line = mCircleLines.get(key);
                if (line != null) {
                    canv.drawLine(line.startX, line.startY, line.endX, line.endY, mLinePaint);
                }
            }

            drawArc(canv, mCircles.get(1), mCircleLines.get(0), mCircleLines.get(1));
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;
        if (touchDisabled) {
            handled = true;
        }
        else {
            Integer touchedCircleNbr;
            CircleArea touchedCircle;
            int xTouch;
            int yTouch;

            // get touch event coordinates and make transparent circle from it
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // it's the first pointer, so clear all existing pointers data
                    activeCircle = null;

                    xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);

                    // check if we've touched inside some circle
                    touchedCircleNbr = getTouchedCircle(xTouch, yTouch);
                    if (touchedCircleNbr != null) {
                        touchedCircle = mCircles.get(touchedCircleNbr);
                        touchedCircle.centerX = xTouch;
                        touchedCircle.centerY = yTouch;
                        activeCircle = touchedCircleNbr;
                    }

                    invalidate();
                    handled = true;
                    break;


                case MotionEvent.ACTION_MOVE:
                    Log.w(TAG, "Move");
                    xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);

                    if (activeCircle != null) {
                        touchedCircle = mCircles.get(activeCircle);
                        touchedCircle.centerX = xTouch;
                        touchedCircle.centerY = yTouch;

                        if (mCircleLines.size() == 2) {
                            CircleLine line;
                            switch (activeCircle) {
                                case 0:
                                    Log.w(TAG, "Updating line 1");
                                    line = mCircleLines.get(0);
                                    line.endX = touchedCircle.centerX;
                                    line.endY = touchedCircle.centerY;
                                    mCircleLines.put(0, line);
                                    break;
                                case 1:
                                    Log.w(TAG, "Updating line 1 and 2");
                                    line = mCircleLines.get(0);
                                    line.startX = touchedCircle.centerX;
                                    line.startY = touchedCircle.centerY;
                                    mCircleLines.put(0, line);
                                    line = mCircleLines.get(1);
                                    line.startX = touchedCircle.centerX;
                                    line.startY = touchedCircle.centerY;
                                    mCircleLines.put(1, line);
                                    break;
                                case 2:
                                    Log.w(TAG, "Updating line 2");
                                    line = mCircleLines.get(1);
                                    line.endX = touchedCircle.centerX;
                                    line.endY = touchedCircle.centerY;
                                    mCircleLines.put(1, line);
                                    break;
                                default:
                                    break;
                            }
                        }

                    }

                    invalidate();
                    handled = true;
                    break;

                case MotionEvent.ACTION_UP:
                    invalidate();
                    handled = true;
                    break;

                case MotionEvent.ACTION_CANCEL:
                    handled = true;
                    break;

                default:
                    // do nothing
                    break;
            }
        }
        return super.onTouchEvent(event) || handled;
    }

    /**
     * Determines touched circle
     *
     * @param xTouch int x touch coordinate
     * @param yTouch int y touch coordinate
     *
     * @return {@link CircleArea} touched circle or null if no circle has been touched
     */
    private Integer getTouchedCircle(final int xTouch, final int yTouch) {
        Integer touchedNbr = null;

        for (Integer key : mCircles.keySet()) {
            CircleArea circle = mCircles.get(key);
            if (circle != null &&
                    (circle.centerX - xTouch) * (circle.centerX - xTouch) + (circle.centerY - yTouch) * (circle.centerY - yTouch) <= circle.radius * circle.radius) {
                touchedNbr = key;
                break;
            }
        }

        return touchedNbr;
    }

    /**
     * Calculates the center point of the canvas and initiates circles.
     * The circles will be initiated more frequent than necessary.
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        iconRect.set(centerX-ICON_HALF_SIZE, centerY-ICON_HALF_SIZE, centerX+ICON_HALF_SIZE, centerY+ICON_HALF_SIZE);

        if (!initiated) {
            centerX = getMeasuredWidth()/2;
            centerY = getMeasuredHeight()/2;
            initCircles();
        }
        invalidate();
    }
}
