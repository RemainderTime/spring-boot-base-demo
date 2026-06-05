package cn.xf.basedemo.common.utils;

/**
 * packageName cn.xf.basedemo.common.utils
 * @author remaindertime
 * @className StringUtil
 * @date 2024/12/11
 * @description 字符串工具类
 */
public class StringUtil {

    /**
     * 驼峰命名法转短横线命名法（kebab-case）
     *
     * @param camelCase 驼峰命名法字符串
     * @return 短横线命名法字符串
     */
    public static String camelToKebabCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        // 使用正则表达式将大写字母前插入一个"-"
        String result = camelCase.replaceAll("([a-z])([A-Z])", "$1-$2");

        // 转换为小写
        return result.toLowerCase();
    }

    /**
     * 驼峰命名法转下划线命名法（snake_case）
     *
     * @param camelCase 驼峰命名法字符串
     * @return 下划线命名法字符串
     */
    public static String camelToSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        // 使用正则表达式将大写字母前插入一个"_"
        String result = camelCase.replaceAll("([a-z])([A-Z])", "$1_$2");

        // 转换为小写
        return result.toLowerCase();
    }
}
