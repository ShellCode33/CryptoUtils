package fr.iut;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


/**
 * Created by shellcode on 3/2/17.
 */
public class MenuView extends Scene {

    VBox root;

    public MenuView(Main main) {
        super(new VBox(), 400, 800);
        root = (VBox) getRoot();

        Button buttonVigenere = new Button("Vigenere");
        Button buttonRSA = new Button("RSA");

        buttonVigenere.setOnAction(action -> main.switchState(State.VIGENERE));
        buttonRSA.setOnAction(action -> main.switchState(State.RSA));


        root.getChildren().addAll(buttonVigenere, buttonRSA);
    }
}
