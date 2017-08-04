package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewDimension extends BaseDrawView {

    final private int textSize = 65;
    public enum state { PREVIEW_BEGIN, PREVIEW_END, COMPLETE }
    private state currentState = DrawViewDimension.state.COMPLETE;
    private PointF sCoordText;
    private PointF dirVert;

    public DrawViewDimension(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.DIMENSION, imageDrawView);
        color = Utillity.getColorString(Color.RED);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "D" : getResources().getString(R.string.dimension);
    }

    @Override
    protected void preCalc() {
        final PointF sCoord1 = getPosition(0);
        final PointF sCoord2 = getPosition(1);
        if (sCoord1 != null && sCoord2 != null) {
            final PointF sCoordMid = new PointF((sCoord1.x + sCoord2.x) / 2, (sCoord1.y + sCoord2.y) / 2);
            final PointF dirVert = Utillity.getUnitVertDirenction(sCoord1, sCoord2);
            this.sCoordText = Utillity.getOffset(sCoordMid, dirVert, DrawViewFactory.getInstance().getMINIMUM_LENGTH() / 2);
            this.dirVert = Utillity.getUnitVertDirenction(sCoord1, sCoord2);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sRegion == null) {
            return;
        }

        RectF vRect = new RectF();
        imageDrawView.sourceToViewRect(sRegion, vRect);
        if (vRect.width() == 0 || vRect.height() == 0) {
            return;
        }

        loadPaint();

        final PointF sCoord1 = getPosition(0);
        final PointF sCoord2 = getPosition(1);
        final PointF vCoord1 = sourceToViewCoord(sCoord1.x, sCoord1.y);
        final PointF vCoord2 = sourceToViewCoord(sCoord2.x, sCoord2.y);

        switch (currentState) {
            case PREVIEW_BEGIN:
                drawPreviewArrow(canvas, vCoord1);
                break;
            case PREVIEW_END:
                drawAssistLine(canvas, vCoord1);
                drawPreviewArrow(canvas, vCoord2);
                break;
            default:
                drawAssistLine(canvas, vCoord1, vCoord2);
                break;
        }

        drawLine(canvas, vCoord1, vCoord2);
        drawText(canvas);

        super.onDraw(canvas);
    }

    @Override
    protected void setSourceRegion() {
        super.setSourceRegion();
        final float MINIMUM_LENGTH = DrawViewFactory.getInstance().getMINIMUM_LENGTH();
        if (sRegion.width() < MINIMUM_LENGTH) {
            sRegion.left -= MINIMUM_LENGTH / 2;
            sRegion.right += MINIMUM_LENGTH / 2;
        }
        if (sRegion.height() < MINIMUM_LENGTH) {
            sRegion.top -= MINIMUM_LENGTH / 2;
            sRegion.bottom += MINIMUM_LENGTH / 2;
        }
    }

    /**
     * 치수선 그리기
     * @param canvas
     */
    private void drawLine(Canvas canvas, final PointF vCoord1, final PointF vCoord2) {
        canvas.drawLine(vCoord1.x, vCoord1.y, vCoord2.x, vCoord2.y, paint);
    }

    /**
     * 치수 텍스트 그리기
     * 치수 생성이 완료 되어야 표시 된다
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        final PointF vCoord = sourceToViewCoord(sCoordText.x, sCoordText.y);
        canvas.drawText(getLengthText(), vCoord.x, vCoord.y, paint);
    }

    /**
     * 미리보기 치수시 양 끝 화살표
     * @param canvas
     * @param vCoord
     */
    protected void drawPreviewArrow(Canvas canvas, final PointF vCoord) {
        final float arrow = 30;
        final float line = 20;

        PointF posArrow1 = Utillity.getOffset(vCoord, new PointF(-1f, -0.2f), arrow);
        PointF posArrowMid = Utillity.getOffset(vCoord, new PointF(-1f, -1f), arrow * 0.4f);
        PointF posArrow2 = Utillity.getOffset(vCoord, new PointF(-0.2f, -1f), arrow);
        PointF posLine = Utillity.getOffset(posArrowMid, new PointF(-1, -1f), line);

        Path path = new Path();
        path.moveTo(vCoord.x, vCoord.y);
        path.lineTo(vCoord.x, vCoord.y);
        path.lineTo(posArrow1.x, posArrow1.y);
        path.lineTo(posArrowMid.x, posArrowMid.y);
        path.lineTo(posArrow2.x, posArrow2.y);
        path.close();

        canvas.drawPath(path, paint);
        canvas.drawLine(posArrowMid.x, posArrowMid.y, posLine.x, posLine.y, paint);
    }

    /**
     * 치추선 시종점 보조선
     * @param canvas
     * @param vCoord
     */
    protected void drawAssistLine(Canvas canvas, final PointF vCoord) {
        float offset = (DrawViewFactory.getInstance().getMINIMUM_LENGTH() / 2) * imageDrawView.getScale();
        PointF pos1 = Utillity.getOffset(vCoord, this.dirVert, offset);
        PointF pos2 = Utillity.getOffset(vCoord, this.dirVert, -offset);
        canvas.drawLine(pos1.x, pos1.y, pos2.x, pos2.y, paint);
    }

    /**
     * 치수선 시종점 보조선
     * @param canvas
     */
    protected void drawAssistLine(Canvas canvas, final PointF vCoord1, final PointF vCoord2) {
        drawAssistLine(canvas,vCoord1);
        drawAssistLine(canvas, vCoord2);
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        paint.setColor(getIndexColor());
        paint.setStrokeWidth(4);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize * imageDrawView.getScale());
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean isContains(float x, float y) {
        if (getPositionSize() >= 2) {
            float MINIMUM_LENGTH = DrawViewFactory.getInstance().getMINIMUM_LENGTH();
            ArrayList<PointF> polygon = new ArrayList<>();
            polygon.add(Utillity.getOffset(getPosition(0), this.dirVert, (MINIMUM_LENGTH / 2)));
            polygon.add(Utillity.getOffset(getPosition(1), this.dirVert, (MINIMUM_LENGTH / 2)));
            polygon.add(Utillity.getOffset(getPosition(1), this.dirVert, -(MINIMUM_LENGTH / 2)));
            polygon.add(Utillity.getOffset(getPosition(0), this.dirVert, -(MINIMUM_LENGTH / 2)));
            return Utillity.isInside(new PointF(x, y), polygon);
        } else {
            return super.isContains(x, y);
        }
    }

    /**
     * 현재 그리기 상태 설정
     * @param currentState
     */
    public void setCurrentState(state currentState) {
        this.currentState = currentState;
    }

    /**
     * 현재 그리기 상태
     * @return
     */
    public state getCurrentState() {
        return currentState;
    }

    /**
     * 치수선 색상
     * @return
     */
    protected int getIndexColor() {
        return Color.parseColor(color);
    }

    /**
     * 화면에 표현될 길이
     * @return
     */
    protected float getLength() {
        return Utillity.getDistance(getPosition(0), getPosition(1)) * getRatioFactor();
    }

    /**
     * 화면에 표현되는 길이 문자
     * @return
     */
    protected String getLengthText() {
        return String.format("%d", (int) getLength());
    }

    /**
     * 기준 치수에 의한 치수 배율
     * @return
     */
    private float getRatioFactor() {
        DrawViewReferenceDimension drawViewReferenceDimension = imageDrawView.getReferenceDimension();
        if (drawViewReferenceDimension != null) {
            return drawViewReferenceDimension.getDimensionRatioFactor();
        } else {
            return 0.0f;
        }
    }
}
