/*
 ******************************************************************************
 * Created by CrackStation.net on 2017.08.01
 * Copyright © 2017 CrackStation.net. Free reuse with citation of source.
 * Website: https://crackstation.net/hashing-security.htm
 * Code Downloaded From: 
 * https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java
 * Documentation by Osman Balci on 2021-02-06
 ******************************************************************************
 */
package edu.vt.globals;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/*
=======================================================================
|   This class implements Salting and Hashing a Password for secure   |
|   storage and retrieval with Key Stretching using the algorithm     |
|   called Password-Based Key Derivation Function 2 (PBKDF2) with     |
|   Hash-based Message Authentication Code (HMAC)                     |
|   Secure Hash Algorithm 1 (SHA-1): "PBKDF2WithHmacSHA1"             |
=======================================================================
 */
public class Password {

    // @SuppressWarnings("serial") suppresses serialVersionUID warning messages
    @SuppressWarnings("serial")
    static public class InvalidHashException extends Exception {

        public InvalidHashException(String message) {
            super(message);
        }

        public InvalidHashException(String message, Throwable source) {
            super(message, source);
        }
    }

    @SuppressWarnings("serial")
    static public class CannotPerformOperationException extends Exception {

        public CannotPerformOperationException(String message) {
            super(message);
        }

        public CannotPerformOperationException(String message, Throwable source) {
            super(message, source);
        }
    }

    /*
     Object-Oriented Paradigm (OOP) Note: In OOP, we have:
    
        - Class Methods   (typed with the "static" keyword")
        - Class Variables (typed with the "static" keyword")
        - Instance Methods
        - Instance Variables
    
    A class method cannot access instance variables, but it can access class variables.
    
    All of the methods in this class are Class Methods.
    All of the variables in this class are Class Variables.
    
    A class method can easily be accessed by specifying ClassName.ClassMethodName
    in any other class in your project without Injecting an object reference
    because the ClassName is the object reference of that class.
    
    -----------------------------------------------------------------------------------

     PBKDF2 (Password-Based Key Derivation Function 2) is a key derivation
     function with a sliding computational cost, aimed to reduce the vulnerability
     of encrypted keys to brute force attacks. [Wikipedia]
    
     In cryptography, SHA-1 (Secure Hash Algorithm 1) is a cryptographic hash function 
     which takes an input and produces a 160-bit (20-byte) hash value known as a message 
     digest - typically rendered as a hexadecimal number, 40 digits long. [Wikipedia]
     */
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";

    // These constants may be changed without breaking existing hashes.
    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 18;

    /*
    PBKDF2_ITERATIONS is used to implement Key Stretching:
    
    The idea is to make the hash function very slow, so that even with a fast GPU or 
    custom hardware, dictionary and brute-force attacks are too slow to be worthwhile. 
    The goal is to make the hash function slow enough to impede attacks, but still 
    fast enough to not cause a noticeable delay for the user.
     */
    public static final int PBKDF2_ITERATIONS = 64000;

    /*
     The salted hashed password string stored and retrieved from the
     database consists of the following parts (parameters or sections):
                0                1              2        3      4
        "algorithmName":"PBKDF2_ITERATIONS":"hashSize":"salt":"hash"
     */
    public static final int HASH_ALGORITHM_INDEX = 0;
    public static final int ITERATION_INDEX = 1;
    public static final int HASH_SIZE_INDEX = 2;
    public static final int SALT_INDEX = 3;
    public static final int PBKDF2_INDEX = 4;

    // Number of parts (parameters or sections) of the salted hashed password string
    public static final int HASH_SECTIONS = 5;

    /*
    -----------------------------------------------------------------------------
     Create a Salted Hashed password String from the user-entered password String
    -----------------------------------------------------------------------------
     */
    public static String createHash(String password)
            throws CannotPerformOperationException {
        return createHash(password.toCharArray());
    }

    public static String createHash(char[] password)
            throws CannotPerformOperationException {

        //-----------------------
        // Generate a Random Salt
        //-----------------------
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        //---------------------------------------------------------
        // Hash the Given Password with the Randomly Generated Salt
        //---------------------------------------------------------
        byte[] hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        int hashSize = hash.length;

        /*
         The salted hashed password string stored and retrieved from the
         database consists of the following parts (parameters or sections):
                    0                1              2        3      4
            "algorithmName":"PBKDF2_ITERATIONS":"hashSize":"salt":"hash"
         */
        String parts = "sha1:"
                + PBKDF2_ITERATIONS
                + ":" + hashSize
                + ":"
                + toBase64(salt)
                + ":"
                + toBase64(hash);
        /*
         Return a String containing the parts (parameters or sections)
         to be securely stored into the database and retrieved from.
         */
        return parts;
    }

    /*
    --------------------------------------------------------
     ***    Validate the password entered by the user    ***
     password    = password entered by the user as a String
     correctHash = String containing the parts created above
    --------------------------------------------------------
     */
    public static boolean verifyPassword(String password, String correctHash)
            throws CannotPerformOperationException, InvalidHashException {
        return verifyPassword(password.toCharArray(), correctHash);
    }

    public static boolean verifyPassword(char[] password, String correctHash)
            throws CannotPerformOperationException, InvalidHashException {

        // Decode the hash into its parts (parameters or sections):
        String[] params = correctHash.split(":");
        if (params.length != HASH_SECTIONS) {
            throw new InvalidHashException(
                    "Fields are missing from the password hash."
            );
        }

        // Currently, Java only supports SHA1.
        if (!params[HASH_ALGORITHM_INDEX].equals("sha1")) {
            throw new CannotPerformOperationException(
                    "Unsupported hash type."
            );
        }

        int iterations = 0;
        try {
            iterations = Integer.parseInt(params[ITERATION_INDEX]);
        } catch (NumberFormatException ex) {
            throw new InvalidHashException(
                    "Could not parse the iteration count as an integer.",
                    ex
            );
        }

        if (iterations < 1) {
            throw new InvalidHashException(
                    "Invalid number of iterations. Must be >= 1."
            );
        }

        byte[] salt = null;
        try {
            // Convert the Salt string argument into an array of bytes.
            salt = fromBase64(params[SALT_INDEX]);
        } catch (IllegalArgumentException ex) {
            throw new InvalidHashException(
                    "Base64 decoding of salt failed.",
                    ex
            );
        }

        byte[] hash = null;
        try {
            // Convert the Hash string argument into an array of bytes.
            hash = fromBase64(params[PBKDF2_INDEX]);
        } catch (IllegalArgumentException ex) {
            throw new InvalidHashException(
                    "Base64 decoding of pbkdf2 output failed.",
                    ex
            );
        }

        int storedHashSize = 0;
        try {
            storedHashSize = Integer.parseInt(params[HASH_SIZE_INDEX]);
        } catch (NumberFormatException ex) {
            throw new InvalidHashException(
                    "Could not parse the hash size as an integer.",
                    ex
            );
        }

        if (storedHashSize != hash.length) {
            throw new InvalidHashException(
                    "Hash length doesn't match stored hash length."
            );
        }

        // Compute the hash of the provided user-entered password, 
        // using the same salt, iteration count, and hash length.
        byte[] testHash = pbkdf2(password, salt, iterations, hash.length);

        return slowEquals(hash, testHash);
    }

    /*
     Compare the hashes in constant time. The password is correct if both hashes match.
     
     To implement Key Stretching, the comparison is done in a slow manner, slow enough
     to impede attacks, but still fast enough to not cause a noticeable delay for the user.
    
     slowEquals is a method created to prevent against timing attacks. This method assures
     that the attacker cannot determine how long it took for the password to fail. 
     It iterates through all values in the byte array regardless if they are equal or not. 
     This prevents the attacker from having enough information to formulate an attack.
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /*
     PBKDF2 (Password-Based Key Derivation Function 2) is a key derivation
     function with a sliding computational cost, aimed to reduce the vulnerability
     of encrypted keys to brute force attacks. 
    
     PBKDF2 applies a pseudorandom function, such as Hash-based Message Authentication Code (HMAC), 
     to the input password along with a Salt value and repeats the process many times to produce 
     a derived key, which can then be used as a cryptographic key in subsequent operations. The added 
     computational work makes password cracking much more difficult, and is known as Key Stretching.
     [Wikipedia]
    
     See the SecretKeyFactory class documentation at:
     https://docs.oracle.com/javase/7/docs/api/javax/crypto/SecretKeyFactory.html
     */
    private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int bytes)
            throws CannotPerformOperationException {
        try {
            PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();

        } catch (NoSuchAlgorithmException ex) {
            throw new CannotPerformOperationException(
                    "Hash algorithm not supported.",
                    ex
            );
        } catch (InvalidKeySpecException ex) {
            throw new CannotPerformOperationException(
                    "Invalid key spec.",
                    ex
            );
        }
    }

    // Converts the String argument into an array of bytes.
    private static byte[] fromBase64(String hex)
            throws IllegalArgumentException {
        return DatatypeConverter.parseBase64Binary(hex);
    }

    // Converts an array of bytes into a String.
    private static String toBase64(byte[] array) {
        return DatatypeConverter.printBase64Binary(array);
    }

}
