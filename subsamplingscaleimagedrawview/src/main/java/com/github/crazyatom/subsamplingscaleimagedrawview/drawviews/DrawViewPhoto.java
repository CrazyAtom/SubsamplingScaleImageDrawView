package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseEditPinView;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.EditPinViewPoint;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewFactory;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.DrawViewSetting;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;
import com.github.crazyatom.subsamplingscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewPhoto extends BaseDrawView {

    private ArrayList<PhotoItem> photoItems = new ArrayList<>();
    private ArrayList<String> removeItemFilenames = new ArrayList<>();
    private Bitmap bitmap;
    private byte dir = BaseEditPinView.Pin.RectPos.NONE.getPos();
    private final BaseEditPinView.Pin.RectPos[] dirs = {
            BaseEditPinView.Pin.RectPos.LEFT,
            BaseEditPinView.Pin.RectPos.TOP,
            BaseEditPinView.Pin.RectPos.RIGHT,
            BaseEditPinView.Pin.RectPos.BOTTOM,
            BaseEditPinView.Pin.RectPos.LEFT_TOP,
            BaseEditPinView.Pin.RectPos.RIGHT_TOP,
            BaseEditPinView.Pin.RectPos.RIGHT_BOTTOM,
            BaseEditPinView.Pin.RectPos.LEFT_BOTTOM
    };
    private Map<BaseEditPinView.Pin.RectPos, ArrowGeoInfo> arrowInfoMap;

    public DrawViewPhoto(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.PHOTO, imageDrawView);
        bitmap = createBitmap();
    }

    @Override
    public void showContentsBox(Context context) {
        // call managed photo window
        if (DrawViewFactory.getInstance().getAddDrawViewPhotoListener() != null) {
            DrawViewFactory.getInstance().getAddDrawViewPhotoListener().showAddPhotoWindow(this.getUniqId());
        }
    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "P" : getResources().getString(R.string.photo);
    }

    @Override
    protected void preCalc() {
        this.arrowInfoMap = new HashMap<>();
        for (BaseEditPinView.Pin.RectPos dir : dirs) {
            this.arrowInfoMap.put(dir, getArrowGeoInfo(dir));
        }
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
    public void setColor(String color) {
        super.setColor(color);
        bitmap = createBitmap();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (sRegion == null) {
            return;
        }
        loadPaint();
        setSourceRegion();

        final float width = getBitmapSizeView(true) / 2;
        final float height = getBitmapSizeView(false) / 2;

        // create bitmap
        final PointF vCoord = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
        canvas.drawBitmap(bitmap, null, new RectF(vCoord.x - width, vCoord.y - height,
                vCoord.x + width, vCoord.y + height), null);

        // draw arrow
        for (BaseEditPinView.Pin.RectPos dir : dirs) {
            if (isDir(dir) == true) {
                drawArrow(canvas, dir);
            }
        }

        super.onDraw(canvas);
    }

    /**
     * 방향 표시 화살표
     * @param canvas
     */
    private void drawArrow(Canvas canvas, final BaseEditPinView.Pin.RectPos dir) {
        final PointF sCoord = this.arrowInfoMap.get(dir).sCoord;
        final PointF vCoord = sourceToViewCoord(sCoord.x, sCoord.y);
        PointF vCoord1 = new PointF(vCoord.x, vCoord.y);
        PointF vCoord2 = new PointF(vCoord.x, vCoord.y);
        PointF vCoord3 = new PointF(vCoord.x, vCoord.y);

        vCoord1.offset(this.arrowInfoMap.get(dir).offset1.x, this.arrowInfoMap.get(dir).offset1.y);
        vCoord2.offset(this.arrowInfoMap.get(dir).offset2.x, this.arrowInfoMap.get(dir).offset2.y);
        vCoord3.offset(this.arrowInfoMap.get(dir).offset3.x, this.arrowInfoMap.get(dir).offset3.y);

        Path path = new Path();
        path.moveTo(vCoord1.x, vCoord1.y);
        path.lineTo(vCoord2.x, vCoord2.y);
        path.lineTo(vCoord3.x, vCoord3.y);
        path.close();

        canvas.drawPath(path, paint);
    }

    /**
     * 화살표 방향
     * @param dir
     * @return 방향
     */
    private PointF getToArrow(final BaseEditPinView.Pin.RectPos dir) {
        switch (dir) {
            case LEFT:
                return Utillity.getNormalize(new PointF(-1, 0));
            case TOP:
                return Utillity.getNormalize(new PointF(0, -1));
            case RIGHT:
                return Utillity.getNormalize(new PointF(1, 0));
            case BOTTOM:
                return Utillity.getNormalize(new PointF(0, 1));
            case LEFT_TOP:
                return Utillity.getNormalize(new PointF(-1, -1));
            case RIGHT_TOP:
                return Utillity.getNormalize(new PointF(1, -1));
            case RIGHT_BOTTOM:
                return Utillity.getNormalize(new PointF(1, 1));
            case LEFT_BOTTOM:
                return Utillity.getNormalize(new PointF(-1, 1));
            default:
                return Utillity.getNormalize(new PointF(-1, 0));
        }
    }

    /**
     * 방향 표시 화살표 기준점
     * @return source coordinate
     */
    private PointF getArrowBaseCoord(final BaseEditPinView.Pin.RectPos dir) {
        final float scale = imageDrawView.getScale();
        final PointF sCoord = getPosition(0);
        final float offset = Math.max(getBitmapSizeView(true), getBitmapSizeView(false)) / scale;
        return Utillity.getOffset(sCoord, getToArrow(dir), offset);
    }

    /**
     * 방향 표시 화살표 좌표
     */
    private ArrowGeoInfo getArrowGeoInfo(final BaseEditPinView.Pin.RectPos dir) {
        final float size = Math.min(getBitmapSizeView(true), getBitmapSizeView(false)) / 3;
        final PointF dirToArrow = getToArrow(dir);
        final PointF dirVert = Utillity.getVertDirection(dirToArrow);

        ArrowGeoInfo arrowGeoInfo = new ArrowGeoInfo(
                new PointF(dirVert.x * -size, dirVert.y * -size),
                new PointF(dirVert.x * size, dirVert.y * size),
                new PointF(dirToArrow.x * size, dirToArrow.y * size));

        return arrowGeoInfo;
    }

    @Override
    protected void loadPaint() {
        super.loadPaint();
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void setSourceRegion() {
        final float scale = imageDrawView.getScale();
        final float width = getBitmapSizeView(true) / scale;
        final float height = getBitmapSizeView(false) / scale;
        final PointF sCoord = getPosition(0);

        sRegion = new RectF((sCoord.x - width), (sCoord.y - height), (sCoord.x + width), (sCoord.y + height));

        if (this.arrowInfoMap != null) {
            final float offset = Math.max(width, height);
            for (BaseEditPinView.Pin.RectPos dir : this.arrowInfoMap.keySet()) {
                if (this.arrowInfoMap.containsKey(dir) == true) {
                    this.arrowInfoMap.get(dir).sCoord = Utillity.getOffset(sCoord, getToArrow(dir), offset);
                }
            }
        }
    }

    /**
     * 색상에 맞는 카메라 이미지
     */
    private Bitmap createBitmap() {
        if (color == null || color.isEmpty() == true || color.compareTo("null") == 0) {
            color = Utillity.getColorString(DrawViewSetting.getInstance().getColor());
        }

        final int indexColor = Color.parseColor(color);
        if (ContextCompat.getColor(getContext(), R.color.white) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_white);
        } else if (ContextCompat.getColor(getContext(), R.color.black) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_black);
        } else if (ContextCompat.getColor(getContext(), R.color.green) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_green);
        } else if (ContextCompat.getColor(getContext(), R.color.red) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_red);
        } else if (ContextCompat.getColor(getContext(), R.color.pink) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_pink);
        } else if (ContextCompat.getColor(getContext(), R.color.orange) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_orange);
        } else if (ContextCompat.getColor(getContext(), R.color.yellow) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_yellow);
        } else if (ContextCompat.getColor(getContext(), R.color.blue) == indexColor) {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_blue);
        } else {
            return BitmapFactory.decodeResource(getResources(), R.drawable.drawing_camera_red);
        }
    }

    private float getBitmapSizeView(final boolean isWidth) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,
                isWidth ? bitmap.getWidth() : bitmap.getHeight(), getResources().getDisplayMetrics());
    }

    public ArrayList<PhotoItem> getPhotoItems() {
        return this.photoItems;
    }

    /**
     * 아이템 초기화
     */
    public void clearPhotoItems() {
        this.photoItems.clear();
    }

    /**
     * 아이템 추가
     * @param photoItems
     */
    public void setPhotoItems(ArrayList<PhotoItem> photoItems) {
        this.photoItems = photoItems;
    }

    /**
     * 삭제된 아이템 파일명 리스트
     * @return
     */
    public ArrayList<String> getRemoveItemFilenames() {
        return removeItemFilenames;
    }

    /**
     * 해당 방향이 on 상태인지 체크
     * @param pos 방향
     * @return boolean
     */
    public boolean isDir(final BaseEditPinView.Pin.RectPos pos) {
        return ((this.dir & pos.getPos()) == pos.getPos());
    }

    /**
     * 해당 방향 on/off
     * @param pos 방향
     */
    public void setDir(final BaseEditPinView.Pin.RectPos pos) {
        this.dir ^= pos.getPos();
    }

    /**
     * 방향 플래그
     * @param dir
     */
    public void setDir(final byte dir) {
        this.dir = dir;
    }

    public byte getDir() {
        return dir;
    }

    /**
     * Class photo information
     */
    public static class PhotoItem {

        String uniqId = "";
        String title;
        String commiter;
        String commitDate;
        String fileName;

        public PhotoItem(String title, String commiter, String commitDate, String fileName) {
//            this.uniqId = Utillity.getUUID();
            this.title = title;
            this.commiter = commiter;
            this.commitDate = commitDate;
            this.fileName = fileName;
        }

        public String getUniqId() {
            return uniqId;
        }

        public void setUniqId(String uniqId) {
            this.uniqId = uniqId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCommiter() {
            return commiter;
        }

        public void setCommiter(String commiter) {
            this.commiter = commiter;
        }

        public String getCommitDate() {
            return commitDate;
        }

        public void setCommitDate(String commitDate) {
            this.commitDate = commitDate;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    /**
     * 화살표 정보
     */
    private class ArrowGeoInfo {
        private PointF sCoord;
        final private PointF offset1;
        final private PointF offset2;
        final private PointF offset3;

        public ArrowGeoInfo(PointF offset1, PointF offset2, PointF offset3) {
            this.offset1 = offset1;
            this.offset2 = offset2;
            this.offset3 = offset3;
        }
    }

    /**
     * Class EditView with direct Arrow
     */
    public class EditArrowView extends EditPinViewPoint {

        public EditArrowView(@NonNull ImageDrawView imageDrawView, @NonNull BaseDrawView drawView) {
            super(imageDrawView, drawView);
        }

        @Override
        public void initPinList() {
            pinList.clear();
            for (Pin.RectPos pos : dirs) {
                pinList.add(new Pin(getArrowBaseCoord(pos), pos));
            }
        }

        @Override
        protected void drawPin(Canvas canvas) {
            for (Pin pin : pinList) {
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(isDir(pin.rectPos) ? Color.RED : Color.WHITE);
                drawArrow(canvas, pin.rectPos);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(Color.DKGRAY);
                drawArrow(canvas, pin.rectPos);
            }
        }

        @Override
        public Pin setPinPressed(final float x, final float y) {
            final Pin findPin = findPin(x, y);
            if (findPin != null) {
                findPin.state = true;
                setDir(findPin.rectPos);
            }
            return findPin;
        }

        @Override
        public boolean isValidMoveablePin() {
            return false;
        }
    }
}
