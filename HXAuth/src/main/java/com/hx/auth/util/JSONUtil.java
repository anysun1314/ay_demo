package com.hx.auth.util;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by Renyulin on 14-4-16 上午9:31.
 */
public class JSONUtil {

    public static Logger logger = Logger.getLogger(JSONUtil.class);

    public static List getListFromPageJSON(String jsonStr) {
        if (jsonStr == null || "".equals(jsonStr)) {
            logger.debug("page 式的 json串为空");
            return null;
        }
        int begin = jsonStr.indexOf("rows\":[");
        if (begin == -1) {
            logger.debug("json串不是约定的page式");
            return null;
        }
        String str = jsonStr.substring(begin + "\"rows\":".length() - 1, jsonStr.length() - 1);
        try {
            return JSONUtil.getListFormJSON(str);
        } catch (Exception e) {
            logger.debug("json串不是约定的page式");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /**
     * 将[{}]式的JSON串转换为List
     *
     * @param jsonStr
     * @return
     */
    public static List getListFormJSON(String jsonStr) {
        return JSON.parseArray(jsonStr);
    }

    public static Object getJSONFromMap(Map map) {
        return JSON.toJSON(map);
    }
}
