package com.github.crazyatom.subsamplingscaleimagedrawview.Event;

import com.github.crazyatom.subsamplingscaleimagedrawview.drawtools.BaseDrawTool;

/**
 * Created by crazy on 2017-07-21.
 */

public interface ChangeDrawToolListener {
    void changeDrawTool(final BaseDrawTool.DrawToolType drawToolType);
}
