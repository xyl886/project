package com.love.product.enumerate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SearchMode {

    MYSQL("mysql", "mySqlSearchStrategyImpl"),

    ELASTICSEARCH("elasticsearch", "esSearchStrategyImpl");

    private final String mode;

    private final String strategy;

    public static String getStrategy(String mode) {
        for (SearchMode value : SearchMode.values()) {
            if (value.getMode().equals(mode)) {
                return value.getStrategy();
            }
        }
        return null;
    }

}
