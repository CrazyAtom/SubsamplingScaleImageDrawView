package kr.co.aroad.subscaleimagedrawview.annotationtools;

import android.content.Context;
import android.graphics.PointF;

import kr.co.aroad.subscaleimagedrawview.annotations.BaseAnnotation;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 13..
 */

public class AnnotationToolTransform extends BaseAnnotationTool {

    private BaseAnnotation selectedAnnotation;

    public AnnotationToolTransform(Context context, ImageDrawView imageDrawView) {
        super(context, imageDrawView);
    }

    @Override
    public BaseAnnotation singleTapUp(int x, int y) {
        this.selectedAnnotation = getSelectedAnnotation(new PointF(x, y));
        if (this.selectedAnnotation == null) {
            return null;
        }

        if (this.selectedAnnotation.getType() == BaseAnnotation.AnnotationType.PHOTO
                || this.selectedAnnotation.getType() == BaseAnnotation.AnnotationType.TEXT) {
            this.selectedAnnotation.showContentsBox(mContext);
            exitTransformState();
        } else {
            onTouchBegin(x, y);
        }

        return this.selectedAnnotation;
    }

    @Override
    public BaseAnnotation longPress(int x, int y) {
        this.selectedAnnotation = getSelectedAnnotation(new PointF(x, y));
        if (this.selectedAnnotation == null) {
            return null;
        }

        if (this.selectedAnnotation.getType() == BaseAnnotation.AnnotationType.PHOTO
                || this.selectedAnnotation.getType() == BaseAnnotation.AnnotationType.TEXT) {
            onTouchBegin(x, y);
        }

        return this.selectedAnnotation;
    }

    @Override
    protected void touchBegin(int x, int y) {

    }

    @Override
    protected void touchMove(int x, int y) {

    }

    @Override
    protected void touchEnd(int x, int y) {

    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    /**
     * 편집 상태의 annotation 편집 상태 해제
     */
    private void exitTransformState() {

    }
}
