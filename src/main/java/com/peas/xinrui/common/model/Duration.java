package com.peas.xinrui.common.model;

import java.util.Calendar;
import java.util.Date;

import com.peas.xinrui.common.utils.StringUtils;

public class Duration {
    public static final Duration FOREVER = new Duration(0, TimeUnit.YEAR);
    private int value;
    private TimeUnit unit;

    public Duration() {
        super();
    }

    public Duration(int value, TimeUnit unit) {
        super();
        this.value = value;
        this.unit = unit;
    }

    public static Duration parse(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        StringBuilder valueBuf = new StringBuilder();
        int unitIndex = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (StringUtils.isNumeric(String.valueOf(c))) {
                valueBuf.append(c);
            } else {
                unitIndex = i;
                break;
            }
        }
        if (valueBuf.length() == 0) {
            return null;
        }
        int value = Integer.parseInt(valueBuf.toString());
        String unitValue = s.substring(unitIndex);
        TimeUnit unit = TimeUnit.find(unitValue);
        if (unit == null) {
            return null;
        }
        return new Duration(value, unit);
    }

    public String toString() {
        return value + unit.getValue();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public Date addDate(Date from) {
        if (value == 0) {
            // forever
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(from);
        cal.add(unit.getCalendarUnit(), value);
        return cal.getTime();
    }

    public boolean forever() {
        return value == 0;
    }

    public boolean longerThan(Duration another) {
        Date now = new Date();
        Date d1 = addDate(now);
        Date d2 = another.addDate(now);
        if (d1 == d2) {
            return false;
        }
        if (d1 != null && d2 == null) {
            return false;
        }
        if (d1 == null && d2 != null) {
            return true;
        }
        return d1.after(d2);
    }

}
