package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

public class EditPinViewRect extends BaseEditPinView {

    public EditPinViewRect(@NonNull ImageDrawView imageDrawView, @NonNull BaseDrawView drawView) {
        super(imageDrawView, drawView);
    }

    @Override
    public void setMovePin(PointF pos) {
        final RectF origin = getBoundaryBox();
        RectF modify = getBoundaryBox();
        for (Pin pin : pinList) {
            if (pin.state == true) {
                switch (pin.rectPos) {
                    case LEFT_TOP:
                        modify.left = pos.x;
                        modify.top = pos.y;
                        break;
                    case TOP:
                        modify.top = pos.y;
                        break;
                    case RIGHT_TOP:
                        modify.top = pos.y;
                        modify.right = pos.x;
                        break;
                    case RIGHT:
                        modify.right = pos.x;
                        break;
                    case RIGHT_BOTTOM:
                        modify.right = pos.x;
                        modify.bottom = pos.y;
                        break;
                    case BOTTOM:
                        modify.bottom = pos.y;
                        break;
                    case LEFT_BOTTOM:
                        modify.bottom = pos.y;
                        modify.left = pos.x;
                        break;
                    case LEFT:
                        modify.left = pos.x;
                        break;
                }
                break;
            }
        }

        final float MINIMUM_LENGTH = DrawViewFactory.getInstance().getMINIMUM_LENGTH() / imageDrawView.getScale();
        if (origin.width() > modify.width() && modify.width() < MINIMUM_LENGTH) {
            modify.left = origin.left;
            modify.right = origin.right;
        }
        if (origin.height() > modify.height() && modify.height() < MINIMUM_LENGTH) {
            modify.top = origin.top;
            modify.bottom = origin.bottom;
        }

        findPinByRectPos(Pin.RectPos.LEFT_TOP).point = new PointF(modify.left, modify.top);
        findPinByRectPos(Pin.RectPos.TOP).point = new PointF((modify.left + modify.right) / 2.0F, modify.top);
        findPinByRectPos(Pin.RectPos.RIGHT_TOP).point = new PointF(modify.right, modify.top);
        findPinByRectPos(Pin.RectPos.RIGHT).point = new PointF(modify.right, (modify.top + modify.bottom) / 2.0F);
        findPinByRectPos(Pin.RectPos.RIGHT_BOTTOM).point = new PointF(modify.right, modify.bottom);
        findPinByRectPos(Pin.RectPos.BOTTOM).point = new PointF((modify.left + modify.right) / 2.0F, modify.bottom);
        findPinByRectPos(Pin.RectPos.LEFT_BOTTOM).point = new PointF(modify.left, modify.bottom);
        findPinByRectPos(Pin.RectPos.LEFT).point = new PointF(modify.left, (modify.top + modify.bottom) / 2.0F);

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
