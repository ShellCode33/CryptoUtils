package fr.iut.model;

import com.sun.org.apache.xml.internal.security.signature.InvalidSignatureValueException;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Base64;

/**
 * Created by shellcode on 2/10/17.
 */
public class RSA {

    int mKeySize;
    BigInteger mPublicKey = null;
    BigInteger mPrivateKey = null;
    BigInteger mMod = null;


    public RSA(int key_size) {
        if(key_size != 512 && key_size != 1024 && key_size != 2048 && key_size != 4096)
            throw new IllegalArgumentException("Supported key size : 512, 1024, 2048, 4096");

        mKeySize = key_size;
    }

    public void generateKeys() {

        System.out.println("Generating keys...");

        SecureRandom random = new SecureRandom();
        BigInteger p = new BigInteger(mKeySize / 2 - 1, 100, random);
        BigInteger q = new BigInteger(mKeySize / 2 - 1, 100, random);
        mMod = p.multiply(q);

        BigInteger phi_n = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e;

        do {
            e = new BigInteger(mKeySize-1, random);
        }while(e.compareTo(BigInteger.ONE) != 1 || e.compareTo(phi_n) != -1 || !e.gcd(phi_n).equals(BigInteger.ONE));

        BigInteger d = e.modInverse(phi_n);

        System.out.println("p = " + p.toString() + "\n");
        System.out.println("q = " + q.toString() + "\n");
        System.out.println("phi(n) = " + phi_n.toString() + "\n");
        System.out.println("e = " + e.toString() + "\n");
        System.out.println("d = " + d.toString() + "\n");

        mPublicKey = e;
        mPrivateKey = d;

        System.out.println("Generation completed !\n");

    }

    public BigInteger getPrivateKey() {
        return mPrivateKey;
    }

    public void setPublicKey(String b64PublicKey) {
        mPublicKey = new BigInteger(Base64.getDecoder().decode(b64PublicKey));
    }

    public void setPrivateKey(String b64PrivateKey) {
        mPrivateKey = new BigInteger(Base64.getDecoder().decode(b64PrivateKey));
    }

    public void setKeySize(int size) {
        mKeySize = size;
    }

    public void setModulus(String b64Modulus) {
        mMod = new BigInteger(Base64.getDecoder().decode(b64Modulus));
    }

    public String getFormatedPrivateKey() {

        return      "<PrivateKey>\n" +
                "    <Exponent>" +
                Base64.getEncoder().encodeToString(mPrivateKey.toByteArray()) +
                "</Exponent>\n" +

                "    <Modulus>" +
                Base64.getEncoder().encodeToString(mMod.toByteArray()) +
                "</Modulus>\n" +
                "</PrivateKey>";
    }

    public BigInteger getPublicKey() {
        return mPublicKey;
    }

    public String getFormatedPublicKey() {
        return      "<PublicKey>\n" +
                "    <Exponent>" +
                Base64.getEncoder().encodeToString(mPublicKey.toByteArray()) +
                "</Exponent>\n" +

                "    <Modulus>" +
                Base64.getEncoder().encodeToString(mMod.toByteArray()) +
                "</Modulus>\n" +
                "</PublicKey>";
    }

    public void savePrivateKeyToFile(String path) {
        saveToFile(path, getFormatedPrivateKey());
    }

    public void savePublicKeyToFile(String path) {
        saveToFile(path, getFormatedPublicKey());
    }

    private void saveToFile(String path, String content) {
        try {
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            writer.print(content);
            writer.close();
        }

        catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public BigInteger getMod() {
        return mMod;
    }

    //retourne de la base 64
    public String encode(String message, BigInteger public_key, BigInteger mod) {

        if(message == null || public_key == null || mod == null)
            throw new InvalidParameterException("parameters can't be null");

        byte msg_bytes[];

        try {
            message = Normalizer.normalize(message, Normalizer.Form.NFD); //fix bugs d'accents
            msg_bytes = message.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        int nb_sub_messages = (int)Math.ceil((double)msg_bytes.length / (mKeySize/8-1));
        System.out.println("" + nb_sub_messages + " sub messages");

        byte [][] blocks = new byte[nb_sub_messages][];

        int copied_bytes = 0;
        int i = 0;
        for(; i < nb_sub_messages-1; i++) {
            byte[] msg_block = Arrays.copyOfRange(msg_bytes, i * mKeySize/8 - i, (i+1) * mKeySize/8 - i - 1);
            copied_bytes += msg_block.length;
            BigInteger message_integer = new BigInteger(msg_block);
            BigInteger cipher = message_integer.modPow(public_key, mod);
            byte [] cipher_bytes = cipher.toByteArray();
            byte [] block = new byte[mKeySize/8];

            for(int j = cipher_bytes.length-1; j >= 0; j--)
                block[j] = cipher_bytes[j];

            blocks[i] = block;
        }

        if(msg_bytes.length - copied_bytes > 0) {
            byte[] msg_block = Arrays.copyOfRange(msg_bytes, copied_bytes, msg_bytes.length);
            BigInteger message_integer = new BigInteger(msg_block);
            BigInteger cipher = message_integer.modPow(public_key, mod);
            byte [] cipher_bytes = cipher.toByteArray();
            byte [] block = new byte[mKeySize/8];

            for(int j = cipher_bytes.length-1; j >= 0; j--)
                block[j] = cipher_bytes[j];

            blocks[i] = block;
        }

        byte [] inline_blocks = new byte[nb_sub_messages * mKeySize / 8];

        int index = 0;
        for(byte [] block : blocks) {
            for(i = 0; i < block.length; i++)
                inline_blocks[index++] = block[i];
        }

        try {
            return new String(Base64.getEncoder().encode(inline_blocks), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decode(String b64, BigInteger private_key, BigInteger mod) {

        if(b64 == null || private_key == null || mod == null)
            throw new InvalidParameterException("parameters can't be null");

        byte bytes[];
        try {
            bytes = Base64.getDecoder().decode(b64.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        int nb_blocks = (bytes.length * 8) / mKeySize;

        for(int i = 0; i < nb_blocks; i++) {
            try {
                BigInteger cipher_integer = new BigInteger(Arrays.copyOfRange(bytes, i * mKeySize/8, (i+1) * mKeySize/8));
                byte [] plaintext_bytes = cipher_integer.modPow(private_key, mod).toByteArray();
                stringBuilder.append(new String(plaintext_bytes, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    public String sign(String b64Cipher, String hashAlgorithm, BigInteger private_key, BigInteger mod) {
        BigInteger cipher = new BigInteger(Base64.getDecoder().decode(b64Cipher));
        String cipherString = cipher.toString();
        String cipherHash = hash(cipherString, hashAlgorithm);
        String result = cipherString + ":" + cipherHash;
        return encode(result, private_key, mod); //we use the private key as a public key in order to sign the message
    }

    public String checkSignatureAndReturnUnsigned(String b64Cipher, String hashAlgorithm, BigInteger public_key, BigInteger mod) throws InvalidSignatureValueException {

        String content = decode(b64Cipher, public_key, mod); //we use the public key to unsign
        String [] parts = content.split(":");

        if(parts.length != 2)
            throw  new InvalidSignatureValueException("Wrong signature ! cipher had been intercepted/modified ???");

        String cipher = parts[0];
        String signature = parts[1];
        String cipherHash = hash(cipher, hashAlgorithm);

        if(!cipherHash.equals(signature))
            throw  new InvalidSignatureValueException("Wrong signature ! cipher had been intercepted/modified ???");

        try {
            return new String(Base64.getEncoder().encode(new BigInteger(cipher).toByteArray()), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String hash(String text, String algo) {
        byte[] hash;

        try {
            hash = MessageDigest.getInstance(algo).digest(text.getBytes("UTF-8"));
        }

        catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return  null;
        }

        StringBuilder hashString = new StringBuilder();

        for (int i = 0; i < hash.length; i++) {

            String hex = Integer.toHexString(hash[i]);

            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            }

            else
                hashString.append(hex.substring(hex.length() - 2));
        }

        return hashString.toString();
    }

    public static String[] getSupportedHashAlgorithms() {
        return new String[] {"MD5", "SHA-1", "SHA-256", "SHA-512"};
    }

    public static Integer[] getSupportedKeySize() {
        return new Integer[] {512, 1024, 2048, 4096};
    }
}