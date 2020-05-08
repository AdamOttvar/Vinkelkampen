package com.example.vinkelkampen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;

public class AngleDrawingView extends View {

    private int centerX;
    private int centerY;

    private static final String TAG = "CirclesDrawingView";

    private Rect mMeasuredRect = new Rect(0, 0, 1, 1);

    /** Stores data about one circle */
    private static class CircleArea {
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
    private static class CircleLine {
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

    /** Default circle radius */
    private final static int DEFAULT_RADIUS = 40;

    /** Number of circles used */
    private static final int CIRCLES_LIMIT = 3;

    /** All available circles */
    private HashMap<Integer, CircleArea> mCircles = new HashMap<Integer, CircleArea>(CIRCLES_LIMIT);
    private Integer activeCircle;

    /** Lines between circles **/
    private HashMap<Integer, CircleLine> mCircleLines = new HashMap<Integer, CircleLine>(CIRCLES_LIMIT-1);

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
     * Initiates the canvas with three circles connected with lines.
     */
    private void initCircles() {
        mCircles.clear();
        mCircleLines.clear();
        CircleArea newCircle = new CircleArea(this.centerX+100, this.centerY-100, DEFAULT_RADIUS);
        mCircles.put(0, newCircle);

        newCircle = new CircleArea(this.centerX, this.centerY, DEFAULT_RADIUS);
        mCircles.put(1, newCircle);

        newCircle = new CircleArea(this.centerX+100, this.centerY+100, DEFAULT_RADIUS);
        mCircles.put(2, newCircle);

        mCircleLines.put(mCircleLines.size(), new CircleLine(this.centerX+100, this.centerY-100, this.centerX, this.centerY));
        mCircleLines.put(mCircleLines.size(), new CircleLine(this.centerX, this.centerY, this.centerX+100, this.centerY+100));
    }

    @Override
    public void onDraw(final Canvas canv) {
        // background bitmap to cover all area
        //canv.drawBitmap(mBitmap, null, mMeasuredRect, null);

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
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        boolean handled = false;

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
                                line.startX = touchedCircle.centerX;
                                line.startY = touchedCircle.centerY;
                                mCircleLines.put(0, line);
                                break;
                            case 1:
                                Log.w(TAG, "Updating line 1 and 2");
                                line = mCircleLines.get(0);
                                line.endX = touchedCircle.centerX;
                                line.endY = touchedCircle.centerY;
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
        //CircleArea touched = null;
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
     * The circles will be initated more frequent than necessary.
     */
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mMeasuredRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
        this.centerX = getMeasuredWidth()/2;
        this.centerY = getMeasuredHeight()/2;
        // TODO: Smarter initiation of the circles. They are "reset" more frequent than necessary now
        initCircles();
        invalidate();
    }
}
