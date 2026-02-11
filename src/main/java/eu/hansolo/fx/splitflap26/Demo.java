package eu.hansolo.fx.splitflap26;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Demo extends Application {
    private SplitFlap splitFlap;

    @Override public void init() {
        splitFlap = new SplitFlap();
    }

    @Override public void start(final Stage stage) {
        StackPane pane  = new StackPane();
        pane.getChildren().add(splitFlap);

        //pane.setEffect(new MotionBlur(0, 2));

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(10);
        camera.setVerticalFieldOfView(true);

        Scene     scene = new Scene(pane, Color.DARKGRAY);
        scene.setCamera(camera);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) { launch(args); }
}
