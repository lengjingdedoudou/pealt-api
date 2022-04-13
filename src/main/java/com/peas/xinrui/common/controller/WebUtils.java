package com.peas.xinrui.common.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sunnysuperman.commons.locale.LocaleUtil;

import com.peas.xinrui.common.utils.StringUtils;

public class WebUtils {

    /**
     * 获取Reqeust参数工具
     */
    public static String getHeader(HttpServletRequest request, String key) {
        String value = request.getParameter(key);
        if (value != null) {
            return value;
        }
        value = request.getHeader(key);
        if (value != null) {
            return value;
        }
        return null;
    }

    /**
     * 检测ip是否合法
     */
    private static boolean isValidIp(String ip) {
        // equalsIgnoreCase与equals区别
        // equalsIgnoreCase是不区分大小写的比较
        // equals是区分大小写的比较
        if (ip != null && ip.length() > 0 && !ip.equalsIgnoreCase("unknown")) {
            return true;
        }
        return false;
    }

    /**
     * 获取当前请求的真实ip地址
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        // X-Forwarded-For 用来表示 HTTP 请求端真实 IP
        // 格式：X-Forwarded-For: client, proxy1, proxy2
        // 第一个逗号前为真实ip，后两个为经过的代理
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null) {
            List<String> proxies = StringUtils.split(ip, ",");
            String remoteIp = proxies.size() > 0 ? proxies.get(0).trim() : null;
            if (isValidIp(remoteIp)) {
                return remoteIp;
            }
        }
        // 获取客户端ip地址
        ip = request.getRemoteAddr();
        if (isValidIp(ip)) {
            return ip;
        }
        return null;
    }

    /**
     * 获取当前请求本地语言
     */
    public static String getLocale(HttpServletRequest request, String key) {
        String locale = getHeader(request, key);
        locale = LocaleUtil.formatLocale(locale);
        if (StringUtils.isNotEmpty(locale)) {
            return locale;
        }
        java.util.Locale requestLocale = request.getLocale();
        if (requestLocale == null) {
            return null;
        }
        String lang = requestLocale.getLanguage();
        if (StringUtils.isEmpty(lang)) {
            return null;
        }
        if (StringUtils.isEmpty(requestLocale.getCountry())) {
            return lang;
        }
        return lang + '_' + requestLocale.getCountry();
    }

    /**
     * 获取请求头的UserAgent信息
     */
    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("user-agent");
    }

}
