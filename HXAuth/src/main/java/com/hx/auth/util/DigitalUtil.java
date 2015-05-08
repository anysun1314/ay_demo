package com.hx.auth.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import sun.misc.BASE64Decoder;

/**
 * Created by IntelliJ IDEA.
 * User: Renyulin
 * Date: 2011-12-29
 * Time: 16:19:16
 * To change this template use File | Settings | File Templates.
 */
public class DigitalUtil {

    /**
     * base64转码
     */
    public static String base64Encode(byte[] be) {
        /*BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(be);*/
    	return new String(Base64.encodeBase64Chunked(be));
    }

    public static String base64EncodeWithOutSpace(byte[] be) {
		byte[] b = new org.apache.commons.codec.binary.Base64()
				.encodeBase64Chunked(be);
		StringBuffer ss = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			if (b[i] == 13) {
				continue;
			} else {
				ss .append((char) b[i]);
			}
		}
		return ss.toString();
	}

    /**
     * base64转码
     */
    public static byte[] base64Decoder(String source) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(source);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /**
     * 对字符串进行摘要  摘要算法是SHA1
     *
     * @param s 摘要原数据
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getMessageDigest(String s)
            throws NoSuchAlgorithmException {
        return getMessageDigest(s, "SHA1");
    }


    /**
     * 对字符串进行摘要
     *
     * @param s 摘要源数据
     * @param algorithm 摘要算法
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getMessageDigest(String s, String algorithm)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        md.update(s.getBytes());
        byte[] bb = md.digest();
        return bb;
    }

    /**
     * 对二进制数组进行摘要
     *
     * @param s
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getMessageDigest(byte[] bs)
            throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(bs);
        byte[] bb = md.digest();
        return bb;
    }

    /**
     * 对源数据进行签名
     *
     * @param source
     * @param signatureAlgorithm
     * @param privateKey
     * @return
     */
    public static byte[] getSignatureValue(byte[] source, String signatureAlgorithm, PrivateKey privateKey) {
        try {
            //加載bc供应商，为支持RSA-PSS-Default签名算法
//            Security.addProvider(new BouncyCastleProvider());
            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initSign(privateKey);
            sig.update(source);
            return sig.sign();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    /**
     * 验证签名
     *
     * @param source
     * @param signatureValue
     * @param signatureAlgorithm
     * @param publicKey
     * @return
     */
    public static boolean validateSignature(byte[] source, byte[] signatureValue, String signatureAlgorithm, PublicKey publicKey) {
        try {
            //加載bc供应商，为支持RSA-PSS-Default签名算法
//            Security.addProvider(new BouncyCastleProvider());
            Signature sig = Signature.getInstance("SHA1WithRSA");
            sig.initVerify(publicKey);
            sig.update(source);
            if (sig.verify(signatureValue)) {
                return true;
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SignatureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
