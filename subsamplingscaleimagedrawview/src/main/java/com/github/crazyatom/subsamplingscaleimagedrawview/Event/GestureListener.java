package com.github.crazyatom.subsamplingscaleimagedrawview.Event;

import android.support.annotation.NonNull;
import android.view.MotionEvent;

/**
 * Created by hangilit on 2017. 7. 12..
 */

public interface GestureListener {
    void onLongPress(@NonNull MotionEvent event);
    void onSingleTabUp(@NonNull MotionEvent event);
}
