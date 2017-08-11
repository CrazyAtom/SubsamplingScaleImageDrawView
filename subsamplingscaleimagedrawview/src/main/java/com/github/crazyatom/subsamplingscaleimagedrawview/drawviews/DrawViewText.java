package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseEditPinView;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.EditPinViewPoint;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewText extends BaseDrawView {

    private Map<Integer, Integer> textSizeMap = new HashMap<>();
    private String content = "";
    private int textSizeKey = R.id.btn_freetext_size2;
    private Dialog dialog;

    public DrawViewText(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.TEXT, imageDrawView);

        this.textSizeMap.clear();
        this.textSizeMap.put(R.id.btn_freetext_size1, 40);
        this.textSizeMap.put(R.id.btn_freetext_size2, 80);
        this.textSizeMap.put(R.id.btn_freetext_size3, 120);
        this.textSizeMap.put(R.id.btn_freetext_size4, 160);
        this.textSizeMap.put(R.id.btn_freetext_size5, 200);
    }

    @Override
    public void showContentsBox(Context context) {
        if (this.dialog == null) {
            createDialog(getContext());
        } else {
            this.dialog.show();
        }

        if (this.content != null) {
            EditText editText = (EditText) this.dialog.findViewById(R.id.contents);
            editText.setText(this.content);
        }

        toggleSoftInput(true);
    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "T" : getResources().getString(R.string.text);
    }

    @Override
    protected void preCalc() {

    }

    @Override
    public BaseEditPinView getEditPinView() {
        return new EditPinViewPoint(imageDrawView, this);
    }

    @Override
    public boolean isValidEditedFlag() {
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sRegion == null) {
            return;
        }

        loadPaint();
        setSourceRegion();

        final PointF sCoord = getPosition(0);
        final PointF vCoord = sourceToViewCoord(sCoord.x, sCoord.y);
        canvas.drawText(content, vCoord.x, vCoord.y, paint);

        super.onDraw(canvas);
    }

    @Override
    protected void setSourceRegion() {
        final PointF sCoord = getPosition(0);
        final float height = textHeight();
        final float width = textWidth();
        final float offset = getThick() / 2;
        sRegion = new RectF(sCoord.x - offset, sCoord.y - (height + offset),
                sCoord.x + (width + offset), sCoord.y + offset);
    }

    private float textWidth() {
        return paint.measureText(content) / imageDrawView.getScale();
    }

    private float textHeight() {
        return getThick();
    }

    /**
     * 페인트 설정
     *
     */
    @Override
    protected void loadPaint() {
        super.loadPaint();
        this.paint.setTextSize(getThick() * imageDrawView.getScale());
    }

    @Override
    public int getThick() {
        return this.textSizeMap.get(textSizeKey);
    }

    @Override
    public void setThick(int thick) {
        for (int key : textSizeMap.keySet()) {
            if (textSizeMap.get(key) == thick) {
                textSizeKey = key;
            }
        }
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private void createDialog(final Context context) {
        final DrawViewSetting setting = DrawViewSetting.getInstance();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_drawview_text, null);

        this.dialog = new Dialog(context);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setContentView(view);
        this.dialog.setCancelable(true);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                onUpdateContent(context, content.compareTo("") != 0);
                dialog.dismiss();
            }
        });
        this.dialog.show();

        setThick(setting.getTextSize());
        final RadioGroup group = (RadioGroup) view.findViewById(R.id.group_text_size);
        group.check(textSizeKey);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
                textSizeKey = checkedId;
                setting.setTextSize(getThick());
            }
        });

        this.dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        this.dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = ((EditText) dialog.findViewById(R.id.contents)).getText().toString();
                onUpdateContent(context, content.compareTo("") != 0);
                dialog.dismiss();
            }
        });
    }

    /**
     * 텍스트 편집 상태에 따른 처리
     * @param context
     * @param state 편집 후 상태
     *              true이면 편집 성공, false이면 내용이 없음
     */
    private void onUpdateContent(Context context, boolean state) {
        if (state == true) {
            imageDrawView.setEditedDrawView(true);
            invalidate();
        } else {
            //내용이 없을 경우 view에서 drawView를 삭제
            imageDrawView.removeDrawView(this);
        }
        toggleSoftInput(false);
        checkContinueTool();
    }
}
