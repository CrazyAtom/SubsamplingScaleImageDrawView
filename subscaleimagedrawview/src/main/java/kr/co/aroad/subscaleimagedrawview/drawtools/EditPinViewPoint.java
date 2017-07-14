package kr.co.aroad.subscaleimagedrawview.drawtools;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

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
}
