package com.github.crazyatom.subscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subscaleimagedrawview.views.ImageDrawView;

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
        checkContinueTool();
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    protected BaseDrawView createDrawView() {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.PHOTO);
        drawView.addPosition(this.base);
        drawView.setColor(Utillity.getColorString(DrawViewSetting.getInstance().getColor()));

        return drawView;
    }

    /**
     * drawView를 view에 추가
     */
    private void injectAnnotation() {
        if (base != null) {
            BaseDrawView drawView = createDrawView();
            if (drawView != null) {
                imageDrawView.addDrawView(drawView);
                drawView.showContentsBox(imageDrawView.getContext());
            }
        }
    }
}
