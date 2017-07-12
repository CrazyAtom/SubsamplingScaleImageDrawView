package kr.co.aroad.subscaleimagedrawview.annotations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;

import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class AnnotationInk extends BaseAnnotation {

    public AnnotationInk(Context context, AnnotationType type, ImageDrawView imageDrawView) {
        super(context, type, imageDrawView);
    }

    @Override
    public void updateAnnotEx(RectF newBbox) {
        final RectF originalBbox = getBbox();

        final float scaleX = newBbox.width() / originalBbox.width();
        final float scaleY = newBbox.height() / originalBbox.height();

        ArrayList<PointF> points = new ArrayList<>();
        for (int updateList = 0; updateList < getPositionSize(); ++updateList) {
            PointF currPoint = getPosition(updateList);
            PointF newPoint = new PointF();
            newPoint.x = (currPoint.x - originalBbox.left) * scaleX + newBbox.left;
            newPoint.y = (currPoint.y - originalBbox.top) * scaleY + newBbox.top;
            points.add(newPoint);
        }

        updateAnnotEx(points);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public boolean isInvalidSaveAnnotEx() {
        return true;
    }

    @Override
    public String getName() {
        return "Ink";
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 2) {
            loadPaint();
            setBbox();

            Path path = new Path();
            PointF vPrev = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
            path.moveTo(vPrev.x, vPrev.y);
            for (int i = 1; i < getPositionSize(); i++) {
                PointF vPoint = sourceToViewCoord(getPosition(i).x, getPosition(i).y);
                path.quadTo(vPrev.x, vPrev.y, (vPoint.x + vPrev.x) / 2, (vPoint.y + vPrev.y) / 2);
                vPrev = vPoint;
            }
            canvas.drawPath(path, this.mPaint);
        }

        super.onDraw(canvas);
    }

    protected void setBbox() {
        RectF rect = new RectF(getMinX(), getMinY(), getMaxX(), getMaxY());
        if (rect.width() <= MINIMUM_LENGTH && rect.height() <= MINIMUM_LENGTH) {
            final float offset = MINIMUM_LENGTH / 2.0f;
            rect.left -= offset;
            rect.top -= offset;
            rect.right += offset;
            rect.bottom += offset;
        }

        setBbox(rect);
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        mPaint.setColor(isPreview() ? Color.parseColor("#4C6C89") : Color.parseColor(mColor));
        mPaint.setStrokeWidth(mThick);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setPathEffect(getDashPathEffect());
    }

    /**
     * Ink 점선 효과
     * @return 미리보기이면 점선 효과를 적용하고, 미리보기가 아니면 적용하지 않는다
     */
    private DashPathEffect getDashPathEffect() {
        return isPreview() ? new DashPathEffect(new float[] {40, 20}, 0.0f) : null;
    }
}
