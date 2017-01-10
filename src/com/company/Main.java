package com.company;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class Main {

    public static final String MY_TEXT = "This is my text";
    public static final int NUMBER_OF_BYTES = 512;

    public static void main(String[] args) {

        BigInteger p = generateBigPrime(NUMBER_OF_BYTES);
        BigInteger q = generateBigPrime(NUMBER_OF_BYTES);

        BigInteger n = calculateModulus(p, q);
        BigInteger phi = calculateEulerFunction(p, q);

        BigInteger e;
        try {
            e = calculateCoprime(phi, n, NUMBER_OF_BYTES);
        }
        catch(CoprimeNotFoundException exc)
        {
            System.err.println("Coprime cannot be found. Exception is \n");
            exc.printStackTrace();
            return;
        }
        BigInteger d = calculateInverseMod(e, phi);

        byte[] encryptedMessage = encryptMessage(MY_TEXT, e, n);

        System.out.println(Arrays.toString(encryptedMessage));
        System.out.println(decryptMessage(encryptedMessage, d, n));
    }


    public static BigInteger generateBigPrime(int numberOfBytes) {
        return BigInteger.probablePrime(numberOfBytes, new Random());
    }

    public static BigInteger calculateModulus(BigInteger number1, BigInteger number2) {
        return number1.multiply(number2);
    }

    public static BigInteger calculateEulerFunction(BigInteger number1, BigInteger number2) {
        BigInteger a1 = number1.subtract(BigInteger.valueOf(1));
        BigInteger a2 = number2.subtract(BigInteger.valueOf(1));
        return a1.multiply(a2);
    }

    public static BigInteger calculateCoprime(BigInteger phi, BigInteger modulus, int numberOfBytes) throws CoprimeNotFoundException {
        BigInteger e = BigInteger.probablePrime(numberOfBytes, new Random());

        while (phi.gcd(e).compareTo(BigInteger.ONE) != 0 && e.compareTo(modulus) < 0) {
            e.add(BigInteger.ONE);
            if (e.compareTo(modulus) >= 0)
                throw new CoprimeNotFoundException("Coprime cannot be found");
        }
        return e;
    }

    public static BigInteger calculateInverseMod(BigInteger number1, BigInteger number2) {
        return number1.modInverse(number2);
    }

    public static BigInteger encryptSingleByte(int a, BigInteger e, BigInteger n) {
        return BigInteger.valueOf(a).modPow(e, n);
    }

    public static BigInteger decryptSingleByte(BigInteger a, BigInteger d, BigInteger n) {
        return a.modPow(d, n);
    }

    public static byte[] encryptMessage(String message, BigInteger e, BigInteger n) {
        return new BigInteger(message.getBytes()).modPow(e, n).toByteArray();
    }

    public static String decryptMessage(byte[] encryptedMessage, BigInteger d, BigInteger n) {
        return new String(new BigInteger(encryptedMessage).modPow(d, n).toByteArray());
    }
}

class CoprimeNotFoundException extends Exception
{
    public CoprimeNotFoundException() {
    }

    public CoprimeNotFoundException(String message) {
        super(message);
    }
}
