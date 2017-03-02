package fr.iut;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private Stage stage;
    private MenuView menuView = new MenuView(this);

    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;

        primaryStage.setTitle("CryptoUtils");
        switchState(State.MENU);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public void switchState(State state) {
        switch (state) {
            case MENU:
                stage.setScene(menuView);
                break;

            case CEASAR:
                //stage.setScene(new CeasarView());
                break;

            case RSA:
                stage.setScene(new RSAView());
                break;

            case VIGENERE:
                stage.setScene(new VigenereView());
                break;
        }

        stage.centerOnScreen();
    }
}
