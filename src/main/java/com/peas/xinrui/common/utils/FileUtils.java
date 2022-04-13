package com.peas.xinrui.common.utils;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.imageio.ImageIO;

import com.sunnysuperman.commons.util.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.peas.xinrui.common.L;

@SuppressWarnings("all")
public class FileUtils extends FileUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);
    private static final String TEMP_DIR = "/opt/data/java07/log/tmp_file";

    public static void deleteQuietly(File file) {
        if (file == null) {
            return;
        }
        try {
            FileUtil.delete(file);
        } catch (Exception e) {
            LOG.error(null, e);
        }
    }

    public static long getRemoteFileSize(String url) throws IOException {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("HEAD");
            conn.getInputStream();
            return conn.getContentLengthLong();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static File getRemoteFile(String url, String fileName) throws Exception {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            InputStream inputStream = conn.getInputStream();
            byte[] getData = readInputStream(inputStream);
            File saveDir = new File(TEMP_DIR);
            if (!saveDir.exists()) {
                saveDir.mkdir();
            }
            File file = new File(saveDir + File.separator + fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getData);
            if (fos != null) {
                fos.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return file;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static byte[] readLocalFile(String fileName) {
        if (StringUtils.isEmpty(fileName)) {
            return null;
        }
        File file = new File(TEMP_DIR + fileName);
        if (!file.exists()) {
            return null;
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len = 0;
            while ((len = in.read(buffer, 0, buf_size)) != -1) {
                bos.write(buffer, 0, buf_size);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            LOG.error(null, e);
            return null;
        } finally {
            try {
                in.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void writeBytesToFile(byte[] bytes, String fileName) {
        try (FileOutputStream fos = new FileOutputStream(TEMP_DIR + fileName)) {
            fos.write(bytes);
        } catch (IOException e) {
            L.error(e);
        }
    }

    public static byte[] readRemoteFile(String url, Integer width, Integer height) throws Exception {
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            inputStream = conn.getInputStream();
            return width != null && height != null ? readInputStream(inputStream, width, height)
                    : readInputStream(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static byte[] readRemoteFile(String url) throws Exception {
        return readRemoteFile(url, null, null);
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

    public static byte[] readInputStream(InputStream inputStream, Integer width, Integer height) throws Exception {
        BufferedImage bufferedImage = null;// Thumbnails.of(inputStream).size(width, height).asBufferedImage();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", bos);
        bos.flush();
        byte[] bytes = bos.toByteArray();
        bos.close();
        return bytes;
    }
}
