/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilidades;

import java.util.Random;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 *
 * @author Rafa
 */
public class GenerarCadenaAlfanumerica {

    private static final int longPass = 8;

    //Si la longitud es un numero impar, se convertira en numero par (+1 digito)
    /**
     * Metodo que retorna una cadena alfanumerica de la longitud pasada por
     * parametro
     *
     * @param longitud
     * @return
     */
    public static String getCadena() {
        int longitud = longPass;
        if (longitud % 2 != 0) {
            longitud += 1;
        }
        String cadenaAleatoria = "";
        long milis = new java.util.GregorianCalendar().getTimeInMillis();
        int date = new java.util.GregorianCalendar().getTime().getDate();
        Random r = new Random(milis+date);
        int i = 0;
        while (i < longitud / 2) {
            char c = (char) r.nextInt(255);
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                cadenaAleatoria += c;
                i++;
            }
        }
        i = 0;
        while (i < longitud / 2) {
            char c = (char) r.nextInt(255);
            if ((c >= '0' && c <= '9')) {
                cadenaAleatoria += c;
                i++;
            }
        }

        return cadenaAleatoria;
    }

    public static boolean validatePassword(String originalPassword, String storedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);

        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;

        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }

        return diff == 0;
    }

    public static String generateStorngPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt().getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);

    }

    private static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt.toString();
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /*public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String originalPassword = "password";
        String generatedSecuredPasswordHash = generateStorngPasswordHash(originalPassword);
        System.out.println(generatedSecuredPasswordHash);

        boolean matched = validatePassword("password", generatedSecuredPasswordHash);
        System.out.println(matched);

        System.out.println("\n ---------- \n");

        matched = validatePassword("password1", generatedSecuredPasswordHash);
        System.out.println(matched);
        
        System.out.println(getCadena());
    }*/
}
