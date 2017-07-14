package kr.co.aroad.subscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

import kr.co.aroad.subscaleimagedrawview.listener.CreateDrawViewListener;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class DrawViewEllipse extends BaseDrawView {

    public DrawViewEllipse(@NonNull ImageDrawView imageDrawView, @Nullable CreateDrawViewListener createDrawViewListener) {
        super(DrawViewType.ELLIPSE, imageDrawView, createDrawViewListener);
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
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 2) {
            loadPaint();
            setBbox();

            final RectF rect = getRect(true);
            canvas.drawOval(rect, paint);

            super.onDraw(canvas);
        }
    }

    protected void setBbox() {
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
        final float offsetW = boundaryBox.width() / 4;
        final float offsetH = boundaryBox.height() / 4;

        // annotation이 여유크기보다 작으면 boundary box로만 판단
        if (Math.min(boundaryBox.width(), boundaryBox.height()) < Math.min(offsetW, offsetH)) {
            return super.isContains(x, y);
        } else {
            ArrayList<PointF> outter = new ArrayList<>();
            outter.add(new PointF(boundaryBox.left, boundaryBox.top));
            outter.add(new PointF(boundaryBox.right, boundaryBox.top));
            outter.add(new PointF(boundaryBox.right, boundaryBox.bottom));
            outter.add(new PointF(boundaryBox.left, boundaryBox.bottom));

            ArrayList<PointF> inner = new ArrayList<>();
            inner.add(new PointF(boundaryBox.left + offsetW, boundaryBox.top + offsetH));
            inner.add(new PointF(boundaryBox.right - offsetW, boundaryBox.top + offsetH));
            inner.add(new PointF(boundaryBox.right - offsetW, boundaryBox.bottom - offsetH));
            inner.add(new PointF(boundaryBox.left + offsetW, boundaryBox.bottom - offsetH));

            return Utillity.isInside(new PointF(x, y), outter) && !Utillity.isInside(new PointF(x, y), inner);
        }
    }
}
