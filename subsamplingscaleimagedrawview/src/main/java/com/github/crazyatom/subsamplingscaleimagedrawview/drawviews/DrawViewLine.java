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

public class DrawViewLine extends BaseDrawView {

    final private float MINIMUM_LENGTH = 100;

    public DrawViewLine(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.LINE, imageDrawView);
    }

    @Override
    public void update(RectF newBbox) {
        if (getPositionSize() >= 2) {
            final RectF originBbox = getBoundaryBox();
            final PointF begin = getPosition(0);
            final PointF end = getPosition(1);

            final float scaleX = newBbox.width() / originBbox.width();
            final float scaleY = newBbox.height() / originBbox.height();

            ArrayList<PointF> points = new ArrayList<>();
            points.add(new PointF((begin.x - originBbox.left) * scaleX + newBbox.left, (begin.y - originBbox.top) * scaleY + newBbox.top));
            points.add(new PointF((end.x - originBbox.left) * scaleX + newBbox.left, (end.y - originBbox.top) * scaleY + newBbox.top));
            update(points);
        }
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
        return isSimple ? "L" : getResources().getString(R.string.line);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 2 && getPosition(0).equals(getPosition(1)) == false) {
            loadPaint();
            setBoundaryBox();

            final PointF vCoord1 = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
            final PointF vCoord2 = sourceToViewCoord(getPosition(1).x, getPosition(1).y);
            canvas.drawLine(vCoord1.x, vCoord1.y, vCoord2.x, vCoord2.y, paint);

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
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public boolean isContains(float x, float y) {
        if (getPositionSize() >= 2) {
            final PointF dirVert = Utillity.getUnitVertDirenction(getPosition(0), getPosition(1));
            ArrayList<PointF> polygon = new ArrayList<>();
            polygon.add(Utillity.getOffset(getPosition(0), dirVert, (MINIMUM_LENGTH / 2)));
            polygon.add(Utillity.getOffset(getPosition(1), dirVert, (MINIMUM_LENGTH / 2)));
            polygon.add(Utillity.getOffset(getPosition(1), dirVert, -(MINIMUM_LENGTH / 2)));
            polygon.add(Utillity.getOffset(getPosition(0), dirVert, -(MINIMUM_LENGTH / 2)));
            return Utillity.isInside(new PointF(x, y), polygon);
        } else {
            return super.isContains(x, y);
        }
    }
}
