package kr.co.aroad.subscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.support.annotation.NonNull;

import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.util.DrawViewFactory;
import kr.co.aroad.subscaleimagedrawview.util.DrawViewSetting;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

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
        previewDrawView = createDrawView();
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
        imageDrawView.removeDrawView(previewDrawView);
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
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.LINE);
        drawView.addPosition(this.begin);
        drawView.addPosition(this.end);
        drawView.setColor(Utillity.getColorString(DrawViewSetting.getInstance().getColor()));
        drawView.setThick(DrawViewSetting.getInstance().getLineWidth());

        return drawView;
    }

    /**
     * annotation을 view에 추가
     */
    private void injectAnnotation() {
        if (this.begin != this.end) {
            BaseDrawView annotation = createDrawView();
            imageDrawView.addDrawView(annotation);
        }
    }
}
