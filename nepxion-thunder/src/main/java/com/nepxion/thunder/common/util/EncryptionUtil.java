package com.nepxion.thunder.common.util;

/**
 * <p>Title: Nepxion Thunder</p>
 * <p>Description: Nepxion Thunder For Distribution</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @email 1394997@qq.com
 * @version 1.0
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import com.nepxion.thunder.common.constant.ThunderConstants;

public class EncryptionUtil {
    public static final String ALGORITHM_MD5 = "MD5";
    public static final String ALGORITHM_SHA = "SHA";
    public static final String ALGORITHM_SHA_256 = "SHA-256";
    public static final String ALGORITHM_SHA_512 = "SHA-512";

    public static String encryptMD5(String text) throws Exception {
        return encrypt(text, ALGORITHM_MD5);
    }

    public static String encryptMD5(String text, String charset) throws Exception {
        return encrypt(text, ALGORITHM_MD5, charset);
    }

    public static String encryptSHA(String text) throws Exception {
        return encrypt(text, ALGORITHM_SHA);
    }

    public static String encryptSHA(String text, String charset) throws Exception {
        return encrypt(text, ALGORITHM_SHA, charset);
    }

    public static String encryptSHA256(String text) throws Exception {
        return encrypt(text, ALGORITHM_SHA_256);
    }

    public static String encryptSHA256(String text, String charset) throws Exception {
        return encrypt(text, ALGORITHM_SHA_256, charset);
    }

    public static String encryptSHA512(String text) throws Exception {
        return encrypt(text, ALGORITHM_SHA_512);
    }

    public static String encryptSHA512(String text, String charset) throws Exception {
        return encrypt(text, ALGORITHM_SHA_512, charset);
    }

    public static String encrypt(String text, String algorithm) throws Exception {
        return encrypt(text, algorithm, ThunderConstants.ENCODING_FORMAT);
    }

    public static String encrypt(String text, String algorithm, String charset) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
        byte[] bytes = messageDigest.digest(text.getBytes(charset));

        return encryptBASE64(bytes);
    }

    public static String encryptBASE64(byte[] bytes) throws Exception {
        BASE64Encoder base64Encoder = new BASE64Encoder();

        return base64Encoder.encodeBuffer(bytes);
    }

    public static class BASE64Encoder extends CharacterEncoder {
        protected int bytesPerAtom() {
            return 3;
        }

        protected int bytesPerLine() {
            return 57;
        }

        private static final char[] pem_array = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };

        protected void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
            int i;
            int j;
            int k;
            if (paramInt2 == 1) {
                i = paramArrayOfByte[paramInt1];
                j = 0;
                k = 0;
                paramOutputStream.write(pem_array[(i >>> 2 & 0x3F)]);
                paramOutputStream.write(pem_array[((i << 4 & 0x30) + (j >>> 4 & 0xF))]);
                paramOutputStream.write(61);
                paramOutputStream.write(61);
            }
            else if (paramInt2 == 2) {
                i = paramArrayOfByte[paramInt1];
                j = paramArrayOfByte[(paramInt1 + 1)];
                k = 0;
                paramOutputStream.write(pem_array[(i >>> 2 & 0x3F)]);
                paramOutputStream.write(pem_array[((i << 4 & 0x30) + (j >>> 4 & 0xF))]);
                paramOutputStream.write(pem_array[((j << 2 & 0x3C) + (k >>> 6 & 0x3))]);
                paramOutputStream.write(61);
            }
            else {
                i = paramArrayOfByte[paramInt1];
                j = paramArrayOfByte[(paramInt1 + 1)];
                k = paramArrayOfByte[(paramInt1 + 2)];
                paramOutputStream.write(pem_array[(i >>> 2 & 0x3F)]);
                paramOutputStream.write(pem_array[((i << 4 & 0x30) + (j >>> 4 & 0xF))]);
                paramOutputStream.write(pem_array[((j << 2 & 0x3C) + (k >>> 6 & 0x3))]);
                paramOutputStream.write(pem_array[(k & 0x3F)]);
            }
        }
    }

    public static abstract class CharacterEncoder {
        protected PrintStream pStream;

        protected abstract int bytesPerAtom();

        protected abstract int bytesPerLine();

        protected void encodeBufferPrefix(OutputStream paramOutputStream) throws IOException {
            this.pStream = new PrintStream(paramOutputStream);
        }

        protected void encodeBufferSuffix(OutputStream paramOutputStream) throws IOException {
        }

        protected void encodeLinePrefix(OutputStream paramOutputStream, int paramInt) throws IOException {
        }

        protected void encodeLineSuffix(OutputStream paramOutputStream) throws IOException {
            this.pStream.println();
        }

        protected abstract void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException;

        protected int readFully(InputStream paramInputStream, byte[] paramArrayOfByte) throws IOException {
            for (int i = 0; i < paramArrayOfByte.length; i++) {
                int j = paramInputStream.read();
                if (j == -1) {
                    return i;
                }
                paramArrayOfByte[i] = ((byte) j);
            }

            return paramArrayOfByte.length;
        }

        public void encode(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
            byte[] arrayOfByte = new byte[bytesPerLine()];

            encodeBufferPrefix(paramOutputStream);
            for (;;)
            {
                int j = readFully(paramInputStream, arrayOfByte);
                if (j == 0) {
                    break;
                }
                encodeLinePrefix(paramOutputStream, j);
                for (int i = 0; i < j; i += bytesPerAtom()) {
                    if (i + bytesPerAtom() <= j) {
                        encodeAtom(paramOutputStream, arrayOfByte, i, bytesPerAtom());
                    } else {
                        encodeAtom(paramOutputStream, arrayOfByte, i, j - i);
                    }
                }
                if (j < bytesPerLine()) {
                    break;
                }
                encodeLineSuffix(paramOutputStream);
            }
            encodeBufferSuffix(paramOutputStream);
        }

        public void encode(byte[] paramArrayOfByte, OutputStream paramOutputStream) throws IOException {
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
            encode(localByteArrayInputStream, paramOutputStream);
        }

        public String encode(byte[] paramArrayOfByte) {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
            String str = null;
            try
            {
                encode(localByteArrayInputStream, localByteArrayOutputStream);

                str = localByteArrayOutputStream.toString("8859_1");
            } catch (Exception localException) {
                throw new Error("CharacterEncoder.encode internal error");
            }

            return str;
        }

        private byte[] getBytes(ByteBuffer paramByteBuffer) {
            Object localObject = null;
            if (paramByteBuffer.hasArray()) {
                byte[] arrayOfByte = paramByteBuffer.array();
                if ((arrayOfByte.length == paramByteBuffer.capacity()) && (arrayOfByte.length == paramByteBuffer.remaining())) {
                    localObject = arrayOfByte;
                    paramByteBuffer.position(paramByteBuffer.limit());
                }
            }
            if (localObject == null) {
                localObject = new byte[paramByteBuffer.remaining()];

                paramByteBuffer.get((byte[]) localObject);
            }

            return (byte[]) localObject;
        }

        public void encode(ByteBuffer paramByteBuffer, OutputStream paramOutputStream) throws IOException {
            byte[] arrayOfByte = getBytes(paramByteBuffer);
            encode(arrayOfByte, paramOutputStream);
        }

        public String encode(ByteBuffer paramByteBuffer) {
            byte[] arrayOfByte = getBytes(paramByteBuffer);

            return encode(arrayOfByte);
        }

        public void encodeBuffer(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
            byte[] arrayOfByte = new byte[bytesPerLine()];

            encodeBufferPrefix(paramOutputStream);
            for (;;) {
                int j = readFully(paramInputStream, arrayOfByte);
                if (j != 0) {
                    encodeLinePrefix(paramOutputStream, j);
                    for (int i = 0; i < j; i += bytesPerAtom()) {
                        if (i + bytesPerAtom() <= j) {
                            encodeAtom(paramOutputStream, arrayOfByte, i, bytesPerAtom());
                        } else {
                            encodeAtom(paramOutputStream, arrayOfByte, i, j - i);
                        }
                    }
                    encodeLineSuffix(paramOutputStream);
                    if (j < bytesPerLine()) {
                        break;
                    }
                }
            }
            encodeBufferSuffix(paramOutputStream);
        }

        public void encodeBuffer(byte[] paramArrayOfByte, OutputStream paramOutputStream) throws IOException {
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
            encodeBuffer(localByteArrayInputStream, paramOutputStream);
        }

        public String encodeBuffer(byte[] paramArrayOfByte) {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
            try {
                encodeBuffer(localByteArrayInputStream, localByteArrayOutputStream);
            } catch (Exception localException) {
                throw new Error("CharacterEncoder.encodeBuffer internal error");
            }

            return localByteArrayOutputStream.toString();
        }

        public void encodeBuffer(ByteBuffer paramByteBuffer, OutputStream paramOutputStream) throws IOException {
            byte[] arrayOfByte = getBytes(paramByteBuffer);
            encodeBuffer(arrayOfByte, paramOutputStream);
        }

        public String encodeBuffer(ByteBuffer paramByteBuffer) {
            byte[] arrayOfByte = getBytes(paramByteBuffer);

            return encodeBuffer(arrayOfByte);
        }
    }
}