package com.gitee.pristine.autoconfigure.util;

/**
 * RC4 加密工具类
 * @author Pristine Xu
 * @date 2022/3/25 16:46
 * @description:
 */
public class Rc4Util {

    public static String decrypt(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return toString(rc4(data, key));
    }

    public static String decrypt(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(rc4(hexString2Bytes(data), key));
    }

    public static byte[] encryptToBytes(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte[] bData = data.getBytes();
        return rc4(bData, key);
    }

    public static String encryptToString(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return toHexString(toString(encryptToBytes(data, key)));
    }

    private static String toString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static byte[] initKey(String aKey) {
        byte[] bKey = aKey.getBytes();
        byte[] state = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (bKey == null || bKey.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((bKey[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % bKey.length;
        }
        return state;
    }

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        // 0x表示十六进制
        return str;
    }

    private static byte[] hexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
        b0 = (char) (b0 << 4);
        char b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
        byte ret = (byte) (b0 ^ b1);
        return ret;
    }

    private static byte[] rc4(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte[] key = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

}
