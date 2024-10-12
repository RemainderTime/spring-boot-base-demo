package cn.xf.basedemo.common.utils;

import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: xf-boot-base
 * @ClassName RSAUtils
 * @description: 加密工具类
 * @author: xiongfeng
 * @create: 2022-06-20 10:37
 **/
public class RSAUtils {

    //算法类型
    private static final String RSA = "RSA";

    //字符编码类型
    private static final String CHARSET = "UTF-8";

    //加密长度
    private static final int ENCRYPT_SIZE = 1024;


    /**
     * 创建rsa密匙对
     *
     * @return
     */
    private static Map<String, String> createEncryptKey() {

        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance(RSA);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
        kpg.initialize(ENCRYPT_SIZE);
        KeyPair keyPair = kpg.generateKeyPair();
        PublicKey aPublic = keyPair.getPublic();
        String publicKey = Base64.encodeBase64URLSafeString(aPublic.getEncoded());

        PrivateKey aPrivate = keyPair.getPrivate();
        String privateKey = Base64.encodeBase64URLSafeString(aPrivate.getEncoded());

        Map<String, String> map = new HashMap<>();

        map.put("publicKey", publicKey);
        map.put("privateKey", privateKey);

        return map;
    }

    /**
     * 获取ras公匙
     *
     * @param publicKeyStr 公匙加密字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPublicKey getPublicKey(String publicKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decodeBase64URLSafe(publicKeyStr));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 获取ras私匙
     *
     * @param privateKeyStr 私匙加密字符串
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPrivateKey getPrivateKey(String privateKeyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {

        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64URLSafe(privateKeyStr));
        RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    /**
     * 公匙加密
     *
     * @param data      字符串
     * @param publicKey
     * @return
     */
    public static String publicEncrypt(String data, RSAPublicKey publicKey) {

        try {

            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] bytes = cipher.doFinal(data.getBytes());
            return Base64.encodeBase64URLSafeString(bytes);
//            return Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static String privateDecryption(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

//            byte[] bytes = cipher.doFinal(Base64.decodeBase64(data.getBytes(CHARSET)));
//            return new String(bytes);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.decodeBase64(data.getBytes(CHARSET), 0, data.getBytes(CHARSET).length), privateKey.getModulus().bitLength()), CHARSET);
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    //rsa切割解码  , ENCRYPT_MODE,加密数据   ,DECRYPT_MODE,解密数据
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;  //最大块
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        try {
            while (datas.length > offSet) {
                if (datas.length - offSet > maxBlock) {
                    //可以调用以下的doFinal（）方法完成加密或解密数据：
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                } else {
                    buff = cipher.doFinal(datas, offSet, datas.length - offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
        byte[] resultDatas = out.toByteArray();
        IOUtils.closeQuietly(out);
        return resultDatas;
    }

    public static void main(String[] args) {

//        Map<String, String> encryptKey = createEncryptKey();
//        String publicKey = encryptKey.get("publicKey");
//        String privateKey = encryptKey.get("privateKey");
//
//        System.out.println("公匙加密串：" + publicKey);
//        System.out.println("私匙加密串：" + privateKey);
//
//        System.out.println();
//
        String data = "data";
        //加密
        try {
            String s = publicEncrypt(data, getPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC_F5UQC1QWsu3QsESQBz9M-GDA9Atm0qVSvwIsy568lyRLi-nq3VvvnmgrlL4yTbngFzyfb2Dn35cNCHsBvIaGuCY3_PpzPqMzVpxr2QlEkhEX9atnJQ1rWexS8QeZtPjpiIwoQrChTzXjD_sYUkDrqSykFplyivf0NSO2WqCBdwIDAQAB"));
            System.out.println("加密后密文：" + s);

//            String ss = "bPrP3VQpVNj7jxzSvVRQQpOCzg4c9HAMd/Sesda0SOxmWbNzP8SnhayV2H9Jpih2sf26O8dOqiNE7V1u5NPgQBIPi6LqX2QiFTjynVLxQBUmISfmQ2Q6K3sjHBIRIhuZPrXijw7CextUUQwzh4VvEVkjyaUnqlMXVRkUGlgqP7M=";
//            String privateKey1 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALiJJ6RPMMh-ETrmppOG7JKINPSFaaZoHjzZkyQl3AcfrpKMmH82j_Pxl4mPvvgKtbR20N-88-nJLT4v4aOz9XYVl5ruE59SsJl_T8YqN-i8L8KH8Wptd0_ee7nDhF4-OGEi-o330daFv20eLpboy6nDkWLmLihKC0jEZWK8MLZzAgMBAAECgYAEhO9gmcPjFRtM6vsnX8WJbSaG2oGU3rXm3Zk56Gd0ETWQRzsw2mA6JC-G4etWXcTHb6V75T-_-PpPrJKFFNItEH-WFRS36xneomycxRG1YTfK1SsGLGF0BV3bLVZx8cQz7VsBY4vqbRCSKtcOZBJpnxI6iHAv07i8w34F6qjfsQJBAORnKUuJQ_GsHHBPT1VhMYjXVepAfTrWtCzRQ648KavbHLAGaRIhX10uj-hAhZLafDqQF8Y7T7GHTlasRL9ubWsCQQDO1R3KScJJSR3KDsnSsF0YCw7V28cr_OVAwiPoro90Me6MUz9yKV88gQlTuJkNFMuu_YdPXYKjlzNVg0zFmtUZAkEAoe9mPtDeZD0TmKkSZUVYul1543C_mPTan5_qrWCoZtkd2MtiuWEB3O4DR7ZfPcQ8KcU5pektUn_NEfRndZYUawJBAJfydOoxeawBLQNODfLcYefR59owlYe5SGpktaCw7O596DPqzId_4Vk_qqx4xueXSXOLCabCmcC4yZue0_2vm7ECQQDLrzXL-BpSqxbvtE0gNKcgaSkEUSOh1QmQFPCHERsOBxcflM6ej71STKglB21JD9m6tM2RySgbtUx4TfOuJTek";
//            String s1 = privateDecryption(ss, getPrivateKey(privateKey1));
//            System.out.println("解密后明文：" + s1);

        } catch (Exception e) {
            throw new IllegalArgumentException();
        }


    }


}
