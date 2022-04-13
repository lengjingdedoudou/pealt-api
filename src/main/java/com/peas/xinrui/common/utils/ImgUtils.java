package com.peas.xinrui.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.sunnysuperman.commons.util.StringUtil;

import com.peas.xinrui.common.L;

public class ImgUtils {

    public static String base64FromURL(String url) {
        SimpleHttpClient client = new SimpleHttpClient();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        SimpleHttpClient.DownloadOptions options = new SimpleHttpClient.DownloadOptions().setMaxSize(3 * 1024 * 1024)
                .setRetrieveResponseHeaders(true);
        boolean downloaded = false;
        try {
            downloaded = client.download(url, baos, options);
        } catch (IOException ex) {
            // ignore
        }
        String result;
        if (downloaded) {
            try {
                baos.flush();
            } catch (IOException e) {
                L.error(e);
            }
            byte[] bytes = baos.toByteArray();
            String contentType = options.getResponseHeaders().get("content-type");
            if (contentType == null) {
                contentType = "image/jpg";
            }
            String prefix = "data:" + contentType + ";base64,";
            String imgAsBase64 = Base64Utils.encode(bytes);
            StringBuilder buf = new StringBuilder(prefix.length() + imgAsBase64.length());
            buf.append(prefix).append(imgAsBase64);
            result = buf.toString();
        } else {
            result = StringUtil.EMPTY;
        }
        return result;
    }

}
