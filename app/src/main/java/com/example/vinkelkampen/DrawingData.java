package com.example.vinkelkampen;

import android.os.Parcelable;

import java.util.HashMap;

public class DrawingData{
    HashMap<Integer, AngleDrawingView.CircleArea> mCircles;
    HashMap<Integer, AngleDrawingView.CircleLine> mCircleLines;

    DrawingData(HashMap<Integer, AngleDrawingView.CircleArea> circles, HashMap<Integer, AngleDrawingView.CircleLine> circleLines) {
        this.mCircles = circles;
        this.mCircleLines = circleLines;
    }
}
