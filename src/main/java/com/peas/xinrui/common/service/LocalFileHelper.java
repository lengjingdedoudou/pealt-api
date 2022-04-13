package com.peas.xinrui.common.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.sunnysuperman.commons.util.FileUtil;
import com.sunnysuperman.http.client.HttpClient;
import com.sunnysuperman.http.client.HttpDownloadOptions;

import org.springframework.stereotype.Component;

import com.peas.xinrui.common.L;
import com.peas.xinrui.common.utils.Base64Utils;
import com.peas.xinrui.common.utils.FileUtils;
import com.peas.xinrui.common.utils.UUIDGeneratorFactory;
import com.peas.xinrui.common.utils.UUIDGeneratorFactory.UUIDGenerator;
import okhttp3.ConnectionPool;

@Component
public class LocalFileHelper {
    private final UUIDGenerator TMP_FILE_ID_CREATOR = UUIDGeneratorFactory.create();
    private final String TMP_DIR = "/tmp/java07";

    private static HttpClient client;

    static {
        client = new HttpClient(new ConnectionPool(10, 30, TimeUnit.SECONDS));
        client.setConnectTimeout(15);
        client.setReadTimeout(30);
    }

    public static class FileWrapper {
        File file;
        String contentType;

        public FileWrapper(File file, String contentType) {
            super();
            this.file = file;
            this.contentType = contentType;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

    }

    private File makeTmpFile(boolean isDir, String extension) throws IOException {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);

        StringBuilder buf = new StringBuilder(TMP_DIR).append('/').append(year).append('/').append(month).append('/')
                .append(day).append('/').append(TMP_FILE_ID_CREATOR.generate());
        if (extension != null) {
            buf.append('.').append(extension);
        }
        File file = new File(buf.toString());
        boolean ok;
        if (isDir) {
            ok = file.mkdirs();
        } else {
            ok = FileUtil.ensureFile(file);
        }
        if (!ok) {
            throw new IOException("Failed to create file/dir");
        }
        return file;
    }

    public File createTmpFile(String extension) throws IOException {
        return makeTmpFile(false, extension);
    }

    public File createTmpDir() throws Exception {
        return makeTmpFile(true, null);
    }

    public File copyFromStream(InputStream in, String extension) throws IOException {
        File file = createTmpFile(extension);
        boolean ok = false;
        try {
            FileUtil.copy(in, new FileOutputStream(file));
            ok = true;
            return file;
        } finally {
            if (!ok) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    public FileWrapper downloadAndGetContentType(String url, long maxSize) throws IOException {
        String extension;
        int paramIndex = url.indexOf('?');
        if (paramIndex > 0) {
            extension = FileUtil.getFileExt(url.substring(0, paramIndex));
        } else {
            extension = FileUtil.getFileExt(url);
        }
        File file = createTmpFile(extension);
        FileWrapper result = null;
        try {
            HttpDownloadOptions options = new HttpDownloadOptions();
            options.setRetrieveResponseHeaders(true);
            options.setMaxSize(maxSize);
            boolean downloaded = client.download(url, new FileOutputStream(file), options);
            if (!downloaded) {
                return null;
            }
            Map<String, String> responseHeaders = options.getResponseHeaders();
            String contentType = responseHeaders.get("content-type");
            result = new FileWrapper(file, contentType);
            return result;
        } finally {
            if (result == null) {
                L.warn("Failed to download: " + url);
                FileUtils.deleteQuietly(file);
            }
        }
    }

    public File download(String url, long maxSize) throws IOException {
        FileWrapper wrapper = downloadAndGetContentType(url, maxSize);
        if (wrapper == null) {
            return null;
        }
        return wrapper.getFile();
    }

    public File download(String url) throws IOException {
        return download(url, -1);
    }

    public long getRemoteContentLength(String url) throws IOException {
        return client.getContentLength(url, null, null);
    }

    public String remoteImgToBase64(String url) throws IOException {
        FileWrapper file = downloadAndGetContentType(url, 3 * 1024 * 1024);
        if (file == null) {
            return null;
        }
        try {
            byte[] bytes = FileUtils.readAsByteArray(file.getFile());
            String contentType = file.getContentType();
            if (contentType == null) {
                contentType = "image/jpg";
            }
            String prefix = "data:" + contentType + ";base64,";
            String imgAsBase64 = Base64Utils.encode(bytes);
            StringBuilder buf = new StringBuilder(prefix.length() + imgAsBase64.length());
            buf.append(prefix).append(imgAsBase64);
            return buf.toString();
        } finally {
            FileUtils.deleteQuietly(file.getFile());
        }
    }
}
