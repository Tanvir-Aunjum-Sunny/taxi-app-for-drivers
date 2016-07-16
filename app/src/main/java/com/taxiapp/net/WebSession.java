package com.taxiapp.net;

import com.google.gson.Gson;
import com.taxiapp.utils.AppLogger;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Util;

import org.apache.commons.lang.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPOutputStream;

import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 *
 */

public class WebSession {

    private static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");
    public static int NET_TIMEOUT = 30;
    public static Gson gson = new Gson();

    private int connectionTimeout;
    private int readTimeout;
    private OkHttpClient client;

    public WebSession() {
        this(NET_TIMEOUT);
    }

    public WebSession(int timeOutInSec) {
        this(timeOutInSec, timeOutInSec);
    }

    public WebSession(int connectionTimeout, int readTimeout) {
        client = new OkHttpClient();
        client.interceptors().add(new LoggingInterceptor());
        this.readTimeout = readTimeout;
        this.connectionTimeout = connectionTimeout;
        client.setConnectTimeout(connectionTimeout, TimeUnit.SECONDS);
        client.setReadTimeout(readTimeout, TimeUnit.SECONDS);
    }

    public void set2gTimeOut() {
        readTimeout = NET_TIMEOUT + NET_TIMEOUT / 2;
        connectionTimeout = NET_TIMEOUT + NET_TIMEOUT / 2;

        client.setConnectTimeout(connectionTimeout, TimeUnit.SECONDS);
        client.setReadTimeout(readTimeout, TimeUnit.SECONDS);
    }

    public void setRegularTimeOut() {
        readTimeout = NET_TIMEOUT;
        connectionTimeout = NET_TIMEOUT;
        client.setConnectTimeout(connectionTimeout, TimeUnit.SECONDS);
        client.setReadTimeout(readTimeout, TimeUnit.SECONDS);
    }

    /**
     * new AppAuthenticator(username, password)
     *
     * @param url
     * @param json
     * @param authenticator
     * @return
     * @throws IOException
     */
    public String postGzip(String url, String json, Authenticator authenticator) throws IOException {
        Builder builder = new Builder();
        builder.header("Content-Encoding", "gzip");

        // Compress data.
        byte[] data = copressData(json);


        RequestBody body = RequestBody.create(JSON, data);

        builder.url(url).post(body);

        Request request = builder.build();

        if (authenticator != null) {
            client.setAuthenticator(authenticator);
        }
        Response response = client.newCall(request).execute();
        verifyResponseCode(response);

        String resString = response.body().string();

        validateResponse(resString);

        return resString;
    }


    public String get(String url, Map<String, String> params, Authenticator authenticator) throws IOException {
        if (authenticator != null) {
            client.setAuthenticator(authenticator);
        }

        String finalUrl = buildURI(url, params);
        Request request = new Builder().url(finalUrl).build();

        Response response = client.newCall(request).execute();
        verifyResponseCode(response);
        String resString = response.body().string();
        validateResponse(resString);
        return resString;
    }

    private String buildURI(String baseUrl, Map<String, String> params)
            throws IOException {
        if (params != null && params.size() > 0) {
            StringBuilder builder = new StringBuilder(baseUrl + "?");

            for (String key : params.keySet()) {
                String parameterName = URLEncoder.encode(key, "UTF-8");
                String parameterValue = StringUtils.isNotBlank(params.get(key)) ? URLEncoder.encode(params.get(key),
                        "UTF-8") : "";
                builder.append(parameterName).append("=")
                        .append(parameterValue).append("&");
            }

            // remove last '&'
            builder.deleteCharAt(builder.length() - 1);

            baseUrl = builder.toString();
        }

        return baseUrl;
    }

    public String post(String url, String data, Authenticator authenticator) throws IOException {
        RequestBody body = RequestBody.create(JSON, data);
        Builder builder = new Builder();
        builder.url(url).post(body);
        Request request = builder.build();
        if (authenticator != null) {
            client.setAuthenticator(authenticator);
        }
        Response response = client.newCall(request).execute();

        String resString = response.body().string();
        validateResponse(resString);

        return resString;
    }

    private void verifyResponseCode(Response response) throws IOException {

        int code = response.code();

        if (code == 404) {
            throw new NetIOException(404, "404 - Not found");
        }

        if (code == 400) {
            throw new NetIOException(400,
                    "400 - The request sent by the client was syntactically incorrect.");
        }

        if (code == 500) {
            AppLogger.get().e(getClass(), "Server Exception : " + response.body().string());
            throw new NetIOException(500,
                    "500 - Unexpected Server error");
        }
    }

    private byte[] copressData(String body) throws IOException {
        byte[] data = body.getBytes("UTF-8");
        ByteArrayOutputStream arr = new ByteArrayOutputStream();
        OutputStream zipper = new GZIPOutputStream(arr);
        zipper.write(data);
        zipper.close();

        return arr.toByteArray();
    }

    private void validateResponse(String resString) throws IOException {
        if (StringUtils.contains(resString,
                "The request sent by the client was syntactically incorrect.")) {
            throw new IOException(
                    "The request sent by the client was syntactically incorrect.");
        }
        //
        AppLogger.get().i(getClass(),
                "Response : " + StringUtils.substring(resString, 0, 500));

    }


    public String get(String url) throws IOException {
        Request request = new Builder().url(url).build();
        Response response = client.newCall(request).execute();
        verifyResponseCode(response);
        String resString = response.body().string();
        validateResponse(resString);
        return resString;
    }

    private class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();

            long contentLength = 0;
            String content = "";
            if (request.body() != null) {
                contentLength = request.body().contentLength();
                content = bodyToString(request);
            }
            AppLogger.get().i(getClass(), "Request: " + request.url() + " Content-Length: " + contentLength);
            AppLogger.get().i(getClass(), "Params: " + content);

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            AppLogger.get().i(getClass(), String.format(
                    "Received response for %s in %.1fms%n\tHTTP Status %d",
                    response.request().url(), (t2 - t1) / 1e6d, response.code()));
            return response;
        }
    }

    public static class BasicAuthenticator implements Authenticator {

        private String username, password;

        public BasicAuthenticator(String u, String p) {
            username = u;
            password = p;
        }

        @Override
        public Request authenticate(Proxy proxy, Response response)
                throws IOException {
            String credential = Credentials.basic(username, password);
            return response.request().newBuilder()
                    .header("Authorization", credential).build();
        }

        @Override
        public Request authenticateProxy(Proxy arg0, Response arg1)
                throws IOException {
            return null;
        }
    }

    public static RequestBody create(final MediaType mediaType,
                                     final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }


    private String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return new String(buffer.readByteArray());
        } catch (final IOException e) {
            return "did not work";
        }
    }
}