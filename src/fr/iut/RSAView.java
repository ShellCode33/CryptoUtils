package fr.iut;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import jdk.internal.util.xml.impl.Input;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by shellcode on 3/2/17.
 */
public class RSAView extends Scene {

    int key;
    BigInteger publicKey;
    BigInteger privateKey;
    BigInteger mod;
    String msg;
    private VBox root;


    public void generateKeys(){
        SecureRandom random = new SecureRandom();
        BigInteger p = new BigInteger(key / 2, 100, random);
        BigInteger q = new BigInteger(key / 2, 100, random);
        BigInteger one = new BigInteger("1");
        BigInteger phi_n = p.subtract(one).multiply(q.subtract(one));
        BigInteger e;

        do {
            e = new BigInteger(key / 2, random);
        }while(e.compareTo(one) <= 0 || !e.gcd(phi_n).equals(one));

        BigInteger d = e.modInverse(phi_n);

        mod = p.multiply(q);

        publicKey = e;
        privateKey = d;
    }

    public String getFormatedPrivateKey() {

        return Base64.getEncoder().encodeToString(privateKey.toByteArray());

    }

    public String getFormatedPublicKey() {
        return Base64.getEncoder().encodeToString(publicKey.toByteArray());

    }

    public String encode(String message, BigInteger public_key, BigInteger mod) {

        if(message == null || public_key == null || mod == null)
            throw new InvalidParameterException("parameters can't be null");

        byte bytes[] = message.getBytes();

        if(bytes.length > key /8)
            throw new InvalidParameterException("The message can't be longer than " + key + " bits !");
        //TODO : découper en plusieurs messages au lieu de lever une exception

        BigInteger message_integer = new BigInteger(bytes);
        BigInteger cipher = message_integer.modPow(public_key, mod);

        return Base64.getEncoder().encodeToString(cipher.toByteArray());
    }

    public String decode(String b64, BigInteger private_key, BigInteger mod) {

        if(b64 == null || private_key == null || mod == null)
            throw new InvalidParameterException("parameters can't be null");

        byte bytes[] = Base64.getDecoder().decode(b64);
        BigInteger cipher_integer = new BigInteger(bytes);

        return new String(cipher_integer.modPow(private_key, mod).toByteArray());
    }



    public RSAView() {
        super(new VBox(), 400, 800);
        root = (VBox) getRoot();

        ComboBox keyList = new ComboBox();
        Label error = new Label();
        Button valid = new Button();

        TextField message = new TextField();
        TextArea tPrivateKey = new TextArea();
        TextArea tPublicKey = new TextArea();
        TextArea tMsgCrypt = new TextArea();
        TextArea tMsgDecrypt = new TextArea();
        TextArea tMod = new TextArea();

        Label lPrivateKey = new Label();
        Label lPublicKey = new Label();
        Label lMsgCrypt = new Label();
        Label lMsgDecrypt = new Label();
        Label lMod = new Label();

        lPrivateKey.setText("Votre clé privée : ");
        lPublicKey.setText("\n Votre clé publique : ");
        lMsgCrypt.setText("\n Votre message crypté : ");
        lMsgDecrypt.setText("\n Votre message décrypté : ");
        lMod.setText("Le modulo est de : ");

        tPrivateKey.setEditable(false);
        tPublicKey.setEditable(false);
        tMsgCrypt.setEditable(false);
        tMsgDecrypt.setEditable(false);
        tMod.setWrapText(true);
        tPrivateKey.setWrapText(true);

        keyList.setPromptText("Choisir la longueur de la clé");

        keyList.getItems().addAll(
                "512",
                "1024",
                "2048",
                "4096"
        );

        message.setText("Entrez votre message");

        valid.setText("Valider");

        valid.setOnAction(e -> {
            if(keyList.getValue() != null && message.getText() != null){
                key = Integer.parseInt(keyList.getValue().toString());
                msg = message.getText();
                error.setText("");
                generateKeys();
                tPrivateKey.setText(getFormatedPrivateKey());
                tPublicKey.setText(getFormatedPublicKey());
                String result = encode(msg, publicKey, mod);
                tMsgCrypt.setText(result);
                tMsgDecrypt.setText(decode(result, privateKey, mod));
                tMod.setText(mod.toString());
            }
            else{
                error.setText("Toutes les données ne sont pas choisies");
            }
        });

        root.getChildren().addAll(keyList, message, valid, error, lPrivateKey, tPrivateKey, lPublicKey, tPublicKey, lMod, tMod, lMsgCrypt, tMsgCrypt, lMsgDecrypt, tMsgDecrypt);
    }
}