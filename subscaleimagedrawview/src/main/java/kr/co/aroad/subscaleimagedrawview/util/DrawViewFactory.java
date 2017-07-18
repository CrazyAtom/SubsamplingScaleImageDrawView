package kr.co.aroad.subscaleimagedrawview.util;

import android.support.annotation.Nullable;

import kr.co.aroad.subscaleimagedrawview.drawviews.*;
import kr.co.aroad.subscaleimagedrawview.listener.NewDrawViewListener;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 14..
 */

public class DrawViewFactory {

    private static DrawViewFactory instance;
    private NewDrawViewListener newDrawViewListener;

    public static DrawViewFactory getInstance() {
        if (instance == null) {
            instance = new DrawViewFactory();
        }
        return instance;
    }

    public static void setListener(@Nullable NewDrawViewListener newDrawViewListener) {
        instance.newDrawViewListener = newDrawViewListener;
    }

    public BaseDrawView create(ImageDrawView imageDrawView, BaseDrawView.DrawViewType type) {
        switch (type) {
            case INK:
                return new DrawViewInk(imageDrawView, newDrawViewListener);
            case RECTANGLE:
                return new DrawViewRectangle(imageDrawView, newDrawViewListener);
            case ELLIPSE:
                return new DrawViewEllipse(imageDrawView, newDrawViewListener);
            case LINE:
                return new DrawViewLine(imageDrawView, newDrawViewListener);
            case CLOUD:
                return new DrawViewCloud(imageDrawView, newDrawViewListener);
            case TEXT:
                return new DrawViewText(imageDrawView, newDrawViewListener);
//            case PHOTO:
//                return new DrawViewPhoto(imageDrawView, newDrawViewListener);
            case DIMENSION:
                return new DrawViewDimension(imageDrawView, newDrawViewListener);
            case DIMENSION_REF:
                return new DrawViewReferenceDimension(imageDrawView, newDrawViewListener);
            default:
                return null;
        }
    }
}
