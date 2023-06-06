package com.love.product;

import com.love.product.entity.base.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PackageName: com.love.product
 * @Description: Main
 * @Author: Administrator
 * @Date: 2023/6/4 19:08
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
            List<String> imgPathList = new ArrayList<>();
            Collections.addAll(imgPathList,"https://18066.oss-cn-hangzhou.aliyuncs.com/218f0c28e70c4c08be85133b9be47aa5.jpg,https://18066.oss-cn-hangzhou.aliyuncs.com/b666fce2ee354ee6b4e8612a863aeba9.jpg,https://18066.oss-cn-hangzhou.aliyuncs.com/be82a9fdf80f478e9f3eb8ca3db45986.jpg,be82a9fdf80f478e9f3eb8ca3db45986.jpg,be82a9fdf80f478e9f3eb8ca3db45986.jpg,be82a9fdf80f478e9f3eb8ca3db45986.jpg,be82a9fdf80f478e9f3eb8ca3db45986.jpg,be82a9fdf80f478e9f3eb8ca3db45986.jpg".split(","));
            log.info("修改前imgpath:"+imgPathList);


            String removeImgPath="https://18066.oss-cn-hangzhou.aliyuncs.com/b666fce2ee354ee6b4e8612a863aeba9.jpg";
            if(!removeImgPath.isEmpty()){
                List<String> removeUrls = new ArrayList<>(Arrays.asList(removeImgPath.split(",")));
                imgPathList.removeAll(removeUrls);
                log.info("移除了"+ removeUrls);
                log.info("移除了"+imgPathList);
            }

                imgPathList.add("imgPath.jpg");


            log.info("修改后最终的imgpath:"+ imgPathList);


            log.info(imgPathList.get(0));
            log.info(imgPathList.stream().map(String::valueOf).collect(Collectors.joining(",")));


    }
}
