package kr.co.aroad.subscaleimagedrawview.Listener;

import android.view.MotionEvent;

/**
 * Created by hangilit on 2017. 7. 12..
 */

public interface GestureListener {
    void onLongPress(MotionEvent event);
    void onSingleTabUp(MotionEvent event);
}
