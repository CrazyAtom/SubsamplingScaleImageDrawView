package kr.co.aroad.subscaleimagedrawview.drawviews;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import kr.co.aroad.subscaleimagedrawview.util.DrawViewSetting;
import kr.co.aroad.subscaleimagedrawview.util.Utillity;
import kr.co.aroad.subscaleimagedrawview.views.ImageDrawView;

/**
 * Created by hangilit on 2017. 7. 17..
 */

public class DrawViewReferenceDimension extends DrawViewDimension {

    private int conversionLength = 1;
    private float dimensionRatioFactor = 0;
    DrawViewSetting.DrawViewDimensionType dimensionType = DrawViewSetting.DrawViewDimensionType.LINE;
    DrawViewSetting.DrawViewDimensionUnit dimensionUnit = DrawViewSetting.DrawViewDimensionUnit.MM;

    public DrawViewReferenceDimension(@NonNull ImageDrawView imageDrawView) {
        super(imageDrawView);
        setType(DrawViewType.DIMENSION_REF);
    }

    @Override
    public void update(ArrayList<PointF> points) {
        super.update(points);
        resetInformation();
    }

    @Override
    protected float getLength() {
        return this.conversionLength;
    }

    @Override
    protected String getLengthText() {
        if (getCurrentState() == state.COMPLETE) {
            return String.format("%,d", (int) getLength()) + DrawViewSetting.DrawViewDimensionUnit.toString(getDimensionUnit());
        } else {
            return "";
        }
    }

    @Override
    protected int getIndexColor() {
        return Color.BLUE;
    }

    /**
     * 기준치수선 재설정
     * 기준치수선이 변경 되면 일반 치수선도 함께 변경 되어야 한다
     */
    private void resetInformation() {
        final float realLength = Utillity.getDistance(getPosition(1), getPosition(0));
        // 치수선 비율 재설정
        setDimensionRatioFactor(conversionLength / realLength);
        // 재설정된 비율에 의한 일반 치수선 갱신
        for (String key : imageDrawView.getDrawViewMap().keySet()) {
            BaseDrawView drawView = imageDrawView.getDrawViewMap().get(key);
            if (drawView.equals(this) == false && drawView.getType() == DrawViewType.DIMENSION) {
                drawView.invalidate();
            }
        }
    }

    /**
     * 화면에 표현될 환산 길이
     */
    public int getConversionLength() {
        return conversionLength;
    }

    /**
     * 화면에 표현될 환산 길이
     * @param conversionLength
     */
    public void setConversionLength(int conversionLength) {
        this.conversionLength = conversionLength;
    }

    /**
     * 환산 길이에 의한 비율
     * @return
     */
    public float getDimensionRatioFactor() {
        return dimensionRatioFactor;
    }

    /**
     * 환산 길이에 의한 비율
     * @param dimensionRatioFactor
     */
    public void setDimensionRatioFactor(float dimensionRatioFactor) {
        this.dimensionRatioFactor = dimensionRatioFactor;
    }

    /**
     * 치추선 타입
     * @return
     */
    public DrawViewSetting.DrawViewDimensionType getDimensionType() {
        return dimensionType;
    }

    /**
     * 치추선 타입
     * @param dimensionType
     */
    public void setDimensionType(DrawViewSetting.DrawViewDimensionType dimensionType) {
        this.dimensionType = dimensionType;
    }

    /**
     * 치추선 단위
     * @return
     */
    public DrawViewSetting.DrawViewDimensionUnit getDimensionUnit() {
        return dimensionUnit;
    }

    /**
     * 치추선 단위
     * @param dimensionUnit
     */
    public void setDimensionUnit(DrawViewSetting.DrawViewDimensionUnit dimensionUnit) {
        this.dimensionUnit = dimensionUnit;
    }
}
