package kr.co.aroad.subscaleimagedrawview.annotationtools;

import android.content.Context;
import android.graphics.PointF;

import kr.co.aroad.subscaleimagedrawview.annotations.AnnotationEllipse;
import kr.co.aroad.subscaleimagedrawview.annotations.AnnotationInk;
import kr.co.aroad.subscaleimagedrawview.annotations.BaseAnnotation;
import kr.co.aroad.subscaleimagedrawview.util.AnnotationSetting;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class AnnotationToolEllipse extends BaseAnnotationTool {
    private AnnotationEllipse mAnnotationEllipse = null;
    private PointF mBegin;
    private PointF mEnd;

    public AnnotationToolEllipse(Context context, ImageDrawView imageDrawView) {
        super(context, imageDrawView);
    }

    @Override
    protected void touchBegin(int x, int y) {
        mBegin = viewToSourceCoord(x, y);
        mEnd = mBegin;
        mAnnotationEllipse = createAnnotation();
        mImageDrawView.addAnnotation(mAnnotationEllipse);
    }

    @Override
    protected void touchMove(int x, int y) {
        mAnnotationEllipse.setPosition(1, viewToSourceCoord(x, y));
        mAnnotationEllipse.invalidate();
    }

    @Override
    protected void touchEnd(int x, int y) {
        mEnd = viewToSourceCoord(x, y);
        mImageDrawView.removeAnnotation(mAnnotationEllipse);
        injectAnnotation();
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
        AnnotationEllipse annotation = createAnnotation();
        mImageDrawView.addAnnotation(annotation);
    }

    /**
     * annotation 생성
     * @return
     */
    private AnnotationEllipse createAnnotation() {
        AnnotationEllipse  annotation = new AnnotationEllipse(mContext, mImageDrawView);
        annotation.addPosition(mBegin);
        annotation.addPosition(mEnd);
        annotation.setColor(Utillity.getColorString(AnnotationSetting.getInstance().getColor()));
        annotation.setThick(AnnotationSetting.getInstance().getLineWidth());

        return annotation;
    }
}
