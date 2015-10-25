package com.boun.app.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class NumberUtils {

    private static final NumberFormat formatterWithTwoPrecision = new DecimalFormat("#0.00");

    public static NumberFormat numberFormatWithTwoPrecision(){
        return formatterWithTwoPrecision;
    }

    public static Double formatWithTwoPrecision(Double number) {
        return ObjectUtils.isNotNull(number) ? new Double(numberFormatWithTwoPrecision().format(number)) : null;
    }

    public static void main(String[] args){
        double d = 2.794515726093741;
        System.out.println(formatWithTwoPrecision(d));
    }
}
