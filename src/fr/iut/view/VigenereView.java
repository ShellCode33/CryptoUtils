package fr.iut.view;

import fr.iut.Controller;
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

import java.util.Arrays;
import java.util.List;

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
        Label titleMsg = new Label();
        Button valid = new Button();
        TextField message = new TextField();


        TableView<List<String>> table = new TableView<>();
        TableColumn<List<String>, String> colLetters = new TableColumn<>("Letters");
        TableColumn<List<String>, String> colFrequencies = new TableColumn<>("Frequencies");
        colLetters.setMinWidth(150);
        colFrequencies.setMinWidth(150);


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

        table.getColumns().addAll(colLetters, colFrequencies);

        title.setText("Vigenere");
        title.setFont(new Font("Courier New", 35));
        titleMsg.setText("Message:");

        message.setPromptText("Entrez votre message");
        message.setFocusTraversable(false);
        valid.setText("Valider");

        valid.setOnAction(action -> {
            table.getItems().clear();
            Vigenere model = controller.getVigenereModel(message.getText());

            for(int i = 0; i < model.getFrequentialAnalysis().getNbDifferentLetters(); i++) {
                char letter = model.getFrequentialAnalysis().getNthLetterWithHighestFrequency(i);
                table.getItems().add(Arrays.asList("" + letter, "" + model.getFrequentialAnalysis().getFrequenceOf(letter)));
            }
        });

        //TabFreq.getColumns().setAll(columnLetter, columnFreq);
        //TabFreq.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        VBox wrapper = new VBox(title, titleMsg, message, valid);
        VBox Frequency = new VBox(table);
        Frequency.setAlignment(Pos.TOP_CENTER);
        Frequency.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));
        Frequency.setSpacing(20);
        Frequency.setPadding(new Insets(30));

        VBox Keys = new VBox();
        Text text = new Text("There is a list of keys from the less probable to the most :");
        Keys.setAlignment(Pos.TOP_CENTER);
        Keys.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));
        Keys.setSpacing(20);
        Keys.setPadding(new Insets(30));
        Keys.getChildren().addAll(text);


        root.setTop(wrapper);
        root.setLeft(table);
        root.setRight(Keys);

    }
}
