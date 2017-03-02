package fr.iut.view;

import fr.iut.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Created by shellcode on 3/2/17.
 */
public class CeasarView extends Scene {

    private VBox root;

    private TextField [] resultInputs = new TextField[26];

    public CeasarView(Controller controller) {
        super(new VBox());
        root = (VBox) getRoot();
        root.setPadding(new Insets(30));
        root.setSpacing(30);
        root.setAlignment(Pos.CENTER);

        Text title = new Text("Ceasar cracker");
        title.setFont(new Font("Courier New", 30));

        HBox inputWrapper = new HBox();
        inputWrapper.setAlignment(Pos.CENTER);
        inputWrapper.setSpacing(10);
        TextField input = new TextField();
        input.setPromptText("Ceasar code...");
        Button crackButton = new Button("Bruteforce");

        //crackButton.setOnAction(action -> crack(input.getText()));

        inputWrapper.getChildren().addAll(input, crackButton);

        Text text = new Text("Here are the possible shifts : ");

        HBox resultsWrapper = new HBox();
        resultsWrapper.setSpacing(50);
        VBox column1 = new VBox();
        column1.setSpacing(10);
        VBox column2 = new VBox();
        column2.setSpacing(10);
        resultsWrapper.getChildren().addAll(column1, column2);

        root.getChildren().addAll(title, inputWrapper, text, resultsWrapper);

        char i;
        for(i = 0; i <= 12; i++) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(10);
            Text letter = new Text(Integer.toString(i));
            resultInputs[i] = new TextField();
            hbox.getChildren().addAll(letter, resultInputs[i]);
            column1.getChildren().add(hbox);
        }

        for(; i < 26; i++) {
            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(10);
            Text letter = new Text(Integer.toString(i));
            resultInputs[i] = new TextField();
            hbox.getChildren().addAll(letter, resultInputs[i]);
            column2.getChildren().add(hbox);
        }
    }
}