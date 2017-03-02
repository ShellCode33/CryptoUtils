package fr.iut.view;

import fr.iut.Controller;
import fr.iut.model.RSA;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.math.BigInteger;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Created by shellcode on 3/2/17.
 */
public class RSAView extends Scene {

    private VBox root;
    private RSA model;

    public RSAView(Controller controller) {
        super(new VBox());
        root = (VBox) getRoot();

        Text title = new Text("RSA");
        title.setFont(new Font("Courier New", 30));

        ComboBox keyList = new ComboBox();
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

        lPrivateKey.setText("Your private key : ");
        lPublicKey.setText("Your public key : ");
        lMsgCrypt.setText("The cipher : ");
        lMsgDecrypt.setText("The plaintext : ");
        lMod.setText("Modulus : ");

        tPrivateKey.setEditable(false);
        tPublicKey.setEditable(false);
        tMsgCrypt.setEditable(false);
        tMsgDecrypt.setEditable(false);
        tMod.setWrapText(true);
        tPrivateKey.setWrapText(true);

        keyList.setPromptText("Choose key size...");
        keyList.getItems().addAll(RSA.getSupportedKeySize());

        message.setPromptText("Your message...");

        valid.setText("Go !");

        valid.setOnAction(e -> {

            Integer key_size = (Integer)keyList.getValue();
            String plaintext = message.getText();
            String publicKey = tPublicKey.getText();
            String privateKey = tPrivateKey.getText();
            String modulus = tMod.getText();

            if(key_size == null || plaintext == null || publicKey == null || privateKey == null || modulus == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please complete all the fields !");
                alert.setContentText(null);
                alert.showAndWait();
                return;
            }

            model = controller.getRSAModel(key_size);
            model.setPublicKey(publicKey);
            model.setPrivateKey(privateKey);
            model.setModulus(modulus);

            //TODO : fais plutot un bouton "Generate" qui propose à l'utilisateur de générer des clés car il doit pouvoir s'il a envie utiliser les siennes en les collant dans les champs
            //model.generateKeys();
            //tPrivateKey.setText(Base64.getEncoder().encodeToString(model.getPrivateKey().toByteArray()));
            //tPublicKey.setText(Base64.getEncoder().encodeToString(model.getPublicKey().toByteArray()));


            //TODO : il faut faire des boutons "crypt" et "decrypt" pour laisser le choix à l'utilisateur de ce qu'il veut faire
            String cipher = model.encode(plaintext, model.getPublicKey(), model.getMod());
            tMsgCrypt.setText(cipher);
            tMsgDecrypt.setText(model.decode(cipher, model.getPrivateKey(), model.getMod()));
        });

        //TODO : va falloir mettre plus en forme que ca parce que la fenetre est trop grande : avec des VBox, HBox, ou en utilisant des panes comme BorderPane par exemple (regarde ma classe CeasarView si tu veux un exemple)
        root.getChildren().addAll(title, keyList, message, valid, lPrivateKey, tPrivateKey, lPublicKey, tPublicKey, lMod, tMod, lMsgCrypt, tMsgCrypt, lMsgDecrypt, tMsgDecrypt);
    }
}