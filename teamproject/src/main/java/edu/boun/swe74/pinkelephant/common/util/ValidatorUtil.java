package edu.boun.swe74.pinkelephant.common.util;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class ValidatorUtil {

    private static final String EMPTY_STRING = "";

    public static boolean isNull(String str) {
        return !(str != null && !str.trim().equalsIgnoreCase(EMPTY_STRING));
    }

    public static boolean isNull(Collection<?> col) {
        return !(col != null && col.size() > 0);
    }

    public static boolean isNull(List<?> col) {
        return !(col != null && col.size() > 0);
    }

    public static boolean isNull(HashMap<?, ?> col) {
        return !(col != null && col.size() > 0);
    }

    public static boolean isNull(Hashtable<?, ?> col) {
        return !(col != null && col.size() > 0);
    }

    public static boolean isNull(Long value) {
        return (value == null);
    }

    public static boolean isNull(Byte value) {
        return (value == null);
    }

    public static boolean isNull(Date date) {
        return (date == null);
    }
}
