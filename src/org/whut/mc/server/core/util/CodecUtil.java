package org.whut.mc.server.core.util;

import java.io.*;

/**
 * Created by yangyang on 2015/12/2.
 */
public class CodecUtil {
    private static final String CHARACTER_SET = "UTF-8";
    private final static char[] HEX = "0123456789abcdef".toCharArray();

    public static byte[] short2Bytes(short value, int offset) {
        byte[] bytes = new byte[2];
        bytes[offset] = (byte) (value >> 8);
        bytes[offset + 1] = (byte) (value);

        return bytes;
    }

    public static short bytes2Short(byte[] bytes, int offset) {
        return (short) (((bytes[offset] << 8) | bytes[offset + 1] & 0xff));
    }

    public static byte[] int2Bytes(int value, int offset) {
        byte[] bytes = new byte[4];
        bytes[offset] = (byte) (value >> 24);
        bytes[offset + 1] = (byte) (value >> 16);
        bytes[offset + 2] = (byte) (value >> 8);
        bytes[offset + 3] = (byte) (value);

        return bytes;
    }

    public static int bytes2Int(byte[] bytes, int offset) {
        return (int) ((((bytes[offset] & 0xff) << 24) | ((bytes[offset + 1] & 0xff) << 16)
                | ((bytes[offset + 2] & 0xff) << 8) | ((bytes[offset + 3] & 0xff))));

    }

    public static byte[] long2Bytes(long value, int offset) {
        byte[] bytes = new byte[8];
        bytes[offset] = (byte) (value >> 56);
        bytes[offset + 1] = (byte) (value >> 48);
        bytes[offset + 2] = (byte) (value >> 40);
        bytes[offset + 3] = (byte) (value >> 32);
        bytes[offset + 4] = (byte) (value >> 24);
        bytes[offset + 5] = (byte) (value >> 16);
        bytes[offset + 6] = (byte) (value >> 8);
        bytes[offset + 7] = (byte) (value);

        return bytes;
    }

    public static long bytes2Long(byte[] bytes, int offset) {
        return ((((long) bytes[offset] & 0xff) << 56) | (((long) bytes[offset + 1] & 0xff) << 48)
                | (((long) bytes[offset + 2] & 0xff) << 40) | (((long) bytes[offset + 3] & 0xff) << 32)
                | (((long) bytes[offset + 4] & 0xff) << 24) | (((long) bytes[offset + 5] & 0xff) << 16)
                | (((long) bytes[offset + 6] & 0xff) << 8) | (((long) bytes[offset + 7] & 0xff)));
    }

    public static byte[] string2Bytes(String str, int start, int end) throws UnsupportedEncodingException {
        String subStr = str.substring(start, end);
        return subStr.getBytes(CHARACTER_SET);
    }

    public static byte[] stringNum2Bytes(String str, int start, int end) throws UnsupportedEncodingException {
        byte[] bt = new byte[0];
        for (int i = start; i < end; i++) {
            Character c = str.charAt(i);
            byte t = (byte) (c - '0');
            byte[] tmp = {t};
            bt = CodecUtil.merge(bt, tmp);
        }
        return bt;
    }

    public static String bytes2String(byte[] bytes) throws UnsupportedEncodingException {
        String str1="";
        StringBuilder sb=new StringBuilder(str1);
        for (byte element: bytes )
        {
            sb.append(String.valueOf(element));
        }
        str1=sb.toString();
        return str1;
    }

    public static byte[] object2Bytes(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray ();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    public static Object bytes2Object(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream (bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }

    public static byte[] merge(byte[] a, byte[] b) {
        int aLen = 0;
        int bLen = 0;

        if (a == null)
            aLen = 0;
        else {
            aLen = a.length;
        }
        if (b == null)
            bLen = 0;
        else {
            bLen = b.length;
        }

        int length = aLen + bLen;
        byte[] rs = new byte[length];
        copy(rs, a, 0);
        copy(rs, b, aLen);

        return rs;
    }

    public static byte[] merge(byte[] a, byte b) {
        byte[] btmp = {b};
        return merge(a, btmp);
    }

    public static void copy(byte[] a, byte[] b, int offset) {
        if (a == null) {
            throw new IllegalArgumentException("Can not copy bytes to null bytes.");
        }

        if (b != null) {
            System.arraycopy(b, 0, a, offset, b.length);
        }
    }

    public static byte[] sub(byte[] b, int offset)
            throws IllegalArgumentException {
        if (b == null)
            throw new IllegalArgumentException("Can not get sub bytes from null bytes.");

        return sub(b, offset, b.length);
    }

    public static byte[] sub(byte[] b, int start, int end)
            throws IllegalArgumentException {
        if (b == null)
            throw new IllegalArgumentException("Can not get sub bytes from null bytes.");

        if (end > b.length)
            end = b.length;

        int length = end - start;
        if (length < 0)
            throw new IllegalArgumentException("Can not get(" + start + ", " + end + ") sub bytes from a bytes.");

        byte[] rs = new byte[length];
        for (int i = 0; i < length; i++) {
            int index = i + start;
            if (index < b.length)
                rs[i] = b[index];
            else
                break;
        }
        return rs;
    }


    public static String getHex(byte b[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++)
            sb.append(HEX[(b[i] >> 4) & 0x0f] + "" + HEX[b[i] & 0x0f]);
        return sb.toString();
    }

    public static void showMsg(byte[] b) {
        for (byte bt : b) {
            System.out.print(bt + " ");
        }
        System.out.println();
    }

    public static boolean compare(byte[] a, byte[] b) {
        if (a.length != b.length) {
            return false;
        } else {
            int len = a.length;
            for (int i = 0; i < len; i++) {
                if (a[i] != b[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String getByteStr(byte[] a, int offset) {
        byte tmp =  a[offset];
        return Byte.toString(tmp);
    }
}
