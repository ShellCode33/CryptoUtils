package fr.iut;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;

import static javafx.geometry.Pos.CENTER;

/**
 * Created by shellcode on 3/2/17.
 */
public class VigenereView extends Scene {

    private VBox root;

    public VigenereView() {
        super(new VBox());
        root = (VBox) getRoot();

        Text title = new Text("Vigenere");
        title.setFont(new Font("Courier New", 30));

        final TableView<String> Freq = new TableView();
        final TableColumn<String, String> columnLetter = new TableColumn<>("Letter");
        final TableColumn<String, String> columnFreq = new TableColumn<>("Frequency");

        Freq.getColumns().setAll(columnLetter, columnFreq);
        //title.setTextAlignment(TextAlignment.CENTER);

        root.getChildren().add(title);
        root.getChildren().add(Freq);




    }
}
