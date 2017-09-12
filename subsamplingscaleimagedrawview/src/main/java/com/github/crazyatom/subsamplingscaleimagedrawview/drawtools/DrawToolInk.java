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

public class DrawToolInk extends BaseDrawTool {

    public DrawToolInk(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    @Override
    protected void touchBegin(int x, int y) {
        previewDrawView = createDrawView(true);
        previewDrawView.addPosition(viewToSourceCoord(x, y));
        imageDrawView.addDrawView(previewDrawView);
    }

    @Override
    protected void touchMove(int x, int y) {
        previewDrawView.addPosition(viewToSourceCoord(x, y));
        previewDrawView.invalidate();
    }

    @Override
    protected void touchEnd(int x, int y) {
        injectAnnotation();
        imageDrawView.removeDrawView(previewDrawView);
        imageDrawView.setEditedDrawView(true);
        checkContinueTool();
    }

    @Override
    public void enter() {

    }

    @Override
    protected BaseDrawView createDrawView(final boolean preview) {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.INK);
        drawView.setPreview(preview);
        drawView.setColor(Utillity.getColorString(DrawViewSetting.getInstance().getColor()));
        drawView.setThick(DrawViewSetting.getInstance().getLineWidth());

        return drawView;
    }

    /**
     * drawView를 view에 추가
     */
    private void injectAnnotation() {
        BaseDrawView drawView = createDrawView(false);
        for (int i = 0; i < previewDrawView.getPositionSize(); ++i) {
            drawView.addPosition(previewDrawView.getPosition(i));
        }
        imageDrawView.addDrawView(drawView);
        imageDrawView.addUndoItem(UndoManager.UndoState.ADD, drawView);
    }
}
