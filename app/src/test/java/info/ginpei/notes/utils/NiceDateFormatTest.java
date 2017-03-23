package info.ginpei.notes.utils;

import android.annotation.SuppressLint;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class NiceDateFormatTest {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void format() throws Exception {
        Date now = simpleDateFormat.parse("2000/02/02 23:59:59");
        Date date;

        date = simpleDateFormat.parse("2000/02/02 12:34:56");
        assertEquals("same day", "12:34", NiceDateFormat.format(now, date));

        date = simpleDateFormat.parse("2000/02/01 12:34:56");
        assertEquals("same month", "02/01", NiceDateFormat.format(now, date));

        date = simpleDateFormat.parse("2000/01/02 12:34:56");
        assertEquals("same year", "01/02", NiceDateFormat.format(now, date));

        date = simpleDateFormat.parse("1999/02/02 12:34:56");
        assertEquals("another year", "1999/02/02", NiceDateFormat.format(now, date));
    }

}