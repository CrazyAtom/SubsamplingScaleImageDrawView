package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import com.github.crazyatom.subsamplingscaleimagedrawview.Event.DrawViewTransformListener;
import com.github.crazyatom.subsamplingscaleimagedrawview.Event.SelectedDrawViewListener;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 13..
 */

public class DrawToolTransform extends BaseDrawTool implements View.OnTouchListener, DrawViewTransformListener {

    private BaseDrawView selectedDrawView;
    private PointF beginPoint;
    private BaseEditPinView editPinView;
    // Listener selected DrawView
    private SelectedDrawViewListener selectedDrawViewListener;

    public DrawToolTransform(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    public void setSelectedDrawView(@NonNull BaseDrawView selectedDrawView) {
        this.selectedDrawView = selectedDrawView;
        if (this.selectedDrawViewListener != null) {
            this.selectedDrawViewListener.onSelectedDrawViewInfo("by" + this.selectedDrawView.getCreater() + " " + Utillity.getTimeString(this.selectedDrawView.getUpdateTime(), null));
        }
    }

    /**
     * DrawView 선택에 대한 이벤트
     */
    public void setSelectedDrawViewListener(SelectedDrawViewListener selectedDrawViewListener) {
        this.selectedDrawViewListener = selectedDrawViewListener;
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
//            touchBegin(x, y);
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
            this.editPinView = this.selectedDrawView.getEditPinView();
            imageDrawView.setEditPinView(editPinView);
            imageDrawView.invalidate();
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
            this.editPinView.setMovePin(new PointF(sCoord.x, sCoord.y));
            this.editPinView.invalidate();
            if (selectedDrawView.getType() == BaseDrawView.DrawViewType.DIMENSION_REF
                    || selectedDrawView.getType() == BaseDrawView.DrawViewType.DIMENSION
                    || selectedDrawView.getType() == BaseDrawView.DrawViewType.LINE) {
                selectedDrawView.update(this.editPinView.getPinPoints());
            } else {
                update(this.editPinView.getBoundaryBox());
            }
        } else {
            RectF sRect = this.selectedDrawView.getSourceRegion();
            final float sDistX = sCoord.x - beginPoint.x;
            final float sDistY = sCoord.y - beginPoint.y;
            beginPoint = sCoord;
            update(new RectF(sRect.left + sDistX, sRect.top + sDistY, sRect.right + sDistX, sRect.bottom + sDistY));
            if (this.editPinView != null) {
                this.editPinView.initPinList();
                this.editPinView.invalidate();
            }
        }
    }

    @Override
    protected void touchEnd(int x, int y) {
//        exitTransformState();
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        if (this.editPinView != null) {
            imageDrawView.clearEditPinView();
            imageDrawView.invalidate();
        }

        if (this.selectedDrawViewListener != null) {
            this.selectedDrawViewListener.onSelectedDrawViewInfo(null);
        }
    }

    @Override
    protected BaseDrawView createDrawView(final boolean preview) {
        return null;
    }

    /**
     * boundary box를 이용한 update
     * @param newBoundaryBox
     */
    private void update(RectF newBoundaryBox) {
        if (this.selectedDrawView != null) {
            this.selectedDrawView.update(newBoundaryBox);
        }
    }

    /**
     * 편집 상태의 drawView 편집 상태 해제
     */
    private void exitTransformState() {
        if (this.editPinView != null) {
            imageDrawView.clearEditPinView();
            imageDrawView.invalidate();
        }

        if (this.selectedDrawView != null) {
            this.selectedDrawView.setEditable(false);
            this.selectedDrawView = null;
        }

        imageDrawView.changeTool(DrawToolType.NONE);
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
