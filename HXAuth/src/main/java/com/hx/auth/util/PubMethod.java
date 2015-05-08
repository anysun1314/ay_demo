package com.hx.auth.util;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * 公用类
 *
 * @author peixiuyue
 */
public class PubMethod {

    /**
     * po对象的拷贝
     * （注：相同名字的属性其类型须一致；且属性的类型最好不是原始类型，类似int.long，最好使用Integer.Long ...）
     *
     * @param srcObj
     * @param desObj
     * @param copyPropertyArr 指定需要拷贝的属性的名称集合，为空表示不限制
     * @return
     * @author wangjianhua
     */
    public static void copyPersistantObject(Object srcObj, Object desObj, String[] copyPropertyArr) {
        if (isEmpty(srcObj) || isEmpty(desObj)) {
            System.err
                    .println("NullPointerException at PubMethod.copyPersistantObject\n...........");
            // throw new NullPointerException();
        }
        Method[] method = srcObj.getClass().getDeclaredMethods();
        for (int index = 0; index < method.length; index++) {
            // PubMethod.toPrint("method[index].getName():"+method[index].getName());
            String methodName = method[index].getName();
            methodName = (methodName == null) ? "" : methodName.trim();
            if (methodName.startsWith("get")
                    && hasMethodByName(desObj, methodName)) {
                String fieldName = methodName.substring(3);// cut 'get'
                String fieldRealName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
                if (copyPropertyArr == null || copyPropertyArr.length == 0 || !isInArray(fieldRealName, copyPropertyArr)) {
                    continue;
                }
                Method desMethod = getMethodByName(desObj, "set" + fieldName);
                Object val = null;
                try {
                    val = method[index].invoke(srcObj, null);
                    if (val == null)// || "".equals(val.toString().trim()))
                    {
                        continue;
                    }
                    desMethod.invoke(desObj, new Object[]{val});
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 判断两对象的值是否不相等
     *
     * @param oldOne
     * @param newOne
     * @return true 不相等；false 相等
     */
    public static boolean isNotEquals(Object oldOne, Object newOne) {
        if (oldOne == null && newOne == null) {
            return false;
        } else if (oldOne == null && newOne != null) {
            return true;
        } else if (oldOne != null && newOne == null) {
            return true;
        }

        if (oldOne instanceof String) {
            if (((String) oldOne).equals((String) newOne)) {
                return false;
            }
        } else if (oldOne instanceof Long) {
            if (((Long) oldOne).longValue() == ((Long) newOne).longValue()) {
                return false;
            }
        } else if (oldOne instanceof Double) {
            if (((Double) oldOne).doubleValue() == ((Double) newOne).doubleValue()) {
                return false;
            }
        } else if (oldOne instanceof Integer) {
            if (((Integer) oldOne).intValue() == ((Integer) newOne).intValue()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String upperCaseFirstLetter(String str) {
        if (PubMethod.isEmpty(str)) {
            return "";
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getValueNoNull(Object obj) {
        if (obj == null) {
            return "";
        } else {
            return obj.toString();
        }
    }

    /**
     * 获得异常的详细信息
     *
     * @param e
     * @return
     */
    public static String getStackTraceString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);

        return sw.getBuffer().toString();
    }

    /**
     * 检查类中是否有指定的方法名
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static boolean hasMethodByName(Object obj, String methodName) {
        boolean hasMethod = false;
        methodName = (methodName == null) ? "" : methodName.trim();
        if (isEmpty(obj) || isEmpty(methodName)) {
            return hasMethod;
        }

        Method[] method = obj.getClass().getDeclaredMethods();
        for (int index = 0; index < method.length; index++) {
            // PubMethod.toPrint("method[index].getName():"+method[index].getName());
            String tmpMethodName = method[index].getName();
            tmpMethodName = (tmpMethodName == null) ? "" : tmpMethodName.trim();
            if (tmpMethodName.equals(methodName)) {
                hasMethod = true;
                break;
            }
        }
        return hasMethod;
    }

    /**
     * 通过指定的方法名找到Method
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Method getMethodByName(Object obj, String methodName) {
        Method resMethod = null;
        methodName = (methodName == null) ? "" : methodName.trim();
        if (isEmpty(obj) || isEmpty(methodName)) {
            return resMethod;
        }

        Method[] method = obj.getClass().getDeclaredMethods();
        for (int index = 0; index < method.length; index++) {
            // PubMethod.toPrint("method[index].getName():"+method[index].getName());
            String tmpMethodName = method[index].getName();
            tmpMethodName = (tmpMethodName == null) ? "" : tmpMethodName.trim();
            if (tmpMethodName.equals(methodName)) {
                resMethod = method[index];
                break;
            }
        }
        return resMethod;
    }

    /**
     * 执行对象中指定的方法名
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Object execMethod(Object obj, String methodName) {
        Method method = getMethodByName(obj, methodName);
        Object val = null;
        try {
            val = method.invoke(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return val;
    }


    public static void addArrayToList(List list, Object[] array) {
        for (Object obj : array) {
            list.add(obj);
        }
    }

    /**
     * 拼写sql 中 in 语句后的串
     *
     * @param values
     * @return
     */
    public static String getSqlInString(String[] values) {
        if (isEmpty(values)) return "";
        StringBuffer res = new StringBuffer("(");
        int firstFlag = 1;
        for (String val : values) {
            if (firstFlag == 1) {
                res.append("'").append(val.trim()).append("'");
                firstFlag = 0;
            } else {
                res.append(",'").append(val.trim()).append("'");
            }
        }
        res.append(")");
        return res.toString();
    }

    /**
     * 拼写sql 中 in 语句后的串
     *
     * @param values
     * @return
     */
    public static String getSqlInString(List<String> values) {
        if (isEmpty(values)) return "";
        StringBuffer res = new StringBuffer("(");
        int firstFlag = 1;
        for (String val : values) {
            if (firstFlag == 1) {
                res.append("'").append(val).append("'");
                firstFlag = 0;
            } else {
                res.append(",'").append(val).append("'");
            }
        }
        res.append(")");
        return res.toString();
    }

    /**
     * 拼写sql 中 in 语句后的串
     *
     * @param values
     * @return
     */
    public static String getSqlInStr(List<Long> values) {
        if (isEmpty(values)) return "";
        StringBuffer res = new StringBuffer("(");
        int firstFlag = 1;
        for (Long val : values) {
            if (firstFlag == 1) {
                res.append(val);
                firstFlag = 0;
            } else {
                res.append(",").append(val);
            }
        }
        res.append(")");
        return res.toString();
    }


    // 判断是否为空。
    public static boolean isEmpty(String Value) {
        return (Value == null || Value.trim().equals(""));
    }

    public static boolean isEmpty(StringBuffer Value) {

        return (Value == null || (Value.toString().trim()).equals(""));
    }

    /*
     * @function:判空 @author:
     */
    public static boolean isEmpty(List list) {
        if (list == null || list.size() == 0)
            return true;
        else
            return false;
    }

    /*
     * @function:判空 @author:
     */
    public static boolean isEmpty(Set set) {
        if (set == null || set.size() == 0)
            return true;
        else
            return false;
    }

    /*
     * @function:判空
     */
    public static boolean isEmpty(Map map) {
        if (map == null || map.size() == 0)
            return true;
        else
            return false;
    }

    // 判断是否为空。
    public static boolean isEmpty(Object Value) {
        if (Value == null)
            return true;
        else
            return false;
    }

    public static boolean isEmpty(Double value) {
        if (value == null || value.doubleValue() == 0.0)
            return true;
        else
            return false;
    }

    // 判断是否为空。
    public static boolean isEmpty(Long obj) {
        if (obj == null || obj.longValue() == 0)
            return true;
        else
            return false;
    }

    // 判断是否为空。
    public static boolean isEmpty(Object[] Value) {
        if (Value == null || Value.length == 0)
            return true;
        else
            return false;
    }

    /*
     * 按要求分割字符串.
     *
     */
    public static String[] splitString(String srcStr, String splitter) {
        if (srcStr == null) return new String[]{""};
        String[] tmpArr = srcStr.split(splitter);
        if (tmpArr == null || tmpArr.length == 0) {
            return new String[]{""};
        } else {
            for (int index = 0; index < tmpArr.length; index++) {
                tmpArr[index] = tmpArr[index].trim();
            }
            return tmpArr;
        }
    }


    public static String getFirstEleFromArr(Object obj) {
        String[] strArr = (String[]) obj;
        if (strArr == null || strArr.length == 0) {
            return "";
        }
        return (String) strArr[0];
    }


    /**
     * 判断字符串s是否包含在数组中
     *
     * @param s
     * @param array
     * @return
     * @throws Exception
     */
    public static boolean isInArray(String s, String[] array) {
        boolean b = false;
        if (s == null)
            return b;
        try {
            for (int i = 0; array != null && i < array.length; i++) {
                if (s.equals(array[i]))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 判断字符串s是否包含在列表中
     *
     * @param s
     * @param array
     * @return
     * @throws Exception
     */
    public static boolean isInArray(String s, List<String> array) {
        boolean b = false;
        if (s == null || isEmpty(array))
            return b;
        try {
            //for (int i = 0; array != null && i < array.length; i++) {
            for (String val : array) {
                if (s.equals(val))
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * 字符数组转换为Long数组
     *
     * @param strArr
     * @return
     */
    public static Long[] strArrToLongArr(String[] strArr) {
        if (PubMethod.isEmpty(strArr)) {
            return null;
        }
        Long[] longArr = new Long[strArr.length];
        int index = -1;
        for (String str : strArr) {
            Long val = new Long(str);
            longArr[++index] = val;
        }
        return longArr;
    }

    /**
     * 文件的拷贝
     *
     * @param src 源文件
     * @param dst 目标文件
     */
    public static void copy(File src, File dst) {
        int BUFFER_SIZE = 2096;
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dst),
                    BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 生成ID:格式为8位年月日+12位流水号，其中12位流水号不足的补0。
     * 流水号为上一个ID的流水号加1.
     * 例如：20110824000000000001
     *
     * @return
     */
    /*public static String generateId(String preId) {
        String newId = "";
        Long oldNum = Long.parseLong(preId.substring(preId.length() - 12, preId.length()));
        Long newNum = 0l;
        if (oldNum < 999999999999l) {
            newNum = oldNum + 1;
        }

        String newNumStr = String.valueOf(newNum);
        String formatter = "000000000000";
        newId = DateUtil.getYMD() + formatter.substring(0, 12 - newNumStr.length()) + newNumStr;
        return newId;
    }*/


    /**
     * 根据map的value值从大到小进行排序
     *
     * @param keyArr 关键词数组
     * @param map    以keyArr为关键词的map
     * @return 返回重新排序后的关键词数组
     */
    public static String[] sortMapValue(String[] keyArr, Map<String, Double> map) {
        for (int i = 0; i < keyArr.length; i++) {
            for (int j = i + 1; j < keyArr.length; j++) {
                Double valuei = map.get(keyArr[i]);
                int n = i + 1;
                while (valuei == null) {
                    if (n >= keyArr.length) break;
                    String temp = keyArr[i];
                    keyArr[i] = keyArr[n];
                    keyArr[n] = temp;
                    valuei = map.get(keyArr[i]);
                    n++;
                }

                if (n >= keyArr.length) continue;
                Double valuej = map.get(keyArr[j]);
                if (valuej == null) continue;
                if (valuej > valuei) {
                    String temp = keyArr[i];
                    keyArr[i] = keyArr[j];
                    keyArr[j] = temp;
                }
            }
        }
        return keyArr;
    }


    /**
     * 流拷贝
     *
     * @param input
     * @param output
     * @return
     * @throws java.io.IOException
     */
    private static int copy(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024 * 5];
        int count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        output.flush();
        return count;
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String fileFullName) {
        File file = null;
        if (fileFullName == null || "".equals(fileFullName)) return;
        try {
            file = new File(fileFullName);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将int转换为指定长度的字符串，前缀以0补位
     *
     * @param intValue
     * @param strLength
     * @return
     */
    public static String int2Str(int intValue, int strLength) {
        if (strLength > 0) {
            char padding = '0';
            StringBuilder result = new StringBuilder();
            String fromStr = String.valueOf(intValue);
            for (int i = fromStr.length(); i < strLength; i++) {
                result.append(padding);
            }
            result.append(fromStr);
            return result.toString();
        } else {
            return null;
        }
    }

    /**
     * 将字符串转换为UTF-8格式
      * @param str
     * @return
     */
    public static String getUTF8String(String str) {
        if (isEmpty(str)) {
            return null;
        } else {
            try {
                return new String(str.getBytes() ,"utf-8");
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
    }

    public static String getRandomStr(int length) {
        if (length < 1) {
            return null;
        }
        StringBuilder result = new StringBuilder("");
        char[] allChars = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'X'};
        for (int i = 0; i < length; i++) {
            int index = new Random().nextInt(allChars.length);
            result.append(allChars[index]);
        }
        return result.toString();
    }

    public static String getDateTimeRadomStr(int length) {
        SimpleDateFormat yyyyMMddHHmmssSSSFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return yyyyMMddHHmmssSSSFormat.format(new Date())+getRandomStr(length);
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     * @param type 要转化的类型
     * @param map 包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     * @throws IntrospectionException
     *             如果分析类属性失败
     * @throws IllegalAccessException
     *             如果实例化 JavaBean 失败
     * @throws InstantiationException
     *             如果实例化 JavaBean 失败
     * @throws InvocationTargetException
     *             如果调用属性的 setter 方法失败
     */
    public static Object convertMap(Class type, Map map)
            throws IntrospectionException, IllegalAccessException,
            InstantiationException, InvocationTargetException {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); // 获取类属性
        Object obj = type.newInstance(); // 创建 JavaBean 对象

        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();

            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                Object value = map.get(propertyName);

                Object[] args = new Object[1];
                args[0] = value;

                descriptor.getWriteMethod().invoke(obj, args);
            }
        }
        return obj;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的  Map 对象
     * @throws IntrospectionException 如果分析类属性失败
     * @throws IllegalAccessException 如果实例化 JavaBean 失败
     * @throws InvocationTargetException 如果调用属性的 setter 方法失败
     */
    public static Map convertBean(Object bean)
            throws IntrospectionException, IllegalAccessException, InvocationTargetException {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);

        PropertyDescriptor[] propertyDescriptors =  beanInfo.getPropertyDescriptors();
        for (int i = 0; i< propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean, new Object[0]);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    } 


}
