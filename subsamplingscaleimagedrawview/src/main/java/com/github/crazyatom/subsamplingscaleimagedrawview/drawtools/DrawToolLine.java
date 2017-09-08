package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
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

public class DrawToolLine extends BaseDrawTool {

    private PointF begin;
    private PointF end;

    public DrawToolLine(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    @Override
    protected void touchBegin(int x, int y) {
        this.begin = viewToSourceCoord(x, y);
        this.end = this.begin;
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
        this.end = viewToSourceCoord(x, y);
        if (Utillity.getDistance(begin, end) < DrawViewFactory.getInstance().getMINIMUM_LENGTH()) {
            PointF dir = Utillity.getUnitDirection(begin, end);
            end = Utillity.getOffset(begin, dir, DrawViewFactory.getInstance().getMINIMUM_LENGTH());
        }
        imageDrawView.removeDrawView(previewDrawView);
        injectAnnotation();
        imageDrawView.setEditedDrawView(true);
        checkContinueTool();
    }

    @Override
    public void enter() {

    }

    @Override
    protected BaseDrawView createDrawView(final boolean preview) {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.LINE);
        drawView.setPreview(preview);
        drawView.addPosition(this.begin);
        drawView.addPosition(this.end);
        drawView.setColor(Utillity.getColorString(DrawViewSetting.getInstance().getColor()));
        drawView.setThick(DrawViewSetting.getInstance().getLineWidth());

        return drawView;
    }

    /**
     * drawView를 view에 추가
     */
    private void injectAnnotation() {
        if (this.begin != this.end) {
            BaseDrawView drawView = createDrawView(false);
            imageDrawView.addDrawView(drawView);
            imageDrawView.addUndoItem(UndoManager.UndoState.REMOVE, drawView, drawView);
        }
    }
}
