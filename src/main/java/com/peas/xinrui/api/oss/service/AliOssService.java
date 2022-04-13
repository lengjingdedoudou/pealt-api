package com.peas.xinrui.api.oss.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Peas
 */
public interface AliOssService {

    /**
     * 创建存储空间
     */
    void createBucket();

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return
     */
    String upload(MultipartFile file);

    /**
     * 下载文件
     *
     * @throws IOException
     */
    void download(String fileName) throws IOException;

    /**
     * 列举文件
     */
    List<String> listFile();

    /**
     * 删除文件
     */
    void deleteFile(String fileName);

}