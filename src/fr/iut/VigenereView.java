package fr.iut;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static javafx.geometry.Pos.CENTER;

/**
 * Created by shellcode on 3/2/17.
 */
public class VigenereView extends Scene {

    private VBox root;
    private String text;
    Map<Character, Integer> frequences = new LinkedHashMap<>();
    int nb_letters = 0;

    public VigenereView() {
        super(new VBox());
        root = (VBox) getRoot();
        root.setPadding(new Insets(30));
        root.setSpacing(30);
        root.setAlignment(Pos.CENTER);

        Label title = new Label();
        Label titleMsg = new Label();
        Button valid = new Button();
        TextField message = new TextField();

        final TableView<String> TabFreq = new TableView();
        final TableColumn<String, String> columnLetter = new TableColumn<>("Letter");
        final TableColumn<String, String> columnFreq = new TableColumn<>("Frequency");

        title.setText("Vigenere");
        title.setFont(new Font("Courier New", 30));
        titleMsg.setText("Message:");

        message.setPromptText("Entrez votre message");
        message.setFocusTraversable(false);
        valid.setText("Valider");

        TabFreq.getColumns().setAll(columnLetter, columnFreq);
        TabFreq.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.getChildren().addAll(title, titleMsg, message, valid, TabFreq);

    }
}

    /*public FrequentialAnalysis(String text) {
            this.text = text;
            clearText();
            compute();
        }

    private void clearText() {
        //On enlève la ponctuation
        String to_remove_chars[] = new String[] {".", ",", "!", "?", ";", ":", "-", "'"};
        for(String to_remove : to_remove_chars)
            text = text.replace(to_remove, "");

        boolean hasNonAlpha = text.matches("^.*[^a-zA-Z0-9 ].*$");

        if(hasNonAlpha)
            throw new IllegalArgumentException("Special chars are not allowed ! Please remove all the '\"éàè@ etc...");

        text = text.toUpperCase();
        text = text.trim();
        text = text.replaceAll(" +", " "); //enlève les espaces multiples
    }

    private void compute() {
        //On compte la fréquence d'apparition de toutes les lettres
        for(char c : text.toCharArray()) {
            if(c >= 'A' && c <= 'Z') {
                Integer freq = frequences.get(c);
                frequences.put(c, freq == null ? 1 : freq+1);
                nb_letters++;
            }
        }

        //On trie la map
        Map<Character, Integer> sorted_frequences = new LinkedHashMap<>();

        while(frequences.size() > 0) { //On va supprimer la frequence la plus grande à chaque fois afin de trier


            Character highest_character = null;
            Integer highest_integer = -1;

            //On trouve la plus grande fréquence des lettres qui restent
            for(Map.Entry<Character, Integer> entry : frequences.entrySet()) {
                if(entry.getValue() > highest_integer) {
                    highest_integer = entry.getValue();
                    highest_character = entry.getKey();
                }
            }

            sorted_frequences.put(highest_character, highest_integer);
            frequences.remove(highest_character);
        }

        frequences = sorted_frequences;
    }


    }
}*/
