package fr.iut.view;

import fr.iut.Controller;
import fr.iut.State;
import fr.iut.model.RSA;
import fr.iut.model.Vigenere;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;

/**
 * Created by shellcode on 3/2/17.
 */
public class VigenereView extends Scene {

    private BorderPane root;
    private Vigenere model;

    public VigenereView(Controller controller) {
        super(new BorderPane());
        root = (BorderPane) getRoot();
        root.setPadding(new Insets(30));

        Label title = new Label();
        Button crackButton = new Button();
        TextArea crackMessageTextArea = new TextArea();
        crackMessageTextArea.setWrapText(true);

        ComboBox countryComboBox = new ComboBox();
        countryComboBox.getItems().addAll(Vigenere.getSupportedCountries());
        countryComboBox.getSelectionModel().selectFirst();

        Button backButton = new Button("Back to menu");
        backButton.setOnAction(action -> controller.switchState(State.MENU));
        backButton.setMinWidth(200);
        backButton.setMinHeight(70);

        TableView<List<String>> tableFrequenciesAnalysis = new TableView<>();
        TableColumn<List<String>, String> colLetters = new TableColumn<>("Letters");
        TableColumn<List<String>, String> colFrequencies = new TableColumn<>("Frequencies");
        colLetters.setMinWidth(150);
        colFrequencies.setMinWidth(150);
        colLetters.setResizable(false);
        colFrequencies.setResizable(false);


        colLetters.setCellValueFactory(data -> {
            List<String> rowValues = data.getValue();
            String cellValue = rowValues.get(0);
            return new ReadOnlyStringWrapper(cellValue);
        });

        colFrequencies.setCellValueFactory(data -> {
            List<String> rowValues = data.getValue();
            String cellValue = rowValues.get(1);
            return new ReadOnlyStringWrapper(cellValue);
        });

        tableFrequenciesAnalysis.getColumns().addAll(colLetters, colFrequencies);

        TableView<List<String>> tableResult = new TableView<>();
        TableColumn<List<String>, String> colKey = new TableColumn<>("Keys");
        TableColumn<List<String>, String> colScore = new TableColumn<>("Scores");
        TableColumn<List<String>, String> colTextDecrypted = new TableColumn<>("Decrypted Message");
        colKey.setMinWidth(150);
        colKey.setResizable(false);
        colScore.setMinWidth(50);
        colScore.setResizable(false);
        colTextDecrypted.setMinWidth(400);
        colTextDecrypted.setResizable(false);

        colKey.setCellValueFactory(data -> {
            List<String> rowValues = data.getValue();
            String cellValue = rowValues.get(0);
            return new ReadOnlyStringWrapper(cellValue);
        });

        colScore.setCellValueFactory(data -> {
            List<String> rowValues = data.getValue();
            String cellValue = rowValues.get(1);
            return new ReadOnlyStringWrapper(cellValue);
        });

        colTextDecrypted.setCellValueFactory(data -> {
            List<String> rowValues = data.getValue();
            String cellValue = rowValues.get(2);
            return new ReadOnlyStringWrapper(cellValue);
        });

        tableResult.getColumns().addAll(colKey, colScore, colTextDecrypted);

        title.setText("Vigenere");
        title.setFont(new Font("Courier New", 35));

        crackMessageTextArea.setPromptText("Cipher to crack...");
        crackMessageTextArea.setText("Veyysuiz k uuwj qfmnd ml wqu wecspe vc drwsov lw apui do ziycovve wimw sov glo vedyuzzewinl jpekuo ! Mmhpfjwiyrnslu e'isd ge hyt ? Vr rozafaiv pe msdw bf mmgorejc fjx qeesa gngsscmbdc b tescir kg mr glo isl yvjwi vsnysf hye vi twvuv");

        crackButton.setText("Crack");

        crackButton.setOnAction(action -> {
            tableFrequenciesAnalysis.getItems().clear();
            tableResult.getItems().clear();


            Vigenere model;

            try {
                model = controller.getVigenereModel(crackMessageTextArea.getText());
                model.crack((String)countryComboBox.getValue());
            }

            catch(IllegalArgumentException e) {
                specialCharsNotAllowedAlert();
                return;
            }

            for(int i = 0; i < model.getFrequentialAnalysis().getNbDifferentLetters(); i++) {
                char letter = model.getFrequentialAnalysis().getNthLetterWithHighestFrequency(i);
                tableFrequenciesAnalysis.getItems().add(Arrays.asList("" + letter, "" + model.getFrequentialAnalysis().getFrequenceOf(letter)));
            }

            LinkedHashMap<String, Float> keys_scores_sorted = model.getBestKeysScoreSorted();

            //Iterate over sorted map in reverse order
            ListIterator<Map.Entry<String, Float>> iter = new ArrayList<>(keys_scores_sorted.entrySet()).listIterator(keys_scores_sorted.size());

            while (iter.hasPrevious()) {
                Map.Entry<String, Float> entry = iter.previous();
                tableResult.getItems().add(Arrays.asList(entry.getKey(), Float.toString(entry.getValue()), model.decode(entry.getKey())));
            }
        });


        Text subHeader1 = new Text("Encoder");
        Text subHeader2 = new Text("Cracker");
        subHeader1.setFont(new Font(20));
        subHeader2.setFont(new Font(20));

        VBox vigenereEncoderVBox = new VBox();
        vigenereEncoderVBox.setAlignment(Pos.TOP_LEFT);

        TextArea messageTextArea = new TextArea();
        messageTextArea.setWrapText(true);
        messageTextArea.setPromptText("Plaintext...");

        TextField keyTextField = new TextField();
        keyTextField.setPromptText("Key...");
        TextField keyTextField2 = new TextField();
        keyTextField2.setPromptText("Key...");


        TextArea resultTextArea = new TextArea();
        resultTextArea.setWrapText(true);
        resultTextArea.setPromptText("Cipher...");

        Button encodeButton = new Button("Encode");
        Button decodeButton = new Button("Decode");
        encodeButton.disableProperty().bind(keyTextField.textProperty().isEmpty());
        decodeButton.disableProperty().bind(keyTextField2.textProperty().isEmpty());

        encodeButton.setOnAction(actionEvent -> {
            try {
                resultTextArea.setText(controller.getVigenereModel(messageTextArea.getText()).encode(keyTextField.getText()));
            }
            catch (IllegalArgumentException e) {
                specialCharsNotAllowedAlert();
            }
        });

        decodeButton.setOnAction(actionEvent -> {
            try {
                messageTextArea.setText(controller.getVigenereModel(resultTextArea.getText()).decode(keyTextField2.getText()));
            }
            catch (IllegalArgumentException e) {
                specialCharsNotAllowedAlert();
            }
        });

        HBox inputWrapper1 = new HBox(keyTextField, encodeButton);
        HBox inputWrapper2 = new HBox(keyTextField2, decodeButton);
        inputWrapper1.setAlignment(Pos.TOP_CENTER);
        inputWrapper2.setAlignment(Pos.TOP_CENTER);
        inputWrapper1.setSpacing(10);
        inputWrapper2.setSpacing(10);

        vigenereEncoderVBox.getChildren().setAll(subHeader1, messageTextArea, inputWrapper1, resultTextArea, inputWrapper2);


        HBox tablesWrapper = new HBox();
        tablesWrapper.setSpacing(20);
        tablesWrapper.getChildren().addAll(tableFrequenciesAnalysis, tableResult);

        HBox inputsHbox = new HBox(countryComboBox, crackButton);
        inputsHbox.setSpacing(10);
        inputsHbox.setAlignment(Pos.CENTER);

        VBox crackVBox = new VBox(subHeader2, crackMessageTextArea, inputsHbox, tablesWrapper);

        vigenereEncoderVBox.setAlignment(Pos.TOP_CENTER);
        vigenereEncoderVBox.setSpacing(10);
        vigenereEncoderVBox.setPadding(new Insets(30));
        vigenereEncoderVBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));

        crackVBox.setAlignment(Pos.TOP_CENTER);
        crackVBox.setSpacing(10);
        crackVBox.setPadding(new Insets(30));
        crackVBox.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));

        title.setPadding(new Insets(10, 0, 20, 0));
        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(vigenereEncoderVBox, new Insets(15));
        BorderPane.setMargin(crackVBox, new Insets(15));
        root.setTop(title);
        root.setLeft(vigenereEncoderVBox);
        root.setRight(crackVBox);
        root.setBottom(backButton);

    }

    private void specialCharsNotAllowedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Special chars are not allowed ! Please remove all the '\"éàè@ etc...");
        alert.setContentText(null);
        alert.showAndWait();
    }
}
