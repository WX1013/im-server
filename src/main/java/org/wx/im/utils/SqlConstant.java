package org.wx.im.utils;

/**
 * @description: SqlConstant
 * @author WangXin
 */
public interface SqlConstant {
    /**
     * LIMIT_1
     */
    String LIMIT_1 = "limit 1";

    String LIMIT_2 = "limit 2";

    /**
     * LIMIT_10
     */
    String LIMIT_10 = "limit 10";

    /**
     * LIMIT_5
     */
    String LIMIT_5 = "limit 5";

    String LIMIT_6 = "limit 6";

    String LIMIT_8 = "limit 8";
    String LIMIT_9 = "limit 9";

    /**
     * LIMIT_3
     */
    String LIMIT_3 = "limit 3";

    /**
     * LIMIT_4
     */
    String LIMIT_4 = "limit 4";

    /**
     * LIMIT_20
     */
    String LIMIT_20 = "limit 20";

    /**
     * LIMIT_30
     */
    String LIMIT_30 = "limit 30";

    /**
     * LIMIT_50
     */
    String LIMIT_50 = "limit 50";

    /**
     * LIMIT_99
     */
    String LIMIT_99 = "limit 99";

    /**
     * desc
     */
    String DESC = "desc";

    /**
     * asc
     */
    String ASC = "asc";

    /**
     * FOR_UPDATE
     */
    String FOR_UPDATE = "for update";

    String FORMAT = " limit %s ";

    /**
     * 生成自定义限制数量sql
     * @param limit
     * @return
     */
    static String limitFormat(Integer limit){
        return String.format(FORMAT, limit);
    }

}
