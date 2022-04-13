package com.peas.xinrui.common.service;

import com.sunnysuperman.commons.util.PlaceholderUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class UrlRouter {
    private static Map<String, String> ROUTES;

    static {
        Map<String, String> routes = new HashMap<>();
        routes.put("trade", "/trade/trade-detail/${id}");
        ROUTES = Collections.unmodifiableMap(routes);
    }

    public static String get(String key, Map<String, Object> params) {
        if (key == null) {
            // defaults to home
            return "/";
        }
        String path = ROUTES.get(key);
        if (path == null) {
            return null;
        }
        return PlaceholderUtil.compile(path, params);
    }
}
