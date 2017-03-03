package fr.iut.view;

import fr.iut.Controller;
import fr.iut.State;
import fr.iut.model.RSA;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.security.InvalidParameterException;
import java.util.Base64;

/**
 * Created by shellcode on 3/2/17.
 */
public class RSAView extends Scene {

    private VBox root;
    private RSA model;

    /*

    textarea pour le cipher
    textarea pour la clé privée (utilisée pour signer)
    combobox pour choisir l'algorithme de hash
    bouton "sign"
    textarea pour le resultat

     */

    public RSAView(Controller controller) {
        super(new VBox());
        root = (VBox) getRoot();
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        Text title = new Text("RSA \n");
        title.setFont(new Font("Courier New", 35));

        VBox enterKeys = new VBox();
        enterKeys.setSpacing(10);

        VBox containsKeys = new VBox();
        containsKeys.setAlignment(Pos.CENTER);
        containsKeys.setSpacing(10);
        containsKeys.setPadding(new Insets(30));
        containsKeys.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));

        HBox keySize = new HBox();
        keySize.setAlignment(Pos.CENTER);
        keySize.setSpacing(10);

        VBox messageBox = new VBox();
        messageBox.setAlignment(Pos.CENTER);
        messageBox.setSpacing(10);
        messageBox.setPadding(new Insets(30));
        messageBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));

        HBox messageButton = new HBox();
        messageButton.setAlignment(Pos.CENTER);
        messageButton.setSpacing(10);
        messageButton.setPadding(new Insets(15));

        VBox sign = new VBox();
        sign.setAlignment(Pos.CENTER);
        sign.setSpacing(10);
        sign.setPadding(new Insets(30));
        sign.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));

        HBox signButton = new HBox();
        signButton.setAlignment(Pos.CENTER);
        signButton.setSpacing(10);

        HBox all = new HBox();
        all.setSpacing(10);

        ComboBox keyCheck = new ComboBox();
        ComboBox signMethod = new ComboBox();
        Button valid = new Button("Generate");
        Button ok = new Button("Ok");
        Button crypt = new Button("Crypt your message");
        Button decrypt = new Button("Decrypt your message");
        Button backButton = new Button("Back to menu");
        Button bSign = new Button("Sign");

        crypt.setDisable(true);
        decrypt.setDisable(true);

        ok.setDisable(true);
        ok.setMinWidth(150);
        ok.setMinHeight(50);

        backButton.setOnAction(action -> controller.switchState(State.MENU));
        backButton.setMinWidth(200);
        backButton.setMinHeight(70);

        TextArea tPrivateKey = new TextArea();
        TextArea tPublicKey = new TextArea();
        TextArea tMsgCrypt = new TextArea();
        TextArea tMsgDecrypt = new TextArea();
        TextArea tMod = new TextArea();
        TextArea tMsgCrypt2 = new TextArea();
        TextArea msgCryptS = new TextArea();

        Label lPrivateKey = new Label();
        Label lPublicKey = new Label();
        Label lMsgCrypt = new Label();
        Label lMsgDecrypt = new Label();
        Label lMod = new Label();
        Label eKeys = new Label();
        Label lMsgCrypt2 = new Label();

        lPrivateKey.setText("Your private key : ");
        lPublicKey.setText("Your public key : ");
        lMsgCrypt.setText("The cipher : ");
        lMsgCrypt2.setText("The cipher : ");
        lMsgDecrypt.setText("The plaintext : ");
        lMod.setText("Modulus : ");
        eKeys.setText("Enter your keys : ");
        eKeys.setFont(new Font("Courier New", 15));

        tPrivateKey.setWrapText(true);
        tPrivateKey.setMaxSize(300,80);

        tPublicKey.setWrapText(true);
        tPublicKey.setMaxSize(300,80);

        tMod.setWrapText(true);
        tMod.setMaxSize(300,80);

        tMsgCrypt.setWrapText(true);
        tMsgCrypt.setMaxSize(400, 150);
        tMsgCrypt.setPromptText("Your crypt message...");
        tMsgDecrypt.setWrapText(true);
        tMsgDecrypt.setMaxSize(400, 150);
        tMsgDecrypt.setPromptText("Your decrypt message...");
        tMsgCrypt2.setWrapText(true);
        tMsgCrypt2.setMaxSize(400, 150);
        msgCryptS.setWrapText(true);
        msgCryptS.setMaxSize(400, 150);
        msgCryptS.setPromptText("Your crypt message signed...");

        keyCheck.setPromptText("Your key size");
        keyCheck.getItems().addAll(RSA.getSupportedKeySize());


        keyCheck.setOnAction(action -> {
            ok.setDisable(false);
            model = controller.getRSAModel((Integer)keyCheck.getValue());
        });


        ok.setOnAction(e -> {
            Integer key_size = (Integer)keyCheck.getValue();
            String publicKey = tPublicKey.getText();
            String privateKey = tPrivateKey.getText();
            String modulus = tMod.getText();

            if(key_size == null || publicKey.length() <=0 || privateKey.length() <=0 || modulus.length() <=0 ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please complete all the fields !");
                alert.setContentText(null);
                alert.showAndWait();
                return;
            }

                tPrivateKey.setEditable(false);
                tPublicKey.setEditable(false);
                tMod.setEditable(false);
                crypt.setDisable(false);
                decrypt.setDisable(false);
            try{
                model.setPublicKey(publicKey);
                model.setPrivateKey(privateKey);
                model.setModulus(modulus);
            } catch (IllegalArgumentException e1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid parameter !");
                alert.setContentText(null);
                alert.showAndWait();
                tPrivateKey.setEditable(true);
                tPublicKey.setEditable(true);
                tMod.setEditable(true);
            }



        });

        valid.setOnAction(e -> {
            tPrivateKey.setEditable(false);
            tPublicKey.setEditable(false);
            tMod.setEditable(false);

            model.generateKeys();

            tPrivateKey.setText(Base64.getEncoder().encodeToString(model.getPrivateKey().toByteArray()));
            tPublicKey.setText(Base64.getEncoder().encodeToString(model.getPublicKey().toByteArray()));
            tMod.setText(Base64.getEncoder().encodeToString(model.getMod().toByteArray()));
        });

        crypt.setOnAction(e -> {
            if(tMsgDecrypt.getText().length() > 0){
                String plaintext = tMsgDecrypt.getText();
                try{
                    String cipher = model.encode(plaintext, model.getPublicKey(), model.getMod());
                    tMsgCrypt.setText(cipher);
                }catch (InvalidParameterException e1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Your message is too long !");
                    alert.setContentText(null);
                    alert.showAndWait();
                }

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You have no message !");
                alert.setContentText(null);
                alert.showAndWait();
            }

        });

        decrypt.setOnAction(e -> {
            if(tMsgCrypt.getText().length() > 0) {
                String plaintext = tMsgCrypt.getText();
                try {
                    tMsgDecrypt.setText(model.decode(plaintext, model.getPrivateKey(), model.getMod()));
                } catch (InvalidParameterException e1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Your message is too long !");
                    alert.setContentText(null);
                    alert.showAndWait();
                }

                catch (IllegalArgumentException e1) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid cipher !");
                    alert.setContentText(null);
                    alert.showAndWait();
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You have no message !");
                alert.setContentText(null);
                alert.showAndWait();
            }


        });

        keySize.getChildren().addAll(keyCheck, valid);
        enterKeys.getChildren().addAll(eKeys, keySize, lPrivateKey, tPrivateKey, lPublicKey, tPublicKey, lMod, tMod);
        containsKeys.getChildren().addAll(enterKeys, ok);

        messageButton.getChildren().addAll(crypt, decrypt);
        messageBox.getChildren().addAll(lMsgCrypt, tMsgCrypt, messageButton, lMsgDecrypt, tMsgDecrypt );

        signButton.getChildren().addAll(signMethod, bSign);
        sign.getChildren().addAll(lMsgCrypt2, tMsgCrypt2, lPrivateKey, tPrivateKey, signButton, msgCryptS);

        all.getChildren().addAll(containsKeys, messageBox, sign);
        root.getChildren().addAll(title, all, backButton);
    }

    public void showInfo() {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("Information");
        info.setHeaderText("Your public key, private key and the modulus must be in base64.");
        info.setContentText(null);
        info.show();
    }
}