package com.ruoyi.common.utils.time;

import java.sql.Timestamp;
import java.util.Date;

public class TimeUtil {

    /**
     * 生成时间戳
     *
     * @param date
     * @return
     */
    public  static long timestampMedthod(Date date) {
        Timestamp timestamp = Timestamp.valueOf(date.toString());
        return timestamp.getTime();
    }
}
