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
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseEditPinView;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.EditPinViewLine;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewLine extends BaseDrawView {

    public DrawViewLine(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.LINE, imageDrawView);
    }

    @Override
    public void showContentsBox(Context context) {

    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "L" : getResources().getString(R.string.line);
    }

    @Override
    protected void preCalc() {

    }

    @Override
    public BaseEditPinView getEditPinView() {
        return new EditPinViewLine(imageDrawView, this);
    }

    @Override
    public boolean isValidEditedFlag() {
        return true;
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
        canvas.drawLine(vCoord1.x, vCoord1.y, vCoord2.x, vCoord2.y, paint);

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

    @Override
    public boolean isContains(float x, float y) {
        if (getPositionSize() >= 2) {
            final PointF dirVert = Utillity.getUnitVertDirenction(getPosition(0), getPosition(1));
            final float MINIMUM_LENGTH = DrawViewFactory.getInstance().getMINIMUM_LENGTH();
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
