package com.xxy.utils;

public class BeanUtils {
    private Object object;

    public static Object stringToPrimitive(String value, Class<?> klass) {
        if(klass.equals(int.class)) {
            return Integer.valueOf(value);
        } else if(klass.equals(boolean.class)) {
            return Boolean.valueOf(value);
        } else if(klass.equals(byte.class)) {
            return Byte.valueOf(value);
        } else if(klass.equals(short.class)) {
            return Short.valueOf(value);
        } else if(klass.equals(long.class)) {
            return Long.valueOf(value);
        } else if(klass.equals(double.class)) {
            return Double.valueOf(value);
        } else if(klass.equals(float.class)) {
            return Float.valueOf(value);
        } else if(klass.equals(char.class)) {
            return String.valueOf(value).charAt(0);
        } else {
            return value;
        }
    }
}
