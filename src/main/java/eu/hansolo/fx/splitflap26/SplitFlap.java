package eu.hansolo.fx.splitflap26;

import eu.hansolo.fx.splitflap26.fonts.Fonts;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.MotionBlur;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


@DefaultProperty("children")
public class SplitFlap extends Region {
    private static final double                PREFERRED_WIDTH  = 150;
    private static final double                PREFERRED_HEIGHT = 200;
    private static final double                MINIMUM_WIDTH    = 50;
    private static final double                MINIMUM_HEIGHT   = 50;
    private static final double                MAXIMUM_WIDTH    = 1024;
    private static final double                MAXIMUM_HEIGHT   = 1024;
    private static final double                ASPECT_RATIO     = PREFERRED_HEIGHT / PREFERRED_WIDTH;
    private              double                halfFlipTime;
    private              double                width;
    private              double                height;
    private              double                flapWidth;
    private              double                flapHeight;
    private              double                flapCornerRadius;
    private              double                flapCenterX;
    private              double                fontOffsetY;
    private              Canvas                backTopCanvas;
    private              GraphicsContext       backTopCtx;
    private              Canvas                backBottomCanvas;
    private              GraphicsContext       backBottomCtx;
    private              Canvas                flapTopCanvas;
    private              GraphicsContext       flapTopCtx;
    private              Canvas                flapBottomCanvas;
    private              GraphicsContext       flapBottomCtx;
    private              Pane                  pane;
    private              Color                 _backgroundColor;
    private              ObjectProperty<Color> backgroundColor;
    private              Color                 _textColor;
    private              ObjectProperty<Color> textColor;
    private              double                fontSize;
    private              Font                  font;
    private              CharacterSet          characterSet;
    private final        String[]              selectedCharacterSet;
    private              int                   nextIndex;
    private              int                   selectedIndex;
    private              int                   prevIndex;
    private              String                currentCharacter;
    private              String                targetCharacter;
    private final        Rotate                rotateTopFlap;
    private final        Rotate                rotateBottomFlap;
    private final        Timeline              timelineTopFlap;
    private final        Timeline              timelineBottomFlap;
    private              boolean               isFlipping;



    // ******************** Constructors **************************************
    public SplitFlap() {
        this(CharacterSet.ALPHA_NUMERIC, Constants.GRAY, Constants.WHITE);
    }
    public SplitFlap(CharacterSet characterSet) {
        this(characterSet, Constants.GRAY, Constants.WHITE);
    }
    public SplitFlap(final Color backgroundColor, final Color textColor) {
        this(CharacterSet.ALPHA_NUMERIC, backgroundColor, textColor);
    }
    public SplitFlap(final Color backgroundColor, final Color textColor, final double flipTime) {
        this(CharacterSet.ALPHA_NUMERIC, backgroundColor, textColor, flipTime);
    }
    public SplitFlap(final CharacterSet characterSet, final Color backgroundColor, final Color textColor) {
        this(characterSet, backgroundColor, textColor, Constants.DEFAULT_FLIP_TIME);
    }
    public SplitFlap(final CharacterSet characterSet, final Color backgroundColor, final Color textColor, final double flipTime) {
        if (flipTime < 10 || flipTime > 1000) { throw new IllegalArgumentException("Flip time must be within 10-1000 ms"); }
        this.characterSet         = characterSet;
        this.selectedCharacterSet = characterSet.characters;
        this._backgroundColor     = backgroundColor;
        this._textColor           = textColor;
        this.fontSize             = 250;
        this.font                 = Fonts.bebasNeue(this.fontSize);
        this.nextIndex            = 1;
        this.selectedIndex        = 0;
        this.prevIndex            = selectedCharacterSet.length - 1;
        this.currentCharacter     = selectedCharacterSet[selectedIndex];
        this.targetCharacter      = selectedCharacterSet[selectedIndex];
        this.rotateTopFlap        = new Rotate(0, Rotate.X_AXIS);
        this.rotateBottomFlap     = new Rotate(90, Rotate.X_AXIS);
        this.timelineTopFlap      = new Timeline();
        this.timelineBottomFlap   = new Timeline();
        this.halfFlipTime         = flipTime / 2;
        this.isFlipping           = false;
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

        // Top half of background
        backTopCanvas    = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        backTopCtx       = backTopCanvas.getGraphicsContext2D();
        backTopCtx.setTextBaseline(VPos.CENTER);
        backTopCtx.setTextAlign(TextAlignment.CENTER);

        // Bottom half of background
        backBottomCanvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        backBottomCtx    = backBottomCanvas.getGraphicsContext2D();
        backBottomCtx.setTextBaseline(VPos.CENTER);
        backBottomCtx.setTextAlign(TextAlignment.CENTER);

        // Top half of flap
        flapTopCanvas    = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        flapTopCanvas.getTransforms().add(rotateTopFlap);
        flapTopCanvas.setCache(true);
        flapTopCanvas.setCacheHint(CacheHint.ROTATE);
        flapTopCanvas.setCacheHint(CacheHint.SPEED);
        flapTopCtx       = flapTopCanvas.getGraphicsContext2D();
        flapTopCtx.setTextBaseline(VPos.CENTER);
        flapTopCtx.setTextAlign(TextAlignment.CENTER);

        // Bottom half of flap
        flapBottomCanvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        flapBottomCanvas.getTransforms().add(rotateBottomFlap);
        flapBottomCanvas.setCache(true);
        flapBottomCanvas.setCacheHint(CacheHint.ROTATE);
        flapBottomCanvas.setCacheHint(CacheHint.SPEED);
        flapBottomCanvas.setVisible(false);
        flapBottomCtx    = flapBottomCanvas.getGraphicsContext2D();
        flapBottomCtx.setTextBaseline(VPos.CENTER);
        flapBottomCtx.setTextAlign(TextAlignment.CENTER);

        pane = new Pane(backTopCanvas, backBottomCanvas, flapTopCanvas, flapBottomCanvas);

        getChildren().setAll(pane);
    }

    private void registerListeners() {
        widthProperty().addListener(_ -> resize());
        heightProperty().addListener(_ -> resize());

        timelineTopFlap.setOnFinished(_ -> {
            flapBottomCanvas.setVisible(true);
            timelineBottomFlap.play();
        });
        timelineBottomFlap.setOnFinished(_ -> {
            flapBottomCanvas.setVisible(false);
            rotateTopFlap.setAngle(0);
            rotateBottomFlap.setAngle(90);
            nextIndex++;
            if (nextIndex >= selectedCharacterSet.length) { nextIndex = 0; }

            selectedIndex = nextIndex - 1;
            if (selectedIndex < 0) { selectedIndex = selectedCharacterSet.length - 1; }

            prevIndex = selectedIndex - 1;
            if (prevIndex < 0) { prevIndex = selectedCharacterSet.length - 1; }

            this.currentCharacter = selectedCharacterSet[selectedIndex];
            redraw();
            this.isFlipping = false;

            if (!this.currentCharacter.equals(this.targetCharacter)) { flip(); }
        });
    }


    // ******************** Methods *******************************************
    public CharacterSet getCharacterSet() { return this.characterSet; }

    public Color getBackgroundColor() { return null == this.backgroundColor ? this._backgroundColor : this.backgroundColor.get(); }
    public void setBackgroundColor(final Color backgroundColor) {
        if (null == this.backgroundColor) {
            this._backgroundColor = backgroundColor;
            redraw();
        } else {
            this.backgroundColor.set(backgroundColor);
        }
    }
    public ObjectProperty<Color> backgroundColorProperty() {
        if (null == this.backgroundColor) {
            this.backgroundColor = new ObjectPropertyBase<>(_backgroundColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return SplitFlap.this; }
                @Override public String getName() { return "backgroundColor"; }
            };
            this._backgroundColor = null;
        }
        return backgroundColor;
    }

    public Color getTextColor() { return null == this.textColor ? this._textColor : this.textColor.get(); }
    public void setTextColor(final Color textColor) {
        if (null == this.textColor) {
            this._textColor = textColor;
            redraw();
        } else {
            this.textColor.set(textColor);
        }
    }
    public ObjectProperty<Color> textColorProperty() {
        if (null == this.textColor) {
            this.textColor = new ObjectPropertyBase<>(_textColor) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return SplitFlap.this; }
                @Override public String getName() { return "textColor"; }
            };
            this._textColor = null;
        }
        return this.textColor;
    }

    public void setCharacter(final String character) {
        if (!characterSetContainsCharacter(character)) { throw new IllegalArgumentException("Character " +  character + " is not in current character set ( " + characterSet.name() + ")"); }
        if (isFlipping) { return; }
        this.targetCharacter = character;
        flip();
    }

    public void setFlipTime(final double flipTime) {
        if (flipTime < 10 || flipTime > 1000) { throw new IllegalArgumentException("Flip time must be within 10-1000 ms"); }
        this.halfFlipTime = flipTime * 0.5;
    }
    public double getFlipTime() { return this.halfFlipTime * 2; }

    public void reset() { setCharacter(" "); }

    public boolean canFlip() { return !isFlipping; }

    private void flip() {
        if (this.currentCharacter.equals(this.targetCharacter) || isFlipping) { return; }

        timelineTopFlap.stop();
        timelineBottomFlap.stop();

        KeyValue keyValueTopFlap0    = new KeyValue(rotateTopFlap.angleProperty(), 90, Interpolator.EASE_IN);
        KeyFrame keyFrameTopFlap0    = new KeyFrame(Duration.millis(halfFlipTime), keyValueTopFlap0);
        timelineTopFlap.getKeyFrames().setAll(keyFrameTopFlap0);

        KeyValue keyValueBottomFlap0 = new KeyValue(rotateBottomFlap.angleProperty(), 0, Interpolator.EASE_OUT);
        KeyFrame keyFrameBottomFlap0 = new KeyFrame(Duration.millis(halfFlipTime), keyValueBottomFlap0);
        timelineBottomFlap.getKeyFrames().setAll(keyFrameBottomFlap0);

        isFlipping = true;
        timelineTopFlap.play();
    }

    private boolean characterSetContainsCharacter(final String character) {
        for (String string : selectedCharacterSet) { if (string.equals(character)) { return true; } }
        return false;
    }

    @Override protected double computeMinWidth(final double height) { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double width) { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double height) { return super.computePrefWidth(height); }
    @Override protected double computePrefHeight(final double width) { return super.computePrefHeight(width); }
    @Override protected double computeMaxWidth(final double height) { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double width) { return MAXIMUM_HEIGHT; }


    // ******************** Resize/Redraw *************************************
    private void resize() {
        width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        height = getHeight() - getInsets().getTop() - getInsets().getBottom();

        if (ASPECT_RATIO * width > height) {
            width = 1 / (ASPECT_RATIO / height);
        } else if (1 / (ASPECT_RATIO / height) > width) {
            height = ASPECT_RATIO * width;
        }

        if (width > 0 && height > 0) {
            final double flapOffset  = Math.min(width, height) * 0.025;
            final double flapOffset1 = flapOffset * 0.5;
            flapWidth        = width * 0.95;
            flapHeight       = height / 2.0 * 0.95;
            flapCornerRadius = height * 0.08;
            flapCenterX      = flapWidth * 0.5;
            fontOffsetY      = height * 0.053;
            fontSize         = height * 1.05;
            font             = Fonts.bebasNeue(fontSize);

            rotateTopFlap.setPivotY(height * 0.5);
            //rotateBottomFlap.setPivotY(-flapOffset1);

            backTopCanvas.setWidth(width);
            backTopCanvas.setHeight(flapHeight);
            backTopCanvas.relocate(flapOffset, flapOffset);

            backBottomCanvas.setWidth(width);
            backBottomCanvas.setHeight(flapHeight);
            backBottomCanvas.relocate(flapOffset, flapOffset + flapOffset1 + flapHeight);

            flapTopCanvas.setWidth(width);
            flapTopCanvas.setHeight(flapHeight);
            flapTopCanvas.relocate(flapOffset, flapOffset);

            flapBottomCanvas.setWidth(width);
            flapBottomCanvas.setHeight(flapHeight);
            flapBottomCanvas.relocate(flapOffset, flapOffset + flapOffset1 + flapHeight);

            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            redraw();
        }
    }

    private void redraw() {
        redrawBackTop();
        redrawBackBottom();
        redrawFlapTop();
        redrawFlapBottom();
    }

    private void redrawBackTop() {
        backTopCtx.setFont(font);
        backTopCtx.clearRect(0, 0, width, height);
        backTopCtx.setFill(getBackgroundColor());
        backTopCtx.fillRoundRect(0, 0, flapWidth, flapHeight, flapCornerRadius, flapCornerRadius);
        backTopCtx.fillRect(0, flapCornerRadius, flapWidth, flapHeight);
        backTopCtx.setFill(getTextColor());
        backTopCtx.fillText(selectedCharacterSet[nextIndex], flapCenterX, fontOffsetY + flapHeight);
    }

    private void redrawBackBottom() {
        backBottomCtx.setFont(font);
        backBottomCtx.clearRect(0, 0, width, height);
        backBottomCtx.setFill(getBackgroundColor());
        backBottomCtx.fillRoundRect(0, 0, flapWidth, flapHeight, flapCornerRadius, flapCornerRadius);
        backBottomCtx.fillRect(0, -flapCornerRadius, flapWidth, flapHeight);
        backBottomCtx.setFill(getTextColor());
        backBottomCtx.fillText(selectedCharacterSet[selectedIndex], flapCenterX, fontOffsetY);
    }

    private void redrawFlapTop() {
        flapTopCtx.setFont(font);
        flapTopCtx.clearRect(0, 0, width, height);
        flapTopCtx.setFill(getBackgroundColor());
        flapTopCtx.fillRoundRect(0, 0, flapWidth, flapHeight, flapCornerRadius, flapCornerRadius);
        flapTopCtx.fillRect(0, flapCornerRadius, flapWidth, flapHeight);
        flapTopCtx.setFill(getTextColor());
        flapTopCtx.fillText(selectedCharacterSet[selectedIndex], flapCenterX, fontOffsetY + flapHeight);
    }

    private void redrawFlapBottom() {
        flapBottomCtx.setFont(font);
        flapBottomCtx.clearRect(0, 0, width, height);
        flapBottomCtx.setFill(getBackgroundColor());
        flapBottomCtx.fillRoundRect(0, 0, flapWidth, flapHeight, flapCornerRadius, flapCornerRadius);
        flapBottomCtx.fillRect(0, -flapCornerRadius, flapWidth, flapHeight);
        flapBottomCtx.setFill(getTextColor());
        flapBottomCtx.fillText(selectedCharacterSet[nextIndex], flapCenterX, fontOffsetY);
    }
}