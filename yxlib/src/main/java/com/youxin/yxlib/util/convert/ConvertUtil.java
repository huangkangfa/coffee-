package com.youxin.yxlib.util.convert;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.youxin.yxlib.util.constant.ConstUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 转换相关工具类
 */
public class ConvertUtil {

    private ConvertUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final int DEFAULT_COVERING_POSITION_LENGTH = 4;
    private static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 16 -> bytes
     * <p>例如：</p>
     * hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hex2Bytes(String hexString) {
        if (TextUtils.isEmpty(hexString)) return null;
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (char2Dec(hexBytes[i]) << 4 | char2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    /**
     * 16 -> 10
     *
     * @param str
     * @return
     */
    public static Integer hex2Decimal(String str) {
        try {
            int dec = Integer.valueOf(str, 16).intValue();
            return Integer.valueOf(dec);
        } catch (Exception localException) {
        }
        return 0;
    }

    /**
     * 16 -> 8
     *
     * @param str
     * @return
     */
    public static String hex2Octal(String str) {
        int dec = hex2Decimal(str).intValue();
        return decimalToOctal(dec);
    }

    /**
     * 16 -> 2 缺位补0
     *
     * @param str
     * @return
     */
    public static String hex2Binary(String str) {
        return hexToBinary(str, true);
    }

    /**
     * 16 -> 2 是否缺位补0
     *
     * @param str
     * @param isCoveringPosition
     * @return
     */
    public static String hexToBinary(String str, boolean isCoveringPosition) {
        int decimal = hex2Decimal(str).intValue();
        String binary = decimalToBinary(decimal);

        if (isCoveringPosition) {
            int len = binary.length();
            if (len < DEFAULT_COVERING_POSITION_LENGTH) {
                for (int i = 0; i < DEFAULT_COVERING_POSITION_LENGTH - len; i++) {
                    binary = "0" + binary;
                }
            }
        }
        return binary;
    }

    /**
     * 10 -> bytes
     *
     * @param a
     * @return
     */
    public static byte[] decimal2bytes(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    /**
     * 10 -> 16
     *
     * @param dec
     * @return
     */
    public static String decimalToHex(int dec) {
        String s = Integer.toHexString(dec).toUpperCase();
        return s.length() == 1 ? "0" + s : s;
    }

    /**
     * 10 -> 8
     *
     * @param dec
     * @return
     */
    public static String decimalToOctal(int dec) {
        return Integer.toOctalString(dec);
    }

    /**
     * 10 -> 2
     *
     * @param dec
     * @return
     */
    public static String decimalToBinary(int dec) {
        return Integer.toBinaryString(dec);
    }

    /**
     * 8 ->16
     *
     * @param octal
     * @return
     */
    public static String octal2Hex(String octal) {
        int dec = octal2Decimal(octal);
        return decimalToHex(dec);
    }

    /**
     * 8 -> 10
     *
     * @param octal
     * @return
     */
    public static int octal2Decimal(String octal) {
        return Integer.valueOf(octal, 8).intValue();
    }

    /**
     * 8 -> 2
     *
     * @param octal
     * @return
     */
    public static String octal2Binary(String octal) {
        int dec = octal2Decimal(octal);
        return Integer.toBinaryString(dec);
    }

    /**
     * 2 -> bytes
     *
     * @param binary 二进制
     * @return bytes
     */
    public static byte[] binary2Bytes(String binary) {
        int lenMod = binary.length() % 8;
        int byteLen = binary.length() / 8;
        // 不是8的倍数前面补0
        if (lenMod != 0) {
            for (int i = lenMod; i < 8; i++) {
                binary = "0" + binary;
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= binary.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * 2 -> 16
     *
     * @param binary
     * @return
     */
    public static String binary2Hex(String binary) {
        int dec = binary2Decimal(binary);
        return decimalToHex(dec);
    }

    /**
     * 2 -> 10
     *
     * @param binary
     * @return
     */
    public static int binary2Decimal(String binary) {
        return Integer.valueOf(binary, 2).intValue();
    }

    /**
     * 2 -> 8
     *
     * @param binary
     * @return
     */
    public static String binary2Octal(String binary) {
        int dec = binary2Decimal(binary);
        return decimalToOctal(dec);
    }

    /**
     * bytes -> 16
     *
     * @param bytes 字节数组
     * @return 16进制大写字符串
     */
    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return (new String(ret)).toUpperCase();
    }

    /**
     * bytes->10
     *
     * @param b
     * @return
     */
    public static int bytes2Decimal(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     * bytes->2
     *
     * @param bytes 字节数组
     * @return Binary  二进制（位、比特）
     */
    public static String bytes2Binary(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * bytes -> chars
     *
     * @param bytes 字节数组
     * @return 字符数组
     */
    public static char[] bytes2Chars(byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * Char -> 10
     *
     * @param hexChar hex单个字节
     * @return 0..15
     */
    private static int char2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * char->byte
     *
     * @param c
     * @return
     */
    private static byte char2Byte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * chars -> bytes
     *
     * @param chars 字符数组
     * @return 字节数组
     */
    public static byte[] chars2Bytes(char[] chars) {
        if (chars == null || chars.length <= 0) return null;
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * ASCII转换为字符串
     *
     * @param s
     * @return
     */
    public static String asciiToString(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "ASCII");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }

    //字符串(16进制)转换为ASCII码
    public static String stringToAscii(String data) {
        StringBuilder result = new StringBuilder("");
        char[] chars = data.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int temp = chars[i];
            result.append(setBytesLen(decimalToHex(temp), 1, false));
        }
        return result.toString();
    }

    /**
     * 中文字符串转gb2312
     *
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String chineseToGb2312(String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "gb2312").replaceAll("%", "");
    }

    /**
     * gb2312转中文
     *
     * @param s
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String gb2312ToChinese(String s) throws UnsupportedEncodingException {
        return new String(hex2Bytes(s), "gb2312");
    }

    /**
     * ip -> long
     * @param strIp
     * @return
     */
    public static long ip2long(String strIp) {
        long[] ip = new long[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * long -> ip
     *
     * @param longIp
     * @return
     */
    public static String long2IP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        // 直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        // 将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        // 将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }


    /**
     * inputStream  ->  outputStream
     *
     * @param is 输入流
     * @return outputStream子类
     */
    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) return null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b, 0, 1024)) != -1) {
                os.write(b, 0, len);
            }
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * outputStream  ->  inputStream
     *
     * @param out 输出流
     * @return inputStream子类
     */
    public ByteArrayInputStream output2InputStream(OutputStream out) {
        if (out == null) return null;
        return new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
    }

    /**
     * inputStream  ->  bytes
     *
     * @param is 输入流
     * @return 字节数组
     */
    public static byte[] inputStream2Bytes(InputStream is) {
        if (is == null) return null;
        return input2OutputStream(is).toByteArray();
    }

    /**
     * bytes  ->  inputStream
     *
     * @param bytes 字节数组
     * @return 输入流
     */
    public static InputStream bytes2InputStream(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return null;
        return new ByteArrayInputStream(bytes);
    }

    /**
     * outputStream  ->  bytes
     *
     * @param out 输出流
     * @return 字节数组
     */
    public static byte[] outputStream2Bytes(OutputStream out) {
        if (out == null) return null;
        return ((ByteArrayOutputStream) out).toByteArray();
    }

    /**
     * outputStream  ->  bytes
     *
     * @param bytes 字节数组
     * @return 字节数组
     */
    public static OutputStream bytes2OutputStream(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) return null;
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            os.write(bytes);
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * inputStream  ->  string   按编码
     *
     * @param is          输入流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String inputStream2String(InputStream is, String charsetName) {
        if (is == null || TextUtils.isEmpty(charsetName)) return null;
        try {
            return new String(inputStream2Bytes(is), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string  ->  inputStream   按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static InputStream string2InputStream(String string, String charsetName) {
        if (string == null || TextUtils.isEmpty(charsetName)) return null;
        try {
            return new ByteArrayInputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * outputStream  ->  string   按编码
     *
     * @param out         输出流
     * @param charsetName 编码格式
     * @return 字符串
     */
    public static String outputStream2String(OutputStream out, String charsetName) {
        if (out == null || TextUtils.isEmpty(charsetName)) return null;
        try {
            return new String(outputStream2Bytes(out), charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * string  ->  outputStream  按编码
     *
     * @param string      字符串
     * @param charsetName 编码格式
     * @return 输入流
     */
    public static OutputStream string2OutputStream(String string, String charsetName) {
        if (string == null || TextUtils.isEmpty(charsetName)) return null;
        try {
            return bytes2OutputStream(string.getBytes(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * bitmap  ->  bytes
     *
     * @param bitmap bitmap对象
     * @param format 格式
     * @return 字节数组
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * bytes  ->  bitmap
     *
     * @param bytes 字节数组
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] bytes) {
        return (bytes == null || bytes.length == 0) ? null : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * drawable转bitmap
     *
     * @param drawable drawable对象
     * @return bitmap
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    /**
     * bitmap转drawable
     *
     * @param res    resources对象
     * @param bitmap bitmap对象
     * @return drawable
     */
    public static Drawable bitmap2Drawable(Resources res, Bitmap bitmap) {
        return bitmap == null ? null : new BitmapDrawable(res, bitmap);
    }

    /**
     * drawable转byteArr
     *
     * @param drawable drawable对象
     * @param format   格式
     * @return 字节数组
     */
    public static byte[] drawable2Bytes(Drawable drawable, Bitmap.CompressFormat format) {
        return drawable == null ? null : bitmap2Bytes(drawable2Bitmap(drawable), format);
    }

    /**
     * byteArr转drawable
     *
     * @param res   resources对象
     * @param bytes 字节数组
     * @return drawable
     */
    public static Drawable bytes2Drawable(Resources res, byte[] bytes) {
        return res == null ? null : bitmap2Drawable(res, bytes2Bitmap(bytes));
    }

    /**
     * view转Bitmap
     *
     * @param view 视图
     * @return bitmap
     */
    public static Bitmap view2Bitmap(View view) {
        if (view == null) return null;
        Bitmap ret = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(ret);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return ret;
    }


    /**
     * 字节数转以unit为单位的内存大小
     *
     * @param byteNum 字节数
     * @param unit    单位类型
     *                <ul>
     *                <li>{@link ConstUtil.MemoryUnit#BYTE}: 字节</li>
     *                <li>{@link ConstUtil.MemoryUnit#KB}  : 千字节</li>
     *                <li>{@link ConstUtil.MemoryUnit#MB}  : 兆</li>
     *                <li>{@link ConstUtil.MemoryUnit#GB}  : GB</li>
     *                </ul>
     * @return 以unit为单位的size
     */
    public static double byte2MemorySize(long byteNum, ConstUtil.MemoryUnit unit) {
        if (byteNum < 0) return -1;
        switch (unit) {
            default:
            case BYTE:
                return (double) byteNum;
            case KB:
                return (double) byteNum / ConstUtil.KB;
            case MB:
                return (double) byteNum / ConstUtil.MB;
            case GB:
                return (double) byteNum / ConstUtil.GB;
        }
    }

    /**
     * 字节数转合适内存大小
     * <p>保留3位小数</p>
     *
     * @param byteNum 字节数
     * @return 合适内存大小
     */
    @SuppressLint("DefaultLocale")
    public static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0) {
            return "shouldn't be less than zero!";
        } else if (byteNum < ConstUtil.KB) {
            return String.format("%.3fB", byteNum + 0.0005);
        } else if (byteNum < ConstUtil.MB) {
            return String.format("%.3fKB", byteNum / ConstUtil.KB + 0.0005);
        } else if (byteNum < ConstUtil.GB) {
            return String.format("%.3fMB", byteNum / ConstUtil.MB + 0.0005);
        } else {
            return String.format("%.3fGB", byteNum / ConstUtil.GB + 0.0005);
        }
    }

    /**
     * 以unit为单位的时间长度转毫秒时间戳
     *
     * @param timeSpan 毫秒时间戳
     * @param unit     单位类型
     *                 <ul>
     *                 <li>{@link ConstUtil.TimeUnit#MSEC}: 毫秒</li>
     *                 <li>{@link ConstUtil.TimeUnit#SEC }: 秒</li>
     *                 <li>{@link ConstUtil.TimeUnit#MIN }: 分</li>
     *                 <li>{@link ConstUtil.TimeUnit#HOUR}: 小时</li>
     *                 <li>{@link ConstUtil.TimeUnit#DAY }: 天</li>
     *                 </ul>
     * @return 毫秒时间戳
     */
    public static long timeSpan2Millis(long timeSpan, ConstUtil.TimeUnit unit) {
        switch (unit) {
            default:
            case MSEC:
                return timeSpan;
            case SEC:
                return timeSpan * ConstUtil.SEC;
            case MIN:
                return timeSpan * ConstUtil.MIN;
            case HOUR:
                return timeSpan * ConstUtil.HOUR;
            case DAY:
                return timeSpan * ConstUtil.DAY;
        }
    }

    /**
     * 毫秒时间戳转以unit为单位的时间长度
     *
     * @param millis 毫秒时间戳
     * @param unit   单位类型
     *               <ul>
     *               <li>{@link ConstUtil.TimeUnit#MSEC}: 毫秒</li>
     *               <li>{@link ConstUtil.TimeUnit#SEC }: 秒</li>
     *               <li>{@link ConstUtil.TimeUnit#MIN }: 分</li>
     *               <li>{@link ConstUtil.TimeUnit#HOUR}: 小时</li>
     *               <li>{@link ConstUtil.TimeUnit#DAY }: 天</li>
     *               </ul>
     * @return 以unit为单位的时间长度
     */
    public static long millis2TimeSpan(long millis, ConstUtil.TimeUnit unit) {
        switch (unit) {
            default:
            case MSEC:
                return millis;
            case SEC:
                return millis / ConstUtil.SEC;
            case MIN:
                return millis / ConstUtil.MIN;
            case HOUR:
                return millis / ConstUtil.HOUR;
            case DAY:
                return millis / ConstUtil.DAY;
        }
    }

    /**
     * 毫秒时间戳转合适时间长度
     *
     * @param millis    毫秒时间戳
     *                  <p>小于等于0，返回null</p>
     * @param precision 精度
     *                  <ul>
     *                  <li>precision = 0，返回null</li>
     *                  <li>precision = 1，返回天</li>
     *                  <li>precision = 2，返回天和小时</li>
     *                  <li>precision = 3，返回天、小时和分钟</li>
     *                  <li>precision = 4，返回天、小时、分钟和秒</li>
     *                  <li>precision &gt;= 5，返回天、小时、分钟、秒和毫秒</li>
     *                  </ul>
     * @return 合适时间长度
     */
    @SuppressLint("DefaultLocale")
    public static String millis2FitTimeSpan(long millis, int precision) {
        if (millis <= 0 || precision <= 0) return null;
        StringBuilder sb = new StringBuilder();
        String[] units = {"天", "小时", "分钟", "秒", "毫秒"};
        int[] unitLen = {86400000, 3600000, 60000, 1000, 1};
        precision = Math.min(precision, 5);
        for (int i = 0; i < precision; i++) {
            if (millis >= unitLen[i]) {
                long mode = millis / unitLen[i];
                millis -= mode * unitLen[i];
                sb.append(mode).append(units[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 指定字节长度补全
     */
    public static String setBytesLen(String s, int bytesNum, boolean zeroAtAfter) {
        if (TextUtils.isEmpty(s))
            return "";

        int len = s.length();
        int length = bytesNum * 2;

        StringBuffer sb = new StringBuffer();
        if (len < length) {
            sb.append(s);
            int temp = length - len;
            for (int i = 0; i < temp; i++) {
                if (zeroAtAfter) {
                    sb.append("0");
                } else {
                    sb.insert(0, "0");
                }
            }
        }else if(len==length){
            return s;
        }else{
            sb.append(s.substring(len - length, len));
        }
        return sb.toString();
    }

}
