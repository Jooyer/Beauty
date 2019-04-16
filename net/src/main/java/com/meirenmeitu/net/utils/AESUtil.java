package com.meirenmeitu.net.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/** https://www.jianshu.com/u/b6913cfbd3ac
 * Desc: AES 对称加密解密
 * Author: Jooyer
 * Date: 2018-11-12
 * Time: 10:01
 */
public class AESUtil {

    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES = "AES";


    /**
     * 生成密钥
     *
     * @param encodeRules 密钥生成规则
     *  return Base64编码返回密钥
     */
    public static String generateSecretKey(String encodeRules) {

        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance(AES);
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            keygen.init(256, new SecureRandom(encodeRules.getBytes()));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            // 生成的秘钥转换成Base64编码,加、解密时需要用Base64还原秘钥
            return Base64Util.encode(raw);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 加密
     *
     * @param plaintext 明文
     * @param key       秘钥
     * @return Base64编码的密文
     */
    public static String encrypt(String plaintext, String key) {
        try {
            // 1.Base64还原秘钥
            byte[] keyBytes = Base64Util.decode(key);
            // 2.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = plaintext.getBytes("UTF-8");
            // 3.还原密钥对象
            SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
            // 4.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            // 第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作,CBC模式需要添加一个参数IvParameterSpec，ECB模式则不需要
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            // 5.加密
            byte[] result = cipher.doFinal(byte_encode);
            // 生成的密文转换成Base64编码出文本,解密时需要用Base64还原出密文
            return Base64Util.encode(result);
        } catch (UnsupportedEncodingException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | InvalidKeyException
                | BadPaddingException
                | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 解密
     *
     * @param cipherText 密文
     * @param key        秘钥
     * @return 明文
     */
    public static String decrypt(String cipherText, String key) {
        try {
            // 1.Base64还原秘钥
            byte[] keyBytes = Base64Util.decode(key);
            // 2.还原密钥对象
            SecretKey secretKey = new SecretKeySpec(keyBytes, AES);
            // 3.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            // 第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作,CBC模式需要添加一个参数IvParameterSpec，ECB模式则不需要
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(new byte[cipher.getBlockSize()]));
            // 4.Base64还原密文
            byte[] cipherBytes = Base64Util.decode(cipherText);
            // 5.解密
            byte[] result = cipher.doFinal(cipherBytes);
            return new String(result, "UTF-8");
        } catch (UnsupportedEncodingException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | InvalidKeyException
                | BadPaddingException
                | NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return "";
    }


}
