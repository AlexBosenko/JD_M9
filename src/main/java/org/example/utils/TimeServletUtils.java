package org.example.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TimeZone;

public class TimeServletUtils {
    public static final String COOKIE_TIME_ZONE_PARAM = "cookieTimeZone";
    public static final String TIME_AT_TIME_ZONE_PARAM = "timeAtTimeZone";
    public static final String QUERY_PARAM = "timezone";
    public static String getUtcDateTime(String parameterName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss z");

        ZonedDateTime curDateTime = ZonedDateTime.now(ZoneOffset.UTC);

        if (parameterName == null) {
            return curDateTime.format(formatter);
        }

        ZoneId zoneId = ZoneId.of(parameterName);
        ZonedDateTime utcDateTime = curDateTime.withZoneSameInstant(zoneId);

        return utcDateTime.format(formatter);
    }

    public static boolean validRequest(HttpServletRequest req, String parameterName) {
        Map<String, String[]> parameterMap = req.getParameterMap();

        if (parameterMap.isEmpty()) {
            return true;
        }

        if (parameterMap.containsKey(parameterName)) {
            String[] parameterValues = parameterMap.get(parameterName);
            if (parameterValues.length > 1) {
                return false;
            } else {
                try {
                    String timeZoneValue = modifyParameter(parameterValues[0]);
                    ZoneId zoneId = ZoneId.of(timeZoneValue);
                    TimeZone timeZone = TimeZone.getTimeZone(zoneId);
                    return timeZone != null;
                } catch (DateTimeException e) {
                    return false;
                }
            }
        }

        return false;
    }

    public static String modifyParameter(String parameter) {
        if (parameter == null) {
            return null;
        }
        return parameter.replace(" ", "+");
    }

    public static String getTimeZoneCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(COOKIE_TIME_ZONE_PARAM)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
