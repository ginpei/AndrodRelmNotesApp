package info.ginpei.notes.utils;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NiceDateFormat {

    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    @NonNull
    public static String format(@NonNull Date nowDate, @NonNull Date targetDate) {
        String result = null;

        Calendar now = Calendar.getInstance();
        now.setTime(nowDate);

        Calendar target = Calendar.getInstance();
        target.setTime(targetDate);

        if (now.get(Calendar.YEAR) != target.get(Calendar.YEAR)) {
            result = new SimpleDateFormat("yyyy/MM/dd").format(targetDate);
        } else if (now.get(Calendar.MONTH) != target.get(Calendar.MONTH) || now.get(Calendar.DAY_OF_MONTH) != target.get(Calendar.DAY_OF_MONTH)) {
            result = new SimpleDateFormat("MM/dd").format(targetDate);
        } else {
            result = new SimpleDateFormat("HH:mm").format(targetDate);
        }

        return result;
    }

    @NonNull
    public static String format(@NonNull Date targetDate) {
        return format(new Date(), targetDate);
    }
}