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
     * @return String
     */
    public static String getColorString(final int idxColor) {
        return "#" + Integer.toHexString(idxColor);
    }

    /**
     * 하이픈(-)이 제거된 UUID
     * @return String
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 두 좌표간의 거리
     * @param pt1
     * @param pt2
     * @return float
     */
    public static float getDistance(final PointF pt1, final PointF pt2) {
        return (float) Math.sqrt(Math.pow(Math.abs(pt1.x - pt2.x), 2) + Math.pow(Math.abs(pt1.y - pt2.y), 2));
    }

    /**
     * 두 좌표간의 방향
     * @param pt1
     * @param pt2
     * @return PointF
     */
    public static PointF getUnitDirection(final PointF pt1, final PointF pt2) {
        PointF dir = new PointF(pt2.x - pt1.x, pt2.y - pt1.y);
        if (dir.length() == 0) {
            dir.x = 1;
            dir.y = 1;
        }
        return getNormalize(dir);
    }

    /**
     * 두 좌표간의 방향의 수직 방향
     * @param pt1
     * @param pt2
     * @return PointF
     */
    public static PointF getUnitVertDirenction(final PointF pt1, final PointF pt2) {
        PointF dir = getUnitDirection(pt1, pt2);
        if (dir.x > 0) {
            return new PointF(dir.y, -dir.x);
        }
        return new PointF(-dir.y, dir.x);
    }

    /**
     * PointF 정규화
     * @param pt
     * @return PointF
     */
    public static PointF getNormalize(final PointF pt) {
        final float length = (float) Math.sqrt(Math.pow(pt.x, 2) + Math.pow(pt.y, 2));
        return new PointF(pt.x / length, pt.y / length);
    }

    /**
     * 좌표 이동
     * @param base 기준점
     * @param dir 이동방향
     * @param offset 이동거리
     * @return PointF
     */
    public static PointF getOffset(final PointF base, final PointF dir, final float offset) {
        return new PointF(base.x + (dir.x * offset), base.y + (dir.y * offset));
    }

    /**
     * 점이 다각형 내부에 존재하는지 판단
     * @param pt
     * @param polygon
     * @return boolean
     */
    public static boolean isInside(final PointF pt, ArrayList<PointF> polygon) {
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
