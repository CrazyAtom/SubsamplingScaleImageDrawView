package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseDrawTool;
import com.github.crazyatom.subsamplingscaleimagedrawview.Event.DrawToolControllViewListener;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseEditPinView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public abstract class BaseDrawView {

    private DrawViewType type;
    private String uniqId;
    private String creater;
    private long updateTime;

    private ArrayList<PointF> pointList = new ArrayList<>();
    protected ImageDrawView imageDrawView;
    protected Paint paint = new Paint();

    protected int thick;
    protected String color;
    private float phase = 0.0f;

    protected boolean showBoundaryBox = false;
    protected RectF sRegion = null;

    private boolean visibility = true;
    private boolean isPreview = false;
    private boolean isEditable = false;
    private boolean isDashEffect = false;

    protected DrawToolControllViewListener drawToolControllViewListener;

    public BaseDrawView(DrawViewType type, @NonNull ImageDrawView imageDrawView) {
        this.type = type;
        this.imageDrawView = imageDrawView;

        this.type = type;
        this.imageDrawView = imageDrawView;
        if (DrawViewFactory.getInstance().getNewDrawViewCallback() != null) {
            setUniqId(DrawViewFactory.getInstance().getNewDrawViewCallback().newUUID());
            setCreater(DrawViewFactory.getInstance().getNewDrawViewCallback().creater());
        } else {
            setUniqId(Utillity.getUUID());
            setCreater("홍길동");
        }

        this.updateTime = System.currentTimeMillis();
        this.thick = 4;
        color = Utillity.getColorString(Color.BLACK);
    }

    protected void initPosition() {
        this.pointList.clear();
    }

    public DrawViewType getType() {
        return this.type;
    }

    public void setType(DrawViewType type) {
        this.type = type;
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
        setSourceRegion();
        preCalc();
    }

    public void setPosition(int index, PointF position) {
        try {
            this.pointList.set(index, position);
            setSourceRegion();
            preCalc();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void addPosition(PointF position) {
        this.pointList.add(new PointF(position.x, position.y));
        setSourceRegion();
        preCalc();
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

    public RectF getSourceRegion() {
        return this.sRegion;
    }

    protected void setSourceRegion() {
        this.sRegion = getRect(false);
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setDrawToolControllViewListener(DrawToolControllViewListener drawToolControllViewListener) {
        this.drawToolControllViewListener = drawToolControllViewListener;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
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

    public void onDraw(Canvas canvas) {
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
        if (this.sRegion != null) {
            Paint paint = new Paint();
            paint.setStrokeWidth(8.0f);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.DKGRAY);
            DashPathEffect dashPathEffect = new DashPathEffect(new float[] {10.0f, 5.0f}, phase);
            paint.setPathEffect(dashPathEffect);
            RectF vRect = new RectF();
            this.imageDrawView.sourceToViewRect(this.sRegion, vRect);
            canvas.drawRect(vRect, paint);

            phase = (phase < 10.0f) ? phase + 1.0f : 0.0f;
//            this.postInvalidateOnAnimation();
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

    /**
     * 편집 상태 설정
     * @return
     */
    public boolean isEditable() {
        return this.isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable = editable;
    }

    /**
     * dash로 표현할지 여부 설정
     * @return
     */
    public boolean isDashEffect() {
        return isDashEffect;
    }

    public void setDashEffect(boolean dashEffect) {
        isDashEffect = dashEffect;
    }

    /**
     * 점선 효과
     * @return 점선 효과 적용 여부에 따라 이펙트 반환
     */
    @Nullable
    protected DashPathEffect getDashPathEffect() {
        return isDashEffect ? new DashPathEffect(new float[] {40, 20}, 0.0f) : null;
    }

    /**
     * 좌표정보 재설정
     *
     * @param points
     */
    public void update(ArrayList<PointF> points) {
        setPoints(points);
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
    public void update(RectF newBbox) {
        final RectF originalBbox = getSourceRegion();

        if (newBbox != null && originalBbox != null) {
            final float scaleX = newBbox.width() / originalBbox.width();
            final float scaleY = newBbox.height() / originalBbox.height();

            ArrayList<PointF> points = new ArrayList<>();
            for (int updateIdx = 0; updateIdx < getPositionSize(); ++updateIdx) {
                PointF currPoint = getPosition(updateIdx);
                PointF newPoint = new PointF();
                newPoint.x = (currPoint.x - originalBbox.left) * scaleX + newBbox.left;
                newPoint.y = (currPoint.y - originalBbox.top) * scaleY + newBbox.top;
                points.add(newPoint);
            }

            update(points);
        }
    }

    /**
     * drawView 정보 팝업
     * @param context
     */
    public abstract void showContentsBox(Context context);

    /**
     * drawView 이름
     *
     * @return
     */
    public abstract String getName(boolean isSimple);

    /**
     * 필요한 정보 미리 계산
     */
    protected abstract void preCalc();

    /**
     * 편집 view
     * @return
     */
    public abstract BaseEditPinView getEditPinView();

    /**
     * 편집 flag 설정이 유효한지 여부
     * @return
     */
    public abstract boolean isValidEditedFlag();

    /**
     * 좌표 x, y가 annotation내에 존재 하는지 체크
     * @param x
     * @param y
     * @return true 이면 drawView 내에 존재, false 이면 drawView 내에 존재 하지 않음
     */
    public boolean isContains(float x, float y) {
        if(this.sRegion == null) {
            return false;
        } else {
            return sRegion.contains(x, y);
        }
    }

    /**
     * 소프트키보드 show/hide
     * @param show boolean
     */
    protected void toggleSoftInput(final boolean show) {
        InputMethodManager manager = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
        if (show == true) {
            manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        } else {
            manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    /**
     * tool을 연속으로 수행할지 여부 확인하여 연속 수행이 아니면 tool 해제
     */
    public void checkContinueTool() {
        if (DrawViewSetting.getInstance().isContinuous() == false) {
            imageDrawView.changeTool(BaseDrawTool.DrawToolType.NONE);
            if (drawToolControllViewListener != null) {
                drawToolControllViewListener.changeDefaultDrawTool();
            }
        }
    }

    /**
     * paint 기본 설정
     */
    protected void loadPaint() {
        paint.setColor(Color.parseColor(color));
        paint.setStrokeWidth(thick);
        paint.setAntiAlias(true);
        paint.setPathEffect(getDashPathEffect());
    }

    protected Resources getResources() {
        return imageDrawView.getResources();
    }

    protected Context getContext() {
        return imageDrawView.getContext();
    }

    /**
     * view 갱신
     */
    public void invalidate() {
        RectF invalidArea = new RectF();
        imageDrawView.sourceToViewRect(getSourceRegion(), invalidArea);
        imageDrawView.invalidate((int)invalidArea.left, (int)invalidArea.top, (int)invalidArea.right, (int)invalidArea.bottom);
    }

    /**
     * drawView type 정의
     */
    public static enum DrawViewType {
        PHOTO,
        TEXT,
        DIMENSION,
        DIMENSION_REF,
        CLOUD,
        INK,
        LINE,
        RECTANGLE,
        ELLIPSE;

        private DrawViewType() {
        }
    }
}
