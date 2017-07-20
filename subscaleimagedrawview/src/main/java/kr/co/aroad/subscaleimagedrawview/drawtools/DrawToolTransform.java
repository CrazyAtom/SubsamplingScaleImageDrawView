package kr.co.aroad.subscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import kr.co.aroad.subscaleimagedrawview.listener.DrawViewTransformListener;
import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 13..
 */

public class DrawToolTransform extends BaseDrawTool implements View.OnTouchListener, DrawViewTransformListener {

    private BaseDrawView selectedDrawView;
    private PointF beginPoint;
    private BaseEditPinView editPinView;

    public DrawToolTransform(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    public void setSelectedDrawView(@NonNull BaseDrawView selectedDrawView) {
        this.selectedDrawView = selectedDrawView;
    }

    @Override
    public BaseDrawView singleTapUp(int x, int y) {
        if (this.selectedDrawView == null) {
            return null;
        }

        if (this.selectedDrawView.getType() == BaseDrawView.DrawViewType.PHOTO
                || this.selectedDrawView.getType() == BaseDrawView.DrawViewType.TEXT) {
            this.selectedDrawView.showContentsBox(imageDrawView.getContext());
            exitTransformState();
        } else {
            createEditPinView();
        }

        return this.selectedDrawView;
    }

    @Override
    public BaseDrawView longPress(int x, int y) {
        if (this.selectedDrawView == null) {
            return null;
        }

        if (this.selectedDrawView.getType() == BaseDrawView.DrawViewType.PHOTO
                || this.selectedDrawView.getType() == BaseDrawView.DrawViewType.TEXT) {
            createEditPinView();
        }

        return this.selectedDrawView;
    }

    /**
     * 편집을 위한 EditPinView 생성
     */
    private void createEditPinView() {
        if (this.selectedDrawView == null) {
            return;
        }

        if (this.editPinView == null) {
            this.editPinView = newPinView(this.selectedDrawView);
            this.editPinView.setClickable(true);
            this.editPinView.setOnTouchListener(this);

            imageDrawView.addView(editPinView);
            this.editPinView.layout(0, 0, imageDrawView.getWidth(), imageDrawView.getHeight());
            this.editPinView.invalidate();
        }
    }

    @Override
    protected void touchBegin(int x, int y) {
        PointF sCoord = viewToSourceCoord(x, y);
        if (this.editPinView != null) {
            this.editPinView.setPinPressed(sCoord.x, sCoord.y);
            if (this.editPinView.isPinPressed() == true) {
                this.editPinView.invalidate();
            } else if (this.editPinView.isContains(sCoord.x, sCoord.y) == true) {
                this.beginPoint = viewToSourceCoord(x, y);
                setBeginEdited(true);
            } else {
                exitTransformState();
            }
        }
    }

    @Override
    protected void touchMove(int x, int y) {
        if (this.selectedDrawView == null || this.editPinView == null) {
            return;
        }

        PointF sCoord = viewToSourceCoord(x, y);
        if (this.editPinView.isPinPressed() == true) {
            if (isInsideView(x, y, x, y) == true) {
                this.editPinView.setMovePin(new PointF(sCoord.x, sCoord.y));
                this.editPinView.invalidate();
                if (selectedDrawView.getType() == BaseDrawView.DrawViewType.DIMENSION_REF
                        || selectedDrawView.getType() == BaseDrawView.DrawViewType.DIMENSION
                        || selectedDrawView.getType() == BaseDrawView.DrawViewType.LINE) {
                    selectedDrawView.update(this.editPinView.getPinPoints());
                } else {
                    update(selectedDrawView, this.editPinView.getBoundaryBox());
                }
            }
        } else {
            RectF sRect = this.selectedDrawView.getBoundaryBox();
            final float sDistX = sCoord.x - beginPoint.x;
            final float sDistY = sCoord.y - beginPoint.y;
            beginPoint = sCoord;

            RectF vRect = new RectF();
            imageDrawView.sourceToViewRect(sRect, vRect);
            final float vDistX = sDistX * imageDrawView.getScale();
            final float vDistY = sDistY * imageDrawView.getScale();
            if (isInsideView(vRect.left + vDistX, vRect.top + vDistY, vRect.right + vDistX, vRect.bottom + vDistY) == true) {
                update(this.selectedDrawView,
                        new RectF(sRect.left + sDistX, sRect.top + sDistY, sRect.right + sDistX, sRect.bottom + sDistY));
                if (this.editPinView != null) {
                    this.editPinView.initPinList();
                    this.editPinView.invalidate();
                }
            }
        }
    }

    @Override
    protected void touchEnd(int x, int y) {
        exitTransformState();
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        if (this.editPinView != null) {
            imageDrawView.removeView(this.editPinView);
            imageDrawView.invalidate();
        }
    }

    @Override
    protected BaseDrawView createDrawView() {
        return null;
    }

    /**
     * drawView type에 따른 pinView생성
     * @param annotation
     * @return
     */
    private BaseEditPinView newPinView(BaseDrawView annotation) {
        switch (annotation.getType()) {
            case DIMENSION_REF:
            case DIMENSION:
            case LINE:
                return new EditPinViewLine(imageDrawView, annotation);
            case PHOTO:
            case TEXT:
                return new EditPinViewPoint(imageDrawView, annotation);
            default:
                return new EditPinViewRect(imageDrawView, annotation);
        }
    }

    /**
     * boundary box를 이용한 update
     * @param annotation
     * @param newBoundaryBox
     */
    private void update(BaseDrawView annotation, RectF newBoundaryBox) {
        if (this.selectedDrawView != null) {
            this.selectedDrawView.update(newBoundaryBox);
        }
    }

    /**
     * 편집 상태의 drawView 편집 상태 해제
     */
    private void exitTransformState() {
        if (this.editPinView != null) {
            imageDrawView.removeView(this.editPinView);
            imageDrawView.invalidate();
        }

        imageDrawView.changeTool(DrawToolType.NONE);

        if (this.selectedDrawView != null) {
            this.selectedDrawView.setEditable(false);
            this.selectedDrawView = null;
        }
    }

    @Override
    public void onChangeThick(int thick) {
        if (this.selectedDrawView != null) {
            this.selectedDrawView.setThick(thick);
            this.selectedDrawView.invalidate();
        }
    }

    @Override
    public void onChangeColor(int color) {
        if (this.selectedDrawView != null) {
            this.selectedDrawView.setColor(Utillity.getColorString(color));
            this.selectedDrawView.invalidate();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return imageDrawView.onTouchEvent(motionEvent);
    }
}
