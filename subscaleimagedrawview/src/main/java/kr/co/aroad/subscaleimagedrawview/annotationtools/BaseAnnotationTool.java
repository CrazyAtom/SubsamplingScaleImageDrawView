package kr.co.aroad.subscaleimagedrawview.annotationtools;

import android.content.Context;
import android.graphics.PointF;

import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public abstract class BaseAnnotationTool {

    protected Context mContext;
    protected ImageDrawView mImageDrawView;
    private boolean mIsBeginEdited = false;

    public BaseAnnotationTool(Context context, ImageDrawView imageDrawView) {
        mContext = context;
        mImageDrawView = imageDrawView;
    }

    public boolean onTouchBegin(int x, int y) {
        mIsBeginEdited = true;
        touchBegin(x, y);
        return true;
    }

    public boolean onTouchMove(int x, int y) {
        if(!mIsBeginEdited) {
            return false;
        } else {
            touchMove(x, y);
            return true;
        }
    }

    public boolean onTouchEnd(int x, int y) {
        if(!mIsBeginEdited) {
            return false;
        } else {
            touchEnd(x, y);
            mIsBeginEdited = false;
            return true;
        }
    }

    protected abstract void touchBegin(int x, int y);

    protected abstract void touchMove(int x, int y);

    protected abstract void touchEnd(int x, int y);

    public abstract void enter();

    public abstract void exit();

    /**
     * 화면 좌표를 소스 좌표로 변환
     * @param vx
     * @param vy
     * @return
     */
    protected final PointF viewToSourceCoord(float vx, float vy) {
        if (mImageDrawView == null) {
            return null;
        }
        return mImageDrawView.viewToSourceCoord(vx, vy);
    }

    /**
     * 소스 좌표를 화면 좌표로 변환
     * @param sx
     * @param sy
     * @return
     */
    protected final PointF sourceToViewCoord(float sx, float sy) {
        if (mImageDrawView == null) {
            return null;
        }
        return mImageDrawView.sourceToViewCoord(sx, sy);
    }
}
