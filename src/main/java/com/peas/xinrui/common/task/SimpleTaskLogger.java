package com.peas.xinrui.common.task;

public class SimpleTaskLogger implements TaskLogger {
    private final String LINE = "\r\n"; // 兼容windows换行符
    private StringBuilder buf = new StringBuilder();

    @Override
    public void append(String line) {
        buf.append(line).append(LINE);
    }

    public StringBuilder getBuffer() {
        return buf;
    }

    @Override
    public void flush() {
    }

}
