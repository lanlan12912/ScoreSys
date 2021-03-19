package com.yelanlan.scoremanagersystem.Utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 常用方法
 */
public class ParamUtils {
    /**
     * 所有的对象都不空Null
     * etc：
     * arg:    空       ----空数组：false
     * arg:  null   --->(Object[])null   : false
     * arg:(Object)null--->Object[null]:false;
     * arg:(Object[])null----->(Object[])null :false;
     * ……
     *
     * @param arg 参数
     * @return
     */
    public static boolean allNotNull(Object... arg) {
        if (arg == null || arg.length == 0) {
            return false;
        }
        for (int i = 0; i < arg.length; i++) {
            Object o = arg[i];
            if (o == null) {
                return false;
            }
            if (o instanceof Object[]) {
                if (((Object[]) o).length == 0) {
                    return false;
                }
            }
            if (o instanceof String) {
                String o1 = (String) o;
                if (StringUtils.isBlank(o1)) {
                    return false;
                }
            }
        }
        return true;
    }
}
