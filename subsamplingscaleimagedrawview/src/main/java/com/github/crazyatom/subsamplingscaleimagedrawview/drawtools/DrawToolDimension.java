package com.github.crazyatom.subsamplingscaleimagedrawview.drawtools;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.DrawViewDimension;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.DrawViewReferenceDimension;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by crazy on 2017-07-11.
 */

public class DrawToolDimension extends BaseDrawTool {

    private boolean isBegin = true;
    private PointF begin;
    private PointF end;
    private Snackbar snackbar;
    private boolean isReferenceDrawMode = false;

    final private String SAVE_DIMENSION_KEY = "save_dimension";

    public DrawToolDimension(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
        showSnackbar();
    }

    @Override
    protected void touchBegin(int x, int y) {
        if (this.isBegin == true) {
            this.begin = getCorrectPos(x, y);
            this.end = this.begin;
            previewDrawView = createDrawView();
            ((DrawViewDimension) previewDrawView).setCurrentState(DrawViewDimension.state.PREVIEW_BEGIN);
            imageDrawView.addDrawView(previewDrawView);
        } else {
            this.end = getCorrectPos(x, y);
            previewDrawView.setPosition(1, this.end);
            previewDrawView.invalidate();
        }
    }

    @Override
    protected void touchMove(int x, int y) {
        if (this.isBegin == true) {
            this.begin = getCorrectPos(x, y);
            this.end = this.begin;
            previewDrawView.setPosition(0, this.begin);
            previewDrawView.setPosition(1, this.end);
            previewDrawView.invalidate();
        } else {
            this.end = getCorrectPos(x, y);
            previewDrawView.setPosition(1, this.end);
            previewDrawView.invalidate();
        }
    }

    @Override
    protected void touchEnd(int x, int y) {
        if (this.isBegin == true) {
            this.isBegin = false;
            ((DrawViewDimension) previewDrawView).setCurrentState(DrawViewDimension.state.PREVIEW_END);
        } else {
            this.isBegin = true;
            ((DrawViewDimension) previewDrawView).setCurrentState(DrawViewDimension.state.COMPLETE);
            this.end = getCorrectPos(x, y);
            imageDrawView.removeDrawView(previewDrawView);
            injectAnnotation();
            checkContinueTool();
        }
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {
        super.exit();

        if (snackbar != null && snackbar.isShown() == true) {
            snackbar.dismiss();
        }
    }

    @Override
    protected void checkContinueTool() {
        if (DrawViewSetting.getInstance().isContinuous() == false || this.isReferenceDrawMode == true) {
            imageDrawView.changeTool(DrawToolType.NONE);
            if (drawToolControllViewListener != null) {
                drawToolControllViewListener.changeDefaultDrawTool();
            }
        }
    }

    @Override
    protected BaseDrawView createDrawView() {
        BaseDrawView drawView = DrawViewFactory.getInstance().create(imageDrawView,
                (isReferenceDrawMode == true) ? BaseDrawView.DrawViewType.DIMENSION_REF : BaseDrawView.DrawViewType.DIMENSION);
        drawView.addPosition(this.begin);
        drawView.addPosition(this.end);

        return drawView;
    }

    /**
     * annotation을 view에 추가
     */
    private void injectAnnotation() {
        if (this.begin != this.end) {
            DrawViewDimension drawView = (DrawViewDimension) createDrawView();
            drawView.setCurrentState(DrawViewDimension.state.COMPLETE);
            imageDrawView.addDrawView(drawView);

            // 기준 치수일 경우 설정 다이얼로그 팝업
            if (isReferenceDrawMode == true) {
                showSetupDialog((DrawViewReferenceDimension) drawView);
            }
        }
    }

    private void showSnackbar() {
        final SharedPreferences sharedPreferences = imageDrawView.getContext().getSharedPreferences("Show_message", Context.MODE_PRIVATE);
        boolean notSeeAgainSaveDimension = sharedPreferences.getBoolean(SAVE_DIMENSION_KEY, false);
        if (notSeeAgainSaveDimension == true) {
            return;
        }

        this.snackbar = Snackbar.make(imageDrawView, "치수 마크업은 페이지 또는 공유 모드 변경시 자동 삭제 됩니다", Snackbar.LENGTH_INDEFINITE);
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
        this.snackbar.setAction("다시 보지 않기", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SAVE_DIMENSION_KEY, true);
                editor.commit();
            }
        });
        this.snackbar.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 snackbar.dismiss();
            }
        });
        this.snackbar.show();
    }

    /**
     * 기준 치수선 작성 모드인지 설정
     * @param referenceDrawMode
     */
    public void setReferenceDrawMode(boolean referenceDrawMode) {
        isReferenceDrawMode = referenceDrawMode;
    }

    /**
     * 기준 치수선 설정 다이얼로그
     * @param referenceDimension
     */
    private void showSetupDialog(final DrawViewReferenceDimension referenceDimension) {
        if (referenceDimension == null) {
            return;
        }

        LayoutInflater inflater = (LayoutInflater) imageDrawView.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View setupView = inflater.inflate(R.layout.dialog_dimension_setup, null);
        final AlertDialog dialog = new AlertDialog.Builder(imageDrawView.getContext())
                .setView(setupView)
                .setCancelable(true)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                        toggleSoftInput(false);
                        imageDrawView.removeDrawView(referenceDimension);
                    }
                })
                .show();

        // show soft keyboard
        toggleSoftInput(true);

        // setup spinner
        final MaterialSpinner spinner = (MaterialSpinner) setupView.findViewById(R.id.dim_unit);
        spinner.setPadding(0, 0, 0, 0);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(DrawViewSetting.DrawViewDimensionUnit.toString(DrawViewSetting.DrawViewDimensionUnit.MM));
        arrayList.add(DrawViewSetting.DrawViewDimensionUnit.toString(DrawViewSetting.DrawViewDimensionUnit.CM));
        arrayList.add(DrawViewSetting.DrawViewDimensionUnit.toString(DrawViewSetting.DrawViewDimensionUnit.M));
        arrayList.add(DrawViewSetting.DrawViewDimensionUnit.toString(DrawViewSetting.DrawViewDimensionUnit.KM));
        spinner.setItems(arrayList);
        spinner.setSelectedIndex(0);

        // setup dimension
        setupView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                toggleSoftInput(false);

                EditText editText = (EditText) setupView.findViewById(R.id.dim_base_comp_len);
                String strLen = editText.getText().toString();
                if (strLen.isEmpty() == true) {
                    imageDrawView.removeDrawView(referenceDimension);
                } else {
                    final int len = Integer.parseInt(editText.getText().toString());
                    final float realLen = Utillity.getDistance(begin, end);
                    referenceDimension.setConversionLength(len);
                    referenceDimension.setDimensionRatioFactor(len / realLen);
                    referenceDimension.setDimensionUnit(DrawViewSetting.DrawViewDimensionUnit.toType(spinner.getText().toString()));
                    referenceDimension.invalidate();

                    // remove current reference dimension
                    DrawViewReferenceDimension current = imageDrawView.getReferenceDimension();
                    if (current != null && referenceDimension.equals(current) == false) {
                        imageDrawView.removeDrawView(current);
                    }
                }
            }
        });

        // cancel setup
        setupView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });
    }

    /**
     * 터치 위치에서 y축으로 일정 간격 떨어진 보정 위치
     * @param x
     * @param y
     * @return PointF
     */
    private PointF getCorrectPos(int x, int y) {
        DisplayMetrics displayMetrics = imageDrawView.getContext().getResources().getDisplayMetrics();
        final int offset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, displayMetrics);
        return viewToSourceCoord(x, (y - offset));
    }
}
