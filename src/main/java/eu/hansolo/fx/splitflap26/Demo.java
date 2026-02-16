package eu.hansolo.fx.splitflap26;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Demo extends Application {
    private static final double    FLIP_TIME = 100;
    private static final boolean   SHADED    = true;
    private static       int       noOfNodes = 0;
    private              SplitFlap splitFlap1;
    private              SplitFlap splitFlap2;
    private              SplitFlap splitFlap3;
    private              SplitFlap splitFlap4;
    private              SplitFlap splitFlap5;


    @Override public void init() {
        splitFlap1 = new SplitFlap(CharacterSet.ALPHA_NUMERIC_EXTENDED);
        splitFlap1.setShaded(SHADED);
        splitFlap1.setFlipTime(FLIP_TIME);
        splitFlap1.setFont(SplitFlapFont.BEBAS);

        splitFlap2 = new SplitFlap(CharacterSet.ALPHA_NUMERIC_EXTENDED);
        splitFlap2.setShaded(SHADED);
        splitFlap2.setFlipTime(FLIP_TIME);
        splitFlap2.setFont(SplitFlapFont.BEBAS_ROUNDED);

        splitFlap3 = new SplitFlap(CharacterSet.ALPHA_NUMERIC_EXTENDED);
        splitFlap3.setShaded(SHADED);
        splitFlap3.setFlipTime(FLIP_TIME);
        splitFlap3.setFont(SplitFlapFont.SWAG_URBANO);

        splitFlap4 = new SplitFlap(CharacterSet.ALPHA_NUMERIC_EXTENDED);
        splitFlap4.setShaded(SHADED);
        splitFlap4.setFlipTime(FLIP_TIME);
        splitFlap4.setFont(SplitFlapFont.DIN_CONDENSED);
        splitFlap4.addEventHandler(FlipEvent.FLIP_STARTED, _ -> { System.out.println("FLIP STARTED"); });
        splitFlap4.addEventHandler(FlipEvent.FLIP_FINISHED, _ -> { System.out.println("FLIP_FINISHED"); });

        splitFlap5 = new SplitFlap(CharacterSet.ALPHA_NUMERIC_EXTENDED);
        splitFlap5.setShaded(SHADED);
        splitFlap5.setFlipTime(FLIP_TIME);
        splitFlap5.setFont(SplitFlapFont.DIN);
    }

    @Override public void start(final Stage stage) {
        HBox      hBox = new HBox(10, splitFlap1, splitFlap2, splitFlap3, splitFlap4, splitFlap5);
        StackPane pane = new StackPane(hBox);
        pane.setPadding(new Insets(20));

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setFieldOfView(10);
        camera.setVerticalFieldOfView(true);

        Scene     scene = new Scene(pane, Color.BLACK);
        scene.setCamera(camera);

        stage.setTitle("SplitFlap 2026");
        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        splitFlap1.setCharacter("D");
        splitFlap2.setCharacter("E");
        splitFlap3.setCharacter("M");
        splitFlap4.setCharacter("O");
        splitFlap5.setCharacter("1");

        // Calculate number of nodes
        calcNoOfNodes(pane);
        System.out.println(noOfNodes + " Nodes in SceneGraph");
    }

    @Override public void stop() {
        Platform.exit();
        System.exit(0);
    }

    private static void calcNoOfNodes(Node node) {
        if (node instanceof Parent) {
            if (((Parent) node).getChildrenUnmodifiable().size() != 0) {
                ObservableList<Node> tempChildren = ((Parent) node).getChildrenUnmodifiable();
                noOfNodes += tempChildren.size();
                for (Node n : tempChildren) { calcNoOfNodes(n); }
            }
        }
    }

    public static void main(String[] args) { launch(args); }
}
