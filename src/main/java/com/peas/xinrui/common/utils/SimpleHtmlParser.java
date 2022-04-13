package com.peas.xinrui.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleHtmlParser {

    private static String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static String regEx_copyright = "<p data-f-id=[^>]*?>.*?</p>"; // 定义script的正则表达式

    // 过滤script标签
    public static String removeScript(String html) {
        if (StringUtils.isEmpty(html)) {
            return "";
        }
        html = removeTag(html, regEx_copyright);
        return removeTag(html, regEx_script);
    }

    private static String removeTag(String html, String reg) {
        Pattern p_script = Pattern.compile(reg, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(html);
        html = m_script.replaceAll("");
        return html.trim();
    }

}
