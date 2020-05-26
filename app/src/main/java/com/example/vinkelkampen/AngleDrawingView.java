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
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Random;

public class AngleDrawingView extends View {
    private static boolean initiated = false;
    private static boolean hideAngle = false;
    private static boolean touchDisabled = false;
    private static boolean oppositeAngle = false;

    /** Icon to show when hiding the angle */
    private Bitmap bitmapIcon = BitmapFactory.decodeResource(getResources(), R.drawable.vk_logo_no_bg);
    private static int ICON_WIDTH_HALF = 175;
    private static int ICON_HEIGHT_HALF = 220;

    private static int centerX;
    private static int centerY;

    /** Rectangles to use for drawing */
    private Rect mMeasuredRect = new Rect(0, 0, 1, 1);
    private Rect iconRect = new Rect(centerX-ICON_WIDTH_HALF, centerY-ICON_HEIGHT_HALF, centerX+ICON_WIDTH_HALF, centerY+ICON_HEIGHT_HALF);
    private RectF arcRect = new RectF(0, 0, 0, 0);

    /** Paint to draw circles */
    private Paint mCirclePaint;
    /** Paint to draw lines */
    private Paint mLinePaint;

    /** Default circle radius */
    private final static int DEFAULT_RADIUS = 40;
    /** Number of circles used */
    private static final int NBR_OF_CIRCLES = 3;

    /** Default values for random angle drawing */
    private static final int LINE_LENGTH = 300;
    private static final double MAX_ANGLE = 3.1;
    private static final double MIN_ANGLE = 0.035;
    Random rand = new Random();

    /** All available circles */
    private static HashMap<Integer, CircleArea> mCircles = new HashMap<>(NBR_OF_CIRCLES);
    private Integer activeCircle;

    /** Lines between circles **/
    private static HashMap<Integer, CircleLine> mCircleLines = new HashMap<>(NBR_OF_CIRCLES -1);

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

    /**
     * Sets a random angle using one random angle for each line.
     */
    public void setRandomAngle() {
        double randomAngle1 = MIN_ANGLE + (MAX_ANGLE - MIN_ANGLE) * rand.nextDouble();
        double randomAngle2 = MIN_ANGLE + (MAX_ANGLE - MIN_ANGLE) * rand.nextDouble();
        boolean changeAngle = rand.nextBoolean();

        hideAngle = true;

        mCircles.clear();
        mCircleLines.clear();
        CircleArea firstCircle = new CircleArea((int) (centerX+LINE_LENGTH*Math.cos(randomAngle1)),
                                              (int) (centerY-LINE_LENGTH*Math.sin(randomAngle1)),
                                              DEFAULT_RADIUS);
        mCircles.put(0, firstCircle);

        CircleArea middleCircle = new CircleArea(centerX, centerY, DEFAULT_RADIUS);
        mCircles.put(1, middleCircle);

        CircleArea thirdCircle = new CircleArea((int) (centerX+LINE_LENGTH*Math.cos(randomAngle2)),
                                   (int) (centerY+LINE_LENGTH*Math.sin(randomAngle2)),
                                   DEFAULT_RADIUS);
        mCircles.put(2, thirdCircle);

        mCircleLines.put(mCircleLines.size(), new CircleLine(middleCircle.centerX, middleCircle.centerY, firstCircle.centerX, firstCircle.centerY));
        mCircleLines.put(mCircleLines.size(), new CircleLine(middleCircle.centerX, middleCircle.centerY, thirdCircle.centerX, thirdCircle.centerY));

        if (changeAngle) {
            toggleOppositeAngle();
        }

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
        CircleArea firstCircle = new CircleArea(centerX+100, centerY-100, DEFAULT_RADIUS);
        mCircles.put(0, firstCircle);

        CircleArea middleCircle = new CircleArea(centerX, centerY, DEFAULT_RADIUS);
        mCircles.put(1, middleCircle);

        CircleArea thirdCircle = new CircleArea(centerX+100, centerY+100, DEFAULT_RADIUS);
        mCircles.put(2, thirdCircle);

        mCircleLines.put(mCircleLines.size(), new CircleLine(middleCircle.centerX, middleCircle.centerY, firstCircle.centerX, firstCircle.centerY));
        mCircleLines.put(mCircleLines.size(), new CircleLine(middleCircle.centerX, middleCircle.centerY, thirdCircle.centerX, thirdCircle.centerY));

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

            CircleArea centerCircle = mCircles.get(1);
            if (centerCircle != null) {
                drawArc(canv, centerCircle, mCircleLines.get(0), mCircleLines.get(1));
            }
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
                        if (touchedCircle != null) {
                            touchedCircle.centerX = xTouch;
                            touchedCircle.centerY = yTouch;
                        }
                        activeCircle = touchedCircleNbr;
                    }

                    invalidate();
                    handled = true;
                    break;


                case MotionEvent.ACTION_MOVE:
                    xTouch = (int) event.getX(0);
                    yTouch = (int) event.getY(0);

                    if (activeCircle != null) {
                        touchedCircle = mCircles.get(activeCircle);
                        if (touchedCircle != null) {
                            touchedCircle.centerX = xTouch;
                            touchedCircle.centerY = yTouch;


                            if (mCircleLines.size() == 2) {
                                CircleLine line;
                                switch (activeCircle) {
                                    case 0:
                                        line = mCircleLines.get(0);
                                        if (line != null) {
                                            line.endX = touchedCircle.centerX;
                                            line.endY = touchedCircle.centerY;
                                        }
                                        mCircleLines.put(0, line);
                                        break;
                                    case 1:
                                        line = mCircleLines.get(0);
                                        if (line != null) {
                                            line.startX = touchedCircle.centerX;
                                            line.startY = touchedCircle.centerY;
                                        }
                                        mCircleLines.put(0, line);
                                        line = mCircleLines.get(1);
                                        if (line != null) {
                                            line.startX = touchedCircle.centerX;
                                            line.startY = touchedCircle.centerY;
                                        }
                                        mCircleLines.put(1, line);
                                        break;
                                    case 2:
                                        line = mCircleLines.get(1);
                                        if (line != null) {
                                            line.endX = touchedCircle.centerX;
                                            line.endY = touchedCircle.centerY;
                                        }
                                        mCircleLines.put(1, line);
                                        break;
                                    default:
                                        break;
                                }
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
        iconRect.set(centerX-ICON_WIDTH_HALF, centerY-ICON_HEIGHT_HALF, centerX+ICON_WIDTH_HALF, centerY+ICON_HEIGHT_HALF);

        if (!initiated) {
            centerX = getMeasuredWidth()/2;
            centerY = getMeasuredHeight()/2;
            initCircles();
        }
        invalidate();
    }
}
