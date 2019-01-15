package com.example.permission.util;

import org.apache.commons.lang3.StringUtils;

/**
 * @author CookiesEason
 * 2019/01/14 16:19
 * 0,0.1,0.1.2
 */
public class LevelUtil {

    public final static String SEPARATOR = ".";

    public static final String ROOT = "0";

    public static String calculateLevel(String parentLevel, int parentId) {
        if (StringUtils.isBlank(parentLevel)) {
            return ROOT;
        } else {
            return StringUtils.join(parentLevel, SEPARATOR, parentId);
        }
    }

}
