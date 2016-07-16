package com.taxiapp.net.api;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.Base64Codec;

/**
 * Created by Amit S on 16/11/15.
 */
public class JWTUtils {

    public static String decrypt(String encData) throws Exception {

        String key = "*TM@6S3!Un^ay_Q-7s";
        return Jwts.parser().setSigningKey(Base64Codec.BASE64.encode(key.getBytes())).parse(encData).getBody().toString();
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }
}
