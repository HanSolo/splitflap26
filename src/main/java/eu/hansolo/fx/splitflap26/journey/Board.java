package eu.hansolo.fx.splitflap26.journey;

import eu.hansolo.fx.splitflap26.CharacterSet;
import eu.hansolo.fx.splitflap26.Constants;
import eu.hansolo.fx.splitflap26.FlipEvent;
import eu.hansolo.fx.splitflap26.Row1;
import eu.hansolo.fx.splitflap26.SplitFlapFont;
import eu.hansolo.fx.splitflap26.fonts.Fonts;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


public class Board extends Application {
    private static final Color                   PANEL_COLOR    = Color.TRANSPARENT;
    private final        SplitFlapFont           FLIP_FONT      = SplitFlapFont.DIN_CONDENSED;
    private static final double                  FLIP_TIME      = 100;
    private static final int                     CHARACTERS     = 15;
    private static final long                    SLIDE_INTERVAL = 7_500_000_000L;
    private static final Image                   BKG_IMAGE      = new Image(Board.class.getResourceAsStream("background.png"));
    private static final Image                   DEP_ICON       = new Image(Board.class.getResourceAsStream("departure1_icon.png"), 96, 96, true, true);
    private static final Image                   ARR_ICON       = new Image(Board.class.getResourceAsStream("arrival1_icon.png"), 96, 96, true, true);
    private final        ImageView               BKG_VIEW       = new ImageView(BKG_IMAGE);
    private final        ImageView               DEP_VIEW       = new ImageView(DEP_ICON);
    private final        ImageView               ARR_VIEW       = new ImageView(ARR_ICON);
    private final        Row1                    row0           = new Row1(CharacterSet.ALPHA, FLIP_FONT, CHARACTERS, FLIP_TIME, Constants.AZUL_BLUE_DARKER, Color.WHITE, true, true);
    private final        Row1                    row1           = new Row1(CharacterSet.ALPHA, FLIP_FONT, CHARACTERS, FLIP_TIME, Constants.AZUL_BLUE_DARKER, Color.WHITE, true, true);
    private final        Row1                    row2           = new Row1(CharacterSet.ALPHA, FLIP_FONT, CHARACTERS, FLIP_TIME, Constants.AZUL_BLUE_DARKER, Color.WHITE, true, true);
    private final        Row1                    row3           = new Row1(CharacterSet.ALPHA, FLIP_FONT, CHARACTERS, FLIP_TIME, Constants.AZUL_BLUE_DARKER, Color.WHITE ,true, true);
    private              Label                   titleLabel;
    private              HBox                    titleBox;
    private              VBox                    vBox;
    private              int                     screenCounter;
    private              AtomicBoolean           flipping;
    private              Map<Row1, Boolean>      flipMap;
    private              EventHandler<FlipEvent> flipFinishedHandler;
    private              long                    lastTimerCall;
    private              AnimationTimer          timer;


    @Override public void init() {
        BKG_VIEW.setOpacity(0.13);

        titleLabel = new Label("ARRIVALS");
        titleLabel.setFont(Fonts.bebasNeue(128));
        titleLabel.setTextFill(Constants.WHITE);

        //titleBox = new HBox(40, DEP_VIEW, titleLabel); // Intor screen
        titleBox = new HBox(40, ARR_VIEW, titleLabel); // Thank you screen
        titleBox.setAlignment(Pos.CENTER_LEFT);

        vBox                = new VBox(10, titleBox, row0, row1, row2, row3);
        vBox.setAlignment(Pos.CENTER);
        vBox.setOpacity(0.75);
        vBox.setPadding(new Insets(20, 20, 50, 50));
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
        lastTimerCall       = System.nanoTime() - SLIDE_INTERVAL;
        timer               = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                if (now - SLIDE_INTERVAL > lastTimerCall) {
                    switch (screenCounter) {
                        case 0  -> setFirstScreen();
                        case 1  -> setSecondScreen();
                        case 2  -> setThirdScreen();
                        default -> reset();
                    }
                    lastTimerCall = now;
                }
            }
        };
        registerListeners();
    }

    private void registerListeners() {
        //row0.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
        //row1.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
        //row2.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
        //row3.addEventHandler(FlipEvent.FLIP_FINISHED, flipFinishedHandler);
    }


    private void setFirstScreen() {
        row0.setText("               ");
        row1.setText("      NEXT     ");
        row2.setText("     FLIGHT    ");
        row3.setText("               ");
        screenCounter++;
    }

    private void setSecondScreen() {
        row0.setText("      THE      ");
        row1.setText("    JOURNEY    ");
        row2.setText("               ");
        row3.setText("               ");
        screenCounter++;
    }

    private void setThirdScreen() {
        row0.setText("      THE      ");
        row1.setText("    JOURNEY    ");
        row2.setText("               ");
        row3.setText("GERRIT GRUNWALD");
        screenCounter++;
    }

    private void setThankYouScreen() {
        row0.setText("               ");
        row1.setText("     THANK     ");
        row2.setText("      YOU      ");
        row3.setText("               ");
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
        StackPane pane = new StackPane(BKG_VIEW, vBox);
        pane.setPadding(new Insets(10));
        pane.setBackground(new Background(new BackgroundFill(PANEL_COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(20);
        camera.setNearClip(0.0);

        Scene scene = new Scene(pane, 1920, 1080, Constants.AZUL_BLUE);
        scene.setCamera(camera);
        //scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> timer.start());
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> setThankYouScreen());

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        //selfCheck();
        //timer.start();
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) { launch(args); }
}
