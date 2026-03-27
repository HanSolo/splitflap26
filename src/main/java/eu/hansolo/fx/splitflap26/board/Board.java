package eu.hansolo.fx.splitflap26.board;

import eu.hansolo.fx.splitflap26.CharacterSet;
import eu.hansolo.fx.splitflap26.Constants;
import eu.hansolo.fx.splitflap26.FlipEvent;
import eu.hansolo.fx.splitflap26.Row;
import eu.hansolo.fx.splitflap26.Row1;
import eu.hansolo.fx.splitflap26.SplitFlap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class Board extends Application {
    private static final Color                   PANEL_COLOR = Color.BLACK;
    private static final double                  FLIP_TIME   = 100;
    private final        Row1                    row0        = new Row1(CharacterSet.ALPHA_NUMERIC, 11, FLIP_TIME, Color.RED, Color.WHITE, true, true);
    private final        Row1                    row1        = new Row1(CharacterSet.ALPHA_NUMERIC, 11, FLIP_TIME, Color.GREEN, Color.WHITE, true, true);
    private final        Row1                    row2        = new Row1(CharacterSet.ALPHA_NUMERIC, 11, FLIP_TIME, Color.BLUE, Color.WHITE, true, true);
    private final        Row1                    row3        = new Row1(CharacterSet.ALPHA_NUMERIC, 11, FLIP_TIME, true, true);
    private              VBox                    vBox;
    private              int                     screenCounter;
    private              AtomicBoolean           flipping;
    private              Map<Row1, Boolean>      flipMap;
    private              EventHandler<FlipEvent> flipFinishedHandler;
    private              long                    lastTimerCall;
    private              AnimationTimer          timer;


    @Override public void init() {
        vBox                = new VBox(10, row0, row1, row2, row3);
        screenCounter       = 0;
        flipping            = new AtomicBoolean(true);
        flipMap             = new HashMap<>() {{
            put(row0, false);
            put(row1, false);
            put(row2, false);
            put(row3, false);
        }};
        flipFinishedHandler = e -> {
            this.flipMap.put((Row1) e.getSource(), true);
            if (flipping.get() && !this.flipMap.values().contains(false)) {
                flipping.set(false);
                flipMap.keySet().forEach((row) -> row.removeEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler));
                timer.start();
            };
        };
        lastTimerCall       = System.nanoTime() - 10_000_000_000L;
        timer               = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                if (now - 12_000_000_000L > lastTimerCall) {
                    switch (screenCounter) {
                        case 0 -> setFirstScreen();
                        case 1 -> setSecondScreen();
                        default -> reset();
                    }
                    lastTimerCall = now;
                }
            }
        };
        registerListeners();
    }

    private void registerListeners() {
        row0.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
        row1.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
        row2.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
        row3.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
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

    private void selfCheck() {
        row0.selfCheck();
        row1.selfCheck();
        row2.selfCheck();
        row3.selfCheck();
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

        selfCheck();
        //timer.start();
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) { launch(args); }
}
