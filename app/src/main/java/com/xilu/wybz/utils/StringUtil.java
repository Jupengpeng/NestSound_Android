package com.xilu.wybz.utils;

import android.content.Context;
import android.text.ClipboardManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.luhu.common.CharsetCst;

/**
 * @author
 * @date : 2013-9-19
 * @desc : 字符串处理工具类
 * <p/>
 * public method
 * <li>isEmpty(String) 字符串是否为空</li>
 * <li>isNotEmpty(String) 字符串是否为非空</li>
 * <li>isBlank(String) 字符串是否为空格串</li>
 * <li>isNotBlank(String) 字符串是否非空格串</li>
 * <li>nullToEmpty(String) 将null转换为空串</li>
 * <li>nullToString(String) 将null转换为字符串NULL</li>
 * <li>halfToFull(String) 半角转全角</li>
 * <li>fullToHalf(String) 全角转半角</li>
 * <li>htmlEscapeCharsToString(String) 处理html中的特殊字符串</li>
 * <li>utf8UrlEncode(String) 将字符串用UTF-8编码</li>
 * <li>urlEncode(String) 将字符串用指定的编码进行编码</li>
 * <li>parseInt(String) parseDouble 将字符串转整形，可带默认值</li>
 */
public class StringUtil {

    public final static String LINE_SEPERATOR = "\r\n";

    /**
     * 用给定的分隔符对字符串进行拆分，并生成数组
     *
     * @param message   需要拆分的字符串
     * @param separator 分隔符
     * @return 生成的数组
     */
    public static ArrayList<String> splitToArrayList(String message, String separator) {
        ArrayList<String> list = new ArrayList<String>();
        int start = 0;
        int index = 0;
        while ((index = message.indexOf(separator, start)) != -1) {
            list.add(message.substring(start, index));
            start = index + separator.length();
        }

        if (start < message.length()) {
            list.add(message.substring(start, message.length()));
        }

        return list;
    }

    /**
     * 半角转全角
     *
     * @param half
     * @return 全角字符串.
     */
    public static String halfToFull(String half) {
        if (isEmpty(half))
            return half;

        char c[] = half.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);
            }
        }

        return new String(c);
    }

    /**
     * 全角转半角
     *
     * @param full
     * @return 半角字符串
     */
    public static String fullToHalf(String full) {
        if (isEmpty(full))
            return full;

        char c[] = full.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);
            }
        }

        String result = new String(c);
        return result;
    }

    /**
     * 处理html中的特殊字符串
     * <p/>
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param html
     * @return
     */
    public static String htmlEscapeCharsToString(String html) {
        return isEmpty(html) ? html : html.replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">").replaceAll("&amp;", "&")
                .replaceAll("&quot;", "\"");
    }

    /**
     * 将字符串用UTF-8编码，发生异常时，抛出异常
     *
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws
     */
/*
    public static String utf8UrlEncode(String str) {
		return urlEncode(str, CharsetCst.UTF_8);
	}
*/

    /**
     * 将字符串用指定的编码进行编码，发生异常时，抛出异常
     *
     * @param str
     * @param charset
     * @return
     */
    public static String urlEncode(String str, String charset) {
        if (!isEmpty(str)) {
            try {
                return URLEncoder.encode(str, charset);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
                throw new RuntimeException(
                        "UnsupportedEncodingException occurred. ", ex);
            }
        }
        return str;
    }

    /**
     * 字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * 字符串是否为非空
     *
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str) {
        return (str != null && str.length() != 0);
    }

    /**
     * 字符串是否为空格串
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * 字符串是否非空格串
     *
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str) {
        return (str != null && str.trim().length() != 0);
    }

    /**
     * 将null转换为空串,如果参数为非null，则直接返回
     *
     * @param str
     * @return
     */
    public static String nullToEmpty(String str) {
        return (str == null ? "" : str);
    }

    /**
     * 将null转换为字符串NULL,如果参数为非null，则直接返回
     *
     * @param str
     * @return
     */
    public static String nullToString(String str) {
        return (str == null ? "NULL" : str);
    }

    // 检测两个字符串是否相同
    public final static boolean isSame(String value1, String value2) {
        if (isEmpty(value1) && isEmpty(value2))
            return true;
        else if (!isEmpty(value1) && !isEmpty(value2))
            return (value1.trim().equalsIgnoreCase(value2.trim()));
        else
            return false;
    }

    // 检测变量的值是否为一个整型数据
    public final static boolean isInt(String value) {
        if (isEmpty(value))
            return false;

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    // 判断变量的值是否为double类型
    public final static boolean isDouble(String value) {
        if (isEmpty(value))
            return false;
        try {
            Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // 解析一个字符串为整数
    public final static int parseInt(String value) {
        if (isInt(value))
            return Integer.parseInt(value);
        return 0;
    }

    public final static int parseInt(String value, int defaultValue) {
        if (isInt(value))
            return Integer.parseInt(value);
        return defaultValue;
    }

    // 解析一个字符串为double
    public final static double parseDouble(String value) {
        if (isDouble(value))
            return Double.parseDouble(value);
        return 0;
    }

    public final static double parseDouble(String value, double defaultValue) {
        if (isDouble(value))
            return Double.parseDouble(value);
        return defaultValue;
    }

    /**
     * 取一个字符串中间，两个字符串标记之间的值
     *
     * @param content 原始的字符串
     * @param begin   起始的字符串
     * @param end     结束的字符串
     * @return 截取后的字符串
     */
    public static String split(String content, String begin, String end) {
        if (StringUtil.isEmpty(content) || StringUtil.isEmpty(begin)) {
            return null;
        }

        int beginIndex = content.indexOf(begin);
        int endIndex = content.length();
        if (end != null) {
            endIndex = content.indexOf(end);
        }

        if (beginIndex == -1) {
            return null;
        }

        if (endIndex == -1) {
            endIndex = content.length();
        }

        int length = begin.length();

        return content.substring(beginIndex + length, endIndex);
    }

    // 编码字符串中 HTML 字符
    public final static String encodeHtml(String s) {
        if (isEmpty(s))
            return "";

        s = s.replaceAll("&", "&amp;");
        s = s.replaceAll("<", "&lt;");
        s = s.replaceAll(">", "&gt;");
        s = s.replaceAll("\"", "&quot;"); // 双引号
        s = s.replaceAll("'", "&apos;"); // 单引号
        s = s.replaceAll(StringUtil.LINE_SEPERATOR, "<br>"); // 回车

        return s;
    }

    // 解码字符串中 HTML 字符
    public final static String decodeHtml(String s) {
        if (isEmpty(s))
            return "";

        s = s.replaceAll("&amp;", "&");
        s = s.replaceAll("&lt;", "<");
        s = s.replaceAll("&gt;", ">");
        s = s.replaceAll("&quot;", "\""); // 双引号
        s = s.replaceAll("&apos;", "'"); // 单引号
        s = s.replaceAll("<br>", StringUtil.LINE_SEPERATOR); // 回车

        return s;
    }

    // 编码 URL 字符串
    public final static String encodeUrl(String url) {
        return encodeUrl(url, "UTF-8");
    }

    public final static String encodeUrl(String url, String charset) {
        if (isEmpty(url))
            return "";

        try {
            url = URLEncoder.encode(url, charset);
        } catch (Exception e) {
        }

        return url;
    }

    // 解码 URL 字符串
    public final static String decodeUrl(String url) {
        return decodeUrl(url, "UTF-8");
    }

    public final static String decodeUrl(String url, String charset) {
        if (isEmpty(url))
            return "";

        try {
            url = URLDecoder.decode(url, charset);
        } catch (Exception e) {
        }

        return url;
    }

    // 判断是否为闰年
    public static boolean isLeapYear(int year) {
        boolean flag = false;
        if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            flag = true;
        }
        return flag;
    }

    // 判断手机格式是否正确
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }

    // 判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    public static String replaceString(String str1, String str2, String tmp,
                                       int i, int j) {
        if (StringUtil.isEmpty(str2) || str2.length() < 6)
            return "";
        if (isSame(str1, str2))
            return str1;
        return str2.substring(0, i) + tmp + str2.substring(j);

    }

    public static String replaceString(String str1, String str2) {
        return replaceString(str1, str2, "***", 2, 5);
    }

    /**
     * 字符串部分替换
     *
     * @param str
     * @param start
     * @param end
     * @param rex
     * @return
     * @author
     */
    public static String makeStrInSaveMode(String str, int start, int end,
                                           String rex) {
        if (start < 0)
            start = 0;
        if (start > str.length())
            start = str.length();
        if (end < start)
            end = start;
        if (end < 0)
            end = 0;
        if (end > str.length())
            end = str.length();
        String reStr = "";
        reStr += str.substring(0, start);
        for (int i = 0; i < end - start; i++) {
            reStr += rex;
        }
        reStr += str.substring(end, str.length());
        // System.out.println(reStr); s
        return reStr;
    }

    /**
     * 验证字符是否包含汉字 ，英文，数字，下划线
     *
     * @param str
     * @return
     */
    public static boolean isChinese_English_Num_UnderLine(String str) {
        Pattern p = Pattern
                .compile("^([\\u4e00-\\u9fa5]*[\\w]*)+$");
        Matcher m = p.matcher(str);

        return m.matches();
    }

    /**
     * 电话号码隐藏中间四位
     *
     * @param mobilePhone
     * @return
     */
    public static String formatPhoneNum(String mobilePhone) {
        if (mobilePhone.length() == 11) {
            return mobilePhone.substring(0, 3) + "****" + mobilePhone.substring(7, 11);
        } else {
            return mobilePhone;
        }
    }

    /**
     * 实现文本复制功能
     * add by june
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by june
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }
    /**
     * 手机号码判定
     * add by june
     *
     * @param mobile
     */
    public static boolean checkPhone(String mobile) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(147))\\d{8}$");
        Matcher m = p.matcher(mobile);
        return m.matches();
    }
}
