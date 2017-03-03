package fr.iut.view;

import fr.iut.Controller;
import fr.iut.State;
import fr.iut.model.Ceasar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * Created by shellcode on 3/2/17.
 */
public class CeasarView extends Scene {

    private BorderPane root;

    private TextField [] resultInputs = new TextField[26];

    public CeasarView(Controller controller) {
        super(new BorderPane());
        root = (BorderPane) getRoot();
        root.setPadding(new Insets(30));

        Text title = new Text("Ceasar code");
        title.setFont(new Font("Courier New", 35));

        Text subtitle1 = new Text("Encoder");
        subtitle1.setFont(new Font("Courier New", 20));

        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPromptText("Message...");

        HBox spinnerWrap = new HBox();
        spinnerWrap.setAlignment(Pos.CENTER);
        Text shiftText = new Text("Shift : ");
        Spinner<Integer> shiftSpinner = new Spinner<>();
        SpinnerValueFactory<Integer> factory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 25);
        shiftSpinner.setValueFactory(factory);
        spinnerWrap.getChildren().addAll(shiftText, shiftSpinner);

        Text resultText = new Text("Result :");
        TextArea resultArea = new TextArea();
        resultArea.setWrapText(true);
        resultArea.setEditable(false);

        Button encodeButton = new Button("Encode");

        encodeButton.setOnAction(actionEvent -> {
            resultArea.setText(Ceasar.encode(textArea.getText(), shiftSpinner.getValue()));
        });

        Text subtitle2 = new Text("Cracker");
        subtitle2.setFont(new Font("Courier New", 20));

        HBox inputCrackWrapper = new HBox();
        inputCrackWrapper.setAlignment(Pos.CENTER);
        inputCrackWrapper.setSpacing(10);
        TextField input = new TextField();
        input.setPromptText("Ceasar code...");
        Button bruteforceButton = new Button("Bruteforce");

        bruteforceButton.setOnAction(action -> {
            Ceasar model = controller.getCeasarModel(input.getText());
            String mostProbableResult = model.getMostProbableAnswer();
            String [] results = model.getResults();

            for(int i = 0; i < 26; i++) {
                resultInputs[i].setStyle("-fx-control-inner-background: white;");
                resultInputs[i].setText(results[i]);

                if(mostProbableResult.equals(results[i]))
                    resultInputs[i].setStyle("-fx-control-inner-background: green;");
            }
        });

        inputCrackWrapper.getChildren().addAll(input, bruteforceButton);

        Text text = new Text("Here are the possible shifts : ");

        HBox crackResultsWrapper = new HBox();
        crackResultsWrapper.setSpacing(50);
        VBox column1 = new VBox();
        column1.setSpacing(10);
        VBox column2 = new VBox();
        column2.setSpacing(10);
        crackResultsWrapper.getChildren().addAll(column1, column2);

        VBox encoderWrapper = new VBox();
        encoderWrapper.setAlignment(Pos.TOP_CENTER);
        encoderWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));
        encoderWrapper.setSpacing(20);
        encoderWrapper.setPadding(new Insets(30));
        encoderWrapper.getChildren().addAll(subtitle1, textArea, spinnerWrap, encodeButton, resultText, resultArea);

        VBox crackWrapper = new VBox();
        crackWrapper.setAlignment(Pos.TOP_CENTER);
        crackWrapper.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(3))));
        crackWrapper.setPadding(new Insets(30));
        crackWrapper.setSpacing(20);
        crackWrapper.getChildren().addAll(subtitle2, inputCrackWrapper, text, crackResultsWrapper);

        Button backButton = new Button("Back to menu");
        backButton.setOnAction(action -> controller.switchState(State.MENU));
        backButton.setMinWidth(200);
        backButton.setMinHeight(70);

        BorderPane.setAlignment(title, Pos.CENTER);
        BorderPane.setAlignment(backButton, Pos.CENTER);
        BorderPane.setMargin(title, new Insets(0, 0, 30, 0));
        BorderPane.setMargin(crackWrapper, new Insets(0, 0, 0, 15));
        BorderPane.setMargin(encoderWrapper, new Insets(0, 15, 0, 0));
        BorderPane.setMargin(backButton, new Insets(30, 0, 0, 0));
        root.setTop(title);
        root.setLeft(encoderWrapper);
        root.setRight(crackWrapper);
        root.setBottom(backButton);

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