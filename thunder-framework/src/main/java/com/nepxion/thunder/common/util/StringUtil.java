package com.nepxion.thunder.common.util;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

public class StringUtil {
    public static String firstLetterToUpper(String value) {
        Character character = Character.toUpperCase(value.charAt(0));
        
        return character.toString().concat(value.substring(1));
    }

    public static String firstLetterToLower(String value) {
        Character character = Character.toLowerCase(value.charAt(0));
        
        return character.toString().concat(value.substring(1));
    }

    /*public static String firstLetterToUpper(String value){
        char[] array = value.toCharArray();
        array[0] -= 32;

        return String.valueOf(array);
    }

    public static String firstLetterToLower(String value){
        char[] array = value.toCharArray();
        array[0] += 32;

        return String.valueOf(array);
    }*/
}