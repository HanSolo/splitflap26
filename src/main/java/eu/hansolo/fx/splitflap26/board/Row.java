package eu.hansolo.fx.splitflap26.board;

import eu.hansolo.fx.splitflap26.CharacterSet;
import eu.hansolo.fx.splitflap26.Constants;
import eu.hansolo.fx.splitflap26.SplitFlap;
import javafx.beans.DefaultProperty;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.Arrays;


@DefaultProperty("children")
public class Row extends Region {
    private static final double       PREFERRED_WIDTH  = 1650;
    private static final double       PREFERRED_HEIGHT = 200;
    private static final double       MINIMUM_WIDTH    = 150;
    private static final double       MINIMUM_HEIGHT   = 20;
    private static final double       MAXIMUM_WIDTH    = 4096;
    private static final double       MAXIMUM_HEIGHT   = 1024;
    private static final double       ASPECT_RATIO     = PREFERRED_HEIGHT / PREFERRED_WIDTH;
    private static final int          NO_OF_CHARACTERS = 11;
    private              double       width;
    private              double       height;
    private              SplitFlap[]  splitFlaps;
    private              Pane         pane;
    private              CharacterSet characterSet;
    private              Color        backgroundColor;
    private              Color        textColor;
    private              double       flipTime;


    // ******************** Constructors **************************************
    public Row() {
        this(CharacterSet.ALPHA_NUMERIC, Constants.GRAY, Constants.WHITE, Constants.DEFAULT_FLIP_TIME);
    }
    public Row(final Color backgroundColor, final Color textColor, final double flipTime) {
        this(CharacterSet.ALPHA_NUMERIC, backgroundColor, textColor, flipTime);
    }
    public Row(final CharacterSet characterSet, final Color backgroundColor, final Color textColor, final double flipTime) {
        this.characterSet    = characterSet;
        this.backgroundColor = backgroundColor;
        this.textColor       = textColor;
        this.flipTime        = flipTime;
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(getPrefWidth(), 0.0) <= 0 || Double.compare(getPrefHeight(), 0.0) <= 0 || Double.compare(getWidth(), 0.0) <= 0 || Double.compare(getHeight(), 0.0) <= 0) {
            if (getPrefWidth() > 0 && getPrefHeight() > 0) {
                setPrefSize(getPrefWidth(), getPrefHeight());
            } else {
                setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        splitFlaps = new SplitFlap[NO_OF_CHARACTERS];
        for (int i = 0; i < NO_OF_CHARACTERS; i++) {
            splitFlaps[i] = new SplitFlap(characterSet, backgroundColor, textColor, flipTime);
        }

        pane = new Pane();
        pane.getChildren().addAll(Arrays.asList(splitFlaps));

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> resize());
        heightProperty().addListener(o -> resize());
    }


    // ******************** Methods *******************************************
    public void setText(final String text) {
        if (text.length() > NO_OF_CHARACTERS) { throw new IllegalArgumentException("Text too long (max " + splitFlaps.length + " characters)"); }
        for (int i = 0 ; i < text.length(); i++) {
            splitFlaps[i].setCharacter(String.valueOf(text.charAt(i)));
        }
    }

    public void reset() {
        for (int i = 0 ; i < NO_OF_CHARACTERS; i++) {
            splitFlaps[i].reset();
        }
    }

    @Override protected double computeMinWidth(final double height) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double width) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double height) { return super.computePrefWidth(height); }
    @Override protected double computePrefHeight(final double width) { return super.computePrefHeight(width); }
    @Override protected double computeMaxWidth(final double height) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double width) { return MAXIMUM_HEIGHT; }


    // ******************** Layout *******************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (ASPECT_RATIO * width > height) {
            width = 1 / (ASPECT_RATIO / height);
        } else if (1 / (ASPECT_RATIO / height) > width) {
            height = ASPECT_RATIO * width;
        }

        if (width > 0 && height > 0) {
            for (int i = 0; i < splitFlaps.length; i++) {
                splitFlaps[i].setPrefSize(height * 0.75, height);
                splitFlaps[i].relocate(i * splitFlaps[i].getPrefWidth(), 0);
            }

            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);
        }
    }
}