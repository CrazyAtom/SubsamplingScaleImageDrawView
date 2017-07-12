package kr.co.aroad.subscaleimagedrawview.util;

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
}
