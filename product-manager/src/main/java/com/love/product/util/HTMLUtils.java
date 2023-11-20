package com.love.product.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HTML工具类
 *
 * @author blue
 * @date 2021/07/27
 */
public class HTMLUtils {
    private static final int THREAD_POOL_SIZE = 10;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    public static Boolean review(String source){
        // 文本审核
        executorService.execute(() -> {
            boolean isPassed = textReview(source);
            if (isPassed) {
                System.out.println("文本审核通过");
            } else {
                System.out.println("文本审核不通过");
            }
        });

        // 图片审核
        String imageUrl = "https://example.com/image.jpg";
        executorService.execute(() -> {
            boolean isPassed = imageReview(imageUrl);
            if (isPassed) {
                System.out.println("图片审核通过");
            } else {
                System.out.println("图片审核不通过");
            }
        });

        // 关闭线程池
        executorService.shutdown();
        return null;
    }

    // 文本审核
    private static boolean textReview(String text) {
        // 自定义DFA算法审核文本
        boolean isSensitive = false;
        // ...
        // 如果检测到敏感词，则将isSensitive设为true
        if (isSensitive) {
            return false; // 敏感词过多，审核不通过
        } else {
            return true; // 敏感词过滤完，审核通过
        }
    }

    // 图片审核
    private static boolean imageReview(String imageUrl) {
        // OCR识别图片中的文本
        // ...
        // 调用阿里云内容安全接口审核图片文本
        boolean isSensitive = false;
        // ...
        // 如果检测到敏感词，则将isSensitive设为true
        if (isSensitive) {
            return false; // 敏感词过多，审核不通过
        } else {
            return true; // 敏感词过滤完，审核通过
        }
    }



    /**
     * 删除标签
     *
     * @param source 需要进行剔除HTML的文本
     * @return 过滤后的内容
     */
    public static String deleteTag(String source) {
        // 敏感词过滤
        source = SensitiveUtils.filter(source);
        // 保留图片标签
        if (source != null) {
            source = source.replaceAll("(?!<(img).*?>)<.*?>", "")
                    .replaceAll("(on[^\"]+)", "");
        }
        return deleteHTMLTag(source);
    }

    /**
     * 删除标签
     *
     * @param source 文本
     * @return 过滤后的文本
     */
    private static String deleteHTMLTag(String source) {
        // 删除转义字符
        source = source.replaceAll("&.{2,6}?;", "");
        // 删除script标签
        source = source.replaceAll("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", "");
        // 删除style标签
        source = source.replaceAll("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", "");
        return source;
    }

}
