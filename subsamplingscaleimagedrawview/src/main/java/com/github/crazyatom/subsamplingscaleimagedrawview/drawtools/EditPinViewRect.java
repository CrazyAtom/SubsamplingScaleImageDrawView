package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

public class EditPinViewRect extends BaseEditPinView {

    public EditPinViewRect(@NonNull ImageDrawView imageDrawView, @NonNull BaseDrawView drawView) {
        super(imageDrawView, drawView);
    }

    @Override
    public void setMovePin(PointF pos) {
        RectF boundaryBox = getBoundaryBox();
        for (Pin pin : pinList) {
            if (pin.state == true) {
                switch (pin.rectPos) {
                    case LEFT_TOP:
                        boundaryBox.left = pos.x;
                        boundaryBox.top = pos.y;
                        break;
                    case TOP:
                        boundaryBox.top = pos.y;
                        break;
                    case RIGHT_TOP:
                        boundaryBox.top = pos.y;
                        boundaryBox.right = pos.x;
                        break;
                    case RIGHT:
                        boundaryBox.right = pos.x;
                        break;
                    case RIGHT_BOTTOM:
                        boundaryBox.right = pos.x;
                        boundaryBox.bottom = pos.y;
                        break;
                    case BOTTOM:
                        boundaryBox.bottom = pos.y;
                        break;
                    case LEFT_BOTTOM:
                        boundaryBox.bottom = pos.y;
                        boundaryBox.left = pos.x;
                        break;
                    case LEFT:
                        boundaryBox.left = pos.x;
                        break;
                }
                break;
            }
        }

        if (boundaryBox.width() <= BaseDrawView.MINIMUM_LENGTH || boundaryBox.height() <= BaseDrawView.MINIMUM_LENGTH) {
            return;
        }

        findPinByRectPos(Pin.RectPos.LEFT_TOP).point = new PointF(boundaryBox.left, boundaryBox.top);
        findPinByRectPos(Pin.RectPos.TOP).point = new PointF((boundaryBox.left + boundaryBox.right) / 2.0F, boundaryBox.top);
        findPinByRectPos(Pin.RectPos.RIGHT_TOP).point = new PointF(boundaryBox.right, boundaryBox.top);
        findPinByRectPos(Pin.RectPos.RIGHT).point = new PointF(boundaryBox.right, (boundaryBox.top + boundaryBox.bottom) / 2.0F);
        findPinByRectPos(Pin.RectPos.RIGHT_BOTTOM).point = new PointF(boundaryBox.right, boundaryBox.bottom);
        findPinByRectPos(Pin.RectPos.BOTTOM).point = new PointF((boundaryBox.left + boundaryBox.right) / 2.0F, boundaryBox.bottom);
        findPinByRectPos(Pin.RectPos.LEFT_BOTTOM).point = new PointF(boundaryBox.left, boundaryBox.bottom);
        findPinByRectPos(Pin.RectPos.LEFT).point = new PointF(boundaryBox.left, (boundaryBox.top + boundaryBox.bottom) / 2.0F);

        invalidate();
    }

    @Override
    public void initPinList() {
        RectF rect = drawView.getBoundaryBox();
        pinList.clear();
        pinList.add(new Pin(new PointF(rect.left, rect.top), Pin.RectPos.LEFT_TOP));
        pinList.add(new Pin(new PointF((rect.left + rect.right) / 2.0F, rect.top), Pin.RectPos.TOP));
        pinList.add(new Pin(new PointF(rect.right, rect.top), Pin.RectPos.RIGHT_TOP));
        pinList.add(new Pin(new PointF(rect.right, (rect.top + rect.bottom) / 2.0F), Pin.RectPos.RIGHT));
        pinList.add(new Pin(new PointF(rect.right, rect.bottom), Pin.RectPos.RIGHT_BOTTOM));
        pinList.add(new Pin(new PointF((rect.left + rect.right) / 2.0F, rect.bottom), Pin.RectPos.BOTTOM));
        pinList.add(new Pin(new PointF(rect.left, rect.bottom), Pin.RectPos.LEFT_BOTTOM));
        pinList.add(new Pin(new PointF(rect.left, (rect.top + rect.bottom) / 2.0F), Pin.RectPos.LEFT));
    }

    /**
     * Pin위치를 이용한 Pin 검색
     * @param rectPos
     * @return
     */
    private Pin findPinByRectPos(Pin.RectPos rectPos) {
        for (Pin pin : pinList) {
            if (pin.rectPos == rectPos) {
                return pin;
            }
        }
        return null;
    }
}
