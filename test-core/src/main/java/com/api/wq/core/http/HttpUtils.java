package com.api.wq.core.http;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * @author ybm
 * @version V1.0
 * @Title
 * @Description
 * @date 2020-05-20
 */
public class HttpUtils
{

	private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * GET 请求拼接字符
	 */
	private static final String URL_PARAM_CONNECT_FLAG = "&";

	private static final String EMPTY = "";

	/**
	 * 请求的Client
	 */
	private static CloseableHttpClient _httpClient;

	/**
	 * Cookie存储
	 */
	private static BasicCookieStore cookieStore;

	static
	{
		cookieStore = new BasicCookieStore();
		_httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
	}

	/**
	 * GET方式提交数据
	 *
	 * @param url
	 *            待请求的URL
	 * @param params
	 *            要提交的数据
	 * @param enc
	 *            编码
	 * @return 响应结果
	 * @throws IOException
	 *             IO异常
	 */
	public static String httpGet(String url, Map<String, String> params, String enc) throws IOException
	{

		StringBuilder totalUrl = new StringBuilder(EMPTY);

		if (totalUrl.indexOf("?") == -1)
		{
			// 返回结果是-1代表没有找到该字符串，那么就在url后面拼接一个问号
			totalUrl.append(url).append("?").append(getUrl(params, enc));
		} else
		{
			totalUrl.append(url).append("&").append(getUrl(params, enc));
		}
		try
		{
			return httpGet(totalUrl.toString());
		} catch (IOException e)
		{
			throw e;
		}
	}

	public static String buildGetUrl(String host,String path,Map<String,String> params ,String enc)
	{
		StringBuilder totalUrl = new StringBuilder();
		totalUrl.append(host);
		totalUrl.append(path);
		if (totalUrl.indexOf("?") == -1)
		{
			// 返回结果是-1代表没有找到该字符串，那么就在url后面拼接一个问号
			totalUrl.append("?").append(getUrl(params, enc));
		} else
		{
			totalUrl.append("&").append(getUrl(params, enc));
		}

		return totalUrl.toString();
	}

	/**
	 * 模拟HttpGet 请求
	 * 
	 * @param url
	 * @return
	 */
	public static String httpGet(String url) throws IOException
	{

		// 单位毫秒
		RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(3000).setConnectTimeout(3000)
				.setSocketTimeout(3000).build();

		CloseableHttpClient httpclient = createHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setConfig(requestConfig);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
		httpGet.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		httpGet.setHeader("Cookie","login=flase; ASP.NET_SessionId=5dmgotxjwvsma4fpcnj3pmbi; Hm_lvt_9007fab6814e892d3020a64454da5a55=1605605400; Hm_lpvt_9007fab6814e892d3020a64454da5a55=1605749462; wxopenid=defoaltid");
		CloseableHttpResponse response = null;
		try
		{
			response = httpclient.execute(httpGet);// 返回请求执行结果
			int statusCode = response.getStatusLine().getStatusCode();// 获取返回的状态值
			if (statusCode != HttpStatus.SC_OK)
			{
				throw new RuntimeException("Http req failed : " + statusCode);
			} else
			{
				String result = EntityUtils.toString(response.getEntity(), "UTF-8");
				return result;
			}
		} catch (Exception e)
		{
			throw e;
		} finally
		{
			if (response != null)
			{
				try
				{
					response.close();// 关闭response
				} catch (IOException e)
				{
					throw e;
				}
			}
			if (httpclient != null)
			{
				try
				{
					httpclient.close();// 关闭httpclient
				} catch (IOException e)
				{
					throw e;
				}
			}
		}
	}

	/**
	 * 下载文件
	 *
	 * @param url
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static void downLoadFile(String url, String file) throws IOException
	{

		CloseableHttpClient httpclient = createHttpClient();
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
		CloseableHttpResponse response = httpclient.execute(httpGet);
		int statusCode = response.getStatusLine().getStatusCode();// 获取返回的状态值
		if (statusCode != HttpStatus.SC_OK)
		{
			throw new RuntimeException("Http req failed : " + statusCode);
		}

		byte[] result = EntityUtils.toByteArray(response.getEntity());

		FileUtils.writeByteArrayToFile(new File(file),result);
	}

	/**
	 * POST方式提交数据
	 *
	 * @param url
	 *            待请求的URL
	 * @param params
	 *            要提交的数据
	 * @return 响应结果
	 * @throws IOException
	 *             IO异常
	 */
	public static HttpResponse httpPost(String url, Map<String, String> params) throws Exception
	{

		// _httpClient
		HttpClient httpClient = createHttpClient();
		// get method
		HttpPost httpPost = new HttpPost(url);
		// set header
		httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

		// set params
		List<NameValuePair> httpParams = new ArrayList<NameValuePair>();
		for (Map.Entry<String, String> entry : params.entrySet())
		{
			httpParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		try
		{
			httpPost.setEntity(new UrlEncodedFormEntity(httpParams));
		} catch (Exception e)
		{
			throw e;
		}

		// response
		HttpResponse response = null;
		try
		{
			response = httpClient.execute(httpPost);
		} catch (Exception e)
		{
			throw e;
		}
		return response;
	}

	/**
	 * POST方式提交数据
	 *
	 * @param url
	 *            待请求的URL
	 * @param params
	 *            要提交的数据
	 * @return 响应结果
	 * @throws IOException
	 *             IO异常
	 */
	public static String httpPostStringResp(String url, Map<String, String> params) throws Exception
	{
		// response
		HttpResponse response = httpPost(url, params);
		String temp = "";
		try
		{
			HttpEntity entity = response.getEntity();
			temp = EntityUtils.toString(entity, "UTF-8");
		} catch (Exception e)
		{
			throw e;
		}

		return temp;
	}

	/**
	 * post请求，参数为json字符串
	 * 
	 * @param url
	 *            请求地址
	 * @param jsonString
	 *            json字符串
	 * @return 响应
	 */
	public static String postJson(String url, String jsonString)
			throws UnsupportedEncodingException, ClientProtocolException, IOException
	{
		String result = null;
		CloseableHttpClient httpClient = createHttpClient();
		HttpPost post = new HttpPost(url);
		CloseableHttpResponse response = null;
		try
		{
			// 构造消息头
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Connection", "Close");
			post.setEntity(new ByteArrayEntity(jsonString.getBytes("UTF-8")));
			response = httpClient.execute(post);
			if (response != null && response.getStatusLine().getStatusCode() == 200)
			{
				HttpEntity entity = response.getEntity();
				result = entityToString(entity);
			}
			return result;
		} catch (UnsupportedEncodingException | ClientProtocolException e)
		{
			throw e;
		} finally
		{
			try
			{
				httpClient.close();
				if (response != null)
				{
					response.close();
				}
			} catch (IOException e)
			{
				throw e;
			}
		}
	}

	private static String entityToString(HttpEntity entity) throws IOException
	{
		String result = null;
		if (entity != null)
		{
			long lenth = entity.getContentLength();
			if (lenth != -1 && lenth < 2048)
			{
				result = EntityUtils.toString(entity, "UTF-8");
			} else
			{
				InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
				CharArrayBuffer buffer = new CharArrayBuffer(2048);
				char[] tmp = new char[1024];
				int l;
				while ((l = reader1.read(tmp)) != -1)
				{
					buffer.append(tmp, 0, l);
				}
				result = buffer.toString();
			}
		}
		return result;
	}

	/**
	 * 据Map生成URL字符串
	 *
	 * @param map
	 *            Map
	 * @param valueEnc
	 *            URL编码
	 * @return URL
	 */
	private static String getUrl(Map<String, String> map, String valueEnc)
	{

		if (null == map || map.keySet().size() == 0)
		{
			return (EMPTY);
		}
		StringBuffer url = new StringBuffer();
		Set<String> keys = map.keySet();
		for (Iterator<String> it = keys.iterator(); it.hasNext();)
		{
			String key = it.next();
			if (map.containsKey(key))
			{
				String val = map.get(key);
				String str = val != null ? val : EMPTY;
				try
				{
					str = URLEncoder.encode(str, valueEnc);
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
				url.append(key).append("=").append(str).append(URL_PARAM_CONNECT_FLAG);
			}
		}
		String strURL = EMPTY;
		strURL = url.toString();
		// 这是为了保证 “&”符号后面一定有参数，如果“&”后面没有东西就把“&”去掉
		if (URL_PARAM_CONNECT_FLAG.equals(EMPTY + strURL.charAt(strURL.length() - 1)))
		{
			strURL = strURL.substring(0, strURL.length() - 1);
		}

		return (strURL);
	}

	private static CloseableHttpClient createHttpClient()
	{
		//if (_httpClient != null)
		//{
		//	return _httpClient;
		//}
		//_httpClient = HttpClients.createDefault();
		//return _httpClient;
		return HttpClients.createDefault();
	}
}
