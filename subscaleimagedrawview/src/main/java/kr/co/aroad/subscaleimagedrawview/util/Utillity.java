package kr.co.aroad.subscaleimagedrawview.util;

import android.graphics.PointF;

/**
 * Created by crazy on 2017-07-11.
 */

public class Utillity {

    /**
     * 인덱스 컬러를 rgb 컬러 스트링으로 변환
     * @param idxColor
     * @return
     */
    public static String getColorString(int idxColor) {
        return "#" + Integer.toHexString(idxColor);
    }

    /**
     * 두 좌표간의 거리
     * @param pt1
     * @param pt2
     * @return
     */
    public static float getDistance(PointF pt1, PointF pt2) {
        return (float) Math.sqrt(Math.pow(Math.abs(pt1.x - pt2.x), 2) + Math.pow(Math.abs(pt1.y - pt2.y), 2));
    }
}
