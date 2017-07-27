package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawToolFreeEraser extends BaseDrawTool {

    private final float MINIMUM_LENGTH = 10.0f;

    private Map<String, BaseDrawView> selectedDrawViewMap = new HashMap<>();
    private Snackbar snackbar;
    private TextView tvSelectedCnt;
    private Button btnSelectAll;
    private Button btnSelectedDelete;
    private boolean stateSelectedAll = false;

    public DrawToolFreeEraser(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
        makeToolSnackbar();
    }

    @Override
    protected void touchBegin(int x, int y) {
        previewDrawView = createDrawView();
        previewDrawView.addPosition(viewToSourceCoord(x, y));
        imageDrawView.addDrawView(previewDrawView);
    }

    @Override
    protected void touchMove(int x, int y) {
        final PointF vCoord = viewToSourceCoord(x, y);
        previewDrawView.addPosition(vCoord);
        previewDrawView.invalidate();

        final PointF pt1 = previewDrawView.getPosition(0);
        final PointF pt2 = previewDrawView.getPosition(previewDrawView.getPositionSize() - 1);
        if (Utillity.getDistance(pt1, pt2) > MINIMUM_LENGTH) {
            addSelected(vCoord);
        }
    }

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
     * 터치 위치에 포함되는 drawView를 선택 맵에 추가
     * @param sCoord 소스 좌표
     */
    private void addSelected(PointF sCoord) {
        for (String key : imageDrawView.getDrawViewMap().keySet()) {
            final BaseDrawView drawView = imageDrawView.getDrawViewMap().get(key);
            if (drawView.isPreview() == false && drawView.isContains(sCoord.x, sCoord.y) == true) {
                selectedDrawViewMap.put(key, drawView);
                drawView.setShowBoundaryBox(true);
                drawView.invalidate();
            }
        }
        onChangeSelectedCount();
    }

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
        Iterator<String> iterator = selectedDrawViewMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            imageDrawView.removeDrawView(selectedDrawViewMap.get(key));
            iterator.remove();
        }
        onChangeSelectedCount();

        if (imageDrawView.getDrawViewMap().isEmpty() == true) {
            Snackbar.make(imageDrawView, "모두 삭제 되었습니다", Snackbar.LENGTH_SHORT).show();
            if (drawToolControllViewListener != null) {
                drawToolControllViewListener.changeDefaultDrawTool();
            }
        }

        imageDrawView.setEditedDrawView(true);
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
    protected BaseDrawView createDrawView() {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView, BaseDrawView.DrawViewType.INK);
        drawView.setPreview(true);
        drawView.setThick(8);
        return drawView;
    }

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
    private void onChangeSelectedCount() {
        tvSelectedCnt.setText(selectedDrawViewMap.size() + "개선택");
    }

    /**
     * 선택 상태에 따라 선택 버튼 텍스트 변경
     */
    private void onChangeSelectAllText() {
        btnSelectAll.setText(stateSelectedAll ? R.string.deselect_all : R.string.select_all);
    }
}
