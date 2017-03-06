package fr.iut.view;

import com.sun.org.apache.xml.internal.security.signature.InvalidSignatureValueException;
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
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        Text title = new Text("RSA \n");
        title.setFont(new Font("Courier New", 35));

        VBox keysWrapper = new VBox();
        keysWrapper.setAlignment(Pos.TOP_CENTER);
        keysWrapper.setSpacing(10);
        keysWrapper.setPadding(new Insets(30));
        keysWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));

        HBox keySize = new HBox();
        keySize.setAlignment(Pos.CENTER);
        keySize.setSpacing(10);

        VBox messageWrapper = new VBox();
        messageWrapper.setAlignment(Pos.TOP_CENTER);
        messageWrapper.setSpacing(10);
        messageWrapper.setPadding(new Insets(30));
        messageWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));

        HBox all = new HBox();
        all.setSpacing(10);

        Button generateButton = new Button("Generate");
        Button okButton = new Button("Ok");
        Button cryptButton = new Button("Crypt your message");
        Button decryptButton = new Button("Decrypt your message");

        Button backButton = new Button("Back to menu");
        backButton.setOnAction(action -> controller.switchState(State.MENU));
        backButton.setMinWidth(200);
        backButton.setMinHeight(70);

        ComboBox keySizesComboBox = new ComboBox();
        keySizesComboBox.getItems().addAll(RSA.getSupportedKeySize());
        keySizesComboBox.getSelectionModel().selectFirst();
        model = controller.getRSAModel((Integer) keySizesComboBox.getValue());

        CheckBox useSignatureCheckBox = new CheckBox("Use signature");

        Label signatureLabel = new Label();

        ComboBox hashAlgoComboBox = new ComboBox();
        hashAlgoComboBox.getItems().addAll(RSA.getSupportedHashAlgorithms());
        hashAlgoComboBox.disableProperty().bind(useSignatureCheckBox.selectedProperty().not());
        hashAlgoComboBox.getSelectionModel().selectLast();

        cryptButton.setDisable(true);
        decryptButton.setDisable(true);

        okButton.setMinWidth(150);
        okButton.setMinHeight(50);

        TextArea privateKeyTextArea = new TextArea();
        TextArea publicKeyTextArea = new TextArea();
        TextArea cryptedTextArea = new TextArea();
        TextArea decryptedTextArea = new TextArea();
        TextArea modulusTextArea = new TextArea();

        Label privateKeyLabel = new Label();
        Label publicKeyLabel = new Label();
        Label cryptLabel = new Label();
        Label decryptLabel = new Label();
        Label modulusLabel = new Label();
        Label keysLabel = new Label();

        privateKeyLabel.setText("Your private key : ");
        publicKeyLabel.setText("Your public key : ");
        cryptLabel.setText("The cipher : ");
        decryptLabel.setText("The plaintext : ");
        modulusLabel.setText("Modulus : ");
        keysLabel.setText("Enter your keys : ");
        keysLabel.setFont(new Font("Courier New", 15));

        privateKeyTextArea.setWrapText(true);
        privateKeyTextArea.setMaxSize(300,80);

        publicKeyTextArea.setWrapText(true);
        publicKeyTextArea.setMaxSize(300,80);

        modulusTextArea.setWrapText(true);
        modulusTextArea.setMaxSize(300,80);

        cryptedTextArea.setWrapText(true);
        cryptedTextArea.setMaxSize(400, 150);
        cryptedTextArea.setPromptText("Cipher...");
        decryptedTextArea.setWrapText(true);
        decryptedTextArea.setMaxSize(400, 150);
        decryptedTextArea.setPromptText("plaintext...");


        keySizesComboBox.setOnAction(action -> {
            model = controller.getRSAModel((Integer)keySizesComboBox.getValue());
        });


        okButton.setOnAction(e -> {
            Integer key_size = (Integer)keySizesComboBox.getValue();
            String publicKey = publicKeyTextArea.getText();
            String privateKey = privateKeyTextArea.getText();
            String modulus = modulusTextArea.getText();

            if(key_size == null || publicKey.length() <=0 || privateKey.length() <=0 || modulus.length() <=0 ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Please complete all the fields !");
                alert.setContentText(null);
                alert.showAndWait();
                return;
            }

                privateKeyTextArea.setEditable(false);
                publicKeyTextArea.setEditable(false);
                modulusTextArea.setEditable(false);
                cryptButton.setDisable(false);
                decryptButton.setDisable(false);
            try{
                model.setKeySize(key_size);
                model.setPublicKey(publicKey);
                model.setPrivateKey(privateKey);
                model.setModulus(modulus);
            } catch (IllegalArgumentException e1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid parameter !");
                alert.setContentText(null);
                alert.showAndWait();
                privateKeyTextArea.setEditable(true);
                publicKeyTextArea.setEditable(true);
                modulusTextArea.setEditable(true);
            }



        });

        generateButton.setOnAction(e -> {
            model.generateKeys();

            privateKeyTextArea.setText(Base64.getEncoder().encodeToString(model.getPrivateKey().toByteArray()));
            publicKeyTextArea.setText(Base64.getEncoder().encodeToString(model.getPublicKey().toByteArray()));
            modulusTextArea.setText(Base64.getEncoder().encodeToString(model.getMod().toByteArray()));
        });

        cryptButton.setOnAction(e -> {

            signatureLabel.setText("");

            if(decryptedTextArea.getText().length() > 0){

                String plaintext = decryptedTextArea.getText();
                String cipher = model.encode(plaintext, model.getPublicKey(), model.getMod());

                if(useSignatureCheckBox.isSelected()) {
                    System.out.println("Signing message...");
                    cipher = model.sign(cipher, (String)hashAlgoComboBox.getValue(), model.getPrivateKey(), model.getMod());
                    signatureLabel.setText("Your message has been signed !");
                    signatureLabel.setTextFill(Color.GREEN);
                }

                cryptedTextArea.setText(cipher);

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("You have no message !");
                alert.setContentText(null);
                alert.showAndWait();
            }


        });

        decryptButton.setOnAction(e -> {

            signatureLabel.setText("");

            if(cryptedTextArea.getText().length() > 0) {

                if(useSignatureCheckBox.isSelected() && hashAlgoComboBox.getValue() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("If you want to sign your message, you need to choose an hash algorithm !");
                    alert.setContentText(null);
                    alert.showAndWait();
                    return;
                }

                String cipher = cryptedTextArea.getText();

                try {
                    if(useSignatureCheckBox.isSelected()) {
                        System.out.println("Unsigning message...");
                        cipher = model.checkSignatureAndReturnUnsigned(cipher, (String)hashAlgoComboBox.getValue(), model.getPublicKey(), model.getMod());
                        signatureLabel.setText("Signature valid !");
                        signatureLabel.setTextFill(Color.GREEN);
                    }

                    System.out.println("decoding...");
                    String decryptedMessage = model.decodeToString(cipher, model.getPrivateKey(), model.getMod());
                    decryptedTextArea.setText(decryptedMessage);

                }

                catch (IllegalArgumentException e1) {
                    e1.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid cipher !");
                    alert.setContentText(null);
                    alert.showAndWait();
                }

                catch (InvalidSignatureValueException i) {
                    signatureLabel.setText("Error wrong signature !");
                    signatureLabel.setTextFill(Color.RED);
                    decryptedTextArea.setText("");
                    return;
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

        keySize.getChildren().addAll(keySizesComboBox, generateButton);
        keysWrapper.getChildren().addAll(keysLabel, keySize, privateKeyLabel, privateKeyTextArea, publicKeyLabel, publicKeyTextArea, modulusLabel, modulusTextArea, okButton);
        VBox.setMargin(useSignatureCheckBox, new Insets(30, 0, 0, 0));
        messageWrapper.getChildren().addAll(cryptLabel, cryptedTextArea, decryptButton, decryptLabel, decryptedTextArea, cryptButton, useSignatureCheckBox, hashAlgoComboBox, signatureLabel);

        all.getChildren().addAll(keysWrapper, messageWrapper);
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