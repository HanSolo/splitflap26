package eu.hansolo.fx.splitflap26.board;

import eu.hansolo.fx.splitflap26.Constants;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Board extends Application {
    private static final Color          PANEL_COLOR      = Color.BLACK;
    private static final Color          BACKGROUND_COLOR = Constants.GRAY;
    private static final Color          TEXT_COLOR       = Constants.WHITE;
    private static final double         FLIP_TIME        = 100;
    private final        Row            row0             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private final        Row            row1             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private final        Row            row2             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private final        Row            row3             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private              VBox           vBox;
    private              int            screenCounter;
    private              long           lastTimerCall;
    private              AnimationTimer timer;


    @Override public void init() {
        vBox          = new VBox(10, row0, row1, row2, row3);
        screenCounter = 0;
        lastTimerCall = System.nanoTime() - 10_000_000_000L;
        timer         = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (now - 12_000_000_000L > lastTimerCall) {
                    switch (screenCounter) {
                        case 0  -> setFirstScreen();
                        case 1  -> setSecondScreen();
                        default -> reset();
                    }
                    lastTimerCall = now;
                }
            }
        };
    }

    private void setFirstScreen() {
        row0.setText("    2026   ");
        row1.setText("  HAN SOLO ");
        row2.setText("   LITTLE  ");
        row3.setText("    TEST   ");
        screenCounter++;
    }

    private void setSecondScreen() {
        row0.setText("  JAVAFX   ");
        row1.setText(" SPLITFLAP ");
        row2.setText("  CONTROL  ");
        row3.setText(" IN ACTION ");
        screenCounter++;
    }

    private void reset() {
        row0.reset();
        row1.reset();
        row2.reset();
        row3.reset();
        screenCounter = 0;
    }

    @Override public void start(final Stage stage) {
        StackPane pane = new StackPane(vBox);
        pane.setPadding(new Insets(10));
        pane.setBackground(new Background(new BackgroundFill(PANEL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(20);
        camera.setNearClip(0.0);

        Scene scene = new Scene(pane);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        timer.start();
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) { launch(args); }
}
