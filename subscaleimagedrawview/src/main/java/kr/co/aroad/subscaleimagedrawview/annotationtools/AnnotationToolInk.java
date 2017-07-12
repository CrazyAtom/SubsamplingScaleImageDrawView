package kr.co.aroad.subscaleimagedrawview.annotationtools;

import android.content.Context;

import kr.co.aroad.subscaleimagedrawview.annotations.AnnotationInk;
import kr.co.aroad.subscaleimagedrawview.annotations.BaseAnnotation;
import kr.co.aroad.subscaleimagedrawview.util.AnnotationSetting;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class AnnotationToolInk extends BaseAnnotationTool {
    private AnnotationInk mAnnotationInk = null;


    public AnnotationToolInk(Context context, ImageDrawView imageDrawView) {
        super(context, imageDrawView);
    }

    @Override
    protected void touchBegin(int x, int y) {
        mAnnotationInk = createAnnotation();
        mAnnotationInk.addPosition(viewToSourceCoord(x, y));
        mImageDrawView.addAnnotation(mAnnotationInk);
    }

    @Override
    protected void touchMove(int x, int y) {
        mAnnotationInk.addPosition(viewToSourceCoord(x, y));
        mAnnotationInk.invalidate();
    }

    @Override
    protected void touchEnd(int x, int y) {
        injectAnnotation();
        mImageDrawView.removeAnnotation(mAnnotationInk);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    /**
     * annotation을 view에 추가
     */
    private void injectAnnotation() {
        AnnotationInk annotation = createAnnotation();
        for (int i = 0; i < mAnnotationInk.getPositionSize(); ++i) {
            annotation.addPosition(mAnnotationInk.getPosition(i));
        }
        mImageDrawView.addAnnotation(annotation);
    }

    /**
     * annotation 생성
     * @return
     */
    private AnnotationInk createAnnotation() {
        AnnotationInk annotation = new AnnotationInk(mContext, BaseAnnotation.AnnotationType.INK, mImageDrawView);
        annotation.setColor(Utillity.getColorString(AnnotationSetting.getInstance().getColor()));
        annotation.setThick(AnnotationSetting.getInstance().getLineWidth());

        return annotation;
    }
}
