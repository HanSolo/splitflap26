package eu.hansolo.fx.splitflap26.clock;

import eu.hansolo.fx.splitflap26.Constants;
import eu.hansolo.fx.splitflap26.fonts.Fonts;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalTime;
import java.time.temporal.ChronoField;


public class Clock extends Application {
    private static final Color          PANEL_COLOR      = Color.BLACK;
    private static final Color          BACKGROUND_COLOR = Constants.GRAY;
    private static final Color          TEXT_COLOR       = Constants.WHITE;
    private static final double         FLIP_TIME        = 500;
    private final        Label          colon            =  new Label(":");
    private final        Hours          hours            = new Hours(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private final        Minutes        minutes          = new Minutes(BACKGROUND_COLOR, TEXT_COLOR, FLIP_TIME);
    private              HBox           timeBox;
    private              long           lastTimerCall;
    private              AnimationTimer timer;


    @Override public void init() {
        colon.setFont(Fonts.bebasNeue(150));
        colon.setTextFill(TEXT_COLOR);
        colon.setTranslateY(-15);
        timeBox       = new HBox(0, hours, colon, minutes);
        timeBox.setAlignment(Pos.CENTER);
        lastTimerCall = System.nanoTime() - 1_000_000_000L;
        timer         = new AnimationTimer() {
            @Override public void handle(final long now) {
                if (now - 1_000_000_000L > lastTimerCall) {
                    update();
                    lastTimerCall = now;
                }
            }
        };
    }

    private void update() {
        final LocalTime currentTime  = LocalTime.now();
        final int       hour         = currentTime.get(ChronoField.HOUR_OF_DAY);
        final int       minute       = currentTime.get(ChronoField.MINUTE_OF_HOUR);
        final String    hoursLeft    = hour < 10 ? "0" : String.valueOf(Integer.toString(hour).charAt(0));
        final String    hoursRight   = String.valueOf(Integer.toString(hour).charAt(hour < 10 ? 0 : 1));
        final String    minutesLeft  = minute < 10 ? "0" : String.valueOf(Integer.toString(minute).charAt(0));
        final String    minutesRight = String.valueOf(Integer.toString(minute).charAt(minute < 10 ? 0 : 1));
        hours.update(hoursLeft, hoursRight);
        minutes.update(minutesLeft, minutesRight);
    }


    @Override public void start(final Stage stage) {
        StackPane pane = new StackPane(timeBox);
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
