package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.UndoManager;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public abstract class DrawToolEraserBase extends BaseDrawTool {

    protected final float MINIMUM_LENGTH;

    protected Map<String, BaseDrawView> selectedDrawViewMap = new HashMap<>();
    private Snackbar snackbar;
    private TextView tvSelectedCnt;
    private Button btnEraserType;
    private Button btnSelectAll;
    private Button btnSelectedDelete;
    private boolean stateSelectedAll = false;

    public DrawToolEraserBase(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
        makeToolSnackbar();

        DisplayMetrics displayMetrics = imageDrawView.getContext().getResources().getDisplayMetrics();
        MINIMUM_LENGTH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics);
    }

    @Override
    protected void touchBegin(int x, int y) {
        previewDrawView = createDrawView(true);
        previewDrawView.addPosition(viewToSourceCoord(x, y));
        imageDrawView.addDrawView(previewDrawView);
    }

    @Override
    protected abstract void touchMove(int x, int y);

    @Override
    protected void touchEnd(int x, int y) {
        imageDrawView.removeDrawView(previewDrawView);
    }

    @Override
    public BaseDrawView singleTapUp(int x, int y) {
        changeSelectedState(viewToSourceCoord(x, y));
        return super.singleTapUp(x, y);
    }

    @Override
    public void enter() {

    }

    /**
     * 선택된 drawView를 해제하고 초기화
     */
    @Override
    public void exit() {
        super.exit();

        for (String key : selectedDrawViewMap.keySet()) {
            selectedDrawViewMap.get(key).setShowBoundaryBox(false);
            selectedDrawViewMap.get(key).invalidate();
        }
        selectedDrawViewMap.clear();

        if (snackbar != null && snackbar.isShown() == true) {
            snackbar.dismiss();
        }
    }

    /**
     * drawView 선택
     */
    protected abstract void addSelected();

    /**
     * 터치 위치의 drawView 선택 상태 변경
     * @param sCoord 소스 좌표
     */
    private void changeSelectedState(PointF sCoord) {
        for (String key : imageDrawView.getDrawViewMap().keySet()) {
            final BaseDrawView drawView = imageDrawView.getDrawViewMap().get(key);
            if (drawView.isPreview() == false && drawView.isContains(sCoord.x, sCoord.y) == true) {
                if (selectedDrawViewMap.containsKey(key) == false) {
                    selectedDrawViewMap.put(key, drawView);
                    drawView.setShowBoundaryBox(true);
                } else {
                    selectedDrawViewMap.remove(key);
                    drawView.setShowBoundaryBox(false);
                }
                drawView.invalidate();
            }
        }
        onChangeSelectedCount();
    }

    /**
     * 선택된 drawView 삭제
     */
    private void remove() {
        final UndoManager.UndoItem UndoItem = this.imageDrawView.addUndoItem(UndoManager.UndoState.REMOVE);
        Iterator<String> iterator = this.selectedDrawViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            UndoItem.pushItem(this.selectedDrawViewMap.get(key));
            this.imageDrawView.removeDrawView(this.selectedDrawViewMap.get(key));
            iterator.remove();
        }
        onChangeSelectedCount();

        if (this.imageDrawView.getDrawViewMap().isEmpty() == true) {
            Snackbar.make(this.imageDrawView, "모두 삭제 되었습니다", Snackbar.LENGTH_SHORT).show();
            if (this.drawToolControllViewListener != null) {
                this.drawToolControllViewListener.changeDefaultDrawTool();
            }
        }

        this.imageDrawView.setEditedDrawView(true);
    }

    /**
     * view의 모든 drawView 선택 또는 선택 해제
     */
    private void changeSelectStateAll() {
        for (String key : imageDrawView.getDrawViewMap().keySet()) {
            final BaseDrawView drawView = imageDrawView.getDrawViewMap().get(key);
            if (drawView.isPreview() == false) {
                if (stateSelectedAll == true) {
                    selectedDrawViewMap.remove(key);
                    drawView.setShowBoundaryBox(false);
                } else {
                    if (selectedDrawViewMap.containsKey(key) == false) {
                        selectedDrawViewMap.put(key, drawView);
                        drawView.setShowBoundaryBox(true);
                    }
                }
                drawView.invalidate();
            }
        }
        onChangeSelectedCount();
    }

    @Override
    protected BaseDrawView createDrawView(final boolean preview) {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, getPreviewType());
        drawView.setPreview(preview);
        drawView.setDashEffect(true);
        drawView.setColor("#4C6C89");
        drawView.setThick(8);
        return drawView;
    }

    protected abstract BaseDrawView.DrawViewType getPreviewType();

    /**
     * 삭제 도구 바
     */
    private void makeToolSnackbar() {
        this.snackbar = Snackbar.make(imageDrawView, "", Snackbar.LENGTH_INDEFINITE);
        this.snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                super.onShown(sb);
                if (showSnackbarListener != null) {
                    showSnackbarListener.onShown(sb.getView().getMeasuredHeight());
                }
            }

            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                if (showSnackbarListener != null) {
                    showSnackbarListener.onDismissed();
                }
            }
        });

        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) this.snackbar.getView();
        snackbarLayout.setBackgroundColor(Color.TRANSPARENT);
        snackbarLayout.findViewById(android.support.design.R.id.snackbar_text).setVisibility(View.INVISIBLE);

        final LayoutInflater inflater = (LayoutInflater) imageDrawView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.snackbar_delete_drawview, null);

        this.btnEraserType = (Button) view.findViewById(R.id.delete_type);
        this.btnEraserType.setText(DrawToolEraserBase.this instanceof DrawToolRectEraser ? R.string.eraser_rectangle : R.string.eraser_free);
        this.btnEraserType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if (DrawToolEraserBase.this instanceof DrawToolRectEraser) {
                    imageDrawView.changeDrawTool(DrawToolType.ERASER_FREE);
                    message = "곡선 선택 삭제로 변경 되었습니다.";
                } else {
                    imageDrawView.changeDrawTool(DrawToolType.ERASER_RECT);
                    message = "범위 선택 삭제로 변경 되었습니다.";
                }
                Toast toast = Toast.makeText(imageDrawView.getContext(), message, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });

        this.tvSelectedCnt = (TextView) view.findViewById(R.id.selected_cnt);
        this.btnSelectAll = (Button) view.findViewById(R.id.select_all);
        this.btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSelectStateAll();
                stateSelectedAll = !stateSelectedAll;
                onChangeSelectAllText();
            }
        });
        onChangeSelectAllText();

        this.btnSelectedDelete = (Button) view.findViewById(R.id.delete);
        this.btnSelectedDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remove();
                checkContinueTool();
            }
        });

        snackbarLayout.addView(view, 0);
        this.snackbar.getView().setPadding(0, 0, 0, 0);
        this.snackbar.show();
    }

    /**
     * 선택된 drawView 갯수 표시
     */
    protected void onChangeSelectedCount() {
        tvSelectedCnt.setText(selectedDrawViewMap.size() + "개선택");
    }

    /**
     * 선택 상태에 따라 선택 버튼 텍스트 변경
     */
    protected void onChangeSelectAllText() {
        btnSelectAll.setText(stateSelectedAll ? R.string.deselect_all : R.string.select_all);
    }
}
