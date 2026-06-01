package cn.xf.basedemo.common.utils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static final String RSA = "RSA";
    private static final String RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    private static final int KEY_SIZE = 1024;

    private static Map<String, String> createEncryptKey() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance(RSA);
            kpg.initialize(KEY_SIZE);
            KeyPair keyPair = kpg.generateKeyPair();

            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            Map<String, String> map = new HashMap<>();
            map.put("publicKey", Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            map.put("privateKey", Base64.getEncoder().encodeToString(privateKey.getEncoded()));
            return map;
        } catch (Exception e) {
            throw new IllegalArgumentException("create RSA key pair failed", e);
        }
    }

    public static RSAPublicKey getPublicKey(String publicKeyStr) throws InvalidKeySpecException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodeBase64(publicKeyStr));
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidKeySpecException("invalid RSA public key", e);
        }
    }

    public static RSAPrivateKey getPrivateKey(String privateKeyStr) throws InvalidKeySpecException {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodeBase64(privateKeyStr));
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidKeySpecException("invalid RSA private key", e);
        }
    }

    public static String publicEncrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encrypted = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE,
                    data.getBytes(StandardCharsets.UTF_8), publicKey.getModulus().bitLength());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("RSA encrypt failed", e);
        }
    }

    public static String privateDecryption(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decrypted = rsaSplitCodec(cipher, Cipher.DECRYPT_MODE,
                    decodeBase64(data), privateKey.getModulus().bitLength());
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("RSA decrypt failed", e);
        }
    }

    private static byte[] decodeBase64(String data) {
        String normalizedData = data.replaceAll("\\s", "");
        try {
            return Base64.getDecoder().decode(normalizedData);
        } catch (IllegalArgumentException e) {
            return Base64.getUrlDecoder().decode(normalizedData);
        }
    }

    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = opmode == Cipher.DECRYPT_MODE ? keySize / 8 : keySize / 8 - 11;
        int offset = 0;
        int index = 0;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            while (datas.length > offset) {
                int inputLen = Math.min(datas.length - offset, maxBlock);
                byte[] buffer = cipher.doFinal(datas, offset, inputLen);
                out.write(buffer, 0, buffer.length);
                index++;
                offset = index * maxBlock;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("RSA block codec failed, maxBlock=" + maxBlock, e);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> encryptKey = createEncryptKey();
        String publicKey = encryptKey.get("publicKey");
        String privateKey = encryptKey.get("privateKey");

        String data = "{\"account\":\"admin\",\"pwd\":\"123456\"}";
        String encrypted = publicEncrypt(data, getPublicKey(publicKey));
        String decrypted = privateDecryption(encrypted, getPrivateKey(privateKey));

        System.out.println("publicKey: " + publicKey);
        System.out.println("privateKey: " + privateKey);
        System.out.println("encrypted: " + encrypted);
        System.out.println("decrypted: " + decrypted);
    }
}
