package com.ws.ssms.system.util;

import java.util.Random;

public class ApiKeyUtil {
	
	/**
	 * @author hxl
	 * @description 生成ApiKey
	 * @date 2019/11/6
	 * @since 1.0
	 */
    public static String makeApiKey() {  
        String token = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";  
        return MD5Util.MD5(token);  
    }  
}
