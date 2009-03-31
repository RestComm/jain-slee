/*
* "Downloading, compilation and use of this TCK
* is governed by the terms of the JAIN SLEE (JSLEE) V1.1 TECHNOLOGY
* COMPATIBILITY KIT LICENCE AGREEMENT, the text of which is included in
* the download package as LICENSE and can be viewed at
* https://jsleetck11.dev.java.net/jslee-1.1-tck-license.txt"
*/

package com.opencloud.util;

import java.util.TimeZone;

public final class SimpleDateFormat {
    public static String toString(long millis) {
        return toStringBuffer(millis).toString();
    }

    public static StringBuffer toStringBuffer(long millis) {
        return appendToStringBuffer(new StringBuffer(), millis, TimeZone.getDefault());
    }

    public static StringBuffer appendToStringBuffer(StringBuffer buf, long millis) {
        return appendToStringBuffer(buf, millis, TimeZone.getDefault());
    }

    public static StringBuffer appendToStringBuffer(StringBuffer buf, long millis, TimeZone tz) {
        if (tz != null) millis += tz.getOffset(millis);

        final int ms = (int)(millis % 1000);
        millis /= 1000;
        final int secs = (int)( millis % 60);
        millis /= 60;
        final int mins = (int)( millis % 60);
        millis /= 60;
        final int hours = (int)(millis % 24);
        millis /= 24;

        // refer http://aa.usno.navy.mil/faq/docs/JD_Formula.html for conversion formula
        // julian calendar day for Jan 1, 1970 = 2440588
        long l = millis + (2440588 + 68569);
        final long n = 4 * l / 146097;
        l -= (146097 * n + 3) / 4;
        final long i = 4000 * ( l + 1) / 1461001;
        l = l - 1461 * i / 4 + 31;
        final long j = 80 * l / 2447;
        final int day = (int)(l - 2447 * j / 80);
        l = j / 11;
        final int mon = (int)(j + 2 - 12 * l);
        final int yr = (int)(100 * (n - 49) + i + l);

        buf.append(yr).append('-');
        if (mon < 10) buf.append('0');
        buf.append(mon).append('-');
        if (day < 10) buf.append('0');
        buf.append(day).append(' ');
        if (hours < 10) buf.append('0');
        buf.append(hours).append(':');
        if (mins < 10) buf.append('0');
        buf.append(mins).append(':');
        if (secs < 10) buf.append('0');
        buf.append(secs).append('.');
        if (ms < 100) buf.append('0');
        if (ms < 10) buf.append('0');
        buf.append(ms);
        return buf;
    }
}
