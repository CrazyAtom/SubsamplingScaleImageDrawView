package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.UndoManager;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class DrawToolCloud extends BaseDrawTool {

    private PointF begin;
    private PointF end;

    public DrawToolCloud(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    @Override
    protected void touchBegin(int x, int y) {
        begin = viewToSourceCoord(x, y);
        end = begin;
        previewDrawView = createDrawView(true);
        imageDrawView.addDrawView(previewDrawView);
    }

    @Override
    protected void touchMove(int x, int y) {
        previewDrawView.setPosition(1, viewToSourceCoord(x, y));
        previewDrawView.invalidate();
    }

    @Override
    protected void touchEnd(int x, int y) {
        end = viewToSourceCoord(x, y);
        imageDrawView.removeDrawView(previewDrawView);

        boolean valid = true;
        if (Math.abs(end.x - begin.x) == 0 || Math.abs(end.y - begin.y) == 0) {
            valid = false;
        }

        if (valid == true) {
            if (Utillity.getDistance(begin, end) < DrawViewFactory.getInstance().getMINIMUM_LENGTH()) {
                end = Utillity.getOffset(begin, new PointF(1, 1), DrawViewFactory.getInstance().getMINIMUM_LENGTH());
            }
            injectAnnotation();
            imageDrawView.setEditedDrawView(true);
        }

        checkContinueTool();
    }

    @Override
    public void enter() {
    }

    @Override
    protected BaseDrawView createDrawView(final boolean preview) {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.CLOUD);
        drawView.setPreview(preview);
        drawView.addPosition(begin);
        drawView.addPosition(end);
        drawView.setColor(Utillity.getColorString(DrawViewSetting.getInstance().getColor()));
        drawView.setThick(DrawViewSetting.getInstance().getLineWidth());

        return drawView;
    }

    /**
     * drawView를 view에 추가
     */
    private void injectAnnotation() {
        BaseDrawView drawView = createDrawView(false);
        imageDrawView.addDrawView(drawView);
        imageDrawView.addUndoItem(UndoManager.UndoState.ADD, drawView);
    }
}
