package com.my.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.my.enums.RequestTypeEnum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpUtil {
    private static final int TIMEOUT = 6000;
    private static final int BUFFER = 5120;

    public static String invokeHttp(String httpUrl) {
        return invokeHttp(httpUrl, 6000);
    }

    public static String invokeHttp(String httpUrl, int timeout) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(httpUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            conn.setDoOutput(true);
            OutputStreamWriter os = new OutputStreamWriter(conn.getOutputStream(), "ISO8859-1");
            os.flush();
            conn.connect();
            if (conn.getResponseCode() == 200) {
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String buffer = null;
                while ((buffer = br.readLine()) != null) {
                    if (StringUtils.isNotEmpty(buffer)) {
                        result.append(buffer);
                    }
                }
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            }
            if (os != null)
                os.close();
        } catch (ConnectException e) {
            System.out.println(httpUrl + " not connectable");
        } catch (IOException e) {
            System.out.println(httpUrl + " not connectable");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
                conn = null;
            }
        }
        return result.toString();
    }

    public static String invokeHttps(String requestUrl) {
        return invokeHttps(requestUrl, null);
    }

    public static String invokeHttps(String requestUrl, String body) {
        return invokeHttps(requestUrl, "GET", body);
    }

    public static String invokeHttps(String requestUrl, String requestMethod, String body) {
        return invokeHttps(requestUrl, requestMethod, null, body);
    }

    public static String invokeHttps(String requestUrl, String requestMethod, Map<String, String> headers, String body) {
        HttpsURLConnection httpUrlConn = null;
        StringBuffer buffer = new StringBuffer();
        try {
            TrustManager[] tm = {new MyTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            //添加请求头
            if (headers != null) {
                Iterator<String> iterator = headers.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    String val = headers.get(key);
                    httpUrlConn.setRequestProperty(key, val);
                }
            }

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setRequestMethod(requestMethod);
            httpUrlConn.connect();

            if (StringUtils.isNotEmpty(body)) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                outputStream.write(body.getBytes("UTF-8"));
                outputStream.close();
            }

            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();

            inputStream.close();
            inputStream = null;
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpUrlConn != null) {
                httpUrlConn.disconnect();
            }
        }
        return null;
    }

    public static final byte[] downloadFileBytes(String fromPath) {
        byte[] data = null;
        try {
            URL url = new URL(fromPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            InputStream inputStream = conn.getInputStream();
            data = readInputStream(inputStream);
            inputStream.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[5120];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return
     */
    public static String httpPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 发送 get 请求
     *
     * @throws Exception
     */
    public static String get(String url) {

        String result = "";
        InputStream in = null;
        try {
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("GET");
            // 建立实际的连接
            conn.connect();
            // 定义输入流来读取URL的响应
            in = conn.getInputStream();
            result = StreamUtils.copyToString(in, Charset.forName("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String toParamString(Map<String, Object> paramMap) throws UnsupportedEncodingException {
        return toParamString(null, paramMap);
    }

    /**
     * Map对象转为参数字符串
     *
     * @param prefix   前缀，通常是"?"
     * @param paramMap 要转换为字符串参数的Map对象
     * @return 转换后的字符串，格式：param1=value1&param2=value2，若要在前面加?请传入prefix参数
     * @throws UnsupportedEncodingException
     */
    public static String toParamString(String prefix, Map<String, Object> paramMap) throws UnsupportedEncodingException {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(prefix.trim())) {
            sb.append(prefix);
        }
        Set<Map.Entry<String, Object>> entrySet = paramMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (StringUtils.isNotBlank(key) && value != null) {
                sb.append(key).append("=")
                        .append(URLEncoder.encode(value.toString(), "UTF-8"))
                        .append("&");
            }
        }
        return (sb.deleteCharAt(sb.length() - 1)).toString();
    }

    /**
     * @param paramMap    参数
     * @param url         地址
     * @param requestType 请求类型，GET或POST
     * @return
     * @throws
     * @Title: 发送第三方请求
     * @Description:
     * @version v1.0 date: 2018/1/16 15:54 author:lixiaohua desc: new
     */
    public static String invoke(Map<String, Object> paramMap, String url, RequestTypeEnum requestType) {
        log.info("----start----执行调用第三方api,参数：{}，url地址：{}，请求类型：{}----start----", JSON.toJSONString(paramMap), url, requestType);
        if (StringUtils.isBlank(url)) {
            throw new IllegalArgumentException("url cannot null");
        }

        switch (requestType) {
            case GET:
                String params;
                try {
                    params = HttpUtil.toParamString("?", paramMap);
                } catch (UnsupportedEncodingException e) {
                    throw new IllegalArgumentException(e.getMessage());
                }
                return HttpUtil.get(url + params);
            case POST:
                return HttpUtil.httpPost(url, JSON.toJSONString(paramMap));
            default:
                throw new IllegalArgumentException("不支持的请求类型，请求类型只能为GET或POST");
        }
    }

}
