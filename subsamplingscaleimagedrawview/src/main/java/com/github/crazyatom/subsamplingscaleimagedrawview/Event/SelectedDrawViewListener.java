package com.github.crazyatom.subsamplingscaleimagedrawview.Event;

import android.support.annotation.Nullable;

/**
 * Created by crazy on 2017-08-09.
 */

public interface SelectedDrawViewListener {
    void onSelectedDrawViewInfo(@Nullable final String info);
    void onSelectedDrawViewCreater(@Nullable final String creater);
    void onSelectedDrawViewUpdateTime(final long updateTime);
}
