package com.springmvc.common.constants;

/**
 * Created by zhangyuguang on 2018/10/17.
 */
public class GlobalConstants {
     /*
      * 获取key加载信息
     */
    public static boolean ptrintKeyLoadMessage(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\r\n      ---------------------------------------------------------------------------     \r\n");
        stringBuffer.append("\r\n        版权归北京 于中食（周口）冷藏物流有限公司  @WEB listener 不能忘                           \r\n");
        stringBuffer.append("\r\n      ----------------------------------------------------------------------------    \r\n");
        System.out.println(stringBuffer.toString());

        return true;
    }
}
