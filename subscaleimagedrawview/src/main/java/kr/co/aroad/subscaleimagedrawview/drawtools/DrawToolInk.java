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

public class DrawToolInk extends BaseDrawTool {

    public DrawToolInk(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    @Override
    protected void touchBegin(int x, int y) {
        previewDrawView = createDrawView();
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
        boolean valid = true;
        for (int i = 0; i < previewDrawView.getPositionSize(); i++) {
            PointF vc = sourceToViewCoord(previewDrawView.getPosition(i).x, previewDrawView.getPosition(i).y);
            if (isInsideView(vc.x, vc.y, vc.x, vc.y) == false) {
                valid = false;
                break;
            }
        }

        if (valid == true) {
            injectAnnotation();
        }
        
        imageDrawView.removeDrawView(previewDrawView);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    protected BaseDrawView createDrawView() {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.INK);
        drawView.setColor(Utillity.getColorString(DrawViewSetting.getInstance().getColor()));
        drawView.setThick(DrawViewSetting.getInstance().getLineWidth());

        return drawView;
    }

    /**
     * annotation을 view에 추가
     */
    private void injectAnnotation() {
        BaseDrawView annotation = createDrawView();
        for (int i = 0; i < previewDrawView.getPositionSize(); ++i) {
            annotation.addPosition(previewDrawView.getPosition(i));
        }
        imageDrawView.addDrawView(annotation);
    }
}
