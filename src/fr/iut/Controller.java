package fr.iut;

import fr.iut.model.Ceasar;
import fr.iut.model.FrequentialAnalysis;
import fr.iut.model.RSA;
import fr.iut.model.Vigenere;
import fr.iut.view.CeasarView;
import fr.iut.view.MenuView;
import fr.iut.view.RSAView;
import fr.iut.view.VigenereView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.util.Arrays;

public class Controller extends Application {

    private Stage stage;

    //views
    private MenuView menuView = new MenuView(this);
    private CeasarView ceasarView = new CeasarView(this);
    private VigenereView vigenereView = new VigenereView(this);
    private RSAView rsaView = new RSAView(this);

    @Override
    public void start(Stage primaryStage) throws Exception{

        stage = primaryStage;

        primaryStage.setTitle("CryptoUtils");
        primaryStage.show();
        switchState(State.MENU);
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
                stage.setScene(ceasarView);
                break;

            case RSA:
                stage.setScene(rsaView);
                rsaView.showInfo();
                break;

            case VIGENERE:
                stage.setScene(vigenereView);
                break;
        }

        stage.centerOnScreen();
    }

    public Ceasar getCeasarModel(String cipher) {
        return new Ceasar(cipher);
    }

    public Vigenere getVigenereModel(String cipher) {
        return new Vigenere(new FrequentialAnalysis(cipher));
    }

    public RSA getRSAModel(int key_size) {
        return new RSA(key_size);
    }
}
