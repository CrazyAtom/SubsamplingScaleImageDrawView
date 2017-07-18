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

    public static DrawViewFactory getInstance() {
        if (instance == null) {
            instance = new DrawViewFactory();
        }
        return instance;
    }

    public BaseDrawView create(ImageDrawView imageDrawView, BaseDrawView.DrawViewType type) {
        switch (type) {
            case INK:
                return new DrawViewInk(imageDrawView);
            case RECTANGLE:
                return new DrawViewRectangle(imageDrawView);
            case ELLIPSE:
                return new DrawViewEllipse(imageDrawView);
            case LINE:
                return new DrawViewLine(imageDrawView);
            case CLOUD:
                return new DrawViewCloud(imageDrawView);
            case TEXT:
                return new DrawViewText(imageDrawView);
            case PHOTO:
                return new DrawViewPhoto(imageDrawView);
            case DIMENSION:
                return new DrawViewDimension(imageDrawView);
            case DIMENSION_REF:
                return new DrawViewReferenceDimension(imageDrawView);
            default:
                return null;
        }
    }
}
