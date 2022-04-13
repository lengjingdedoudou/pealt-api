package com.peas.xinrui.api.file.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.sunnysuperman.commons.util.FileUtil;
import com.sunnysuperman.commons.util.JSONUtil;
import com.sunnysuperman.commons.util.PlaceholderUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.peas.xinrui.api.file.entity.AliUploadToken;
import com.peas.xinrui.api.file.entity.OSSConfig;
import com.peas.xinrui.api.file.entity.UploadOptions;
import com.peas.xinrui.api.file.entity.UploadToken;
import com.peas.xinrui.common.L;
import com.peas.xinrui.common.exception.ServiceException;
import com.peas.xinrui.common.model.ErrorCode;
import com.peas.xinrui.common.resources.R;
import com.peas.xinrui.common.utils.SimpleHttpClient;
import com.peas.xinrui.common.utils.StringUtils;
import com.peas.xinrui.common.utils.URLUtils;
import com.peas.xinrui.common.utils.UUIDCreatorFactory;

@Service
public class FileService implements ErrorCode {
    private static final int MAX_UPLOAD_SIZE = 15 * 1024 * 1024;
    private static final String STS_API_VERSION = "2015-04-01";
    private static final UUIDCreatorFactory.UUIDCreator TMP_FILE_ID_CREATOR = UUIDCreatorFactory.get();
    private String POLICY_TEMPLATE;
    @Autowired
    private OSSConfig ossConfig;
    private OSS ossClient;
    private final UUIDCreatorFactory.UUIDCreator ossFileNameCreator = UUIDCreatorFactory.get();

    private static AssumeRoleResponse assumeRole(final String accessKeyId, final String accessKeySecret,
            final String roleArn, final String roleSessionName, final String policy, final int expireSeconds)
            throws Exception {
        // 创建一个 Aliyun Acs Client, 用于发起 OpenAPI 请求
        final IClientProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret);
        final DefaultAcsClient client = new DefaultAcsClient(profile);
        // 创建一个 AssumeRoleRequest 并设置请求参数
        final AssumeRoleRequest request = new AssumeRoleRequest();
        request.setVersion(STS_API_VERSION);
        request.setMethod(MethodType.POST);
        request.setProtocol(ProtocolType.HTTPS);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);
        request.setPolicy(policy);
        request.setDurationSeconds((long) expireSeconds);
        // 发起请求，并得到response
        return client.getAcsResponse(request);
    }

    @PostConstruct
    public void init() throws Exception {
        POLICY_TEMPLATE = FileUtil.read(R.getStream("file/file_ali_policy.json"));
        POLICY_TEMPLATE = JSONUtil.toJSONString(JSONUtil.parse(POLICY_TEMPLATE));
        ossClient = new OSSClientBuilder().build(ossConfig.getInternalEndpoint(), ossConfig.getKey(),
                ossConfig.getSecret());
    }

    public String generateObjectKey(final String namespace, final String fileName, final int randomLength) {
        return generateObjectKey(namespace, fileName, null, randomLength);
    }

    public String generateObjectKey(String namespace, final String fileName, String ext, final int randomLength) {
        if (StringUtils.isEmpty(namespace)) {
            namespace = "f";
        }
        final Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH) + 1;
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final StringBuilder buf = new StringBuilder();
        if (ossConfig.getNamespace() != null) {
            buf.append(ossConfig.getNamespace()).append('/');
        }
        buf.append(namespace).append('/').append(year).append('/').append(month).append('/').append(day).append('/')
                .append(StringUtils.randomLowerCaseAlphanumeric(1)).append('/').append(ossFileNameCreator.create());
        if (randomLength > 0) {
            buf.append(StringUtils.randomLowerCaseAlphanumeric(randomLength));
        }
        if (StringUtils.isEmpty(ext)) {
            ext = FileUtil.getFileExt(fileName);
        }
        if (ext != null) {
            buf.append('.').append(ext);
        }
        return buf.toString();
    }

    public String upload(final File file, final UploadOptions options) throws Exception {
        final ObjectMetadata metadata = new ObjectMetadata();
        String contentType = options.getContentType();
        if (contentType == null) {
            contentType = Files.probeContentType(file.toPath());
        }
        if (contentType != null) {
            metadata.setContentType(contentType);
        }
        if (options.getPermission() != null) {
            switch (options.getPermission()) {
                case PRIVATE:
                    metadata.setObjectAcl(CannedAccessControlList.Private);
                    break;
                case PUBLIC_READ:
                    metadata.setObjectAcl(CannedAccessControlList.PublicReadWrite);
                    break;
                default:
                    break;
            }
        }
        final String fileName = options.getFileName() != null ? options.getFileName() : file.getName();
        final String objectKey = generateObjectKey(options.getNamespace(), fileName, options.getRandomLength());
        ossClient.putObject(ossConfig.getBucket(), objectKey, file, metadata);
        return new StringBuilder("https://").append(ossConfig.getCanonicalDomain()).append("/").append(objectKey)
                .toString();
    }

    public UploadToken uploadToken(final String namespace, final String fileName, final int fileSize, final boolean cdn)
            throws Exception {
        if (fileSize > MAX_UPLOAD_SIZE) {
            throw new ServiceException(ERR_FILE_SIZE);
        }
        final String objectKey = generateObjectKey(namespace, fileName, 8);
        String policy;
        {
            final Map<String, Object> context = new HashMap<>(2);
            context.put("bucket", ossConfig.getBucket());
            context.put("objectKey", objectKey);
            policy = PlaceholderUtil.compile(POLICY_TEMPLATE, context);
        }
        final int expireIn = 1800;
        final AssumeRoleResponse response = assumeRole(ossConfig.getKey(), ossConfig.getSecret(), ossConfig.getPutArn(),
                "adm", policy, expireIn);
        final AliUploadToken token = new AliUploadToken();
        token.setVendor("ali");
        token.setAccessKey(response.getCredentials().getAccessKeyId());
        token.setAccessSecret(response.getCredentials().getAccessKeySecret());
        token.setStsToken(response.getCredentials().getSecurityToken());
        token.setRegion(ossConfig.getRegion());
        token.setEndpoint("https://oss-" + ossConfig.getRegion() + ".aliyuncs.com");

        token.setBucket(ossConfig.getBucket());
        token.setKey(objectKey);
        token.setUrl("https://" + (cdn ? ossConfig.getCdnDomain() : ossConfig.getCanonicalDomain()) + "/" + objectKey);
        token.setExpireIn(expireIn);
        return token;
    }

    public String makeSignedUrl(final String objectKey, final int expireSeconds, final boolean cdn) throws Exception {
        final Date expiration = new Date(System.currentTimeMillis() + 1000L * expireSeconds);
        final String signedUrl = ossClient.generatePresignedUrl(ossConfig.getBucket(), objectKey, expiration)
                .toString();
        final String finalUrl = URLUtils.replaceDomain(signedUrl,
                (cdn ? ossConfig.getCdnDomain() : ossConfig.getCanonicalDomain()));
        return finalUrl;
    }

    public void delete(final File file) {
        if (file == null) {
            return;
        }
        try {
            file.delete();
        } catch (final Exception e) {
            L.error(e);
        }
    }

    public String md5(final File file) throws IOException {
        final InputStream in = new FileInputStream(file);
        try {
            return DigestUtils.md5Hex(in);
        } finally {
            FileUtil.close(in);
        }
    }

    public File createTmpFile(final String namespace, String fileName, final String extension) throws Exception {
        final File dir = new File("/tmp/hanlin-api", namespace);
        dir.mkdirs();
        if (StringUtils.isEmpty(fileName)) {
            fileName = TMP_FILE_ID_CREATOR.create();
        }
        if (extension != null) {
            fileName += "." + extension;
        }
        final File file = new File(dir, fileName);
        return file;
    }

    public File createTmpFile(final String namespace, final String extension) throws Exception {
        return createTmpFile(namespace, null, extension);
    }

    /***
     * 下载文件
     * 
     * @param url
     * @param file
     */
    public void download(final String url, final File file) {
        try {
            final FileOutputStream out = new FileOutputStream(file);
            new SimpleHttpClient().download(url, out);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件夹
     *
     * @param folderPath
     * @param folderName
     * @return
     */
    public String createFolder(final String folderPath, final String folderName) {
        final File file = new File(folderPath + File.separator + folderName);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getPath();
    }

    public File createFile(final String filePath, final String fileName) {
        File file = null;
        if (!StringUtils.isEmpty(filePath) && !StringUtils.isEmpty(fileName)) {
            file = new File(filePath + File.separator + fileName);
        } else {
            throw new ServiceException(ErrorCode.ERR_ILLEGAL_ARGUMENT);
        }
        return file;
    }

    /**
     * 获取文件长度
     *
     * @param file
     */
    public int getFileSize(final File file) {
        int length = 0;
        if (file.exists() && file.isFile()) {
            final String fileName = file.getName();
            length = (int) file.length();
            System.out.println("文件" + fileName + "的大小是：" + length);
            return length;
        }
        return length;
    }

    /**
     * java对象写入文件
     *
     * @param properties
     * @param folder
     * @param fileName
     * @param separator
     */
    public void obgectToFile(final Properties properties, final String folder, final String fileName,
            final String separator) {
        PrintWriter printWriter = null;
        try {
            // 创建自动刷新字符打印流对象
            printWriter = new PrintWriter(new FileWriter(createFile(folder, fileName)), true);
            // 拿到properties中所有的key
            final Set<Object> keys = properties.keySet();
            // 遍历所有的key
            for (final Object object : keys) {
                // 将object强转成String
                final String key = (String) object;
                // 通过key获取到相对应的值
                final String value = properties.getProperty(key);
                // key和值写入到文件中
                printWriter.println(key + separator + value);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

}
