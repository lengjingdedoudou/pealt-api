package cn.maidaotech.java07.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.sunnysuperman.commons.bean.Bean;
import com.sunnysuperman.commons.util.FileUtil;
import com.sunnysuperman.commons.util.FormatUtil;
import com.sunnysuperman.commons.util.JSONUtil;

import org.apache.commons.lang3.ClassUtils;
import org.junit.Assert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.peas.xinrui.api.file.entity.AliUploadToken;
import com.peas.xinrui.common.utils.FileUtils;
import com.peas.xinrui.common.utils.SimpleHttpClient;
import com.peas.xinrui.common.utils.StringUtils;
import com.peas.xinrui.common.utils.URLUtils;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class ApiMock
{
    public static String API_VERSION = "";

    public static class ApiError
    {
        private int errcode;

        public ApiError(int errcode)
        {
            super();
            this.errcode = errcode;
        }

        public int getErrcode()
        {
            return errcode;
        }

        public void setErrcode(int errcode)
        {
            this.errcode = errcode;
        }

    }

    private String responseBody;

    private OkHttpClient getClient()
    {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.DAYS).readTimeout(1, TimeUnit.DAYS)
                .build();
        return client;
    }

    private Request.Builder requestBuilder(String api, Map<String, ?> params, Map<String, Object> headers)
    {
        String url = "http://localhost:8080/" + api;
        Request.Builder builder = new Request.Builder().url(URLUtils.setURLParams(url, params));
        if (headers != null)
        {
            for (Entry<String, Object> entry : headers.entrySet())
            {
                if (entry.getValue() == null)
                {
                    continue;
                }
                builder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        return builder;
    }

    protected abstract Map<String, Object> wrapHeaders() throws Exception;

    private Map<String, Object> headers() throws Exception
    {
        Map<String, Object> headers = new HashMap<>();
        headers.put("x-api-version", API_VERSION);
        Map<String, Object> wrapHeaders = wrapHeaders();
        if (wrapHeaders != null)
        {
            headers.putAll(wrapHeaders);
        }
        return headers;
    }

    private ApiMock post(String api, Map<String, Object> params, Map<String, Object> headers) throws IOException
    {
        if (params != null)
        {
            boolean uploadMode = false;
            for (Object value : params.values())
            {
                if (value == null)
                {
                    continue;
                }
                if (value instanceof File)
                {
                    uploadMode = true;
                    break;
                }
            }
            if (uploadMode)
            {
                return doUpload(api, params, headers);
            }
        }
        Request.Builder builder = requestBuilder(api, null, headers);
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params != null)
        {
            for (Entry<String, Object> entry : params.entrySet())
            {
                String value = StringUtils.nullToEmpty(FormatUtil.parseString(entry.getValue()));
                bodyBuilder.add(entry.getKey(), value);
                System.out.println(entry.getKey() + ": " + value);
            }
        }
        Request request = builder.post(bodyBuilder.build()).build();
        execute(request);
        return this;
    }

    private ApiMock doUpload(String api, Map<String, Object> params, Map<String, Object> headers) throws IOException
    {
        Request.Builder builder = requestBuilder(api, null, headers);
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (String key : params.keySet())
        {
            Object value = params.get(key);
            if (value instanceof File)
            {
                File file = (File) value;
                bodyBuilder.addPart(
                        Headers.of("Content-Disposition",
                                "form-data; name=\"" + key + "\";filename=\"" + file.getName() + "\""),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file));
            }
            else
            {
                bodyBuilder.addFormDataPart(key, FormatUtil.parseString(value));
            }
        }
        Request request = builder.post(bodyBuilder.build()).build();
        execute(request);
        return this;
    }

    private Map<String, Object> paramsAsMap(Object[] paramsAsArray)
    {
        Map<String, Object> params = new HashMap<>();
        if (paramsAsArray != null && paramsAsArray.length > 0)
        {
            params = new HashMap<>(paramsAsArray.length / 2);
            for (int i = 0; i < paramsAsArray.length; i += 2)
            {
                Object value = paramsAsArray[i + 1];
                if (value == null)
                {
                    continue;
                }
                params.put(paramsAsArray[i].toString(), value);
            }
        }
        return params;
    }

    private void execute(Request request) throws IOException
    {
        Response response = getClient().newCall(request).execute();
        if (response.code() != 200)
        {
            System.out.println(response);
            throw new IOException("Failed to call " + request.url() + ", error-code: " + response.code());
        }
        responseBody = response.body().string();
        System.out.println(JSONUtil.toJSONString(JSONUtil.parse(responseBody), null,
                SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.PrettyFormat));
    }

    public ApiMock post(String api, Object... params) throws Exception
    {
        System.out.println(api);
        return post(api, paramsAsMap(params), headers());
    }

    public ApiMock post(String api, Map<String, Object> params) throws Exception
    {
        System.out.println(api);
        return post(api, params, headers());
    }

    public ApiMock post(String api, Map<String, ?> params, String mediaType, String body, Map<String, Object> headers)
            throws IOException
    {
        Request.Builder reqBuilder = requestBuilder(api, params, headers);
        RequestBody reqBody = RequestBody.create(MediaType.parse(mediaType), body);
        Request request = reqBuilder.post(reqBody).build();
        execute(request);
        return this;
    }

    public ApiMock assertError()
    {
        return assertError(-1, null);
    }

    public ApiMock assertError(int errorCode)
    {
        return assertError(errorCode, null);
    }

    public ApiMock assertError(int errorCode, String errorMsg)
    {
        Map<String, Object> ret = JSONUtil.parseJSONObject(responseBody);
        int realErrorCode = FormatUtil.parseInteger(ret.get("errcode")).intValue();
        if (errorCode <= 0)
        {
            Assert.assertTrue(realErrorCode > 0);
        }
        else
        {
            Assert.assertTrue(realErrorCode == errorCode);
        }
        if (errorMsg != null)
        {
            Assert.assertTrue(ret.get("errmsg").toString().equals(errorMsg));
        }
        return this;
    }

    public String rawResult()
    {
        return responseBody;
    }

    public ApiMock assertFail()
    {
        Map<String, Object> ret = JSONUtil.parseJSONObject(responseBody);
        Assert.assertTrue(FormatUtil.parseInteger(ret.get("errcode")).intValue() > 0);
        return this;
    }

    public ApiMock assertOK()
    {
        result();
        return this;
    }

    private Object result()
    {
        Map<String, Object> ret = JSONUtil.parseJSONObject(responseBody);
        Assert.assertTrue(FormatUtil.parseInteger(ret.get("errcode")).intValue() == 0);
        Object result = ret.get("result");
        return result;
    }

    public String resultAsString()
    {
        return FormatUtil.parseString(result());
    }

    public boolean resultAsBoolean()
    {
        return FormatUtil.parseBooleanStrictly(result());
    }

    public Integer resultAsInt()
    {
        return FormatUtil.parseInteger(result());
    }

    public Long resultAsLong()
    {
        return FormatUtil.parseLong(result());
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> resultAsMap()
    {
        return (Map<String, Object>) result();
    }

    public <T> T resultAsBean(Class<T> clazz)
    {
        Map<?, ?> result = (Map<?, ?>) result();
        try
        {
            return Bean.fromMap(result, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> resultAsList(Class<T> clazz)
    {
        if (clazz.equals(String.class) || ClassUtils.isPrimitiveOrWrapper(clazz))
        {
            return (List<T>) result();
        }
        return Bean.fromJson(JSONUtil.toJSONString(result()), clazz);
    }

    public <T> Page<T> resultAsPage(Class<T> clazz)
    {
        Map<?, ?> result = (Map<?, ?>) result();
        int total = FormatUtil.parseInteger(result.get("totalElements"));
        Map<String, Object> pageable = JSONUtil.parseJSONObject(FormatUtil.parseString(result.get("pageable")));
        int pageNumber = FormatUtil.parseInteger(pageable.get("pageNumber"));
        int pageSize = FormatUtil.parseInteger(pageable.get("pageSize"));
        List<?> items = (List<?>) result.get("content");
        List<T> convertedItems = new ArrayList<>();
        for (Object item : items)
        {
            Map<?, ?> itemAsMap = (Map<?, ?>) item;
            try
            {
                convertedItems.add(Bean.fromMap(itemAsMap, clazz.newInstance()));
            } catch (InstantiationException | IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
        Page<T> page = new PageImpl<>(convertedItems, PageRequest.of(pageNumber, pageSize), total);
        return page;
    }

    protected abstract String uploadTokenAPI();

    public String uploadFile(File file, boolean isPrivate) throws Exception
    {
        AliUploadToken token = post(uploadTokenAPI(), "fileName", file.getName(), "fileSize", file.length(),
                "isPrivate", isPrivate).resultAsBean(AliUploadToken.class);

        OSSClient ossClient = new OSSClient(token.getEndpoint(),
                new DefaultCredentialProvider(token.getAccessKey(), token.getAccessSecret(), token.getStsToken()),
                null);
        ossClient.putObject(token.getBucket(), token.getKey(), file);
        ossClient.shutdown();
        return token.getUrl();
    }

    public String uploadFileFromURL(String url, boolean isPrivate) throws Exception
    {
        File file = new File(System.getProperty("user.dir") + "/tmp/" + System.currentTimeMillis() + "."
                + FileUtils.getFileExt(url));
        FileUtils.ensureFile(file);
        String img;
        try
        {
            SimpleHttpClient client = new SimpleHttpClient();
            boolean downloaded = client.download(url, new FileOutputStream(file), null);
            if (!downloaded)
            {
                throw new IOException("Failed to download: " + url);
            }
            img = uploadFile(file, isPrivate);
        } finally
        {
            FileUtils.deleteQuietly(file);
        }
        return img;
    }

    public String uploadFileFromURL(String url) throws Exception
    {
        return uploadFileFromURL(url, false);
    }

    public boolean download(OutputStream out, String api, Object... params) throws Exception
    {
        Request.Builder reqBuilder = requestBuilder(api, paramsAsMap(params), headers());
        Response response = getClient().newCall(reqBuilder.get().build()).execute();
        InputStream in = null;
        try
        {
            in = response.body().byteStream();
            FileUtil.copy(in, out);
            return response.isSuccessful();
        } finally
        {
            FileUtil.close(in);
            FileUtil.close(out);
        }
    }

    protected void setApiVersion(String version)
    {
        ApiMock.API_VERSION = version;
    }
}
