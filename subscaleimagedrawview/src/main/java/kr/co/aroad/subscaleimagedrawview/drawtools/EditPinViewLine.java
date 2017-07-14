package kr.co.aroad.subscaleimagedrawview.drawtools;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

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
