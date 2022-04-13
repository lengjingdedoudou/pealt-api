package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.util.StringUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends StringUtil {
    public static final String LOWERCASE_ALPHA_NUMERIC = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String NUMERIC = "0123456789";
    private static final String ALPHA_NUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String EXPLICIT_ALPHA_NUMERIC = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final Pattern EMAIL_CHECKER = Pattern
            .compile("^([a-z0-9A-Z]+[-|\\._]?)+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    private static final Pattern MOBILE_CHECKER = Pattern.compile("[0-9]*");
    private static final Pattern IDENTITY_CHECKER = Pattern
            .compile("^[1-9]\\d{5}(19|20)\\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]");

    private static final AffineTransform atf = new AffineTransform();
    private static final FontRenderContext frc = new FontRenderContext(atf, true, true);
    private static final String allnums = "0123456789";
    private static final String ELLIPSIS = "...";

    public static void main(String[] args) {
        System.out.println(randomAlphanumeric(32));
        System.out.println(randomAlphanumeric(43));
    }

    public static int getWidth(String str) {
        if (isEmpty(str)) {
            return 0;
        }
        Font font = new Font(null, 0, 12);
        return (int) font.getStringBounds(str, frc).getWidth();
    }

    public static boolean validMobile(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return str.length() > 0 && str.length() <= 11 && isNum.matches();
    }

    public static boolean isEmail(String input) {
        if (input == null) {
            return false;
        }
        int len = input.length();
        if (len == 0 || len > 64) {
            return false;
        }
        Matcher matcher = EMAIL_CHECKER.matcher(input);
        return matcher.matches();
    }

    public static boolean passwordLength(String input) {
        return input.length() <= 18 && input.length() >= 6;
    }

    public static boolean MobileLength(String input) {
        if (input == null)
            return false;
        return input.length() == 11;
    }

    public static boolean isNotChinaMobile(String input) {
        if (input == null)
            return false;
        Matcher matcher = MOBILE_CHECKER.matcher(input);
        return !(input.length() == 11 && matcher.matches());
    }

    public static boolean isNull(Object o) {
        return o == null || o.toString().trim().equals("");
    }

    public static String getRandNum(int length) {
        return randomString(allnums, length);
    }

    public static boolean isValidIdentity(String s) {
        if (isEmpty(s)) {
            return false;
        }
        Matcher matcher = IDENTITY_CHECKER.matcher(s);
        if (!matcher.matches()) {
            return false;
        }
        int[] w = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 };
        int sum = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            int num = ch == 'x' || ch == 'X' ? 10 : Integer.parseInt(String.valueOf(ch));
            sum += num * w[i];
        }
        if (sum % 11 != 1) {
            return false;
        }
        return true;
    }

    public static boolean isUpperCaseAlpha(char c) {
        return c >= 'A' && c <= 'Z';
    }

    public static boolean isLowerCaseAlpha(char c) {
        return c >= 'a' && c <= 'z';
    }

    public static boolean isAlpha(char c) {
        // 大写字母
        if (c >= 65 && c <= 90) {
            return true;
        }
        // 小写字母
        if (c >= 97 && c <= 122) {
            return true;
        }
        return false;
    }

    public static boolean isAlphanumeric(String s) {
        return isTargetString(ALPHA_NUMERIC, s);
    }

    public static String randomAlphanumeric(int length) {
        return randomString(ALPHA_NUMERIC, length);
    }

    public static String randomLowerCaseAlphanumeric(int length) {
        return randomString(LOWERCASE_ALPHA_NUMERIC, length);
    }

    public static boolean isLowerCaseAlphanumeric(String s) {
        return isTargetString(LOWERCASE_ALPHA_NUMERIC, s);
    }

    public static String randomExplicitAlphanumeric(int length) {
        return randomString(EXPLICIT_ALPHA_NUMERIC, length);
    }

    public static String createReferralCode() {
        String[] beforeShuffle = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
                "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
        List<String> list = Arrays.asList(beforeShuffle);
        Collections.shuffle(list);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
        }
        String afterShuffle = sb.toString();
        String result = afterShuffle.substring(5, 9);
        return result;
    }

    public static boolean isUpperCaseAlpha(String s) {
        if (isEmpty(s)) {
            return false;
        }
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < 65 || c > 90) {
                return false;
            }
        }
        return true;
    }

    public static String randomNumeric(int length) {
        return randomString(NUMERIC, length);
    }

    // TODO use Validators.validateUsername instead
    public static String validateUsername(String username) throws Exception {
        username = nullToEmpty(username).toLowerCase();
        if (username.length() < 6 || username.length() > 18) {
            return null;
        }
        if (!isAlpha(username.charAt(0))) {
            // 要以英文字母开头
            return null;
        }
        for (int i = 0; i < username.length(); i++) {
            char c = username.charAt(i);
            if (isAlpha(c) || isNumeric(c) || c == '_') {
                // 英文字母/数字/下划线
                continue;
            }
            return null;
        }
        return username;
    }

    public static String validatePassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 18) {
            return null;
        }
        return password;
    }

    public static String validateStrongPassword(String password) {
        if (password == null || password.length() < 6 || password.length() > 18) {
            return null;
        }
        boolean containsNumeric = false;
        boolean containsLetter = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (c >= '0' && c <= '9') {
                containsNumeric = true;
            } else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
                containsLetter = true;
            } else if (c == '_' || c == '-') {

            } else {
                return null;
            }
        }
        if (containsNumeric && containsLetter) {
            return password;
        }
        return null;
    }

    public static String encryptPassword(String password, String salt) {
        if (password == null) {
            return null;
        }
        String encryptPassword = DigestUtils.sha256Hex(password + salt);
        encryptPassword = DigestUtils.sha256Hex(encryptPassword);
        return encryptPassword;
    }

    public static String trimByLength(String s, int len) {
        if (s == null) {
            return s;
        }
        int originalLen = s.length();
        if (originalLen <= len || originalLen <= ELLIPSIS.length()) {
            return s;
        }
        return s.substring(0, len - ELLIPSIS.length()) + ELLIPSIS;
    }

    public static String limitByLength(String s, int maxLength) {
        if (s == null) {
            return null;
        }
        s = s.trim();
        int len = s.length();
        if (len == 0) {
            return null;
        }
        if (len > maxLength) {
            s = s.substring(0, maxLength);
        }
        return s;
    }

    // 订单号
    public static String getOrderNumber() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate = sdf.format(new Date());
        StringBuilder result = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 3; i++) {
            result.append(random.nextInt(10));
        }
        return newDate + result;
    }

    public static String hideString(String s) {
        if (s == null) {
            return null;
        }
        int len = s.length();
        if (len < 1) {
            return null;
        }
        final String placeholder = "*";

        int hideLen = (int) Math.ceil(len / 3d);
        if (hideLen == 0) {
            return placeholder;
        }
        if (hideLen == len) {
            return placeholder;
        }
        int startLen = (int) Math.ceil((len - hideLen) / 2d); // >0
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < startLen; i++) {
            buf.append(s.charAt(i));
        }
        for (int i = 0; i < hideLen; i++) {
            buf.append(placeholder);
        }
        int tailOffset = startLen + hideLen;
        for (int i = tailOffset; i < len; i++) {
            buf.append(s.charAt(i));
        }
        return buf.toString();
    }

    public static String filterFourBytesChars(String s) {
        if (isEmpty(s)) {
            return s;
        }
        return s.replaceAll("[^\\u0000-\\uFFFF]", EMPTY);
    }

    public static String pad(Number number, int length, boolean limitToMax) {
        String s = number.toString();
        int padLen = length - s.length();
        if (padLen > 0) {
            StringBuilder buf = new StringBuilder(length);
            for (int i = 0; i < padLen; i++) {
                buf.append('0');
            }
            buf.append(s);
            return buf.toString();
        }
        if (padLen == 0 || !limitToMax) {
            return s;
        }
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            buf.append('9');
        }
        return buf.toString();
    }

    public static String pad(Number number, int length) {
        return pad(number, length, false);
    }

    public static Set<String> splitAsSet(String s, String separatorChars) {
        List<String> tokens = StringUtils.split(s, separatorChars);
        if (tokens == null) {
            return null;
        }
        return new HashSet<>(tokens);
    }

    public static int[] splitAsIntArray(String s, String separatorChars) {
        List<String> tokens = StringUtils.split(s, separatorChars);
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }
        int[] array = new int[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            int value = Integer.parseInt(tokens.get(i));
            array[i] = value;
        }
        return array;
    }

    public static long[] splitAsLongArray(String s, String separatorChars) {
        List<String> tokens = StringUtils.split(s, separatorChars);
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }
        long[] array = new long[tokens.size()];
        for (int i = 0; i < tokens.size(); i++) {
            long value = Long.parseLong(tokens.get(i));
            array[i] = value;
        }
        return array;
    }

    public static List<Long> splitAsLongList(String s, String separatorChars) {
        List<String> tokens = StringUtils.split(s, separatorChars);
        if (tokens == null || tokens.isEmpty()) {
            return null;
        }
        List<Long> list = new ArrayList<>(tokens.size());
        for (int i = 0; i < tokens.size(); i++) {
            list.add(Long.valueOf(tokens.get(i)));
        }
        return list;
    }

    public static String trimUtf8mb4(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }
        return s.replaceAll("[^\\u0000-\\uD7FF\\uE000-\\uFFFF]", "");
    }

    public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
        return isEmpty(str) ? defaultStr : str;
    }

    public static String defaultString(final String str, final String defaultStr) {
        return str == null ? defaultStr : str;
    }

    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return org.apache.commons.lang3.StringUtils.replace(str, "'", "''");
    }

    public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }

    public static String getIdentityBirth(String identity, String format) {
        String substring = identity.substring(6, 14);
        String year = substring.substring(0, 4);
        String month = substring.substring(4, 6);
        String day = substring.substring(6, 8);
        return year + format + month + format + day;
    }

    public static Integer getIdentityBirthYear(String identity) {
        return Integer.parseInt(identity.substring(0, 4));
    }

    public static Integer getIdentityBirthMonth(String identity) {
        return Integer.parseInt(identity.substring(4, 6));
    }

    public static Integer getIdentityBirthDay(String identity) {
        return Integer.parseInt(identity.substring(6, 8));
    }

    public static String concat(String... strs) {
        int len = 0;
        for (String str : strs) {
            len += str.length();
        }
        StringBuilder buf = new StringBuilder(len);
        for (String str : strs) {
            buf.append(str);
        }
        return buf.toString();
    }

    public static String concat(String delimiter, Object... objects) {
        String[] strs = new String[objects.length];
        int i = 0;
        for (Object obj : objects) {
            String s = obj.toString();
            strs[i] = s;
            i++;
        }
        return concatWithDelimiter(delimiter, strs);
    }

    public static String concatWithDelimiter(String delimiter, String... strs) {
        final int delimiterLen = delimiter.length();
        int len = 0;
        for (String str : strs) {
            len += str.length() + delimiterLen;
        }
        len -= delimiterLen;
        StringBuilder buf = new StringBuilder(len);
        boolean first = true;
        for (String str : strs) {
            if (first) {
                first = false;
            } else {
                buf.append(delimiter);
            }
            buf.append(str);
        }
        return buf.toString();
    }

    public static Integer day(Long time) {
        return Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(new Date(time)));
    }

    public static Integer month(Long time) {
        return Integer.parseInt(new SimpleDateFormat("yyyyMM").format(new Date(time)));
    }

    public static Integer year(Long time) {
        return Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date(time)));
    }

    public static String httpsToHttp(String url) {
        String[] split = url.split(":");
        return "http:" + split[1];
    }

    public static List<String> wordSeg(String s) throws IOException {
        List<String> words = new ArrayList<>();
        IKSegmenter ik = new IKSegmenter(new StringReader(s), false);
        Lexeme lex;
        while ((lex = ik.next()) != null) {
            String lexemeText = lex.getLexemeText();
            words.add(lexemeText);
        }
        return words;
    }

}