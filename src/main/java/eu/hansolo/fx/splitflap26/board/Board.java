package eu.hansolo.fx.splitflap26.board;

import eu.hansolo.fx.splitflap26.Constants;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Board extends Application {
    private static final double FLIP_TIME = 150;
    private final        Row    row0      = new Row(Constants.AZUL_BLUE_BRIGHT, Constants.WHITE, FLIP_TIME);
    private final        Row    row1      = new Row(Constants.AZUL_BLUE_BRIGHT, Constants.WHITE, FLIP_TIME);
    private final        Row    row2      = new Row(Constants.AZUL_BLUE_BRIGHT, Constants.WHITE, FLIP_TIME);
    private final        Row    row3      = new Row(Constants.AZUL_BLUE_BRIGHT, Constants.WHITE, FLIP_TIME);
    private              VBox   vBox;


    @Override public void init() {
        vBox = new VBox(row0, row1, row2, row3);
    }

    @Override public void start(final Stage stage) {
        StackPane pane = new StackPane(vBox);
        pane.setBackground(new Background(new BackgroundFill(Constants.AZUL_BLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(20);
        camera.setVerticalFieldOfView(true);

        Scene scene = new Scene(pane, Color.DARKGRAY);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        row1.setText("    THE    ");
        row2.setText("  JOURNEY  ");

        scene.setOnMousePressed(_ -> {
            row1.reset();
            row2.reset();
        });
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) { launch(args); }
}
