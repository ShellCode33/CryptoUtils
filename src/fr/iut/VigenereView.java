package fr.iut;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/**
 * Created by shellcode on 3/2/17.
 */
public class VigenereView extends Scene {

    private VBox root;

    public VigenereView() {
        super(new VBox());
        root = (VBox) getRoot();
    }
}
