package com.sequoia.infrastructure.util;

/**
 * HexUtil：进制转换工具类
 *
 * @author KVLT
 * @date 2022-03-30.
 */
public class HexUtil {

    /**
     * 62进制可用的字符串(26小写+26大写+10数字)
     */
    private static final String BASE_DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final char[] BASE_DIGITS_ARR = BASE_DIGITS.toCharArray();
    private static final int BASE = BASE_DIGITS.length();

    /**
     * 通过余数获取对应的64进制表示
     */
    private static final char[] DIGITS_CHAR = BASE_DIGITS.toCharArray();

    /**
     * 这里预留了足够的空位122位
     */
    private static final int FAST_SIZE = 'z';

    /**
     * 这个是为了存放字母对应的值，比如-对应63，但是-是45，也就是 digitsIndex[45]=63
     *   [digitsChar[-]会自动转变成45，这样子十六进制转十进制，就可以获取到前面的数字了。
     */
    private static final int[] DIGITS_INDEX = new int[FAST_SIZE + 1];
    static {
        for (int i = 0; i < FAST_SIZE; i++) {
            DIGITS_INDEX[i] = -1;
        }
        //构造：a[117]=30这样的数组
        for (int i = 0; i < BASE; i++) {
            DIGITS_INDEX[DIGITS_CHAR[i]] = i;
        }
    }

    /**
     * 62进制转十进制
     * @param s
     * @return
     */
    public static long hex62To10(String s) {
        long result = 0L;
        long multiplier = 1;
        for (int pos = s.length() - 1; pos >= 0; pos--) {
            int index = getIndex(s, pos);
            result += index * multiplier;
            multiplier *= BASE;
        }
        return result;
    }

    /**
     * 十进制转62进制
     * @param number
     * @return
     */
    public static String hex10To62(long number) {
        return hexByBase(number, BASE);
//        if (number < 0)
//            System.out.println("Number(Base64) must be positive: " + number);
//        if (number == 0) {
//            return "0";
//        }
//        StringBuilder buf = new StringBuilder();
//        while (number != 0) {
//            //获取余数
//            buf.append(DIGITS_CHAR[(int) (number % BASE)]);
//            //剩下的值
//            number /= BASE;
//        }
//        //反转
//        return buf.reverse().toString();
    }

    private static String hexByBase(long origin, int base) {
        long num = 0;
        if (origin < 0) {
            num = ((long)2 * 0x7fffffff) + origin + 2;
        } else {
            num = origin;
        }

        char[] buf = new char[32];
        int charPos = 32;
        while ((num / base) > 0) {
            buf[--charPos] = BASE_DIGITS_ARR[(int)(num % base)];
            num /= base;
        }
        buf[--charPos] = BASE_DIGITS_ARR[(int)(num % base)];
        return new String(buf, charPos, (32 - charPos));
    }

    //获取对应的的64进制的值
    private static int getIndex(String s, int pos) {
        char c = s.charAt(pos);
        if (c > FAST_SIZE) {
            System.out.println("Unknow character for Base64: " + s);
        }
        int index = DIGITS_INDEX[c];
        if (index == -1) {
            System.out.println("Unknow character for Base64: " + s);
        }
        return index;
    }

    public static void main(String[] args) {
        long l = 866002477056L;
        String str = hex10To62(l);
        System.out.println(str);
        Long g = hex62To10(str);
        System.out.println(g);
    }

}
