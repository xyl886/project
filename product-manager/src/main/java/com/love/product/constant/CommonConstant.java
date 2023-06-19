package com.love.product.constant;

import com.love.product.config.fileupload.AliYunOSSConfig;

public interface CommonConstant {

    int ONE = 1;

    int ZERO = 0;

    int FALSE = 0;

    int TRUE = 1;

    int BLOGGER_ID = 1;

    int DEFAULT_CONFIG_ID = 1;

    int DEFAULT_ABOUT_ID = 1;

    String PRE_TAG = "<mark>";

    String POST_TAG = "</mark>";

    String CURRENT = "current";

    String SIZE = "size";
    Long EXPIRE_TIME = 60L;
    String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z])[0-9a-zA-Z]{6,20}$";
    String EMAIL_REGEX="^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
    String ALIYUNOSS_PREFIX="https://" + AliYunOSSConfig.BUCKET_NAME + "." + AliYunOSSConfig.END_POINT + "/";
    String DEFAULT_SIZE = "10";

    String DEFAULT_NICKNAME = "用户";

    String COMPONENT = "Layout";

    String UNKNOWN = "未知";

    String APPLICATION_JSON = "application/json;charset=utf-8";

    String CAPTCHA = "验证码";

    String CHECK_REMIND = "审核提醒";

    String COMMENT_REMIND = "评论提醒";

    String MENTION_REMIND = "@提醒";

}
