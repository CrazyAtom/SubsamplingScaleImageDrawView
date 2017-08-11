package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class DrawToolPhoto extends BaseDrawTool {

    private PointF base;

    public DrawToolPhoto(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    @Override
    protected void touchBegin(int x, int y) {

    }

    @Override
    protected void touchMove(int x, int y) {

    }

    @Override
    protected void touchEnd(int x, int y) {
        this.base = viewToSourceCoord(x, y);
        injectAnnotation();
        imageDrawView.setEditedDrawView(true);
        checkContinueTool();
    }

    @Override
    public void enter() {

    }

    @Override
    protected BaseDrawView createDrawView(final boolean preview) {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.PHOTO);
        drawView.setPreview(preview);
        drawView.addPosition(this.base);
        drawView.setColor(Utillity.getColorString(DrawViewSetting.getInstance().getColor()));

        return drawView;
    }

    /**
     * drawView를 view에 추가
     */
    private void injectAnnotation() {
        if (base != null) {
            BaseDrawView drawView = createDrawView(false);
            if (drawView != null) {
                imageDrawView.addDrawView(drawView);
                drawView.showContentsBox(imageDrawView.getContext());
            }
        }
    }
}
