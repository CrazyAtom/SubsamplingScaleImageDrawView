package kr.co.aroad.subscaleimagedrawview.util;

import android.support.annotation.Nullable;

import kr.co.aroad.subscaleimagedrawview.drawviews.BaseDrawView;
import kr.co.aroad.subscaleimagedrawview.drawviews.DrawViewEllipse;
import kr.co.aroad.subscaleimagedrawview.drawviews.DrawViewInk;
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
            case ELLIPSE:
                return new DrawViewEllipse(imageDrawView, createDrawViewListener);
            default:
                return null;
        }
    }
}
