package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
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
        this.textSizeMap.put(R.id.btn_freetext_size1, 20);
        this.textSizeMap.put(R.id.btn_freetext_size2, 40);
        this.textSizeMap.put(R.id.btn_freetext_size3, 60);
        this.textSizeMap.put(R.id.btn_freetext_size4, 80);
        this.textSizeMap.put(R.id.btn_freetext_size5, 100);
    }

    @Override
    public void update(RectF newBbox) {
        ArrayList<PointF> points = new ArrayList<>();
        points.add(new PointF(newBbox.left, newBbox.top + getThick()));
        update(points);
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
    public boolean isInvalidSaveAnnotEx() {
        return true;
    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "T" : getResources().getString(R.string.text);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 1) {
            loadPaint();
            setBoundaryBox();

            final PointF vCoord = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
            canvas.drawText(content, vCoord.x, vCoord.y, paint);

            super.onDraw(canvas);
        }
    }

    protected void setBoundaryBox() {
        final PointF position = getPosition(0);
        final float textWidth = paint.measureText(content) / imageDrawView.getScale();
        setBoundaryBox(new RectF(position.x, position.y - getThick(), position.x + textWidth, position.y));
    }

    /**
     * 페인트 설정
     *
     */
    private void loadPaint() {
        this.paint.setColor(Color.parseColor(color));
        this.paint.setTextSize(getThick() * imageDrawView.getScale());
        this.paint.setAntiAlias(true);
    }

    @Override
    public boolean isContains(float x, float y) {
        ArrayList<PointF> boundary = new ArrayList<>();
        boundary.add(new PointF(boundaryBox.left, boundaryBox.top));
        boundary.add(new PointF(boundaryBox.right, boundaryBox.top));
        boundary.add(new PointF(boundaryBox.right, boundaryBox.bottom));
        boundary.add(new PointF(boundaryBox.left, boundaryBox.bottom));
        return Utillity.isInside(new PointF(x, y), boundary);
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

        this.dialog.findViewById(R.id.btn_cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        this.dialog.findViewById(R.id.btn_ok).setOnClickListener(new OnClickListener() {
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
            invalidate();
        } else {
            //내용이 없을 경우 view에서 drawView를 삭제
            imageDrawView.removeDrawView(this);
        }
        toggleSoftInput(false);

        checkContinueTool();
    }
}
