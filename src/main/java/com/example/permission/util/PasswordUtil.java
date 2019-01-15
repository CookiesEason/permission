package com.example.permission.util;

import java.util.Random;

/**
 * @author CookiesEason
 * 2019/01/15 17:07
 */
public class PasswordUtil {

    private final static String[] WORD = {
            "a", "b", "c", "d", "e", "f", "g",
            "h", "j", "k", "m", "n",
            "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z",
            "A", "B", "C", "D", "E", "F", "G",
            "H", "J", "K", "M", "N",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"
    };

    private final static String[] NUM = {
            "2", "3", "4", "5", "6", "7", "8", "9"
    };

    public static String generate() {
        StringBuilder password = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        boolean flag = false;
        int length = random.nextInt(3) + 8;
        for (int i = 0; i < length; i++) {
            if (flag) {
                password.append(NUM[random.nextInt(NUM.length)]);
            } else {
                password.append(WORD[random.nextInt(WORD.length)]);
            }
            flag = !flag;
        }
        return password.toString();
    }
}
