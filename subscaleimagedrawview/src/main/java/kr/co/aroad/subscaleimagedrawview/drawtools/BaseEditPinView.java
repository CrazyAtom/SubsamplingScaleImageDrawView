package kr.co.aroad.subscaleimagedrawview.drawtools;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-06-22.
 */

public abstract class BaseEditPinView extends View {

    protected static class Pin {
        protected enum RectPos {
            LEFT_TOP,
            TOP,
            RIGHT_TOP,
            RIGHT,
            RIGHT_BOTTOM,
            BOTTOM,
            LEFT_BOTTOM,
            LEFT,
            NONE
        }

        public PointF point;
        public boolean state = false;
        public RectPos rectPos = RectPos.NONE;

        public Pin(PointF point, RectPos rectPos) {
            this.point = point;
            this.rectPos = rectPos;
        }

        public Pin(PointF point) {
            this.point = point;
        }
    }

    protected ImageDrawView imageDrawView;
    protected ArrayList<Pin> pinList = new ArrayList<>();
    protected BaseDrawView drawView;
    private Paint paint = new Paint();
    private float phase = 0.0f;

    public BaseEditPinView(@NonNull ImageDrawView imageDrawView, @NonNull BaseDrawView drawView) {
        super(imageDrawView.getContext());
        this.imageDrawView = imageDrawView;
        this.drawView = drawView;
        this.initPinList();
    }

    @Override
    public void invalidate() {
        super.invalidate();
        bringToFront();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setStrokeWidth(8.0f);
        paint.setAntiAlias(true);

        // boundary
        final RectF boundary = getBoundaryBox();
        if (boundary != null) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.argb(20, 0, 0, 0));
            DashPathEffect dashPathEffect = new DashPathEffect(new float[] {10.0f, 5.0f}, phase);
            paint.setPathEffect(dashPathEffect);
            RectF vRect = new RectF();
            imageDrawView.sourceToViewRect(boundary, vRect);
            canvas.drawRect(vRect, this.paint);

            phase = (phase < 10.0f) ? phase + 1.0f : 0.0f;
            this.postInvalidateOnAnimation();
        }

        // pin
        paint.setPathEffect(null);
        for (Pin pin : pinList) {
            RectF pinRect = getRectPin(imageDrawView.sourceToViewCoord(pin.point.x, pin.point.y));
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(pin.state ? Color.RED : Color.WHITE);
            canvas.drawOval(pinRect, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.DKGRAY);
            canvas.drawOval(pinRect, paint);
        }
    }

    /**
     * pin 갯수
     * @return
     */
    public int getPinCount() {
        return pinList.size();
    }

    /**
     * Pin 영역 크기
     * @return
     */
    public int getPIN_SIZE() {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
    }

    /**
     * Pin 영역
     * @param point
     * @return
     */
    protected RectF getRectPin(PointF point) {
        final int pinSize = getPIN_SIZE();
        return new RectF(point.x - pinSize, point.y - pinSize, point.x + pinSize, point.y + pinSize);
    }

    /**
     * Pin 영역에 속하는지 판단하고 상태 변경
     * 가장 가까운 Pin 하나만 상태 변경
     * @param x
     * @param y
     */
    public void setPinPressed(float x, float y) {
        this.initPinListState();

        Pin findPin = null;
        float dist = Float.MAX_VALUE;
        final float pinSize = (getPIN_SIZE() * 2f) / imageDrawView.getScale();
        for (Pin pin : pinList) {
            RectF rect = new RectF(pin.point.x - pinSize, pin.point.y - pinSize, pin.point.x + pinSize, pin.point.y + pinSize);
            if (rect.contains(x, y)) {
                if (Utillity.getDistance(pin.point, new PointF(x, y)) < dist) {
                    dist = Utillity.getDistance(pin.point, new PointF(x, y));
                    pin.state = true;
                    if (findPin != null) {
                        findPin.state = false;
                    }
                    findPin = pin;
                }
            }
        }
    }

    /**
     * 눌러진 Pin이 있는지 체크
     * @return
     */
    public boolean isPinPressed() {
        for (Pin pin : pinList) {
            if (pin.state == true) {
                return true;
            }
        }
        return false;
    }

    /**
     * PinView 영역에 속하는지 판단
     * @param x
     * @param y
     * @return
     */
    public boolean isContains(float x, float y) {
        return getBoundaryBox().contains(x, y);
    }

    /**
     * Pin 상태 초기화
     */
    protected void initPinListState() {
        for (Pin pin : pinList) {
            pin.state = false;
        }
    }

    /**
     * Pin 외곽 영역
     * @return
     */
    public RectF getBoundaryBox() {
        ArrayList<PointF> pinPoints = getPinPoints();

        float left = Collections.min(pinPoints, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.x, o2.x);
            }
        }).x;

        float top = Collections.min(pinPoints, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.y, o2.y);
            }
        }).y;

        float right = Collections.max(pinPoints, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.x, o2.x);
            }
        }).x;

        float bottom = Collections.max(pinPoints, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.y, o2.y);
            }
        }).y;

        return new RectF(left, top, right, bottom);
    }

    /**
     * Pin 좌표
     * @return
     */
    public ArrayList<PointF> getPinPoints() {
        ArrayList<PointF> points = new ArrayList<>();
        for (Pin pin : pinList) {
            points.add(pin.point);
        }
        return points;
    }

    /**
     * Pin 이동
     * @param pos
     */
    public abstract void setMovePin(PointF pos);

    /**
     * Pin 초기화
     */
    public abstract void initPinList();
}

