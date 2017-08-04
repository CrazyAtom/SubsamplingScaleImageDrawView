package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewCloud extends BaseDrawView {

    final private float INTERVAL_RATIO = 0.2f;
    private float sInterval = 0;

    public DrawViewCloud(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.CLOUD, imageDrawView);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "C" : getResources().getString(R.string.cloud);
    }

    @Override
    protected void preCalc() {
        final float min = Math.min(sRegion.width(), sRegion.height());
        this.sInterval = min * INTERVAL_RATIO;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sRegion == null || this.sInterval == 0) {
            return;
        }

        RectF vRect = new RectF();
        imageDrawView.sourceToViewRect(sRegion, vRect);
        if (vRect.width() == 0 || vRect.height() == 0) {
            return;
        }

        final int vInterval = (int) (sInterval * imageDrawView.getScale());
        if (vInterval ==  0) {
            return;
        }

        loadPaint();

        final float offset = vInterval / 2;
        vRect.left += offset;
        vRect.top += offset;
        vRect.right -= offset;
        vRect.bottom -= offset;
        drawLeftToRight(canvas, vRect, vInterval);
        drawTopToBottom(canvas, vRect, vInterval);

        super.onDraw(canvas);
    }

    /**
     * 좌에서 우로 타원조각 그리기
     * @param canvas
     * @param vRect 구름 형상이 그려질 기준 사각형 뷰 좌표
     * @param vInterval 뷰 좌표에서의 타원 간격
     */
    private void drawLeftToRight(Canvas canvas, final RectF vRect, final float vInterval) {
        final float offset = vInterval / 2.0f;

        PointF posTop = new PointF(vRect.left, vRect.top);
        PointF posBottom = new PointF(vRect.left, vRect.bottom);
        RectF ovalTop = new RectF();
        RectF ovalBottom = new RectF();

        final int cnt = Math.round(vRect.width() / vInterval);
        for (int i = 0; i < cnt; ++i) {
            if (i == cnt - 1) {
                ovalTop.set(posTop.x, (posTop.y - offset), vRect.right, (posTop.y + offset));
                ovalBottom.set(posBottom.x, (posBottom.y - offset), vRect.right, (posBottom.y + offset));
            } else {
                ovalTop.set(posTop.x, (posTop.y - offset), (posTop.x + vInterval), (posTop.y + offset));
                ovalBottom.set(posBottom.x, (posBottom.y - offset), (posBottom.x + vInterval), (posBottom.y + offset));
            }
            canvas.drawArc(ovalTop, 180, 180, false, paint);
            posTop.offset(vInterval, 0);
            canvas.drawArc(ovalBottom, 180, -180, false, paint);
            posBottom.offset(vInterval, 0);
        }
    }

    /**
     * 상단에서 하단으로 타원조각 그리기
     * @param canvas
     * @param vRect 구름 형상이 그려질 기준 사각형 뷰 좌표
     */
    private void drawTopToBottom(Canvas canvas, final RectF vRect, final float vInterval) {
        final float offset = vInterval / 2.0f;

        PointF posLeft = new PointF(vRect.left, vRect.top);
        PointF posRight = new PointF(vRect.right, vRect.top);
        RectF ovalLeft = new RectF();
        RectF ovalRight = new RectF();

        final int cnt = Math.round(vRect.height() / vInterval);
        for (int i = 0; i < cnt; ++i) {
            if (i == cnt - 1) {
                ovalLeft.set((posLeft.x - offset), posLeft.y, (posLeft.x + offset), vRect.bottom);
                ovalRight.set((posRight.x - offset), posRight.y, (posRight.x + offset), vRect.bottom);
            } else {
                ovalLeft.set((posLeft.x - offset), posLeft.y, (posLeft.x + offset), (posLeft.y + vInterval));
                ovalRight.set((posRight.x - offset), posRight.y, (posRight.x + offset), (posRight.y + vInterval));
            }
            canvas.drawArc(ovalLeft, 270, -180, false, paint);
            posLeft.offset(0, vInterval);
            canvas.drawArc(ovalRight, 270, 180, false, paint);
            posRight.offset(0, vInterval);
        }
    }



    /**
     * 페인트 설정
     *
     */
    @Override
    protected void loadPaint() {
        super.loadPaint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
    }
}
