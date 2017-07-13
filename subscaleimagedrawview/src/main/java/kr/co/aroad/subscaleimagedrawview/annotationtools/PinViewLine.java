package kr.co.aroad.subscaleimagedrawview.annotationtools;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;

import kr.co.aroad.subscaleimagedrawview.annotations.BaseAnnotation;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-06-22.
 */

public class PinViewLine extends PinView {

    public PinViewLine(Context context, ImageDrawView imageDrawView, BaseAnnotation annotation) {
        super(context, imageDrawView, annotation);
    }

    @Override
    public void setMovePin(PointF pos) {
        for (Pin pin : pinList) {
            if (pin.state == true) {
                pin.point = pos;
                break;
            }
        }
        invalidate();
    }

    @Override
    public void initPinList() {
        pinList.clear();
        for (int i = 0; i < annotation.getPositionSize(); i++) {
            pinList.add(new Pin(annotation.getPosition(i)));
        }
    }

    @Override
    public RectF getBoundaryBox() {
        return annotation.getBbox();
    }
}
