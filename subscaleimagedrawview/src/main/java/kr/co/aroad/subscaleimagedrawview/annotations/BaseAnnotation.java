package kr.co.aroad.subscaleimagedrawview.annotations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public abstract class BaseAnnotation extends View {

    public static final float MINIMUM_LENGTH = 30.0f;

    private AnnotationType mType;
    private String mUniqId = UUID.randomUUID().toString().replace("-", "");

    private ArrayList<PointF> mPoints = new ArrayList<>();
    protected ImageDrawView mImageDrawView;
    protected Paint mPaint = new Paint();

    protected int mThick = 4;
    protected String mColor = "#" + Integer.toHexString(Color.BLACK);

    protected boolean mShowBBox = false;
    protected RectF mBbox = null;

    private boolean isPreview = false;
    private boolean isEditable = false;


    public BaseAnnotation(Context context, AnnotationType type, ImageDrawView imageDrawView) {
        super(context);
        mType = type;
        mImageDrawView = imageDrawView;
    }

    protected void initPosition() {
        mPoints.clear();
    }

    public AnnotationType getType() {
        return mType;
    }

    public String getUniqId() {
        return mUniqId;
    }

    public void setUniqId(String uniqId) {
        mUniqId = uniqId;
    }

    public PointF getPosition(int index) {
        try {
            return mPoints.get(index);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setPoints(ArrayList<PointF> points) {
        mPoints = points;
    }

    public void setPosition(int index, PointF position) {
        try {
            mPoints.set(index, position);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void addPosition(PointF position) {
        mPoints.add(new PointF(position.x, position.y));
    }

    public int getPositionSize() {
        return mPoints.size();
    }

    public int getThick() {
        return mThick;
    }

    public void setThick(int thick) {
        mThick = thick;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public boolean isShowBBox() {
        return mShowBBox;
    }

    public void setShowBBox(boolean showBBox) {
        mShowBBox = showBBox;
    }

    public RectF getBbox() {
        return mBbox;
    }

    public void setBbox(RectF bbox) {
        mBbox = bbox;
    }

    /**
     * 좌표 리스트 중 가장 큰 x값
     *
     * @return
     */
    protected float getMaxX() {
        return Collections.max(mPoints, new Comparator<PointF>() {
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
        return Collections.max(mPoints, new Comparator<PointF>() {
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
        return Collections.min(mPoints, new Comparator<PointF>() {
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
        return Collections.min(mPoints, new Comparator<PointF>() {
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
        if (mShowBBox == true) {
            drawBBox(canvas);
        }
    }

    /**
     * 외곽 사각 영역 그리기
     *
     * @param canvas
     */
    protected void drawBBox(Canvas canvas) {
        if (mBbox != null) {
            Paint paint = new Paint();
            paint.setStrokeWidth(8.0f);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.DKGRAY);
            DashPathEffect dashPathEffect = new DashPathEffect(new float[] {10.0f, 5.0f}, 1.0f);
            paint.setPathEffect(dashPathEffect);
            RectF vRect = new RectF();
            mImageDrawView.sourceToViewRect(mBbox, vRect);
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
        float left = getMinX(mPoints);
        float right = getMaxX(mPoints);
        float top = getMinY(mPoints);
        float bottom = getMaxY(mPoints);
        RectF sRect = new RectF(left, top, right, bottom);

        if (toView) {
            RectF vRect = new RectF();
            return mImageDrawView.sourceToViewRect(sRect, vRect);
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
        return isPreview;
    }

    /**
     * 미리보기 상태 설정
     *
     * @param preview
     */
    public void setPreview(boolean preview) {
        isPreview = preview;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    /**
     * 좌표정보 재설정
     *
     * @param points
     */
    public void updateAnnotEx(ArrayList<PointF> points) {
        initPosition();
        mPoints.addAll(points);
        invalidate();
    }

    /**
     * 화면 좌표를 소스 좌표로 변환
     * @param vx
     * @param vy
     * @return
     */
    protected final PointF viewToSourceCoord(float vx, float vy) {
        if (mImageDrawView == null) {
            return null;
        }
        return mImageDrawView.viewToSourceCoord(vx, vy);
    }

    /**
     * 소스 좌표를 화면 좌표로 변환
     * @param sx
     * @param sy
     * @return
     */
    protected final PointF sourceToViewCoord(float sx, float sy) {
        if (mImageDrawView == null) {
            return null;
        }
        return mImageDrawView.sourceToViewCoord(sx, sy);
    }

    /**
     * bondary box 정보를 이용하여 annotation 갱신
     *
     * @param newBbox
     */
    abstract public void updateAnnotEx(RectF newBbox);

    /**
     * annotation 정보 팝업
     * @param context
     */
    abstract public void showContentsBox(Context context);

    /**
     * 저장 하는 annotation인지 여부
     * @return
     */
    abstract public boolean isInvalidSaveAnnotEx();

    /**
     * annotation 이름
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
     * @return true 이면 annotation 내에 존재, false 이면 annotation 내에 존재 하지 않음
     */
    public boolean isContains(float x, float y) {
        if(this.mBbox == null) {
            return false;
        } else {
            RectF rect = new RectF(this.mBbox);
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
     * annotation type 정의
     */
    public static enum AnnotationType {
        PHOTO,
        TEXT,
        DIMENSION,
        CLOUD,
        INK,
        LINE,
        RECTANGLE,
        ELLIPSE,
        ERASER;

        private AnnotationType() {
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
