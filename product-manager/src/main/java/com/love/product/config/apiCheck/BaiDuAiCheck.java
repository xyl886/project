package com.love.product.config.apiCheck;

import com.alibaba.fastjson2.JSONObject;
import com.baidu.aip.contentcensor.AipContentCensor;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

@Data
@Configuration
@ConfigurationProperties("baidu.api") //指定配置文件
public class BaiDuAiCheck implements InitializingBean {

    private  String appId;

    private  String apiKey;

    private  String secretKey;

    //定义公共静态常量
    public static String APP_ID;

    public static String API_KEY;

    public static String SECRET_KEY;

    @Override
    public void afterPropertiesSet() {
        APP_ID = appId;
        API_KEY = apiKey;
        SECRET_KEY = secretKey;
    }


    public static JSONObject moderateContent(String content) {
        // 创建AipContentCensor对象
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        // 调用内容审核API
        return JSONObject.from(client.textCensorUserDefined(content));

    }

    /**
     * 识别文本中的敏感词汇(需要对百度API增强)
     *
     * @param text
     */
    public static String discernSensitiveWords(String text, HashMap<String, String> options) throws UnsupportedEncodingException {
        String resp;
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        resp = client.antiSpam(URLDecoder.decode(text, "UTF-8"), options).toString();
        //标识审核是否通过的结果所在未知
        //System.out.println(spam);
        return resp;
    }
    /**
     * 识别图中的敏感内容(需要对百度API增强)
     *
     * @param image
     */
    public static boolean discernSensitiveImage(byte[] image) throws IOException {
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        // 参数为本地图片文件二进制数组
        String resp = client.imageCensorUserDefined(image, null).toString();
        //截取conclusionType的值
        String result = resp.substring(resp.length() - 2,resp.length()-1);
        return result.equals("1");
    }
}
