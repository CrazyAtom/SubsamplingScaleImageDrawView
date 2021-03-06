package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawToolFreeEraser extends DrawToolEraserBase {

    public DrawToolFreeEraser(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    @Override
    protected void touchMove(int x, int y) {
        final PointF sCoord = viewToSourceCoord(x, y);
        previewDrawView.addPosition(sCoord);
        previewDrawView.invalidate();

        final PointF sCoordFirst = previewDrawView.getPosition(0);
        final PointF vCoord1 = sourceToViewCoord(sCoordFirst.x, sCoordFirst.y);
        final PointF vCoord2 = sourceToViewCoord(sCoord.x, sCoord.y);
        if (Utillity.getDistance(vCoord1, vCoord2) > MINIMUM_LENGTH) {
            addSelected();
        }
    }

    /**
     * 터치 위치에 포함되는 drawView를 선택 맵에 추가
     */
    @Override
    protected void addSelected() {
        final PointF sCoordLast = previewDrawView.getPosition(previewDrawView.getPositionSize() - 1);
        for (String key : imageDrawView.getDrawViewMap().keySet()) {
            final BaseDrawView drawView = imageDrawView.getDrawViewMap().get(key);
            if (drawView.isPreview() == false && drawView.isContains(sCoordLast.x, sCoordLast.y) == true) {
                selectedDrawViewMap.put(key, drawView);
                drawView.setShowBoundaryBox(true);
                drawView.invalidate();
            }
        }
        onChangeSelectedCount();
    }

    @Override
    protected BaseDrawView.DrawViewType getPreviewType() {
        return BaseDrawView.DrawViewType.INK;
    }
}
