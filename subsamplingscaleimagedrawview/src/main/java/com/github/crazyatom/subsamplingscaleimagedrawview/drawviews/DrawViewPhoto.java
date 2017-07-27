package com.github.crazyatom.subsamplingscaleimagedrawview.drawviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

import java.util.ArrayList;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
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

    public DrawViewPhoto(@NonNull ImageDrawView imageDrawView) {
        super(DrawViewType.PHOTO, imageDrawView);
    }

    @Override
    public void showContentsBox(Context context) {
        // call managed photo window
        if (DrawViewFactory.getInstance().getAddDrawViewPhotoListener() != null) {
            DrawViewFactory.getInstance().getAddDrawViewPhotoListener().showAddPhotoWindow(this.getUniqId());
        }
    }

    @Override
    public boolean isInvalidSaveAnnotEx() {
        return true;
    }

    @Override
    public String getName(boolean isSimple) {
        return isSimple ? "P" : getResources().getString(R.string.photo);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getPositionSize() >= 1) {
            final Bitmap bitmap = createBitmap();
            final float w = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, bitmap.getWidth(), getResources().getDisplayMetrics()) / 2;
            final float h = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, bitmap.getHeight(), getResources().getDisplayMetrics()) / 2;

            // create bitmap
            final PointF vCoord = sourceToViewCoord(getPosition(0).x, getPosition(0).y);
            float left = vCoord.x - w;
            float top = vCoord.y - h;
            float right = vCoord.x + w;
            float bottom = vCoord.y + h;
            canvas.drawBitmap(bitmap, null, new RectF(left, top, right, bottom), null);

            // set boundary box
            left = getPosition(0).x - (w / imageDrawView.getScale());
            top = getPosition(0).y - (h / imageDrawView.getScale());
            right = getPosition(0).x + (w / imageDrawView.getScale());
            bottom = getPosition(0).y + (h / imageDrawView.getScale());
            setBoundaryBox(new RectF(left, top, right, bottom));

            super.onDraw(canvas);
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

    @Override
    public boolean isContains(float x, float y) {
        return super.isContains(x, y);
    }

    public ArrayList<PhotoItem> getPhotoItems() {
        return photoItems;
    }

    public void setPhotoItems(ArrayList<PhotoItem> photoItems) {
        this.photoItems = photoItems;
    }

    public ArrayList<String> getRemoveItemFilenames() {
        return removeItemFilenames;
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
}
