package com.peas.xinrui.api.ui.entity;

import java.util.ArrayList;
import java.util.List;

public class UITypeVO {

    private static List<Byte> list = null;

    public static List<Byte> types() {
        if (list == null) {
            list = new ArrayList<>();
            for (UIType t : UIType.values()) {
                list.add(t.getType());
            }
        }
        return list;
    }

    public static boolean contains(Byte type) {
        return types().contains(type);
    }

}
