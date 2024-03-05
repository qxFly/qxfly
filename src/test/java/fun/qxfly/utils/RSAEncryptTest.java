package fun.qxfly.utils;

import org.junit.jupiter.api.Test;

import java.util.Map;

class RSAEncryptTest {

    @Test
    void genKeyPair() throws Exception {
                //生成公钥和私钥
        Map<Integer, String> keyMap = RSAEncrypt.genKeyPair();
        //加密字符串
        String message = "fly334955";
        System.out.println("随机生成的公钥为:" + keyMap.get(0));
        System.out.println("随机生成的私钥为:" + keyMap.get(1));
        String messageEn = RSAEncrypt.encrypt(message,keyMap.get(0));
        String messageEn2 = RSAEncrypt.decrypt(messageEn,keyMap.get(1));
        System.out.println(messageEn);
        System.out.println(messageEn2);

    }
}