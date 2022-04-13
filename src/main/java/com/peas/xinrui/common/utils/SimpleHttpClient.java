package com.peas.xinrui.common.utils;

import com.sunnysuperman.commons.util.FileUtil;
import com.sunnysuperman.commons.util.FormatUtil;
import okhttp3.*;

import java.io.*;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class SimpleHttpClient {
    private int connectTimeout = 20;
    private int readTimeout = 30;
    private int responseCode;
    private Proxy proxy;
    private Authenticator proxyAuthenticator;

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public SimpleHttpClient setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public SimpleHttpClient setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public SimpleHttpClient setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public Authenticator getProxyAuthenticator() {
        return proxyAuthenticator;
    }

    public void setProxyAuthenticator(Authenticator proxyAuthenticator) {
        this.proxyAuthenticator = proxyAuthenticator;
    }

    public int getResponseCode() {
        return responseCode;
    }

    private String getUrl(String url, Map<String, Object> params) {
        if (params == null) {
            return url;
        }
        StringBuilder urlBuf = new StringBuilder(url);
        boolean appendQuestionMark = url.indexOf('?') < 0;
        for (Entry<String, Object> entry : params.entrySet()) {
            if (appendQuestionMark) {
                urlBuf.append('?');
                appendQuestionMark = false;
            } else {
                urlBuf.append('&');
            }
            try {
                urlBuf.append(entry.getKey()).append('=')
                        .append(URLEncoder.encode(entry.getValue().toString(), StringUtils.UTF8));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        String getUrl = urlBuf.toString();
        return getUrl;
    }

    private Request getRequestBuilder(String url, Map<String, Object> params, Map<String, Object> headers) {
        Request.Builder builder = new Request.Builder().url(getUrl(url, params));
        if (headers != null) {
            for (Entry<String, Object> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        return builder.build();
    }

    private Response execute(Request request) throws IOException {
        Response response = getClient().newCall(request).execute();
        responseCode = response.code();
        return response;
    }

    public String get(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        Request request = getRequestBuilder(url, params, headers);
        Response response = execute(request);
        return response.body().string();
    }

    public String post(String url, Map<String, Object> params, Map<String, Object> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            for (Entry<String, Object> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        if (params != null) {
            for (Entry<String, Object> entry : params.entrySet()) {
                bodyBuilder.add(entry.getKey(), entry.getValue().toString());
            }
        }
        Request request = builder.post(bodyBuilder.build()).build();
        Response response = getClient().newCall(request).execute();
        responseCode = response.code();
        return response.body().string();
    }

    public String post(String url, String mediaType, String body, Map<String, Object> headers) throws IOException {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers != null) {
            for (Entry<String, Object> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue().toString());
            }
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType), body);
        Request request = builder.post(requestBody).build();
        Response response = getClient().newCall(request).execute();
        responseCode = response.code();
        return response.body().string();
    }

    public boolean download(String url, OutputStream out) throws IOException {
        return download(url, out, null);
    }

    public boolean download(String url, OutputStream out, DownloadOptions options) throws IOException {
        if (options == null) {
            options = new DownloadOptions();
        }
        Request request = getRequestBuilder(url, options.getParams(), options.getHeaders());
        Response response = execute(request);
        InputStream in = null;
        try {
            if (options.getMaxSize() > 0) {
                long length = FormatUtil.parseLongValue(response.header("Content-Length"), -1);
                if (length < 0 || length > options.getMaxSize()) {
                    return false;
                }
            }
            if (options.isRetrieveResponseHeaders()) {
                Map<String, List<String>> headers = response.headers().toMultimap();
                Map<String, String> responseHeaders = new HashMap<>();
                for (Entry<String, List<String>> entry : headers.entrySet()) {
                    responseHeaders.put(entry.getKey(), entry.getValue().get(0));
                }
                options.setResponseHeaders(responseHeaders);
            }
            in = response.body().byteStream();
            FileUtil.copy(in, out);
            return response.isSuccessful();
        } finally {
            FileUtil.close(in);
            FileUtil.close(out);
        }
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS);
        if (proxy != null) {
            builder.proxy(proxy);
        }
        if (proxyAuthenticator != null) {
            builder.proxyAuthenticator(proxyAuthenticator);
        }
        return builder.build();
    }

    public static String getPageSource(File file) {
        StringBuffer sb = new StringBuffer();
        try {
            // 使用openStream得到一输入流并由此构造一个BufferedReader对象
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            // 读取www资源
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            bufferedReader.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        return sb.toString();
    }

    public static class DownloadOptions {
        private Map<String, Object> params;
        private Map<String, Object> headers;
        private long maxSize = -1;
        private boolean retrieveResponseHeaders;
        private Map<String, String> responseHeaders;

        public Map<String, Object> getParams() {
            return params;
        }

        public DownloadOptions setParams(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public Map<String, Object> getHeaders() {
            return headers;
        }

        public DownloadOptions setHeaders(Map<String, Object> headers) {
            this.headers = headers;
            return this;
        }

        public long getMaxSize() {
            return maxSize;
        }

        public DownloadOptions setMaxSize(long maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public boolean isRetrieveResponseHeaders() {
            return retrieveResponseHeaders;
        }

        public DownloadOptions setRetrieveResponseHeaders(boolean retrieveResponseHeaders) {
            this.retrieveResponseHeaders = retrieveResponseHeaders;
            return this;
        }

        public Map<String, String> getResponseHeaders() {
            return responseHeaders;
        }

        public DownloadOptions setResponseHeaders(Map<String, String> responseHeaders) {
            this.responseHeaders = responseHeaders;
            return this;
        }

    }

}
