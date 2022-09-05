package com.github.zmzhoustar.webshell.utils;


import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;

/**
 * AES加解密工具
 * java使用AES加密解密 AES-128-ECB加密
 * 与mysql数据库aes加密算法通用
 * 数据库aes加密解密
 * -- 加密
 * SELECT to_base64(AES_ENCRYPT('www.gowhere.so','jkl;POIU1234++=='));
 * -- 解密
 * SELECT AES_DECRYPT(from_base64('Oa1NPBSarXrPH8wqSRhh3g=='),'jkl;POIU1234++==');
 *
 * @author scott
 * @date 2022/04/15 17:39
 **/
@Slf4j
public class AESUtils {
    private static final String CIPHER = "AES/ECB/PKCS7Padding";
    private static final String SECRET = "AES";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     */
    public static String encrypt(String sSrc, String sKey) {
        try {
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(raw, SECRET));
            //"算法/模式/补码方式"
            byte[] encrypted = cipher.doFinal(sSrc.getBytes(StandardCharsets.UTF_8));
            //此处使用BASE64做转码功能，同时能起到2次加密的作用。
            return new BASE64Encoder().encode(encrypted);
        } catch (Exception ex) {
            log.error("encrypt failed.", ex);
            return null;
        }
    }

    /**
     * 解密
     */
    public static String decrypt(String sSrc, String sKey) {
        try {
            byte[] raw = sKey.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(raw, SECRET));
            //先用base64解密
            byte[] original = cipher.doFinal(new BASE64Decoder().decodeBuffer(sSrc));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            log.error("decrypt failed.", ex);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String cKey = "ws9ybUMn4F81t5oPKqJrqLKxERaYAS12";
        // 需要加密的字串
        String cSrc = "Zp@940701";
        log.warn(cSrc);
        // 加密
        String enString = AESUtils.encrypt(cSrc, cKey);
        log.warn("加密后的字串是：" + enString);

        // 解密
        String deString = AESUtils.decrypt(enString, cKey);
        log.warn("解密后的字串是：" + deString);
        //qDE3qfdpeJ333Pf3mRaDdw==
    }
}
