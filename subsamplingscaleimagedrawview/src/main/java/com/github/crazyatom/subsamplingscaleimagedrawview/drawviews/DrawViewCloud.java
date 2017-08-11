package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseEditPinView;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.EditPinViewRect;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

import java.util.ArrayList;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewCloud extends BaseDrawView {

    final private float INTERVAL_RATIO = 0.2f;
    private ArrayList<ArcPiece> arcPieces;

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
        final float interval = min * INTERVAL_RATIO;
        if (interval > 0) {
            final float offset = interval / 2;
            final RectF baseRect = new RectF(sRegion.left + offset, sRegion.top + offset, sRegion.right - offset, sRegion.bottom - offset);

            arcPieces = new ArrayList<>();
            // left to right
            PointF posTop = new PointF(baseRect.left, baseRect.top);
            PointF posBottom = new PointF(baseRect.left, baseRect.bottom);
            int cnt = Math.round(baseRect.width() / interval);
            for(int i = 0; i < cnt; ++i) {
                if (i == (cnt - 1)) {
                    arcPieces.add(new ArcPiece(new RectF(posTop.x, (posTop.y - offset), baseRect.right, (posTop.y + offset)), 180, 180));
                    arcPieces.add(new ArcPiece(new RectF(posBottom.x, (posBottom.y - offset), baseRect.right, (posBottom.y + offset)), 180, -180));
                } else {
                    arcPieces.add(new ArcPiece(new RectF(posTop.x, (posTop.y - offset), (posTop.x + interval), (posTop.y + offset)), 180, 180));
                    arcPieces.add(new ArcPiece(new RectF(posBottom.x, (posBottom.y - offset), (posBottom.x + interval), (posBottom.y + offset)), 180, -180));
                }
                posTop.offset(interval, 0);
                posBottom.offset(interval, 0);
            }
            // top to bottom
            PointF posLeft = new PointF(baseRect.left, baseRect.top);
            PointF posRight = new PointF(baseRect.right, baseRect.top);
            cnt = Math.round(baseRect.height() / interval);
            for(int i = 0; i < cnt; ++i) {
                if (i == (cnt - 1)) {
                    arcPieces.add(new ArcPiece(new RectF((posLeft.x - offset), posLeft.y, (posLeft.x + offset), baseRect.bottom), 270, -180));
                    arcPieces.add(new ArcPiece(new RectF((posRight.x - offset), posRight.y, (posRight.x + offset), baseRect.bottom), 270, 180));
                } else {
                    arcPieces.add(new ArcPiece(new RectF((posLeft.x - offset), posLeft.y, (posLeft.x + offset), (posLeft.y + interval)), 270, -180));
                    arcPieces.add(new ArcPiece(new RectF((posRight.x - offset), posRight.y, (posRight.x + offset), (posRight.y + interval)), 270, 180));
                }
                posLeft.offset(0, interval);
                posRight.offset(0, interval);
            }
        }
    }

    @Override
    public BaseEditPinView getEditPinView() {
        return new EditPinViewRect(imageDrawView, this);
    }

    @Override
    public boolean isValidEditedFlag() {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (arcPieces == null) {
            return;
        }

        loadPaint();

        try {
            for (ArcPiece arcPiece : arcPieces) {
                if (arcPiece.oval != null) {
                    canvas.drawArc(imageDrawView.sourceToViewRect(arcPiece.oval, new RectF()), arcPiece.startAngle, arcPiece.sweepAngle, false, paint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDraw(canvas);
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

    /**
     * 호 조각 정보 클래스
     */
    private class ArcPiece {
        public RectF oval;
        public float startAngle;
        public float sweepAngle;

        public ArcPiece(RectF oval, float startAngle, float sweepAngle) {
            this.oval = oval;
            this.startAngle = startAngle;
            this.sweepAngle = sweepAngle;
        }
    }
}
