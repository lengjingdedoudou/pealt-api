package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.util.FormatUtil;
import com.sunnysuperman.commons.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;

public class URLUtils {

    public static boolean isValidHttpURL(String url) {
        if (url == null) {
            return false;
        }
        return url.startsWith("http://") || url.startsWith("https://");
    }

    public static String getDomainFromURL(String url) {
        if (url == null) {
            return null;
        }
        // http://xx.yourdomain.com/xx
        int protocolIndex = url.indexOf("//");
        if (protocolIndex < 0) {
            return null;
        }
        int domainStartIndex = protocolIndex + 2;
        int domainEndIndex = url.indexOf('/', domainStartIndex);
        if (domainEndIndex < 0) {
            return url.substring(domainStartIndex);
        }
        String domain = url.substring(domainStartIndex, domainEndIndex);
        return domain;
    }

    public static String[] getDomainAndPathFromURL(String url) {
        if (url == null) {
            return null;
        }
        // http://xx.yourdomain.com/xx
        int protocolIndex = url.indexOf("//");
        if (protocolIndex < 0) {
            return null;
        }
        int domainStartIndex = protocolIndex + 2;
        int domainEndIndex = url.indexOf('/', domainStartIndex);
        String domain;
        String path = null;
        if (domainEndIndex < 0) {
            domain = url.substring(domainStartIndex);
        } else {
            domain = url.substring(domainStartIndex, domainEndIndex);
            path = url.substring(domainEndIndex + 1);
        }
        if (path == null) {
            path = StringUtil.EMPTY;
        }
        return new String[] { domain, path };
    }

    public static String getHash(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        int index = url.indexOf('#');
        if (index < 0) {
            return null;
        }
        String hash = url.substring(index + 1);
        if (hash.isEmpty()) {
            return null;
        }
        return hash;
    }

    public static String replaceDomain(String url, String domain) {
        if (url == null) {
            return null;
        }
        // http://xx.yourdomain.com/xx
        int protocolIndex = url.indexOf("//");
        if (protocolIndex < 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        buf.append(url, 0, protocolIndex).append("//");
        int domainStartIndex = protocolIndex + 2;
        int domainEndIndex = url.indexOf('/', domainStartIndex);
        if (domainEndIndex < 0) {
            return null;
        }
        buf.append(domain).append(url.substring(domainEndIndex));
        return buf.toString();
    }

    public static String decodeURL(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        try {
            return URLDecoder.decode(url, StringUtil.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String encodeURL(String url) {
        if (StringUtil.isEmpty(url)) {
            return null;
        }
        try {
            return URLEncoder.encode(url, StringUtil.UTF8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String wrapURL(String url, Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        int hashIndex = url.indexOf('#');
        String hash = null;
        if (hashIndex > 0) {
            hash = url.substring(hashIndex);
            url = url.substring(0, hashIndex);
        }
        StringBuilder buf = new StringBuilder(url);
        if (url.indexOf('?') > 0) {
            buf.append('&');
        } else {
            buf.append('?');
        }
        int i = 0;
        for (Entry<String, ?> entry : params.entrySet()) {
            String value = FormatUtil.parseString(entry.getValue());
            if (i > 0) {
                buf.append("&");
            }
            buf.append(entry.getKey()).append("=");
            if (value != null) {
                try {
                    buf.append(URLEncoder.encode(value, StringUtil.UTF8));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            i++;
        }
        if (hash != null) {
            buf.append(hash);
        }
        return buf.toString();
    }

    public static Map<String, String> parseURLParams(String search) {
        List<String> pairs = StringUtil.split(search, "&");
        if (pairs == null || pairs.isEmpty()) {
            return null;
        }
        Map<String, String> kvs = new HashMap<>(pairs.size());
        for (String pair : pairs) {
            List<String> tokens = StringUtil.split(pair, "=");
            String key = tokens.get(0);
            if (key.isEmpty()) {
                continue;
            }
            String value = tokens.size() > 1 ? tokens.get(1) : null;
            if (value != null) {
                try {
                    value = URLDecoder.decode(value, StringUtil.UTF8);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                value = StringUtil.EMPTY;
            }
            kvs.put(key, value);
        }
        return kvs;
    }

    public static Map<String, String> getURLParamsBySearch(String search) {
        List<String> pairs = StringUtils.split(search, "&");
        if (pairs == null || pairs.isEmpty()) {
            return null;
        }
        Map<String, String> kvs = new HashMap<>(pairs.size());
        for (String pair : pairs) {
            List<String> tokens = StringUtils.split(pair, "=");
            String key = tokens.get(0);
            if (key.isEmpty()) {
                continue;
            }
            String value = tokens.size() > 1 ? tokens.get(1) : null;
            if (value != null) {
                try {
                    value = URLDecoder.decode(value, StringUtils.UTF8);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                value = StringUtils.EMPTY;
            }
            kvs.put(key, value);
        }
        return kvs;
    }

    public static String setURLHashParams(String url, Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        int hashIndex = url.indexOf('#');
        StringBuilder buf = new StringBuilder(url);
        if (hashIndex < 0) {
            buf.append("#?");
        } else {
            boolean hasHashParams = url.indexOf('?', hashIndex) >= 0;
            buf.append(hasHashParams ? '&' : '?');
        }
        int i = 0;
        for (Entry<String, ?> entry : params.entrySet()) {
            String value = FormatUtil.parseString(entry.getValue());
            if (i > 0) {
                buf.append("&");
            }
            buf.append(entry.getKey()).append("=");
            if (value != null) {
                try {
                    buf.append(URLEncoder.encode(value, StringUtils.UTF8));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            i++;
        }
        return buf.toString();
    }

    public static String setURLParams(String url, Map<String, ?> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        int hashIndex = url.indexOf('#');
        String hash = null;
        if (hashIndex > 0) {
            hash = url.substring(hashIndex);
            url = url.substring(0, hashIndex);
        }
        StringBuilder buf = new StringBuilder(url);
        if (url.indexOf('?') > 0) {
            buf.append('&');
        } else {
            buf.append('?');
        }
        int i = 0;
        for (Entry<String, ?> entry : params.entrySet()) {
            String value = FormatUtil.parseString(entry.getValue());
            if (i > 0) {
                buf.append("&");
            }
            buf.append(entry.getKey()).append("=");
            if (value != null) {
                try {
                    buf.append(URLEncoder.encode(value, StringUtils.UTF8));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            i++;
        }
        if (hash != null) {
            buf.append(hash);
        }
        return buf.toString();
    }
}
