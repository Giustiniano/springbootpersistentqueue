package com.springboot.exercise.utils;

import java.util.Collection;

public class ValidationUtils {

    public static boolean isNullOrEmptyOrBlank(String string){
        if(string == null || string.trim().isEmpty()){
            return true;
        } else return false;
    }

    public static void throwIllegalArgExIfNullEmptyOrBlank(String string, String fieldName){
        if(fieldName == null){
            fieldName = "field";
        }
        if(isNullOrEmptyOrBlank(string)){
            throw new IllegalArgumentException(fieldName + " cannot be null, empty or blank");
        }
    }

    public static boolean isNull(Object obj){
        return obj == null;
    }

    public static void throwIllegalArgExIfNull(Object obj, String fieldName){
        if(isNull(obj)){
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public static boolean isNullOrEmpty(Collection c){
        if(isNull(c)){
            return true;
        }
        else {
            return c.isEmpty();
        }
    }

    public static boolean isNegative(Integer i){
        return i < 0;
    }

    public static void throwIllegalArgExIfNegative(Integer i, String fieldName){
        if(isNegative(i)){
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    public static void throwIllegalArgExIfNullOrEmpty(Collection c, String fieldName){
        if(isNullOrEmpty(c)){
            throw new IllegalArgumentException(fieldName + " cannot be null or empty");
        }
    }
}
