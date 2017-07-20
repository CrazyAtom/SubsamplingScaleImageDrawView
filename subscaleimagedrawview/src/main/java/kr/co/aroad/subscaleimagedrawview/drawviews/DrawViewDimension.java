package kr.co.aroad.subscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import kr.co.aroad.subscaleimagedrawview.R;
import kr.co.aroad.subscaleimagedrawview.util.DrawViewSetting;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewDimension extends BaseDrawView {

    final private int textSize = 15;
    public enum state { PREVIEW_BEGIN, PREVIEW_END, COMPLETE }
    private state currentState = DrawViewDimension.state.COMPLETE;

    public DrawViewDimension(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.DIMENSION, imageDrawView);
        color = Utillity.getColorString(Color.RED);
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
        return false;
    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "D" : getResources().getString(R.string.dimension);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 2) {
            loadPaint();
            setBoundaryBox();

            switch (currentState) {
                case PREVIEW_BEGIN:
                    drawPreviewArrow(canvas, getPosition(0));
                    break;
                case PREVIEW_END:
                    drawAssistLine(canvas, getPosition(0));
                    drawPreviewArrow(canvas, getPosition(1));
                    break;
                default:
                    drawAssistLine(canvas);
                    break;
            }

            drawLine(canvas);
            drawText(canvas);

            super.onDraw(canvas);
        }
    }

    protected void setBoundaryBox() {
        final RectF rect = getRect(false);
        if (rect.width() < MINIMUM_LENGTH) {
            rect.left -= MINIMUM_LENGTH / 2;
            rect.right += MINIMUM_LENGTH / 2;
        }
        if (rect.height() < MINIMUM_LENGTH) {
            rect.top -= MINIMUM_LENGTH / 2;
            rect.bottom += MINIMUM_LENGTH / 2;
        }
        setBoundaryBox(rect);
    }

    /**
     * 치수선 그리기
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        final PointF vCoord1 = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
        final PointF vCoord2 = sourceToViewCoord(getPosition(1).x, getPosition(1).y);
        canvas.drawLine(vCoord1.x, vCoord1.y, vCoord2.x, vCoord2.y, paint);
    }

    /**
     * 치수 텍스트 그리기
     * 치수 생성이 완료 되어야 표시 된다
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        final PointF vCoord = sourceToViewCoord(getTextPos().x, getTextPos().y);
        canvas.drawText(getLengthText(), vCoord.x, vCoord.y, paint);
    }

    /**
     * 미리보기 치수시 양 끝 화살표
     * @param canvas
     * @param sCoord
     */
    protected void drawPreviewArrow(Canvas canvas, PointF sCoord) {
        final float arrow = 30;
        final float line = 20;

        PointF vCoord = sourceToViewCoord(sCoord.x, sCoord.y);
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
     * @param sCoord
     */
    protected void drawAssistLine(Canvas canvas, PointF sCoord) {
        final PointF dirVert = Utillity.getUnitVertDirenction(getPosition(0), getPosition(1));
        PointF pos1 = Utillity.getOffset(sCoord, dirVert, MINIMUM_LENGTH / 2);
        PointF pos2 = Utillity.getOffset(sCoord, dirVert, -MINIMUM_LENGTH / 2);
        pos1 = sourceToViewCoord(pos1.x, pos1.y);
        pos2 = sourceToViewCoord(pos2.x, pos2.y);
        canvas.drawLine(pos1.x, pos1.y, pos2.x, pos2.y, paint);
    }

    /**
     * 치수선 시종점 보조선
     * @param canvas
     */
    protected void drawAssistLine(Canvas canvas) {
        drawAssistLine(canvas, getPosition(0));
        drawAssistLine(canvas, getPosition(1));
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        paint.setColor(getIndexColor());
        paint.setStrokeWidth(thick);
        paint.setAntiAlias(true);
        paint.setTextSize(textSize * imageDrawView.getScale());
        paint.setStyle(Paint.Style.FILL);
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
     * 치수의 텍스트 위치
     * @return
     */
    protected PointF getTextPos() {
        final PointF pos = new PointF((getPosition(0).x + getPosition(1).x) / 2,
                (getPosition(0).y + getPosition(1).y) / 2);
        final PointF dirVert = Utillity.getUnitVertDirenction(getPosition(0), getPosition(1));
        return Utillity.getOffset(pos, dirVert, MINIMUM_LENGTH / 2);
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
