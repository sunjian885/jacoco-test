package com.common.utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    static String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAmmOLsLvqW9puLqvdChcvUFZhtWAxqQ1nP+3uokDxGwWuh3hnJ4nk2vD9QlDQ/deOJPwo0kB2yL4XEe3OJ3rr1wUdcOvYEq7IXNnL98L0LDEHgj4RN9f72XE7h7thOgBCsPh9Eea3QkBa00RYg6VZNq9brioLNWE4QfWxq/QpdQIDAQAB";
    static String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAICaY4uwu+pb2m4uq90KFy9QVmG1YDGpDWc/7e6iQPEbBa6HeGcnieTa8P1CUND9144k/CjSQHbIvhcR7c4neuvXBR1w69gSrshc2cv3wvQsMQeCPhE31/vZcTuHu2E6AEKw+H0R5rdCQFrTRFiDpVk2r1uuKgs1YThB9bGr9Cl1AgMBAAECgYBNrnSA9cGc390CfzibLTQyBUIYhTnU5XvOKWSsp9+4hA0bjoMhNFXsIoA9SuiMRTkGiLq0YcREvB9uygquY1Sw6hXdXT5g5WtO7jOm1TaFQbwMlP6hpZ8RskuLW5jm3WIP2MrN0lPry+zknfBLHnGedrBOzzdWCFwg4257TEL4AQJBAPNrnGXvdWz0OdZB58XFsI4HqvBo890gM2nC7C6+pHBjMuOBkD9gmGvNkME/FP1cPMXXy0aOzVm343JSwzNq8gECQQCHP8ZVaeE5Bwl2U1zhBeg799mXZ8vWz4Hp7Vmq62dNLi/+wAPX3FQCKm+EbmTHQ/AzGvN45CIpsD4fPW/05Y91AkBaNidgH76FAn3syb/7q6gi+vR+5GZ8LNLg/zxIlp6aiCjz57BtzH6wdR6Qf7BntSdQqwjKvWGdPmkslT+CbsABAkEAhXsDmzir90Riqk0L1WmnEchDD5J5MsAJT33YiT9a7GkxJRMMt/XTU2/eL61j+OWsIkPvFtjQfqRaKyrPW7tUIQJAG06KnfaYli7nlI7n6cjpcEkA5iTIsV5bgIjzIsTa1ewad9ywunKox2mqDPpgqMH3CjPfygnHFoNPl0ukfYfcmw==";
    /**
     * RSA 加密   解密
     *
     * @param password   需要加密的数据
     * publicKeyBase64 公钥字符串
     * privateKeyBase64 私钥字符串
     */
    public static String encrypt(String password) throws Exception {

        // 将公钥和私钥的Base64编码字符串转换为PublicKey对象
        PublicKey publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase64)));

        // 使用公钥加密数据
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = encryptCipher.doFinal(password.getBytes());
        //密文作为参数传输到后端时+号变成了空格，"+"替换成"_"
        return Base64.getEncoder().encodeToString(encryptedBytes).replace("+","_");
    }


    public static void main(String[] args) throws Exception{
        String password = "Cc123456";

        // 将公钥和私钥的Base64编码字符串转换为PublicKey和PrivateKey对象
        String publicKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAmmOLsLvqW9puLqvdChcvUFZhtWAxqQ1nP+3uokDxGwWuh3hnJ4nk2vD9QlDQ/deOJPwo0kB2yL4XEe3OJ3rr1wUdcOvYEq7IXNnL98L0LDEHgj4RN9f72XE7h7thOgBCsPh9Eea3QkBa00RYg6VZNq9brioLNWE4QfWxq/QpdQIDAQAB";
        String privateKeyBase64 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAICaY4uwu+pb2m4uq90KFy9QVmG1YDGpDWc/7e6iQPEbBa6HeGcnieTa8P1CUND9144k/CjSQHbIvhcR7c4neuvXBR1w69gSrshc2cv3wvQsMQeCPhE31/vZcTuHu2E6AEKw+H0R5rdCQFrTRFiDpVk2r1uuKgs1YThB9bGr9Cl1AgMBAAECgYBNrnSA9cGc390CfzibLTQyBUIYhTnU5XvOKWSsp9+4hA0bjoMhNFXsIoA9SuiMRTkGiLq0YcREvB9uygquY1Sw6hXdXT5g5WtO7jOm1TaFQbwMlP6hpZ8RskuLW5jm3WIP2MrN0lPry+zknfBLHnGedrBOzzdWCFwg4257TEL4AQJBAPNrnGXvdWz0OdZB58XFsI4HqvBo890gM2nC7C6+pHBjMuOBkD9gmGvNkME/FP1cPMXXy0aOzVm343JSwzNq8gECQQCHP8ZVaeE5Bwl2U1zhBeg799mXZ8vWz4Hp7Vmq62dNLi/+wAPX3FQCKm+EbmTHQ/AzGvN45CIpsD4fPW/05Y91AkBaNidgH76FAn3syb/7q6gi+vR+5GZ8LNLg/zxIlp6aiCjz57BtzH6wdR6Qf7BntSdQqwjKvWGdPmkslT+CbsABAkEAhXsDmzir90Riqk0L1WmnEchDD5J5MsAJT33YiT9a7GkxJRMMt/XTU2/eL61j+OWsIkPvFtjQfqRaKyrPW7tUIQJAG06KnfaYli7nlI7n6cjpcEkA5iTIsV5bgIjzIsTa1ewad9ywunKox2mqDPpgqMH3CjPfygnHFoNPl0ukfYfcmw==";

        PublicKey publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyBase64)));
        PrivateKey privateKey = KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyBase64)));

        // 使用公钥加密数据
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = encryptCipher.doFinal(password.getBytes());
        //密文作为参数传输到后端时+号变成了空格，"+"替换成"_"
        String result =Base64.getEncoder().encodeToString(encryptedBytes).replace("+","_");

        // 使用私钥解密数据
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);

        String decryptedText = new String(decryptedBytes);
        System.out.println("加密后的数据: " + result);
        System.out.println("解密后的数据: " + decryptedText);
    }

}
