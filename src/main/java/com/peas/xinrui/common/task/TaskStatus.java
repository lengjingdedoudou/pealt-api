package com.peas.xinrui.common.task;

public enum TaskStatus {
    TODO(1), ING(2), SUCCESS(3), FAIL(4), PARTLY_SUCCESS(5);

    private byte value;

    private TaskStatus(int value) {
        this.value = (byte) value;
    }

    public byte value() {
        return this.value;
    }

    public static TaskStatus find(byte value) {
        for (TaskStatus item : TaskStatus.values()) {
            if (item.value == value) {
                return item;
            }
        }
        return null;
    }
}
