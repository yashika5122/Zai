package com.zai.weather.util;

import java.util.Base64;

public class Base64Util {

    public static String decode(String encodedString) {
        return new String(Base64.getDecoder().decode(encodedString));
    }
}