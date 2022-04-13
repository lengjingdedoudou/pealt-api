package com.peas.xinrui.common.resources;

import com.sunnysuperman.commons.locale.LocaleBundle;
import com.sunnysuperman.commons.locale.LocaleBundle.LocaleBundleOptions;
import com.sunnysuperman.commons.util.FileUtil;
import com.sunnysuperman.commons.util.StringUtil;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.Collections;
import java.util.Map.Entry;

import com.peas.xinrui.common.L;
import com.peas.xinrui.common.utils.StringUtils;

@StaticInit
public class LocaleBundles {
    private static SimpleLocaleBundle BUNDLE = null;

    static {
        try {
            String[] locales = new String[] { "zh_CN" };
            L.warn("Locales: " + StringUtil.join(locales, ","));

            LocaleBundleOptions options = new LocaleBundleOptions();
            options.setDefaultLocale(locales[0]);
            options.setPrefLocales(locales);
            options.setCompileStartToken("{");
            options.setCompileEndToken("}");
            BUNDLE = new SimpleLocaleBundle(options);
            for (String local : locales) {
                local = local.trim();
                java.util.Map<String, String> props = FileUtil.readProperties(R.getStream("locals/" + local + ".ini"),
                        StringUtil.UTF8, false);
                for (Entry<String, String> entry : props.entrySet()) {
                    String key = StringUtils.trimToNull(entry.getKey());
                    String value = StringUtils.trimToNull(entry.getValue());
                    if (key == null || value == null) {
                        continue;
                    }
                    BUNDLE.put(key, local, value);
                }
            }
            BUNDLE.finishPut();
        } catch (Exception ex) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            }
            throw new UndeclaredThrowableException(ex);
        }
    }

    public static boolean containsKey(String key) {
        return BUNDLE.containsKey(key);
    }

    public static String get(String locale, String key) {
        return BUNDLE.getWithParams(locale, key, null);
    }

    public static String getWithParams(String locale, String key, java.util.Map<String, Object> context) {
        if (context == null) {
            context = Collections.emptyMap();
        }
        return BUNDLE.getWithParams(locale, key, context);
    }

    public static String getWithArrayParams(String locale, String key, Object[] params) {
        return BUNDLE.getWithArrayParams(locale, key, params);
    }

    private static class SimpleLocaleBundle extends LocaleBundle {

        public SimpleLocaleBundle(LocaleBundleOptions options) {
            super(options);
        }

        @Override
        protected void put(String key, String locale, String value) throws Exception {
            super.put(key, locale, value);
        }

        @Override
        protected void finishPut() {
            super.finishPut();
        }

    }

}
