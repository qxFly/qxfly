package fun.qxfly.utils;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAEncrypt {

    /**
     * 随机生成密钥对
     *
     * @return map存公钥秘钥；key:0 -公钥  ；key:1 -私钥
     * @throws NoSuchAlgorithmException
     */
    public static Map<Integer, String> genKeyPair() throws NoSuchAlgorithmException {
        Map<Integer, String> result = new HashMap<>();
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded(), true));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded(), true));
        // 将公钥和私钥保存到Map
        result.put(0, publicKeyString);  //0表示公钥
        result.put(1, privateKeyString);  //1表示私钥
        return result;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 明文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str);
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }


    /**
     * RSA私钥加密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encryptByPrivate(String str, String privateKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey pubKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
//        String outStr = java.util.Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes("UTF-8")));
        return Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
    }

    /**
     * RSA公钥解密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 明文
     * @throws Exception 加密过程中的异常信息
     */
    public static String decryptByPublicKey(String str, String publicKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str);
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey priKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }


//    public static void main(String[] args) throws Exception {
//        //生成公钥和私钥
//        Map<Integer, String> keyMap = genKeyPair();
//        //加密字符串
//        String message = "什么是快乐";
//        System.out.println("随机生成的公钥为:" + keyMap.get(0));
//        System.out.println("随机生成的私钥为:" + keyMap.get(1));
//        String messageEn = encrypt(message, keyMap.get(0));
//        String messageEn2 = decrypt(messageEn, keyMap.get(1));
//        System.out.println(messageEn);
//        System.out.println(messageEn2);
//    }
}

