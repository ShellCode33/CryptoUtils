package fr.iut;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


/**
 * Created by shellcode on 3/2/17.
 */
public class CeasarView extends Scene {

    private VBox root;

    public CeasarView() {
        super(new VBox());
        root = (VBox) getRoot();

        Button button = new Button("test");
        root.getChildren().add(button);
    }
}