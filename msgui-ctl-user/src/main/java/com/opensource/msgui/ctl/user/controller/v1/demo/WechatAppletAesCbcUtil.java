package com.opensource.msgui.ctl.user.controller.v1.demo;


import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;

/**
 * 微信小程序 加密 解密 获取unionid
 * AES-128-CBC 加密方式
 * 注：
 * AES-128-CBC可以自己定义“密钥”和“偏移量“。
 * AES-128是jdk自动生成的“密钥”。
 */
public class WechatAppletAesCbcUtil {


    static {
        //BouncyCastle是一个开源的加解密解决方案，主页在http://www.bouncycastle.org/
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * AES解密
     *
     * @param data           //密文，被加密的数据
     * @param key            //秘钥
     * @param iv             //偏移量
     * @param encodingFormat //解密后的结果需要进行的编码
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key, String iv, String encodingFormat) {
        System.out.println("WechatAppletAesCbcUtil_start:=====" + data + "key: " + key + " iv:" + iv);

        //被加密的数据
        byte[] dataByte = Base64.decodeBase64(data);
        //加密秘钥
        byte[] keyByte = Base64.decodeBase64(key);
        //偏移量
        byte[] ivByte = Base64.decodeBase64(iv);


        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");

            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));

            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化

            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, encodingFormat);
                return result;
            }
            return null;
        } catch (NoSuchAlgorithmException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            System.out.println("异常1：" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    public static String decryptPhone(byte[] sessionkey, byte[] iv, byte[] encryptedData, String encodingFormat)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(sessionkey, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] resultByte = cipher.doFinal(encryptedData);
        if (null != resultByte && resultByte.length > 0) {
            String result = null;
            try {
                result = new String(resultByte, encodingFormat);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return result;
        }
        return null;
    }

}

