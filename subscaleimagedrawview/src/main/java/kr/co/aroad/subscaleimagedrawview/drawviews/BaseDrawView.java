package kr.co.aroad.subscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import kr.co.aroad.subscaleimagedrawview.listener.CreateDrawViewListener;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public abstract class BaseDrawView extends View {

    public static final float MINIMUM_LENGTH = 30.0f;

    private DrawViewType type;
    private String uniqId;

    private ArrayList<PointF> pointList = new ArrayList<>();
    protected ImageDrawView imageDrawView;
    protected Paint paint = new Paint();

    protected int thick;
    protected String color;

    protected boolean showBoundaryBox = false;
    protected RectF boundaryBox = null;

    private boolean isPreview = false;
    private boolean isEditable = false;


    public BaseDrawView(DrawViewType type, @NonNull ImageDrawView imageDrawView, @Nullable CreateDrawViewListener createDrawViewListener) {
        super(imageDrawView.getContext());
        this.type = type;
        this.imageDrawView = imageDrawView;

        this.type = type;
        this.imageDrawView = imageDrawView;
        if (createDrawViewListener != null) {
            setUniqId(createDrawViewListener.newUUID());
        } else {
            setUniqId(Utillity.getUUID());
        }
        setThick(4);
        setColor(Utillity.getColorString(Color.BLACK));
    }

    protected void initPosition() {
        this.pointList.clear();
    }

    public DrawViewType getType() {
        return this.type;
    }

    public String getUniqId() {
        return this.uniqId;
    }

    public void setUniqId(String uniqId) {
        this.uniqId = uniqId;
    }

    public PointF getPosition(int index) {
        try {
            return this.pointList.get(index);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPoints(ArrayList<PointF> points) {
        this.pointList = points;
    }

    public void setPosition(int index, PointF position) {
        try {
            this.pointList.set(index, position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void addPosition(PointF position) {
        this.pointList.add(new PointF(position.x, position.y));
    }

    public int getPositionSize() {
        return this.pointList.size();
    }

    public int getThick() {
        return this.thick;
    }

    public void setThick(int thick) {
        this.thick = thick;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public boolean isShowBoundaryBox() {
        return this.showBoundaryBox;
    }

    public void setShowBoundaryBox(boolean show) {
        this.showBoundaryBox = show;
    }

    public RectF getBoundaryBox() {
        return this.boundaryBox;
    }

    public void setBoundaryBox(RectF box) {
        this.boundaryBox = box;
    }

    /**
     * 좌표 리스트 중 가장 큰 x값
     *
     * @return
     */
    protected float getMaxX() {
        return Collections.max(this.pointList, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.x, o2.x);
            }
        }).x;
    }

    /**
     * 좌표 리스트 중 가장 큰 y값
     *
     * @return
     */
    protected float getMaxY() {
        return Collections.max(this.pointList, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.y, o2.y);
            }
        }).y;
    }

    /**
     * 좌표 리스트 중 가장 작은 x값
     *
     * @return
     */
    protected float getMinX() {
        return Collections.min(this.pointList, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.x, o2.x);
            }
        }).x;
    }

    /**
     * 좌표 리스트 중 가장 작은 y값
     *
     * @return
     */
    protected float getMinY() {
        return Collections.min(this.pointList, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.y, o2.y);
            }
        }).y;
    }

    /**
     * 좌표 리스트 중 가장 큰 x값
     *
     * @return
     */
    protected float getMaxX(ArrayList<PointF> points) {
        return Collections.max(points, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.x, o2.x);
            }
        }).x;
    }

    /**
     * 좌표 리스트 중 가장 큰 y값
     *
     * @return
     */
    protected float getMaxY(ArrayList<PointF> points) {
        return Collections.max(points, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.y, o2.y);
            }
        }).y;
    }

    /**
     * 좌표 리스트 중 가장 작은 x값
     *
     * @return
     */
    protected float getMinX(ArrayList<PointF> points) {
        return Collections.min(points, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.x, o2.x);
            }
        }).x;
    }

    /**
     * 좌표 리스트 중 가장 작은 y값
     *
     * @return
     */
    protected float getMinY(ArrayList<PointF> points) {
        return Collections.min(points, new Comparator<PointF>() {
            @Override
            public int compare(PointF o1, PointF o2) {
                return Float.compare(o1.y, o2.y);
            }
        }).y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.showBoundaryBox == true) {
            drawBBox(canvas);
        }
    }

    /**
     * 외곽 사각 영역 그리기
     *
     * @param canvas
     */
    protected void drawBBox(Canvas canvas) {
        if (this.boundaryBox != null) {
            Paint paint = new Paint();
            paint.setStrokeWidth(8.0f);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.DKGRAY);
            DashPathEffect dashPathEffect = new DashPathEffect(new float[] {10.0f, 5.0f}, 1.0f);
            paint.setPathEffect(dashPathEffect);
            RectF vRect = new RectF();
            this.imageDrawView.sourceToViewRect(this.boundaryBox, vRect);
            canvas.drawRect(vRect, paint);
        }
    }

    /**
     * 외곽 사각형
     *
     * @param toView true이면 화면 좌표, false이면 소스 좌표
     * @return 화면 또는 소스 좌표 rect
     */
    protected RectF getRect(boolean toView) {
        final float left = getMinX(this.pointList);
        final float right = getMaxX(this.pointList);
        final float top = getMinY(this.pointList);
        final float bottom = getMaxY(this.pointList);
        final RectF sRect = new RectF(left, top, right, bottom);

        if (toView) {
            final RectF vRect = new RectF();
            return this.imageDrawView.sourceToViewRect(sRect, vRect);
        } else {
            return sRect;
        }
    }

    /**
     * 미리보기 인지 여부
     *
     * @return true이면 미리보기, false이면 미리보기 아님
     */
    public boolean isPreview() {
        return this.isPreview;
    }

    /**
     * 미리보기 상태 설정
     *
     * @param preview
     */
    public void setPreview(boolean preview) {
        this.isPreview = preview;
    }

    public boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
    }

    /**
     * 좌표정보 재설정
     *
     * @param points
     */
    public void update(ArrayList<PointF> points) {
        initPosition();
        this.pointList.addAll(points);
        invalidate();
    }

    /**
     * 화면 좌표를 소스 좌표로 변환
     * @param vx
     * @param vy
     * @return
     */
    protected final PointF viewToSourceCoord(float vx, float vy) {
        if (this.imageDrawView == null) {
            return null;
        }
        return this.imageDrawView.viewToSourceCoord(vx, vy);
    }

    /**
     * 소스 좌표를 화면 좌표로 변환
     * @param sx
     * @param sy
     * @return
     */
    protected final PointF sourceToViewCoord(float sx, float sy) {
        if (this.imageDrawView == null) {
            return null;
        }
        return this.imageDrawView.sourceToViewCoord(sx, sy);
    }

    /**
     * bondary box 정보를 이용하여 drawView 갱신
     *
     * @param newBbox
     */
    abstract public void update(RectF newBbox);

    /**
     * drawView 정보 팝업
     * @param context
     */
    abstract public void showContentsBox(Context context);

    /**
     * 저장 하는 annotation인지 여부
     * @return
     */
    abstract public boolean isInvalidSaveAnnotEx();

    /**
     * drawView 이름
     *
     * @return
     */
    public String getName() {
        return getType().toString(false);
    }

    /**
     * 좌표 x, y가 annotation내에 존재 하는지 체크
     * @param x
     * @param y
     * @return true 이면 drawView 내에 존재, false 이면 drawView 내에 존재 하지 않음
     */
    public boolean isContains(float x, float y) {
        if(this.boundaryBox == null) {
            return false;
        } else {
            RectF rect = new RectF(this.boundaryBox);
            float temp;
            if(rect.left > rect.right) {
                temp = rect.left;
                rect.left = rect.right;
                rect.right = temp;
            }

            if(rect.top > rect.bottom) {
                temp = rect.top;
                rect.top = rect.bottom;
                rect.bottom = temp;
            }

            return rect.contains(x, y);
        }
    }

    /**
     * drawView type 정의
     */
    public static enum DrawViewType {
        PHOTO,
        TEXT,
        DIMENSION,
        CLOUD,
        INK,
        LINE,
        RECTANGLE,
        ELLIPSE;

        private DrawViewType() {
        }

        public String toString(boolean isSimple) {
            switch (this) {
                case PHOTO:
                    return isSimple ? "P" : "Photo";
                case TEXT:
                    return isSimple ? "T" : "Text";
                case DIMENSION:
                    return isSimple ? "D" : "Dimension";
                case CLOUD:
                    return isSimple ? "C" : "Cloud";
                case INK:
                    return isSimple ? "I" : "Ink";
                case LINE:
                    return isSimple ? "L" : "Line";
                case RECTANGLE:
                    return isSimple ? "R" : "Rectangle";
                case ELLIPSE:
                    return isSimple ? "E" : "Ellipse";
                default:
                    return "";
            }
        }
    }
}
