package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Created by Jonas Eiselt on 2017-12-04.
 */
class SettingsView extends VBox
{
    private NumberField newCustomerRateNF;
    private NumberField advPoolProcRateNF;
    private NumberField commPoolProcRateNF;
    private NumberField advPoolExitRateNF;
    private NumberField commPoolExitRateNF;
    private NumberField avgExitRateNF;

    void initializeView()
    {
        String fontStyle = "-fx-font-family: 'Droid Sans'; "
                + "-fx-font-size: 11pt;";

        setSpacing(15);
        setPadding(new Insets(15));
        setStyle(fontStyle + " -fx-border-color: #ccc; -fx-border-width: 1;");

        Label titleLabel = new Label("Settings");
        titleLabel.setStyle(" -fx-font-weight: bold; -fx-font-family: 'Droid Sans'; -fx-font-size: 12pt;");

        HBox newCustomerBox = getNewCustomerBox();
        VBox timeAdvPoolBox = getTimeAdvPoolBox();
        VBox timeCommPoolBox = getTimeCommPoolBox();
        HBox exitAdvPoolRateBox = getExitAdvPoolRateBox();
        HBox exitCommPoolRateBox = getExitCommPoolRateBox();
        HBox avgExitTimeBox = getAvgExitTimeBox();

        getChildren().addAll(titleLabel, newCustomerBox, new Separator(), timeAdvPoolBox, new Separator(),
                timeCommPoolBox, new Separator(), exitAdvPoolRateBox, new Separator(), exitCommPoolRateBox,
                new Separator(), avgExitTimeBox);
    }

    private HBox getNewCustomerBox()
    {
        Label label1 = new Label("New customer show up every");
        label1.setPrefWidth(190);
        label1.setWrapText(true);

        newCustomerRateNF = new NumberField("500");
        newCustomerRateNF.setPrefColumnCount(4);

        Label label2 = new Label("ms");

        HBox root = new HBox(10);
        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(label1, newCustomerRateNF, label2);

        return root;
    }

    private VBox getTimeAdvPoolBox()
    {
        Label label1 = new Label("Processing time for adventure pool queue is");
        label1.setPrefWidth(190);
        label1.setWrapText(true);

        advPoolProcRateNF = new NumberField("1000");
        advPoolProcRateNF.setPrefColumnCount(4);

        Label label2 = new Label("ms");
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().addAll(label1, advPoolProcRateNF, label2);


        Label label3 = new Label("(Reception → Pool → Exit)");

        VBox root = new VBox(5);
        root.getChildren().addAll(box, label3);

        return root;
    }

    private VBox getTimeCommPoolBox()
    {
        Label label1 = new Label("Processing time for common pool queue is");
        label1.setPrefWidth(190);
        label1.setWrapText(true);

        commPoolProcRateNF = new NumberField("1000");
        commPoolProcRateNF.setPrefColumnCount(4);

        Label label2 = new Label("ms");
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(label1, commPoolProcRateNF, label2);


        Label label3 = new Label("(Reception → Pool → Exit)");

        VBox root = new VBox(5);
        root.getChildren().addAll(box, label3);

        return root;
    }

    private HBox getExitAdvPoolRateBox()
    {
        Label label1 = new Label("Likeliness for visitor in adventure pool to exit is 1 out of");
        label1.setPrefWidth(190);
        label1.setWrapText(true);

        advPoolExitRateNF = new NumberField("4");
        advPoolExitRateNF.setPrefColumnCount(4);

        HBox root = new HBox(10);
        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(label1, advPoolExitRateNF);

        return root;
    }

    private HBox getExitCommPoolRateBox()
    {
        Label label1 = new Label("Likeliness for visitor in common pool to exit is 1 out of");
        label1.setPrefWidth(190);
        label1.setWrapText(true);

        commPoolExitRateNF = new NumberField("4");
        commPoolExitRateNF.setPrefColumnCount(4);

        HBox root = new HBox(10);
        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(label1, commPoolExitRateNF);

        return root;
    }

    private HBox getAvgExitTimeBox()
    {
        Label label1 = new Label("Average time to exit exit area is");
        label1.setPrefWidth(190);
        label1.setWrapText(true);

        avgExitRateNF = new NumberField("4000");
        avgExitRateNF.setPrefColumnCount(4);

        Label label2 = new Label("ms");

        HBox root = new HBox(10);
        root.setAlignment(Pos.CENTER_LEFT);
        root.getChildren().addAll(label1, avgExitRateNF, label2);

        return root;
    }

    int getNewCustomerRate()
    {
        return newCustomerRateNF.getInt();
    }

    int getAdvPoolProcRate()
    {
        return advPoolProcRateNF.getInt();
    }

    int getCommPoolProcRate()
    {
        return commPoolProcRateNF.getInt();
    }

    int getAdvPoolExitRate()
    {
        return advPoolExitRateNF.getInt();
    }

    int getCommPoolExitRate()
    {
        return commPoolExitRateNF.getInt();
    }

    int getAvgExitRate()
    {
        return avgExitRateNF.getInt();
    }
}
