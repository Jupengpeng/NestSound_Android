package com.xilu.wybz.http.rsa;

import android.support.v4.content.res.TypedArrayUtils;
import android.util.Log;
import com.xilu.wybz.utils.Base64Utils;
import org.apache.commons.lang3.ArrayUtils;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
/**
 * @author June 2016/4/28
 */
public final class RSAUtils {
    public final static String CHARSET = "UTF-8";
    public final static String RSA_PUCLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEIGwePp63orI08aZ+vWKgUJJcN7HDrgVpX9pja2465tzrbdWLif26RhiIn2lVz6QuEWhJwlM7cTMYYH1bacy4Z7e1eOIH6hlp3/TKiZMhjNJbyafuQjBkvs4sQVOaW/G4iKu5W+SFcYdCOiTo6vZe6KD9IbBXaL1P3BkaMHq2kQIDAQAB";

    /**
     * 随机生成RSA密钥对(默认密钥长度为1024)
     *
     * @return
     */
    public static KeyPair generateRSAKeyPair() {
        return generateRSAKeyPair(1024);
    }

    /**
     * 随机生成RSA密钥对
     *
     * @param keyLength 密钥长度，范围：512～2048<br>
     *                  一般1024
     * @return
     */
    public static KeyPair generateRSAKeyPair(int keyLength) {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(keyLength);
            return kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用公钥加密 <br>
     * 每次加密的字节数，不能超过密钥的长度值减去11
     *
     * @param dataStr 需加密数据的byte数据
     * @return 加密后的byte型数据
     */
    public static String encryptByPublicKey(String dataStr) {
        try {
            byte[] data = dataStr.getBytes(CHARSET);
            PublicKey publicKey = loadPublicKey(RSA_PUCLIC_KEY);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            byte[] dataRturn = null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i += 100) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 100));
                sb.append(new String(doFinal));
                dataRturn = ArrayUtils.addAll(dataRturn, doFinal);
            }
            return encodeConvert(dataRturn);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Exception", e.toString());
            return "";
        }
    }

    /**
     * 用公钥解密
     *
     * @param dataStr 经过encryptedData()加密返回的byte数据
     * @return
     */
    public static String decryptByPublicKey(String dataStr) {
        try {
            byte[] data = decodeConvert(dataStr);
            PublicKey publicKey = loadPublicKey(RSA_PUCLIC_KEY);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < data.length; i += 128) {
                byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(data, i, i + 128));
                sb.append(new String(doFinal));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 从字符串中加载公钥
     *
     * @param publicKeyStr 公钥数据字符串
     * @throws Exception 加载公钥时产生的异常
     */
    public static PublicKey loadPublicKey(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64Utils.decode(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("公钥非法");
        } catch (NullPointerException e) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从字符串中加载私钥<br>
     * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
     *
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64Utils.decode(privateKeyStr);
//             X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException e) {
            throw new Exception("私钥非法");
        } catch (NullPointerException e) {
            throw new Exception("私钥数据为空");
        }
    }


    /**
     * 将加密内容的 Base64 编码转换为二进制内容
     *
     * @param str
     * @return
     */
    public static byte[] decodeConvert(String str) {
        return Base64Utils.decode(str);
    }

    /**
     * 对加密后的二进制结果转换为 Base64 编码
     *
     * @param bytes
     * @return
     */
    public static String encodeConvert(byte[] bytes) {
        return Base64Utils.encode(bytes);
    }

}
