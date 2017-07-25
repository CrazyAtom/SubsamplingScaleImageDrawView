package com.github.crazyatom.subsamplingscaleimagedrawview.Event;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseDrawTool;

/**
 * Created by hangilit on 2017. 7. 25..
 */

public interface ChangeDrawToolCallback {
    void changed(final BaseDrawTool.DrawToolType drawToolType);
}
