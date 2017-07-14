package kr.co.aroad.subscaleimagedrawview.drawtools;

import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

import kr.co.aroad.subscaleimagedrawview.listener.DrawViewTransformListener;
import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 13..
 */

public class DrawToolTransform extends BaseDrawTool implements DrawViewTransformListener {

    private BaseDrawView selectedDrawView;
    private PointF beginPoint;
    private BaseEditPinView baseEditPinView;

    public DrawToolTransform(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
    }

    public void setSelectedDrawView(@NonNull BaseDrawView selectedDrawView) {
        this.selectedDrawView = selectedDrawView;
    }

    @Override
    public BaseDrawView singleTapUp(int x, int y) {
        if (this.selectedDrawView == null) {
            return null;
        }

        if (this.selectedDrawView.getType() == BaseDrawView.DrawViewType.PHOTO
                || this.selectedDrawView.getType() == BaseDrawView.DrawViewType.TEXT) {
            this.selectedDrawView.showContentsBox(imageDrawView.getContext());
            exitTransformState();
        } else {
            onTouchBegin(x, y);
        }

        return this.selectedDrawView;
    }

    @Override
    public BaseDrawView longPress(int x, int y) {
        if (this.selectedDrawView == null) {
            return null;
        }

        if (this.selectedDrawView.getType() == BaseDrawView.DrawViewType.PHOTO
                || this.selectedDrawView.getType() == BaseDrawView.DrawViewType.TEXT) {
            onTouchBegin(x, y);
        }

        return this.selectedDrawView;
    }

    @Override
    protected void touchBegin(int x, int y) {
        if (this.selectedDrawView == null) {
            return;
        }

        beginPoint = viewToSourceCoord(x, y);
        this.selectedDrawView.setEditable(true);
        if (baseEditPinView == null) {
            baseEditPinView = newPinView(this.selectedDrawView);
            baseEditPinView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    PointF sCoord = viewToSourceCoord(motionEvent.getX(), motionEvent.getY());
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            baseEditPinView.setPinPressed(sCoord.x, sCoord.y);
                            if (baseEditPinView.isPinPressed() == true) {
                                baseEditPinView.invalidate();
                            } else if (baseEditPinView.isContains(sCoord.x, sCoord.y) == true) {
                                beginPoint = viewToSourceCoord(motionEvent.getX(), motionEvent.getY());
                                setBeginEdited(true);
                            } else {
                                exitTransformState();
                            }
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if (baseEditPinView.isPinPressed() == true) {
                                if (isInsideView(motionEvent.getX(), motionEvent.getY(), motionEvent.getX(), motionEvent.getY()) == true) {
                                    baseEditPinView.setMovePin(new PointF(sCoord.x, sCoord.y));
                                    baseEditPinView.invalidate();
                                    if (selectedDrawView.getType() == BaseDrawView.DrawViewType.DIMENSION
                                            || selectedDrawView.getType() == BaseDrawView.DrawViewType.LINE) {
                                        selectedDrawView.update(baseEditPinView.getPinPoints());
                                    } else {
                                        update(selectedDrawView, baseEditPinView.getBoundaryBox());
                                    }
                                }
                            } else {
                                onTouchMove((int) motionEvent.getX(), (int) motionEvent.getY());
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            exitTransformState();
                            break;
                    }
                    return imageDrawView.onTouchEvent(motionEvent);
                }
            });
            baseEditPinView.setClickable(true);
            imageDrawView.addView(baseEditPinView);
            baseEditPinView.layout(0, 0, imageDrawView.getWidth(), imageDrawView.getHeight());
            baseEditPinView.invalidate();
        }
    }

    @Override
    protected void touchMove(int x, int y) {
        if (this.selectedDrawView != null && this.selectedDrawView.isEditable() == true && baseEditPinView.isPinPressed() == false) {
            PointF sc = viewToSourceCoord(x, y);
            RectF sRect = this.selectedDrawView.getBoundaryBox();
            final float sDistX = sc.x - beginPoint.x;
            final float sDistY = sc.y - beginPoint.y;
            beginPoint = sc;

            RectF vRect = new RectF();
            imageDrawView.sourceToViewRect(sRect, vRect);
            final float vDistX = sDistX * imageDrawView.getScale();
            final float vDistY = sDistY * imageDrawView.getScale();
            if (isInsideView(vRect.left + vDistX, vRect.top + vDistY, vRect.right + vDistX, vRect.bottom + vDistY) == true) {
                update(this.selectedDrawView,
                        new RectF(sRect.left + sDistX, sRect.top + sDistY, sRect.right + sDistX, sRect.bottom + sDistY));
                if (baseEditPinView != null) {
                    baseEditPinView.initPinList();
                    baseEditPinView.invalidate();
                }
            }
        }
    }

    @Override
    protected void touchEnd(int x, int y) {

    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        if (baseEditPinView != null) {
            imageDrawView.removeView(baseEditPinView);
            imageDrawView.invalidate();
        }
    }

    @Override
    protected BaseDrawView createDrawView() {
        return null;
    }

    /**
     * drawView type에 따른 pinView생성
     * @param annotation
     * @return
     */
    private BaseEditPinView newPinView(BaseDrawView annotation) {
        switch (annotation.getType()) {
            case DIMENSION:
            case LINE:
                return new EditPinViewLine(imageDrawView, annotation);
            case PHOTO:
            case TEXT:
                return new EditPinViewPoint(imageDrawView, annotation);
            default:
                return new EditPinViewRect(imageDrawView, annotation);
        }
    }

    /**
     * boundary box를 이용한 update
     * @param annotation
     * @param newBoundaryBox
     */
    private void update(BaseDrawView annotation, RectF newBoundaryBox) {
        if (this.selectedDrawView != null) {
            this.selectedDrawView.update(newBoundaryBox);
        }
    }

    /**
     * 편집 상태의 drawView 편집 상태 해제
     */
    private void exitTransformState() {
        if (baseEditPinView != null) {
            imageDrawView.removeView(baseEditPinView);
            imageDrawView.invalidate();
        }

        imageDrawView.changeTool(DrawToolType.NONE);

        if (this.selectedDrawView != null) {
            this.selectedDrawView.setEditable(false);
            this.selectedDrawView = null;
        }
    }

    @Override
    public void onChangeThick(int thick) {
        if (this.selectedDrawView != null) {
            this.selectedDrawView.setThick(thick);
            this.selectedDrawView.invalidate();
        }
    }

    @Override
    public void onChangeColor(int color) {
        if (this.selectedDrawView != null) {
            this.selectedDrawView.setColor(Utillity.getColorString(color));
            this.selectedDrawView.invalidate();
        }
    }
}
