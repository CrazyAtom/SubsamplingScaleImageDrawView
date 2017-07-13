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

import kr.co.aroad.subscaleimagedrawview.Listener.GestureListener;
import kr.co.aroad.subscaleimagedrawview.annotations.BaseAnnotation;
import kr.co.aroad.subscaleimagedrawview.annotationtools.AnnotationToolEllipse;
import kr.co.aroad.subscaleimagedrawview.annotationtools.AnnotationToolInk;
import kr.co.aroad.subscaleimagedrawview.annotationtools.BaseAnnotationTool;

/**
 * Created by crazy on 2017-07-11.
 */

public class ImageDrawView extends SubsamplingScaleImageView implements View.OnTouchListener, GestureListener {

    private ImageDrawView.GestureType gestureType = GestureType.VIEW;
    private BaseAnnotationTool annotationTool = null;
    private Map<String, BaseAnnotation> annotationMap = new HashMap<>();

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
        if (annotationTool != null && gestureType == GestureType.EDIT) {
            return annotationTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * annotation touch event
     * @param event
     * @return boolean
     */
    private boolean annotationTouchEvent(@NonNull MotionEvent event) {
        boolean consumed = false;
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                annotationTool.onTouchBegin(x, y);
                break;
            case MotionEvent.ACTION_UP:
                annotationTool.onTouchEnd(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                annotationTool.onTouchMove(x, y);
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

        for (String key : annotationMap.keySet()) {
            annotationMap.get(key).invalidate();
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

        for (String key : annotationMap.keySet()) {
            annotationMap.get(key).bringToFront();
            annotationMap.get(key).layout(0, 0, w, h);
        }

        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * annotation 추가
     *  - annotation 리스트에 추가
     *  - annotation child view 추가
     * @param annotation 추가할 annotation 뷰
     */
    public void addAnnotation(BaseAnnotation annotation) {
        if (annotation != null) {
            annotationMap.put(annotation.getUniqId(), annotation);
            addView(annotation);
        }
    }

    /**
     * annotation 삭제
     *  - annotation 리스트에서 삭제
     *  - annotation child view 삭제
     *  - 삭제 리스트에 추가 (database에 저장 할때 삭제 플래그 처리를 위해)
     *
     * @param annotation 삭제할 annotation 뷰
     */
    public void removeAnnotation(BaseAnnotation annotation) {
        annotationMap.remove(annotation.getUniqId());
        removeView(annotation);
    }

    public void changeTool(BaseAnnotationTool.AnnotationToolType toolType) {
        switch (toolType) {
            case NONE:
                annotationTool = null;
                gestureType = GestureType.VIEW;
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
                annotationTool = new AnnotationToolInk(getContext(), this);
                gestureType = GestureType.EDIT;
                break;
            case LINE:
                break;
            case RECTANGLE:
                break;
            case ELLIPSE:
                annotationTool = new AnnotationToolEllipse(getContext(), this);
                gestureType = GestureType.EDIT;
                break;
            case ERASER:
                break;
            case TRANSFORM:
                break;
            default:
                annotationTool = null;
                gestureType = GestureType.VIEW;
        }
    }

    @Override
    public void onLongPress(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        if (annotationTool != null) {
            annotationTool.longPress(x, y);
        }
    }

    @Override
    public void onSingleTabUp(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        if (annotationTool != null) {
            annotationTool.singleTapUp(x, y);
        }
    }

    /**
     * annotation 리스트 반환
     * @return annotation map
     */
    public Map<String, BaseAnnotation> getAnnotationMap() {
        return annotationMap;
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
