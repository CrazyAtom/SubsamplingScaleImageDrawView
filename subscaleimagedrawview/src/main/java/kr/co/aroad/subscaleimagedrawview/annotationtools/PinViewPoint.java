package kr.co.aroad.subscaleimagedrawview.annotationtools;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;

import kr.co.aroad.subscaleimagedrawview.annotations.BaseAnnotation;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-06-23.
 */

public class PinViewPoint extends PinView {

    public PinViewPoint(Context context, ImageDrawView imageDrawView, BaseAnnotation annotation) {
        super(context, imageDrawView, annotation);
    }

    @Override
    public void setMovePin(PointF pos) {
        invalidate();
    }

    @Override
    public void initPinList() {
        pinList.clear();
    }

    @Override
    public RectF getBoundaryBox() {
        return annotation.getBbox();
    }
}
