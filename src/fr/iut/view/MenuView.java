package fr.iut.view;

import fr.iut.Controller;
import fr.iut.State;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Created by shellcode on 3/2/17.
 */
public class MenuView extends Scene {

    BorderPane root;

    public MenuView(Controller main) {
        super(new BorderPane());
        root = (BorderPane) getRoot();
        root.setPadding(new Insets(30, 30, 0, 30));

        Text title = new Text("Welcome in CyptoUtils");
        title.setFont(new Font("Courier New", 30));
        Text footer = new Text("Developped by Claire, Valentine, Clément !");

        VBox buttons = new VBox();
        buttons.setSpacing(30);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(50));

        Button buttonVigenere = new Button("Vigenere");
        Button buttonRSA = new Button("RSA");
        Button buttonCeasar = new Button("Ceasar");
        buttonVigenere.setMinWidth(200);
        buttonRSA.setMinWidth(200);
        buttonCeasar.setMinWidth(200);

        buttons.getChildren().addAll(buttonCeasar, buttonVigenere, buttonRSA);

        buttonVigenere.setOnAction(action -> main.switchState(State.VIGENERE));
        buttonRSA.setOnAction(action -> main.switchState(State.RSA));
        buttonCeasar.setOnAction(action -> main.switchState(State.CEASAR));

        root.setTop(title);
        root.setCenter(buttons);
        root.setBottom(footer);

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(buttons, Pos.CENTER);
        BorderPane.setAlignment(footer, Pos.CENTER);
    }
}
