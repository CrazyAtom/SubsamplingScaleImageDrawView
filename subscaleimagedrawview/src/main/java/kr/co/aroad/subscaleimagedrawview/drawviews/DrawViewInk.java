package kr.co.aroad.subscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class DrawViewInk extends BaseDrawView {

    public DrawViewInk(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.INK, imageDrawView);
    }

    @Override
    public void update(RectF newBbox) {
        final RectF originalBbox = getBoundaryBox();

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
            setBoundaryBox();

            Path path = new Path();
            PointF vPrev = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
            path.moveTo(vPrev.x, vPrev.y);
            for (int i = 1; i < getPositionSize(); i++) {
                PointF vPoint = sourceToViewCoord(getPosition(i).x, getPosition(i).y);
                path.quadTo(vPrev.x, vPrev.y, (vPoint.x + vPrev.x) / 2, (vPoint.y + vPrev.y) / 2);
                vPrev = vPoint;
            }
            canvas.drawPath(path, this.paint);
        }

        super.onDraw(canvas);
    }

    protected void setBoundaryBox() {
        RectF rect = new RectF(getMinX(), getMinY(), getMaxX(), getMaxY());
        if (rect.width() <= MINIMUM_LENGTH && rect.height() <= MINIMUM_LENGTH) {
            final float offset = MINIMUM_LENGTH / 2.0f;
            rect.left -= offset;
            rect.top -= offset;
            rect.right += offset;
            rect.bottom += offset;
        }

        setBoundaryBox(rect);
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        paint.setColor(isPreview() ? Color.parseColor("#4C6C89") : Color.parseColor(color));
        paint.setStrokeWidth(thick);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(getDashPathEffect());
    }

    /**
     * Ink 점선 효과
     * @return 미리보기이면 점선 효과를 적용하고, 미리보기가 아니면 적용하지 않는다
     */
    private DashPathEffect getDashPathEffect() {
        return isPreview() ? new DashPathEffect(new float[] {40, 20}, 0.0f) : null;
    }

    @Override
    public boolean isContains(float x, float y) {
        return super.isContains(x, y);
    }
}
