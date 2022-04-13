package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.util.FormatUtil;
import com.sunnysuperman.commons.util.StringUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    public static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT+08:00");
    public static final Date INIT_DATE = new Date(0L);

    public static final int SECOND_PER_HOUR = 3600;

    public static final int SECOND_PER_DAY = 24 * SECOND_PER_HOUR;

    public static Date getForeverDate() {
        return DateUtils.addYears(new Date(), 1000);
    }

    public static Calendar getDefaultCalendar() {
        Calendar cal = Calendar.getInstance(DEFAULT_TIMEZONE);
        return cal;
    }

    public static Calendar getDefaultCalendar(Date date) {
        Calendar cal = getDefaultCalendar();
        cal.setTime(date);
        return cal;
    }

    public static Calendar getDefaultCalendar(long ts) {
        Calendar cal = getDefaultCalendar();
        cal.setTimeInMillis(ts);
        return cal;
    }

    public static int date2day(Calendar cal) {
        return cal.get(Calendar.YEAR) * 10000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int date2day(Date date) {
        Calendar cal = getDefaultCalendar();
        cal.setTime(date);
        return date2day(cal);
    }

    public static int date2day(long ts) {
        Date date = new Date();
        date.setTime(ts);
        return date2day(date);
    }

    public static int date2week(Calendar cal) {
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.setMinimalDaysInFirstWeek(7);
        return cal.getWeekYear() * 100 + cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int date2week(Date date) {
        Calendar cal = getDefaultCalendar();
        cal.setTime(date);
        return date2week(cal);
    }

    public static int date2month(Calendar cal) {
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1;
    }

    public static Calendar day2date(int day, Calendar cal) {
        cal.clear();
        cal.set(Calendar.YEAR, day / 10000);
        cal.set(Calendar.MONTH, (day % 10000) / 100 - 1);
        cal.set(Calendar.DAY_OF_MONTH, day % 100);
        return cal;
    }

    public static Date getYestodayStart() {
        Calendar cal = getDefaultCalendar();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getPreDayStart(Long milliseconds) {
        Date date = new Date();
        date.setTime(milliseconds);
        return getPreDayStart(date);
    }

    public static Date getPreDayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getTodayStart() {
        Calendar cal = getDefaultCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getTomorrowStart() {
        Calendar cal = getDefaultCalendar();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getDayStart(Long milliseconds) {
        Date date = new Date();
        date.setTime(milliseconds);
        return getDayStart(date);
    }

    public static Date getDayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static Date getNextDayStart(Long milliseconds) {
        Date date = new Date();
        date.setTime(milliseconds);
        return getNextDayStart(date);
    }

    public static Date getNextDayStart(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static int getMonth(Date time) {
        Calendar cal = getDefaultCalendar();
        if (time != null) {
            cal.setTime(time);
        }
        return getMonth(cal);
    }

    public static int getMonth(Calendar cal) {
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        return year * 100 + month;
    }

    public static Date parseDate(String s) {
        return parseDate(s, DEFAULT_TIMEZONE);
    }

    public static Date parseDate(String s, TimeZone timezone) {
        if (StringUtil.isEmpty(s)) {
            return null;
        }
        if (s.charAt(s.length() - 1) == 'Z') {
            // parse iso8601
            return FormatUtil.parseISO8601Date(s);
        }
        // yyyy(-|/)MM(-|/)dd HH(:mm)(:ss)
        s = s.trim();
        String dateString = s;
        String timeString = null;
        int timeIndex = s.indexOf(' ');
        if (timeIndex > 0) {
            dateString = s.substring(0, timeIndex);
            timeString = s.substring(timeIndex + 1);
        }
        Calendar cal = Calendar.getInstance(timezone);
        cal.clear();
        {
            List<String> tokens = StringUtils.split(dateString, dateString.indexOf('/') > 0 ? "/" : "-");
            if (tokens.size() != 3) {
                return null;
            }
            int year = Integer.parseInt(tokens.get(0).trim());
            int month = Integer.parseInt(tokens.get(1).trim());
            int day = Integer.parseInt(tokens.get(2).trim());
            int theDay = year * 10000 + month * 100 + day;
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month - 1);
            cal.set(Calendar.DAY_OF_MONTH, day);
            if (date2day(cal) != theDay) {
                return null;
            }
        }
        if (timeString != null) {
            List<String> tokens = StringUtils.split(timeString, ":");
            int hour = Integer.parseInt(tokens.get(0).trim());
            if (hour < 0 || hour >= 24) {
                return null;
            }
            int minute = tokens.size() > 1 ? Integer.parseInt(tokens.get(1).trim()) : 0;
            if (minute < 0 || minute >= 60) {
                return null;
            }
            int second = tokens.size() > 2 ? Integer.parseInt(tokens.get(2).trim()) : 0;
            if (second < 0 || second >= 60) {
                return null;
            }
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, minute);
            cal.set(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    public static int getLastDay(int day, Calendar cal) {
        day2date(day, cal);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return date2day(cal);
    }

    public static int getNextDay(int day, Calendar cal) {
        day2date(day, cal);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return date2day(cal);
    }

    public static Date clearDateTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public static boolean isValidTimezone(float timezone) {
        return timezone >= -14 && timezone <= 14;
    }

    public static float ensureValidTimezone(Float v, float defaultTimezone) {
        if (v == null) {
            return defaultTimezone;
        }
        float timezone = v.floatValue();
        if (!isValidTimezone(timezone)) {
            return defaultTimezone;
        }
        return timezone;
    }

    public static String format(Date date, String f) {
        DateFormat format = new SimpleDateFormat(f);
        format.setTimeZone(DEFAULT_TIMEZONE);
        return format.format(date);
    }

    private static String pad2(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    public static String formatToSecond(Date date, TimeZone timezone) {
        if (date == null) {
            return null;
        }
        return formatToSecond(date.getTime(), timezone);
    }

    public static String formatToSecond(long timestamp, TimeZone timezone) {
        if (timezone == null) {
            timezone = DEFAULT_TIMEZONE;
        }
        Calendar cal = Calendar.getInstance(timezone);
        cal.setTimeInMillis(timestamp);
        StringBuilder buf = new StringBuilder();
        buf.append(cal.get(Calendar.YEAR)).append('-');
        buf.append(pad2(cal.get(Calendar.MONTH) + 1)).append('-');
        buf.append(pad2(cal.get(Calendar.DATE))).append(' ');
        buf.append(pad2(cal.get(Calendar.HOUR_OF_DAY))).append(':');
        buf.append(pad2(cal.get(Calendar.MINUTE))).append(':');
        buf.append(pad2(cal.get(Calendar.SECOND)));
        return buf.toString();
    }

    public static String formatToMinute(Date date, TimeZone timezone) {
        if (date == null) {
            return null;
        }
        return formatToMinute(date.getTime(), timezone);
    }

    public static String formatToMinute(long timestamp, TimeZone timezone) {
        if (timezone == null) {
            timezone = DEFAULT_TIMEZONE;
        }
        Calendar cal = Calendar.getInstance(timezone);
        cal.setTimeInMillis(timestamp);
        StringBuilder buf = new StringBuilder();
        buf.append(cal.get(Calendar.YEAR)).append('-');
        buf.append(pad2(cal.get(Calendar.MONTH) + 1)).append('-');
        buf.append(pad2(cal.get(Calendar.DATE))).append(' ');
        buf.append(pad2(cal.get(Calendar.HOUR_OF_DAY))).append(':');
        buf.append(pad2(cal.get(Calendar.MINUTE)));
        return buf.toString();
    }

    public static String formatToDay(Date date, TimeZone timezone) {
        if (date == null) {
            return null;
        }
        if (timezone == null) {
            timezone = DEFAULT_TIMEZONE;
        }
        Calendar cal = Calendar.getInstance(timezone);
        cal.setTime(date);
        StringBuilder buf = new StringBuilder();
        buf.append(cal.get(Calendar.YEAR)).append('-');
        buf.append(pad2(cal.get(Calendar.MONTH) + 1)).append('-');
        buf.append(pad2(cal.get(Calendar.DATE)));
        return buf.toString();
    }

    public static long getBetweenDays(Date t1, Date t2) {
        if (t1.getTime() > t2.getTime()) {
            return -getBetweenDays(t2, t1);
        }
        Calendar d1 = getDefaultCalendar(t1);
        Calendar d2 = getDefaultCalendar(t2);
        long days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y1 = d1.get(Calendar.YEAR);
        int y2 = d2.get(Calendar.YEAR);
        while (y1 < y2) {
            days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
            d1.add(Calendar.YEAR, 1);
            y1 = d1.get(Calendar.YEAR);
        }
        return days;
    }

    public static long[] getTodayStartEnd() {
        Calendar cal = DateUtils.getDefaultCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long since = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        long until = cal.getTimeInMillis();
        return new long[] { since, until };
    }

    public static long[] getMonthStartEnd() {
        Calendar cal = DateUtils.getDefaultCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long since = cal.getTimeInMillis();
        cal.add(Calendar.MONTH, 1);
        long until = cal.getTimeInMillis();
        return new long[] { since, until };
    }

    public static int getCanonicalWeek(Calendar cal) {
        cal.setFirstDayOfWeek(Calendar.SUNDAY);
        cal.setMinimalDaysInFirstWeek(7);
        return cal.getWeekYear() * 100 + cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getCanonicalMonth(Calendar cal) {
        return cal.get(Calendar.YEAR) * 100 + cal.get(Calendar.MONTH) + 1;
    }

    public static boolean isValidDate(String s) {
        if (s == null || s.length() != 10) {
            return false;
        }
        // yyyy-MM-dd
        String[] tokens = StringUtils.splitAsArray(s, "-");
        if (tokens.length != 3) {
            return false;
        }
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if (!StringUtils.isNumeric(token)) {
                return false;
            }
            if (i == 0) {
                if (token.length() != 4) {
                    return false;
                }
            } else {
                if (token.length() != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String formatDuration(long durationInSecond) {
        int second = (int) (durationInSecond % 60);
        long minutes = (durationInSecond - second) / 60;
        int minute = (int) (minutes % 60);
        long hour = minutes / 60;
        return new StringBuilder(9).append(StringUtils.pad(hour, 2)).append(':').append(StringUtils.pad(minute, 2))
                .append(':').append(StringUtils.pad(second, 2)).toString();
    }

    public static String formatUTCOffset(TimeZone timezone) {
        int offset = timezone.getRawOffset();
        boolean negative = offset < 0;
        offset = Math.abs(offset);
        int minutes = offset / 60000;
        int minute = minutes % 60;
        int hour = minutes / 60;
        return new StringBuilder(5).append(negative ? "-" : "+").append(StringUtils.pad(hour, 2))
                .append(StringUtils.pad(minute, 2)).toString();
    }

    public static String formatToYear(long time) {
        Date date = new Date(time);
        return new SimpleDateFormat("yyyy").format(date);
    }

    /**
     * 获取当月第一天的开始时间
     *
     * @return
     */
    public static Long getMonthFirstDayTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).with(TemporalAdjusters.firstDayOfMonth())
                .toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当月最后一天的结束时间
     *
     * @return
     */
    public static Long getMonthLastDayTime() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX).with(TemporalAdjusters.lastDayOfMonth())
                .toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 获取当天的开始时间 单位：毫秒
     *
     * @return Long
     */
    public static Long initTimeByDay() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    public static String getFormatedDateTime(long t) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(t);
    }

    public static String getBirthdateAge(Integer year, Integer month, Integer day) {
        int age = 0;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH) + 1;
        int dayNow = calendar.get(Calendar.DATE);
        if (yearNow == year) {
            age = 1;
        } else if (yearNow > year) {
            age = yearNow - year;
            if (monthNow > month) {
                age = age + 1;
            } else if (monthNow == month) {
                if (dayNow > day) {
                    age = age + 1;
                }

            }
        }
        return String.valueOf(age);
    }

    public static String getBirthDateConstellation(long birthday) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("Mdd");
        Date birthdayDate = new Date(birthday);
        String format = simpleDateFormat.format(birthdayDate);
        int date = Integer.parseInt(format);
        if (date >= 121 && date <= 219) {
            return "水瓶座";
        } else if (date >= 220 && date <= 320) {
            return "双鱼座";
        } else if (date >= 321 && date <= 420) {
            return "白羊座";
        } else if (date >= 421 && date <= 521) {
            return "金牛座";
        } else if (date >= 522 && date <= 621) {
            return "双子座";
        } else if (date >= 622 && date <= 722) {
            return "巨蟹座";
        } else if (date >= 723 && date <= 823) {
            return "狮子座";
        } else if (date >= 824 && date <= 923) {
            return "处女座";
        } else if (date >= 924 && date <= 1023) {
            return "天秤座";
        } else if (date >= 1024 && date <= 1122) {
            return "天蝎座";
        } else if (date >= 1123 && date <= 1221) {
            return "射手座";
        } else {
            return "魔蝎座";
        }
    }

    /**
     * 获取从当天时间到指定天数的结束时间
     *
     * @param past
     * @return
     */
    public static Long getFutureMillisecond(int past) {
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate.plusDays(past), LocalTime.MAX);
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }

    /**
     * 将指定日期转换为毫秒值
     *
     * @param string
     * @param suffix
     */
    public static void formatterToMilli(String string, String suffix) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(suffix, Locale.CANADA);
        System.out.println(LocalDateTime.parse(string, formatter).toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    /**
     * 计算当前日期与{@code endDate}的间隔天数
     *
     * @param endDate
     * @return 间隔天数
     */
    public static long until(LocalDate endDate) {
        return LocalDate.now().until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 计算日期{@code startDate}与{@code endDate}的间隔天数
     *
     * @param startDate
     * @param endDate
     * @return 间隔天数
     */
    public static long until(LocalDate startDate, LocalDate endDate) {
        return startDate.until(endDate, ChronoUnit.DAYS);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int differentDaysByMillisecond(Date date1, Date date2) {
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24));
        return days;
    }

}
