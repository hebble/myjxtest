package com.my.utils;

import java.util.Random;
/**
 * 随机数生成工具
 */
public class RandomUtil {
	public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String numberCharAndLetterChar = "0123456789abcdefghijklmnopqrstuvwxyz";
    public static final String numberChar = "0123456789";
    
    /**
     * 返回一个定长的随机字数
     * 
     * @param length
     *            随机数长度
     * @return 
     */
    public static String numString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的纯大小写字母的字符串
     * @param length 随机字符串长度
     * @return
     */
    public static String generateLetterString(int length){
    	 StringBuffer sb = new StringBuffer();
         Random random = new Random();
         for (int i = 0; i < length; i++) {
             sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
         }
         return sb.toString();
    }
    
    /**
     * 返回一个定长的纯大写字母的字符串
     * @param length 随机字符串长度
     * @return
     */
    public static String generateUpperLetterString(int length){
    	return generateLetterString(length).toUpperCase();
    }
    
    /**
     * 返回一个定长的纯小写字母的字符串
     * @param length 随机字符串长度
     * @return
     */
    public static String generateLowerLetterString(int length){
    	return generateLetterString(length).toLowerCase();
    }
    
    /**
     * 返回一个定长的随机数字加小写字母字符串
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateMixString(int length) {
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(numberCharAndLetterChar.charAt(random.nextInt(numberCharAndLetterChar.length())));
        }
        return sb.toString();
    }

    /**
     * 返回一个定长的随机小写字母加数字字符串
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateLowerString(int length) {
        return generateMixString(length).toLowerCase();
    }

    /**
     * 返回一个定长的随机大写字母加数字字符串
     * 
     * @param length
     *            随机字符串长度
     * @return 随机字符串
     */
    public static String generateUpperString(int length) {
        return generateMixString(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     * 
     * @param length
     *            字符串长度
     * @return 纯0字符串
     */
    public static String generateZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     * 
     * @param num
     *            数字
     * @param fixdlenth
     *            字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(long num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     * 
     * @param num
     *            数字
     * @param fixdlenth
     *            字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(int num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
                    + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new Random().nextInt(50));
        System.out.println(generateMixString(6));
        System.out.println(generateLowerString(20));
        System.out.println(generateUpperString(20));
        System.out.println(generateZeroString(6));
        System.out.println(toFixdLengthString(123, 15));
        System.out.println(toFixdLengthString(123L, 15));
        System.out.println(generateLetterString(10));
        System.out.println(generateUpperLetterString(18));
        System.out.println(generateLowerLetterString(18));
        System.out.println(generateString(30));
        System.out.println(generateLetterString(10));
    }


}
