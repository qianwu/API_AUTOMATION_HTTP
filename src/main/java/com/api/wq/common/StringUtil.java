package com.api.wq.common;


import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author wuqian
 */
public class StringUtil extends StringUtils {
    public static String convertStyleToJavaStyle(String dbStyleString){
        dbStyleString = dbStyleString.toLowerCase();
        String[] tokens = dbStyleString.split("_");
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];
            if(i == 0){
                sb.append(token);
            }else{
                char c = token.charAt(0);
                if(c >= 'a' || c <= 'z') {
                    c = (char)(c - 32);
                }
                sb.append(c);
                sb.append(token.substring(1));
            }
        }
        return sb.toString();
    }

    public static <T>List<T> convertStringToBeanList(String source, Class<T> clazz){
        List<T> beanList = JSON.parseArray(source,clazz);
        return beanList;
    }

}
