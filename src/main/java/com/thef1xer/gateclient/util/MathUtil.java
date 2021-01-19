package com.thef1xer.gateclient.util;

import org.apache.logging.log4j.core.util.Integers;

public class MathUtil {
    public static boolean isInteger(String s) {
        try {
            Integers.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
