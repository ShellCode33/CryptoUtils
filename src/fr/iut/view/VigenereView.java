package fr.iut.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import fr.iut.Controller;
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

    public VigenereView(Controller controller) {
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
