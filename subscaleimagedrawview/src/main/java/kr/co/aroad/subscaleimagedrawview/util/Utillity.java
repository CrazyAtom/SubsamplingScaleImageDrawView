package kr.co.aroad.subscaleimagedrawview.util;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.UUID;

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
     * 하이픈(-)이 제거된 UUID
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
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

    /**
     * 점이 다각형 내부에 존재하는지 판단
     *
     * @param pt
     * @param polygon
     * @return
     */
    public static boolean isInside(PointF pt, ArrayList<PointF> polygon) {
        // crosses는 점 pt와 오른쪽 반직선과 다각형과의 교점의 개수
        int crosses = 0;
        for (int i = 0; i < polygon.size(); ++i) {
            int j = (i + 1) % polygon.size();
            // 점 pt가 선분(polygon[i], polygon[j])의 y좌표 사이에 있음
            if ((polygon.get(i).y > pt.y) != (polygon.get(j).y > pt.y)) {
                // atX는 점 pt를 지나는 수평선과 선분(polygon[i], polygon[j])의 교점
                float atX = (polygon.get(j).x - polygon.get(i).x) * (pt.y - polygon.get(i).y) / (polygon.get(j).y - polygon.get(i).y) + polygon.get(i).x;
                //atX가 오른쪽 반직선과의 교점이 맞으면 교점의 개수를 증가
                if (pt.x < atX) {
                    crosses++;
                }
            }
        }

        return crosses % 2 > 0;
    }
}
