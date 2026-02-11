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
    private static final Color          PANEL_COLOR      = Constants.AZUL_BLUE;
    private static final Color          BACKGROUND_COLOR = Constants.AZUL_BLUE_BRIGHT;
    private static final Color          TEXT_COLOR       = Constants.WHITE;
    private static final double         FLIP_TIME        = 150;
    private final        Row            row0             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private final        Row            row1             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private final        Row            row2             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private final        Row            row3             = new Row(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private              VBox           vBox;
    private              boolean        toggle;
    private              long           lastTimerCall;
    private              AnimationTimer timer;


    @Override public void init() {
        vBox          = new VBox(row0, row1, row2, row3);
        toggle        = false;
        lastTimerCall = System.nanoTime();
        timer         = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (now - 15_000_000_000L > lastTimerCall) {
                    toggle ^= true;
                    if (toggle) {
                        setTitle();
                    } else {
                        setSubTitle();
                    }
                    lastTimerCall = now;
                }
            }
        };
    }

    private void setTitle() {
        row0.reset();
        row1.setText("    THE    ");
        row2.setText("  JOURNEY  ");
        row3.reset();
    }

    private void setSubTitle() {
        row0.setText("FOLLOW JAVA");
        row1.setText("CODE ON ITS");
        row2.setText("WAY THROUGH");
        row3.setText("  THE JVM  ");
    }

    @Override public void start(final Stage stage) {
        StackPane pane = new StackPane(vBox);
        pane.setPadding(new Insets(10));
        pane.setBackground(new Background(new BackgroundFill(PANEL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(30);
        camera.setNearClip(0.0);

        Scene scene = new Scene(pane);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        /*
        scene.setOnKeyPressed(evt -> {
            final KeyCode keyCode = evt.getCode();
            if (keyCode == KeyCode.ESCAPE) {
                row0.reset();
                row1.reset();
                row2.reset();
                row3.reset();
            } else if (keyCode == KeyCode.T) {
                row0.selfTest();
                row1.selfTest();
                row2.selfTest();
                row3.selfTest();
            }
        });
        */

        timer.start();
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) { launch(args); }
}
