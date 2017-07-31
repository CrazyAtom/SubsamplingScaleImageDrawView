package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewCloud extends BaseDrawView {

    final private float INTERVAL_RATIO = 0.2f;

    public DrawViewCloud(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.CLOUD, imageDrawView);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public boolean isInvalidSaveAnnotEx() {
        return true;
    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "C" : getResources().getString(R.string.cloud);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (getPositionSize() < 2 || getRect(false).width() == 0 || getRect(false).height() == 0) {
            return;
        }

        loadPaint();
        setBoundaryBox();

        final RectF vRect = getRect(true);
        final float intervalLength = getIntervalLength();
        final float offset = intervalLength / 2.0f;
        vRect.left += offset;
        vRect.top += offset;
        vRect.right -= offset;
        vRect.bottom -= offset;
        drawLineLTToRT(canvas, vRect);
        drawLineRTToRB(canvas, vRect);
        drawLineRBToLB(canvas, vRect);
        drawLineLBToLT(canvas, vRect);

        super.onDraw(canvas);
    }

    protected void setBoundaryBox() {
        final RectF rect = getRect(false);
        setBoundaryBox(rect);
    }

    /**
     * 구름 형상 타원 간격 길이
     * @return float
     */
    private float getIntervalLength() {
        final RectF vRect = getRect(true);
        final float minLength = Math.min(vRect.width(), vRect.height());
        return minLength * INTERVAL_RATIO;
    }

    /**
     * 좌상단에서 우상단으로 타원조각 그리기
     * @param canvas
     * @param vRect 구름 형상이 그려질 기준 사각형 뷰 좌표
     */
    private void drawLineLTToRT(Canvas canvas, RectF vRect) {
        final float intervalLength = getIntervalLength();
        final float offset = intervalLength / 2.0f;

        PointF pos = new PointF(vRect.left, vRect.top);
        RectF oval = new RectF();

        final int cnt = Math.round(vRect.width() / intervalLength);
        for (int i = 0; i < cnt; ++i) {
            if (i == cnt - 1) {
                oval.set(pos.x, (pos.y - offset), vRect.right, (pos.y + offset));
            } else {
                oval.set(pos.x, (pos.y - offset), (pos.x + intervalLength), (pos.y + offset));
            }
            canvas.drawArc(oval, 180, 180, false, paint);
            pos.offset(intervalLength, 0);
        }
    }

    /**
     * 우상단에서 우하단으로 타원조각 그리기
     * @param canvas
     * @param vRect 구름 형상이 그려질 기준 사각형 뷰 좌표
     */
    private void drawLineRTToRB(Canvas canvas, RectF vRect) {
        final float intervalLength = getIntervalLength();
        final float offset = intervalLength / 2.0f;

        PointF pos = new PointF(vRect.right, vRect.top);
        RectF oval = new RectF();

        final int cnt = Math.round(vRect.height() / intervalLength);
        for (int i = 0; i < cnt; ++i) {
            if (i == cnt - 1) {
                oval.set((pos.x - offset), pos.y, (pos.x + offset), vRect.bottom);
            } else {
                oval.set((pos.x - offset), pos.y, (pos.x + offset), (pos.y + intervalLength));
            }
            canvas.drawArc(oval, 270, 180, false, paint);
            pos.offset(0, intervalLength);
        }
    }

    /**
     * 우하단에서 좌하단으로 타원조각 그리기
     * @param canvas
     * @param vRect 구름 형상이 그려질 기준 사각형 뷰 좌표
     */
    private void drawLineRBToLB(Canvas canvas, RectF vRect) {
        final float intervalLength = getIntervalLength();
        final float offset = intervalLength / 2.0f;

        PointF pos = new PointF(vRect.right, vRect.bottom);
        RectF oval = new RectF();

        final int cnt = Math.round(vRect.width() / intervalLength);
        for (int i = 0; i < cnt; ++i) {
            if (i == cnt - 1) {
                oval.set(vRect.left, (pos.y - offset), pos.x, (pos.y + offset));
            } else {
                oval.set((pos.x - intervalLength), (pos.y - offset), pos.x, (pos.y + offset));
            }
            canvas.drawArc(oval, 0, 180, false, paint);
            pos.offset(-intervalLength, 0);
        }
    }

    /**
     *  좌하단에서 좌상단으로 타원조각 그리기
     * @param canvas
     * @param vRect 구름 형상이 그려질 기준 사각형 뷰 좌표
     */
    private void drawLineLBToLT(Canvas canvas, RectF vRect) {
        final float intervalLength = getIntervalLength();
        final float offset = intervalLength / 2.0f;

        PointF pos = new PointF(vRect.left, vRect.bottom);
        RectF oval = new RectF();

        final int cnt = Math.round(vRect.height() / intervalLength);
        for (int i = 0; i < cnt; ++i) {
            if (i == cnt - 1) {
                oval.set((pos.x - offset), vRect.top, (pos.x + offset), pos.y);
            } else {
                oval.set((pos.x - offset), (pos.y - intervalLength), (pos.x + offset), pos.y);
            }
            canvas.drawArc(oval, 90, 180, false, paint);
            pos.offset(0, -intervalLength);
        }
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        paint.setColor(Color.parseColor(color));
        paint.setStrokeWidth(thick);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public boolean isContains(float x, float y) {
        return super.isContains(x, y);
// 내부 영역 제거가 필요 할때 사용하자
//        final float intervalLength = getIntervalLength();
//        final float offset = intervalLength / 2.0f;
//
//        // annotation이 여유크기보다 작으면 boundary box로만 판단
//        if (Math.min(boundaryBox.width(), boundaryBox.height()) < offset) {
//            return super.isContains(x, y);
//        } else {
//            ArrayList<PointF> outter = new ArrayList<>();
//            outter.add(new PointF(boundaryBox.left, boundaryBox.top));
//            outter.add(new PointF(boundaryBox.right, boundaryBox.top));
//            outter.add(new PointF(boundaryBox.right, boundaryBox.bottom));
//            outter.add(new PointF(boundaryBox.left, boundaryBox.bottom));
//
//            ArrayList<PointF> inner = new ArrayList<>();
//            inner.add(new PointF(boundaryBox.left + offset, boundaryBox.top + offset));
//            inner.add(new PointF(boundaryBox.right - offset, boundaryBox.top + offset));
//            inner.add(new PointF(boundaryBox.right - offset, boundaryBox.bottom - offset));
//            inner.add(new PointF(boundaryBox.left + offset, boundaryBox.bottom - offset));
//
//            return Utillity.isInside(new PointF(x, y), outter) && !Utillity.isInside(new PointF(x, y), inner);
//        }
    }
}
