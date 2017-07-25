package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-06-23.
 */

public class EditPinViewPoint extends BaseEditPinView {

    public EditPinViewPoint(@NonNull ImageDrawView imageDrawView, @NonNull BaseDrawView drawView) {
        super(imageDrawView, drawView);
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
        return drawView.getBoundaryBox();
    }

    @Override
    protected int getBoundraryColor() {
        return Color.argb(70, 0, 0, 0);
    }
}
