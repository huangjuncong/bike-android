package com.coder520.mamabike.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 参数检查工具类
 *
 */
public class CheckParamUtil {

	/**
     * 正则表达式：验证身份证
     */
    public static final String REGEX_ID_CARD = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

    /**
	 * 验证对象[字符串]是否为空
	 * @param obj
	 * @return 是：true，否：false
	 */
	public static boolean isNull(Object obj) {
		return obj == null || isNull(toString(obj));
	}

    public static String toString(Object obj) {
		String str = (obj == null) ? "" : obj.toString();
		return isNull(str) ? "" : str;
	}

	/**
	 * 验证字符串是否为空
	 * @param str
	 * @return 是：true，否：false
	 * @author 王浩
	 */
	public static boolean isNull(String str) {
		return str == null || "".equals(str.trim()) || "null".equals(str.trim()) || "undefined".equals(str.trim());
	}

	/**
	 * 验证字符串是否为整数
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isInteger(String str) {
		return !isNull(str) && str.matches("^-?\\d+$");
	}

	/**
	 * 验证对象是否为整数
	 * @param obj
	 * @return 是：true，否：false
	 */
	public static boolean isInteger(Object obj) {
		return obj != null && obj.toString().matches("^-?\\d+$");
	}

	/**
	 * 检查身份证
	 * @param idCardNumber
	 * @return
	 */
	public static boolean isIdCardNumber(String idCardNumber) {
		return idCardNumber != null && idCardNumber != "" && idCardNumber.toString().matches(REGEX_ID_CARD);
	}

	/**
	 * 判定收货地址是否包含不需要显示的字符串
	 * @param addr
	 * @return
	 */
	public static boolean checkAddr(String addr){
		if(CheckParamUtil.isMobile(addr) || CheckParamUtil.isNull(addr) || CheckParamUtil.isQQNumber(addr)
			|| addr.contains("@") || addr.contains(".com") || addr.contains("不需要") || addr.contains("充值") 
			|| addr.contains("游戏") || addr.contains("点卡") || addr.contains("账号") || addr.contains("角色")
			){
				return false;
		}else{
			return true;
		}
	}

	/**
	 * 验证字符串是否为身份证格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isIdCard(String str) {
		if (str == null || str.length() != 18) return false;

        // 17位加权因子，与身份证号前17位依次相乘。
        int w[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
        int sum = 0;// 保存级数和
        for (int i = 0; i < str.length() - 1; i++)
        {
            sum += new Integer(str.substring(i, i + 1)) * w[i];
        }
        /**
         * 校验结果，上一步计算得出的结果与11取模，得到的结果相对应的字符就是身份证最后一位，也就是校验位。
         * 例如：0对应下面数组第一个元素，以此类推。
         */
        String sums[] = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        if (sums[(sum % 11)].equals(str.substring(str.length() - 1, str.length())))
        {// 与身份证最后一位比较
            return true;
        }
        return false;
	}

	/**
	 * 验证字符串是否为护照格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isPassportCard(String str){
		return !isNull(str) && str.matches("(P\\d{7})|(G\\d{8})");
	}

	/**
	 * 验证字符串是否为导游证格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isGuideCard(String str){
		return !isNull(str) && str.matches("^D(-)?\\d{4}(-)?\\d{6}");
	}

	/**
	 * 验证字符串是否为手机号码
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isMobile(String str) {
		return !isNull(str) && str.matches("^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
	}

	/**
	 * 验证字符串是否是qq号
	 * @param str
	 * @return
	 */
	public static boolean isQQNumber(String str) {
		return !isNull(str) && str.matches("[1-9]\\d{4,14}");
	}

	/**
	 * 验证字符串是否为车牌号格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isBusNumber(String str) {
		return !isNull(str) && str.matches("^[\u4e00-\u9fa5|WJ]{1}[A-Z0-9]{6}$");
	}

	/**
	 * 验证字符串是否为电话号码[座机]格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isPhone(String str) {
		return !isNull(str) && str.matches("^(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");
	}

	/**
	 * 验证字符串是否为浮点数
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isDouble(String str) {
		return !isNull(str) && str.matches("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$");
	}

	/**
	 * 验证字符串是否为Email格式
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isEmail(String str) {
		return !isNull(str) && str.matches("^[0-9a-z][a-z0-9\\._-]{1,}@[a-z0-9-]{1,}[a-z0-9]\\.[a-z\\.]{1,}[a-z]$");
	}

	/**
	 * 验证字符串是否为数值型
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isNum(String str) {
		return !isNull(str) && str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 验证字符串是否为数值型
	 * @param str
	 * @return 是：true，否：false
	 */
	public static boolean isNotNullAndNotNum(String str) {
		return !isNull(str) && !isNum(str);
	}

	/**
	 * 验证对象是否为数值型
	 * @param obj
	 * @return 是：true，否：false
	 */
	public static boolean isNum(Object obj) {
		return obj != null && isNum(obj.toString());
	}

	/**
	 * 验证文件名后辍是否为图片格式（jpeg,jpg,png,bmp,gif)
	 * @param fileName
	 * @return 是：true，否：false
	 */
	public static boolean isImageSuffix(String fileName) {
		String[] fileNameArray = fileName.split("\\.");
		if (fileNameArray.length > 0) {
			String fileSuffix = fileNameArray[fileNameArray.length - 1].toLowerCase();
			if (fileSuffix.equals("png") || fileSuffix.equals("jpg") || fileSuffix.equals("jpeg")
					|| fileSuffix.equals("bmp") || fileSuffix.equals("gif")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 验证文件名否为图片格式 后辍+fileType
	 * @param fileName
	 * @param fileType
	 * @return 是：true，否：false
	 */
	public static boolean isImage(String fileName, String fileType) {
		String[] fileNameArray = fileName.split("\\.");
		if (fileNameArray.length > 0) {
			String fileSuffix = fileNameArray[fileNameArray.length - 1].toLowerCase();
			if (fileSuffix.equals("png") || fileSuffix.equals("jpg") || fileSuffix.equals("jpeg")
					|| fileSuffix.equals("bmp") || fileSuffix.equals("gif")) {
				if (!isNull(fileType)) {
					if (fileType.indexOf("png") != -1 || fileType.indexOf("jpeg") != -1 || fileType.indexOf("jpg") != -1
							|| fileType.indexOf("gif") != -1 || fileType.indexOf("bmp") != -1) {
						return true;
					}
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 验证字符串是否为yyyy-MM-dd格式的合法日期
	 * @param str
	 * @return boolean
	 */
	public static final boolean isDate(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(false);// 此设置用于验证日期的合法性
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证字符串是否为yyyy-MM格式的日期
	 * @param str
	 * @return
	 */
	public static final boolean isDateYm(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		sdf.setLenient(false);// 此设置用于验证日期的合法性
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证字符串是否为yyyy格式的日期
	 * @param str
	 * @return
	 */
	public static final boolean isDateY(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		sdf.setLenient(false);// 此设置用于验证日期的合法性
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证字符串是否为yyyy-MM-dd HH:mm:ss格式的合法日期时间
	 * @param str
	 * @return boolean
	 */
	public static final boolean isDatetime(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setLenient(false);// 此设置用于验证日期的合法性
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证对象是否为日期时间
	 * @param obj
	 * @return boolean
	 */
	public static final boolean isDatetime(Object obj){
		return obj != null && isDatetime(obj.toString());
	}

	/**
	 * 验证字符串是否为yyyy-MM-dd HH:mm:ss格式的合法日期时间
	 * @param str
	 * @return boolean
	 */
	public static final boolean isDatetimeNoSecond(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setLenient(false);// 此设置用于验证日期的合法性
		try {
			sdf.parse(str);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 验证对象是否为合法的日期格式
	 * @param obj 验证对象
	 * @param pattern 匹配格式
	 * @return boolean
	 */
	public static final boolean isDateTime(Object obj,String pattern){
		if(obj == null || pattern == null){
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);// 此设置用于验证日期的合法性
		try {
			if(obj.getClass() == Date.class || obj instanceof Date){
				Date date = (Date) obj;
				sdf.format(date);
			}else{
				sdf.parse(obj.toString());
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}
