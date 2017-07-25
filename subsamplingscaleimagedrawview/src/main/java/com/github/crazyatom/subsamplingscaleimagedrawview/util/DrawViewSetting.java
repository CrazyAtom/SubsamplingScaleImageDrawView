package com.github.crazyatom.subsamplingscaleimagedrawview.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by crazy on 2017-07-11.
 */

public class DrawViewSetting {

    private static DrawViewSetting instance;

    public enum DrawViewDimensionType {
        LINE,
        CURVE;

        DrawViewDimensionType() {
        }
    }

    public enum DrawViewDimensionUnit {
        MM,
        CM,
        M,
        KM;

        DrawViewDimensionUnit() {
        }

        public static String toString(DrawViewDimensionUnit type) {
            switch (type) {
                case MM:
                    return "mm";
                case CM:
                    return "cm";
                case M:
                    return "m";
                case KM:
                    return "km";
            }
            return "mm";
        }

        public static DrawViewDimensionUnit toType(String type) {
            switch (type) {
                case "mm":
                    return MM;
                case "cm":
                    return CM;
                case "m":
                    return M;
                case "km":
                    return KM;
            }
            return MM;
        }
    }

    private SharedPreferences mPreferences;

    private static final String COLOR_KEY = "Color";
    private static final String LINE_WIDTH_KEY = "LineWidth";
    private static final String CONTINUOUS_KEY = "Continuous";
    private static final String TEXT_SIZE_KEY = "TextSize";


    public static DrawViewSetting getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new DrawViewSetting(context);
    }

    private DrawViewSetting(Context context) {
        this.mPreferences = context.getSharedPreferences("drawview_setting", 0);
    }

    /**
     * 생상 설정
     *
     * @param color
     */
    public void setColor(int color) {
        SharedPreferences.Editor editor = this.mPreferences.edit();
        editor.putInt(COLOR_KEY, color);
        editor.commit();
    }

    /**
     * 색상 반환
     *
     * @return
     */
    public int getColor() {
        return mPreferences.getInt(COLOR_KEY, Color.RED);
    }

    /**
     * 라인 두께 설정
     *
     * @param width
     */
    public void setLineWidth(int width) {
        SharedPreferences.Editor editor = this.mPreferences.edit();
        editor.putInt(LINE_WIDTH_KEY, width);
        editor.commit();
    }

    /**
     * 라인 두께 반환
     *
     * @return
     */
    public int getLineWidth() {
        return mPreferences.getInt(LINE_WIDTH_KEY, 4);
    }


    /**
     * 연속 생성 설정
     *
     * @param continuous
     */
    public void setContinuous(boolean continuous) {
        SharedPreferences.Editor editor = this.mPreferences.edit();
        editor.putBoolean(CONTINUOUS_KEY, continuous);
        editor.commit();
    }

    /**
     * 연속 생성 설정 여부
     *
     * @return
     */
    public boolean isContinuous() {
        return mPreferences.getBoolean(CONTINUOUS_KEY, true);
    }

    /**
     * 텍스트 크기 설정
     *
     * @param width
     */
    public void setTextSize(int width) {
        SharedPreferences.Editor editor = this.mPreferences.edit();
        editor.putInt(TEXT_SIZE_KEY, width);
        editor.commit();
    }

    /**
     * 텍스트 크기 반환
     *
     * @return
     */
    public int getTextSize() {
        return mPreferences.getInt(TEXT_SIZE_KEY, 20);
    }
}
