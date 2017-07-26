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

public class DrawViewRectangle extends BaseDrawView {

    public DrawViewRectangle(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.RECTANGLE, imageDrawView);
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
        return isSimple ? "R" : getResources().getString(R.string.rectangle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 2 && getPosition(0).equals(getPosition(1)) == false) {
            loadPaint();
            setBoundaryBox();

            final RectF rect = getRect(true);
            canvas.drawRect(rect, paint);

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
//        final float offset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
//
//        // annotation이 여유크기보다 작으면 boundary box로만 판단
//        if (Math.min(boundaryBox.width(), boundaryBox.height()) < offset) {
//            return super.isContains(x, y);
//        } else {
//            ArrayList<PointF> outter = new ArrayList<>();
//            outter.add(new PointF(boundaryBox.left - offset, boundaryBox.top - offset));
//            outter.add(new PointF(boundaryBox.right + offset, boundaryBox.top - offset));
//            outter.add(new PointF(boundaryBox.right + offset, boundaryBox.bottom + offset));
//            outter.add(new PointF(boundaryBox.left - offset, boundaryBox.bottom + offset));
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
