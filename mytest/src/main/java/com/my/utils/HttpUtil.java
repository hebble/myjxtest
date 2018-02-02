package com.my.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import org.apache.commons.lang3.StringUtils;

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
		HttpsURLConnection httpUrlConn = null;
		StringBuffer buffer = new StringBuffer();
		try {
			TrustManager[] tm = { new MyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new SecureRandom());
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);

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
}
