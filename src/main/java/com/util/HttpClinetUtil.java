package com.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.Map;

public class HttpClinetUtil {
    // 默认字符集
    private static final String ENCODING = "UTF-8";

    /**
     * @Title sendPost
     * @author wangjy
     */
    public static String sendPost(String url, Map<String, String> headers, JSONObject data) {
        // 请求返回结果
        String resultJson = null;
        // 创建Client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost();

        try {
            // 设置请求地址
            httpPost.setURI(new URI(url));
            // 设置请求头
            if (headers != null) {
                Header[] allHeader = new BasicHeader[headers.size()];
                int i = 0;
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    allHeader[i] = new BasicHeader(entry.getKey(), entry.getValue());
                    i++;
                }
                httpPost.setHeaders(allHeader);
            }
            // 设置实体
            httpPost.setEntity(new StringEntity(JSON.toJSONString(data)));
            // 发送请求,返回响应对象
            CloseableHttpResponse response = client.execute(httpPost);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // 获取响应结果
                resultJson = EntityUtils.toString(response.getEntity(), ENCODING);
                JSONObject jsonObject = JSONObject.parseObject(resultJson);
                System.out.println(resultJson);
                System.out.println(jsonObject.get("responseBody"));
                System.out.println(jsonObject.getJSONObject("response"));
            } else {
                System.out.println("响应失败，状态码：" + status);
            }

        } catch (Exception e) {
            System.out.println("发送post请求失败" + e.getMessage());
        } finally {
            httpPost.releaseConnection();
        }
        return "";
    }
}
