package com.peas.xinrui.api.ui.entity;

import java.util.ArrayList;
import java.util.List;

public class UIComponentKeyVO {

    private static List<String> list = null;

    public static List<String> types() {
        if (list == null) {
            list = new ArrayList<>();
            for (UIComponentKey t : UIComponentKey.values()) {
                list.add(t.name());
            }
        }
        return list;
    }

    public static boolean contains(String type) {
        return types().contains(type);
    }

}
