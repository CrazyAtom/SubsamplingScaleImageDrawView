/*
Copyright 2014 David Morrissey

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package kr.co.aroad.subscaleimagedrawview.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.aroad.subscaleimagedrawview.listener.GestureListener;
import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.drawtools.*;

/**
 * Created by crazy on 2017-07-11.
 */

public class ImageDrawView extends SubsamplingScaleImageView implements View.OnTouchListener, GestureListener {

    private ImageDrawView.GestureType gestureType = GestureType.VIEW;
    private BaseDrawTool drawTool = null;
    private Map<String, BaseDrawView> drawViewMap = new HashMap<>();
    private boolean isEditedDrawView = false;

    private PointF vPrevious;
    private PointF vStart;
    private boolean drawing = false;

    private int strokeWidth;

    private List<PointF> sPoints;

    public ImageDrawView(Context context, AttributeSet attr) {
        super(context, attr);
        initialise();
    }

    public ImageDrawView(Context context) {
        super(context, null);
    }

    private void initialise() {
        setOnTouchListener(this);
        setGestureListener(this);

        float density = getResources().getDisplayMetrics().densityDpi;
        strokeWidth = (int)(density/60f);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (this.drawTool != null && this.gestureType == GestureType.EDIT) {
            return drawViewTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * drawView touch event
     * @param event
     * @return boolean
     */
    private boolean drawViewTouchEvent(@NonNull MotionEvent event) {
        boolean consumed = false;
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.drawTool.onTouchBegin(x, y);
                break;
            case MotionEvent.ACTION_UP:
                this.drawTool.onTouchEnd(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                this.drawTool.onTouchMove(x, y);
                consumed = true;
                break;
        }
        return consumed || super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw anything before image is ready.
        if (!isReady()) {
            return;
        }

        for (String key : this.drawViewMap.keySet()) {
            this.drawViewMap.get(key).invalidate();
        }
    }

    public void reset() {
        this.sPoints = null;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int w = right - left;
        final int h = bottom - top;

        for (String key : this.drawViewMap.keySet()) {
            this.drawViewMap.get(key).bringToFront();
            this.drawViewMap.get(key).layout(0, 0, w, h);
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * drawView 추가
     *  - drawView 리스트에 추가
     *  - drawView child view 추가
     * @param drawView 추가할 drawView 뷰
     */
    public void addDrawView(@NonNull BaseDrawView drawView) {
        this.drawViewMap.put(drawView.getUniqId(), drawView);
        addView(drawView);
    }

    /**
     * drawView 삭제
     *  - drawView 리스트에서 삭제
     *  - drawView child view 삭제
     *  - 삭제 리스트에 추가 (database에 저장 할때 삭제 플래그 처리를 위해)
     *
     * @param drawView 삭제할 drawView 뷰
     */
    public void removeDrawView(@NonNull BaseDrawView drawView) {
        this.drawViewMap.remove(drawView.getUniqId());
        removeView(drawView);
    }

    public void changeTool(BaseDrawTool.DrawToolType toolType) {
        if (drawTool != null) {
            drawTool.exit();
        }

        switch (toolType) {
            case NONE:
                this.drawTool = null;
                this.gestureType = GestureType.VIEW;
                break;
            case PHOTO:
                break;
            case TEXT:
                break;
            case DIMENSION:
                break;
            case CLOUD:
                break;
            case INK:
                this.drawTool = new DrawToolInk(this);
                this.gestureType = GestureType.EDIT;
                break;
            case LINE:
                break;
            case RECTANGLE:
                this.drawTool = new DrawToolRectangle(this);
                this.gestureType = GestureType.EDIT;
                break;
            case ELLIPSE:
                this.drawTool = new DrawToolEllipse(this);
                this.gestureType = GestureType.EDIT;
                break;
            case ERASER:
                this.drawTool = new DrawToolFreeEraser(this);
                this.gestureType = GestureType.EDIT;
                break;
            case TRANSFORM:
                this.drawTool = new DrawToolTransform(this);
                this.gestureType = GestureType.EDIT;
                break;
            default:
                this.drawTool = null;
                this.gestureType = GestureType.VIEW;
                break;
        }
    }

    @Override
    public void onLongPress(@NonNull MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        // VIEW 상태에서 drawView 위치에 터치 하면 편집 tool로 변경하고 longPress
        if (this.gestureType == GestureType.VIEW) {
            BaseDrawView selectedAnnotation = getSelectedDrawView(new PointF(x, y));
            if (selectedAnnotation != null) {
                changeTool(BaseDrawTool.DrawToolType.TRANSFORM);
                ((DrawToolTransform) this.drawTool).setSelectedDrawView(selectedAnnotation);
                this.drawTool.longPress(x, y);
            }
            // EDIT 상태에서는 tool의 longPress
        } else {
            if (this.drawTool != null) {
                this.drawTool.longPress(x, y);
            }
        }
    }

    @Override
    public void onSingleTabUp(@NonNull MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        // VIEW 상태에서 drawView 위치에 터치 하면 편집 tool로 변경하고 singleTapUp
        if (this.gestureType == GestureType.VIEW) {
            BaseDrawView selectedAnnotation = getSelectedDrawView(new PointF(x, y));
            if (selectedAnnotation != null) {
                changeTool(BaseDrawTool.DrawToolType.TRANSFORM);
                ((DrawToolTransform) this.drawTool).setSelectedDrawView(selectedAnnotation);
                this.drawTool.singleTapUp(x, y);
            }
        // EDIT 상태에서는 tool의 singleTapUp
        } else {
            if (this.drawTool != null) {
                this.drawTool.singleTapUp(x, y);
            }
        }
    }

    /**
     * point에 위치하는 drawView 찾기
     * @param point
     * @return drawView
     */
    protected BaseDrawView getSelectedDrawView(PointF point) {
        PointF sc = viewToSourceCoord(point.x, point.y);
        for (String key : this.drawViewMap.keySet()) {
            if (this.drawViewMap.get(key).isContains(sc.x, sc.y) == true) {
                return this.drawViewMap.get(key);
            }
        }
        return null;
    }

    /**
     * drawView 리스트 반환
     * @return drawView map
     */
    public Map<String, BaseDrawView> getDrawViewMap() {
        return this.drawViewMap;
    }

    /**
     * drawView 편집 여부
     * @return boolean
     */
    public boolean isEditedDrawView() {
        return this.isEditedDrawView;
    }

    /**
     * drawView 편집 여부 설정
     * @param editedDrawView boolean
     */
    public void setEditedDrawView(boolean editedDrawView) {
        this.isEditedDrawView = editedDrawView;
    }

    /**
     * 현재 제스쳐의 상태
     * VIEW : 화면 보기 상태
     * EDIT : 화면 편집 상태
     */
    public static enum GestureType {
        VIEW,
        EDIT
    }
}
