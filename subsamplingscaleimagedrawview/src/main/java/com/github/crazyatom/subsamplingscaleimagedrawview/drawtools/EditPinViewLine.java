package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-06-22.
 */

public class EditPinViewLine extends BaseEditPinView {

    public EditPinViewLine(@NonNull ImageDrawView imageDrawView, @NonNull BaseDrawView drawView) {
        super(imageDrawView, drawView);
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
        for (int i = 0; i < drawView.getPositionSize(); i++) {
            pinList.add(new Pin(drawView.getPosition(i)));
        }
    }

    @Override
    public RectF getBoundaryBox() {
        return drawView.getBoundaryBox();
    }
}
