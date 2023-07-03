package com.love.product.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UploadMode {

    OSS("oss", "ossUploadStrategyImpl"),

    MINIO("minio", "minioUploadStrategyImpl");

    private final String mode;

    private final String strategy;

    public static String getStrategy(String mode) {
        for (UploadMode value : UploadMode.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }

}
