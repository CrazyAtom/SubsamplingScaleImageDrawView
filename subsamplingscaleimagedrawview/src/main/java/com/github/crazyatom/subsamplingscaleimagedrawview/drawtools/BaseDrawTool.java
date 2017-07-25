package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.Event.ShowSnackbarListener;
import com.github.crazyatom.subsamplingscaleimagedrawview.Event.DrawToolControllViewListener;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public abstract class BaseDrawTool {

    protected ImageDrawView imageDrawView;
    protected BaseDrawView previewDrawView;
    private boolean isBeginEdited = false;
    protected DrawToolControllViewListener drawToolControllViewListener;
    protected ShowSnackbarListener showSnackbarListener;

    public BaseDrawTool(@NonNull ImageDrawView imageDrawView) {
        this.imageDrawView = imageDrawView;
    }

    public boolean onTouchBegin(int x, int y) {
        this.isBeginEdited = true;
        touchBegin(x, y);
        return true;
    }

    public boolean onTouchMove(int x, int y) {
        if(!this.isBeginEdited) {
            return false;
        } else {
            touchMove(x, y);
            return true;
        }
    }

    public boolean onTouchEnd(int x, int y) {
        if(!this.isBeginEdited) {
            return false;
        } else {
            touchEnd(x, y);
            this.isBeginEdited = false;
            this.imageDrawView.setEditedDrawView(true);
            return true;
        }
    }

    public BaseDrawView singleTapUp(int x, int y) {
        return null;
    }

    public BaseDrawView longPress(int x, int y) {
        return null;
    }

    protected abstract void touchBegin(int x, int y);

    protected abstract void touchMove(int x, int y);

    protected abstract void touchEnd(int x, int y);

    public abstract void enter();

    public abstract void exit();

    /**
     * DrawView 생성
     * @return
     */
    protected abstract BaseDrawView createDrawView();

    public void setBeginEdited(boolean mIsBeginEdited) {
        this.isBeginEdited = mIsBeginEdited;
    }

    /**
     * 화면 좌표를 소스 좌표로 변환
     * @param vx
     * @param vy
     * @return
     */
    protected final PointF viewToSourceCoord(float vx, float vy) {
        if (this.imageDrawView == null) {
            return null;
        }
        return this.imageDrawView.viewToSourceCoord(vx, vy);
    }

    /**
     * 소스 좌표를 화면 좌표로 변환
     * @param sx
     * @param sy
     * @return
     */
    protected final PointF sourceToViewCoord(float sx, float sy) {
        if (this.imageDrawView == null) {
            return null;
        }
        return this.imageDrawView.sourceToViewCoord(sx, sy);
    }

    public void setDrawToolControllViewListener(DrawToolControllViewListener drawToolControllViewListener) {
        this.drawToolControllViewListener = drawToolControllViewListener;
    }

    public DrawToolControllViewListener getDrawToolControllViewListener() {
        return drawToolControllViewListener;
    }

    public void setShowSnackbarListener(ShowSnackbarListener showSnackbarListener) {
        this.showSnackbarListener = showSnackbarListener;
    }

    public ShowSnackbarListener getShowSnackbarListener() {
        return showSnackbarListener;
    }

    /**
     * tool을 연속으로 수행할지 여부 확인하여 연속 수행이 아니면 tool 해제
     */
    public void checkContinueTool() {
        if (DrawViewSetting.getInstance().isContinuous() == false) {
            imageDrawView.changeTool(DrawToolType.NONE);
            if (drawToolControllViewListener != null) {
                drawToolControllViewListener.changeDefaultDrawTool();
            }
        }
    }

    /**
     * 영역이 뷰 내에 존재하는지 판단
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return boolean
     */
    protected boolean isInsideView(final float left, final float top, final float right, final float bottom) {
        return (left > 0.0f && right < this.imageDrawView.getWidth()
                && top > 0.0f && bottom < this.imageDrawView.getHeight());
    }

    /**
     * 소프트키보드 show/hide
     * @param show boolean
     */
    protected void toggleSoftInput(final boolean show) {
        InputMethodManager manager = (InputMethodManager) imageDrawView.getContext().getSystemService(imageDrawView.getContext().INPUT_METHOD_SERVICE);
        if (show == true) {
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public static enum DrawToolType {
        NONE,
        PHOTO,
        TEXT,
        DIMENSION,
        DIMENSION_REF,
        CLOUD,
        INK,
        LINE,
        RECTANGLE,
        ELLIPSE,
        ERASER,
        TRANSFORM
    }
}
