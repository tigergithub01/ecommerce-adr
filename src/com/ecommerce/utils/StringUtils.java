package com.ecommerce.utils;

public class StringUtils {
	public static boolean isEmpty(CharSequence cs){
		return cs == null || cs.length() == 0 || cs.equals("null");
	}
	
	public static boolean isNotEmpty(CharSequence cs){
		return !isEmpty(cs);
	}
	
	public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0 || cs.equals("null")) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	
	 public static boolean isNotBlank(CharSequence cs) {
	        return !isBlank(cs);
	    }
	
}
