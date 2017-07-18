package kr.co.aroad.subscaleimagedrawview.util;

import android.support.annotation.Nullable;

import kr.co.aroad.subscaleimagedrawview.drawviews.*;
import kr.co.aroad.subscaleimagedrawview.listener.CreateDrawViewListener;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewFactory {

    private static DrawViewFactory instance;
    private CreateDrawViewListener createDrawViewListener;

    public static DrawViewFactory getInstance() {
        if (instance == null) {
            instance = new DrawViewFactory();
        }
        return instance;
    }

    public static void setListener(@Nullable CreateDrawViewListener createDrawViewListener) {
        instance.createDrawViewListener = createDrawViewListener;
    }

    public BaseDrawView create(ImageDrawView imageDrawView, BaseDrawView.DrawViewType type) {
        switch (type) {
            case INK:
                return new DrawViewInk(imageDrawView, createDrawViewListener);
            case RECTANGLE:
                return new DrawViewRectangle(imageDrawView, createDrawViewListener);
            case ELLIPSE:
                return new DrawViewEllipse(imageDrawView, createDrawViewListener);
            case LINE:
                return new DrawViewLine(imageDrawView, createDrawViewListener);
            case CLOUD:
                return new DrawViewCloud(imageDrawView, createDrawViewListener);
            case TEXT:
                return new DrawViewText(imageDrawView, createDrawViewListener);
//            case PHOTO:
//                return new DrawViewPhoto(imageDrawView, createDrawViewListener);
            case DIMENSION:
                return new DrawViewDimension(imageDrawView, createDrawViewListener);
            case DIMENSION_REF:
                return new DrawViewReferenceDimension(imageDrawView, createDrawViewListener);
            default:
                return null;
        }
    }
}
