package kr.co.aroad.subscaleimagedrawview.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

/**
 * Created by crazy on 2017-07-11.
 */

public class AnnotationSetting {

    private static AnnotationSetting instance;

    public enum AnnotExDimensionType {
        LINE,
        CURVE;

        AnnotExDimensionType() {
        }
    }

    public enum AnnotExDimensionUnit {
        MM,
        CM,
        M,
        KM;

        AnnotExDimensionUnit() {
        }

        public static String toString(AnnotExDimensionUnit type) {
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

        public static AnnotExDimensionUnit toType(String type) {
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
    private static final String FREE_TEXT_SIZE_KEY = "FreeTextSize";


    public static AnnotationSetting getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new AnnotationSetting(context);
    }

    private AnnotationSetting(Context context) {
        this.mPreferences = context.getSharedPreferences("annotation_setting", 0);
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
     * 마크업 연속 생성 설정
     *
     * @param continuous
     */
    public void setContinuous(boolean continuous) {
        SharedPreferences.Editor editor = this.mPreferences.edit();
        editor.putBoolean(CONTINUOUS_KEY, continuous);
        editor.commit();
    }

    /**
     * 마크업 연속 생성
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
    public void setFreeTextSize(int width) {
        SharedPreferences.Editor editor = this.mPreferences.edit();
        editor.putInt(FREE_TEXT_SIZE_KEY, width);
        editor.commit();
    }

    /**
     * 텍스트 크기 반환
     *
     * @return
     */
    public int getFreeTextSize() {
        return mPreferences.getInt(FREE_TEXT_SIZE_KEY, 20);
    }
}
