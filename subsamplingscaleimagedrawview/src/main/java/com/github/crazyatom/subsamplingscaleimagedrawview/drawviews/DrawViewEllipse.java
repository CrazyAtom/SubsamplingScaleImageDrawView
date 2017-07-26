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
 * Created by crazy on 2017-07-11.
 */

public class DrawViewEllipse extends BaseDrawView {

    public DrawViewEllipse(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.ELLIPSE, imageDrawView);
    }

    @Override
    public void update(RectF newBbox) {
        ArrayList<PointF> points = new ArrayList<>();
        points.add(new PointF(newBbox.left, newBbox.top));
        points.add(new PointF(newBbox.right, newBbox.bottom));
        update(points);
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
        return isSimple ? "E" : getResources().getString(R.string.ellipse);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 2 && getPosition(0).equals(getPosition(1)) == false) {
            loadPaint();
            setBoundaryBox();

            final RectF rect = getRect(true);
            canvas.drawOval(rect, paint);

            super.onDraw(canvas);
        }
    }

    protected void setBoundaryBox() {
        final RectF rect = getRect(false);
        setBoundaryBox(rect);
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
    }

    @Override
    public boolean isContains(float x, float y) {
        return super.isContains(x, y);
        // 내부 영역 제거가 필요 할때 사용하자
//        final float offsetW = boundaryBox.width() / 4;
//        final float offsetH = boundaryBox.height() / 4;
//
//        // annotation이 여유크기보다 작으면 boundary box로만 판단
//        if (Math.min(boundaryBox.width(), boundaryBox.height()) < Math.min(offsetW, offsetH)) {
//            return super.isContains(x, y);
//        } else {
//            ArrayList<PointF> outter = new ArrayList<>();
//            outter.add(new PointF(boundaryBox.left, boundaryBox.top));
//            outter.add(new PointF(boundaryBox.right, boundaryBox.top));
//            outter.add(new PointF(boundaryBox.right, boundaryBox.bottom));
//            outter.add(new PointF(boundaryBox.left, boundaryBox.bottom));
//
//            ArrayList<PointF> inner = new ArrayList<>();
//            inner.add(new PointF(boundaryBox.left + offsetW, boundaryBox.top + offsetH));
//            inner.add(new PointF(boundaryBox.right - offsetW, boundaryBox.top + offsetH));
//            inner.add(new PointF(boundaryBox.right - offsetW, boundaryBox.bottom - offsetH));
//            inner.add(new PointF(boundaryBox.left + offsetW, boundaryBox.bottom - offsetH));
//
//            return Utillity.isInside(new PointF(x, y), outter) && !Utillity.isInside(new PointF(x, y), inner);
//        }
    }
}
