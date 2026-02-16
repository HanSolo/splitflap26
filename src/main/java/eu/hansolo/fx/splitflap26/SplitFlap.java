package eu.hansolo.fx.splitflap26;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;


@DefaultProperty("children")
public class SplitFlap extends Region {
    public static final  double                  DEFAULT_FLIP_TIME = 100;
    public static final  double                  PREFERRED_WIDTH   = 120;
    public static final  double                  PREFERRED_HEIGHT  = 200;
    private static final double                  MINIMUM_WIDTH     = 50;
    private static final double                  MINIMUM_HEIGHT    = 50;
    private static final double                  MAXIMUM_WIDTH     = 1024;
    private static final double                  MAXIMUM_HEIGHT    = 1024;
    private static final double                  ASPECT_RATIO      = PREFERRED_HEIGHT / PREFERRED_WIDTH;
    private              double                  halfFlipTime;
    private              double                  width;
    private              double                  height;
    private              double                  flapWidth;
    private              double                  flapHeight;
    private              double                  flapCenterX;
    private              double                  fontOffsetY;
    private              double                  fontOffsetYBottom;
    private              Canvas                  backCanvas;
    private              GraphicsContext         backCtx;
    private              Canvas                  backTopCanvas;
    private              GraphicsContext         backTopCtx;
    private              Canvas                  backBottomCanvas;
    private              GraphicsContext         backBottomCtx;
    private              Canvas                  flapTopCanvas;
    private              GraphicsContext         flapTopCtx;
    private              Canvas                  flapBottomCanvas;
    private              GraphicsContext         flapBottomCtx;
    private              Pane                    pane;
    private              LinearGradient          axisGradient;
    private              Color                   _backgroundColor;
    private              ObjectProperty<Color>   backgroundColor;
    private              Color                   topBackgroundColor;
    private              Color                   bottomBackgroundColor;
    private              Color                   topFlapBackgroundColor;
    private              Color                   bottomFlapBackgroundColor;
    private              Color                   _textColor;
    private              ObjectProperty<Color>   textColor;
    private              Color                   topTextColor;
    private              Color                   bottomTextColor;
    private              boolean                 _axisVisible;
    private              BooleanProperty         axisVisible;
    private              boolean                 _shaded;
    private              BooleanProperty         shaded;
    private              double                  fontSize;
    private              SplitFlapFont           _splitFlapFont;
    private              ObjectProperty<SplitFlapFont>    splitFlapFont;
    private              Font                    font;
    private              CharacterSet            _characterSet;
    private              ObjectProperty<CharacterSet> characterSet;
    private              String[]                selectedCharacterSet;
    private              int                     nextIndex;
    private              int                     selectedIndex;
    private              int                     prevIndex;
    private              String                  currentCharacter;
    private              String                  targetCharacter;
    private final        Rotate                  rotateTopFlap;
    private final        Rotate                  rotateBottomFlap;
    private final        Timeline                timelineTopFlap;
    private final        Timeline                timelineBottomFlap;
    private final        BooleanProperty         flipping;
    private final        ChangeListener<Boolean> flippingListener;



    // ******************** Constructors **************************************
    public SplitFlap() {
        this(CharacterSet.ALPHA_NUMERIC, SplitFlapFont.BEBAS, Constants.GRAY, Constants.WHITE, DEFAULT_FLIP_TIME, true, false);
    }
    public SplitFlap(CharacterSet characterSet) {
        this(characterSet, SplitFlapFont.BEBAS, Constants.GRAY, Constants.WHITE, DEFAULT_FLIP_TIME, true, false);
    }
    public SplitFlap(final Color backgroundColor, final Color textColor) {
        this(CharacterSet.ALPHA_NUMERIC, SplitFlapFont.BEBAS, backgroundColor, textColor, DEFAULT_FLIP_TIME, true, false);
    }
    public SplitFlap(final Color backgroundColor, final Color textColor, final double flipTime) {
        this(CharacterSet.ALPHA_NUMERIC, SplitFlapFont.BEBAS, backgroundColor, textColor, flipTime, true, false);
    }
    public SplitFlap(final CharacterSet characterSet, final Color backgroundColor, final Color textColor) {
        this(characterSet, SplitFlapFont.BEBAS, backgroundColor, textColor, DEFAULT_FLIP_TIME, true, false);
    }
    public SplitFlap(final CharacterSet characterSet, final SplitFlapFont splitFlapFont, final Color backgroundColor, final Color textColor, final double flipTime, final boolean axisVisible, final boolean shaded) {
        if (flipTime < 10 || flipTime > 1000) { throw new IllegalArgumentException("Flip time must be within 10-1000 ms"); }
        this._characterSet             = characterSet;
        this.selectedCharacterSet      = characterSet.characters;
        this.axisGradient              = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, backgroundColor.darker()), new Stop(0.23, backgroundColor.brighter().brighter()), new Stop(1, backgroundColor.darker()));
        this._backgroundColor          = backgroundColor;
        this.topBackgroundColor        = darker(backgroundColor);
        this.bottomBackgroundColor     = brighter(backgroundColor);
        this.topFlapBackgroundColor    = darker(darker(topBackgroundColor));
        this.bottomFlapBackgroundColor = darker(darker(bottomBackgroundColor));
        this._textColor                = textColor;
        this.topTextColor              = darker(textColor);
        this.bottomTextColor           = brighter(textColor);
        this._axisVisible              = axisVisible;
        this._shaded                   = shaded;
        this._splitFlapFont            = splitFlapFont;
        this.fontSize                  = splitFlapFont.font.getSize();
        this.font                      = splitFlapFont.font;
        this.nextIndex                 = 1;
        this.selectedIndex             = 0;
        this.prevIndex                 = selectedCharacterSet.length - 1;
        this.currentCharacter          = selectedCharacterSet[selectedIndex];
        this.targetCharacter           = selectedCharacterSet[selectedIndex];
        this.rotateTopFlap             = new Rotate(0, Rotate.X_AXIS);
        this.rotateBottomFlap          = new Rotate(90, Rotate.X_AXIS);
        this.timelineTopFlap           = new Timeline();
        this.timelineBottomFlap        = new Timeline();
        this.halfFlipTime              = flipTime / 2;
        this.flipping                  = new SimpleBooleanProperty(false);
        this.flippingListener          = (o, ov, nv) -> { if (!nv) { setCharacter(" "); } };

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

        // Control background with "rotation axis"
        backCanvas = new  Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT);
        backCtx    = backCanvas.getGraphicsContext2D();

        // Top half of background
        backTopCanvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        backTopCtx    = backTopCanvas.getGraphicsContext2D();
        backTopCtx.setTextBaseline(VPos.CENTER);
        backTopCtx.setTextAlign(TextAlignment.CENTER);
        backCanvas.setVisible(isAxisVisible());
        backCanvas.setManaged(isAxisVisible());

        // Bottom half of background
        backBottomCanvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        backBottomCtx    = backBottomCanvas.getGraphicsContext2D();
        backBottomCtx.setTextBaseline(VPos.CENTER);
        backBottomCtx.setTextAlign(TextAlignment.CENTER);

        // Top half of flap
        flapTopCanvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        flapTopCanvas.getTransforms().add(rotateTopFlap);
        flapTopCanvas.setCache(true);
        flapTopCanvas.setCacheHint(CacheHint.ROTATE);
        flapTopCanvas.setCacheHint(CacheHint.SPEED);
        flapTopCtx = flapTopCanvas.getGraphicsContext2D();
        flapTopCtx.setTextBaseline(VPos.CENTER);
        flapTopCtx.setTextAlign(TextAlignment.CENTER);

        // Bottom half of flap
        flapBottomCanvas = new Canvas(PREFERRED_WIDTH, PREFERRED_HEIGHT * 0.5);
        flapBottomCanvas.getTransforms().add(rotateBottomFlap);
        flapBottomCanvas.setCache(true);
        flapBottomCanvas.setCacheHint(CacheHint.ROTATE);
        flapBottomCanvas.setCacheHint(CacheHint.SPEED);
        flapBottomCanvas.setVisible(false);
        flapBottomCtx = flapBottomCanvas.getGraphicsContext2D();
        flapBottomCtx.setTextBaseline(VPos.CENTER);
        flapBottomCtx.setTextAlign(TextAlignment.CENTER);

        pane = new Pane(backCanvas, backTopCanvas, backBottomCanvas, flapTopCanvas, flapBottomCanvas);
        pane.setBackground(Background.EMPTY);

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
            this.flipping.set(false);
            this.flipping.removeListener(flippingListener);

            if (this.currentCharacter.equals(this.targetCharacter)) {
                fireEvent(new FlipEvent(SplitFlap.this, null, FlipEvent.FLIP_FINISHED));
            } else {
                flip();
            }
        });
    }


    // ******************** Methods *******************************************
    public CharacterSet getCharacterSet() { return null == this.characterSet ? this._characterSet : this.characterSet.get(); }
    public void setCharacterSet(final CharacterSet characterSet) {
        if (null == this.characterSet) {
            this._characterSet        = characterSet;
            this.selectedCharacterSet = characterSet.characters;
            redraw();
        } else {
            this.characterSet.set(characterSet);
        }
    }
    public ObjectProperty<CharacterSet> characterSetProperty() {
        if (null == this.characterSet) {
            this.characterSet = new ObjectPropertyBase<>(_characterSet) {
                @Override protected void invalidated() {
                    selectedCharacterSet = get().characters;
                    redraw();
                }
                @Override public Object getBean() { return SplitFlap.this; }
                @Override public String getName() { return "characterSet"; }
            };
        }

        return this.characterSet;
    }

    public Color getBackgroundColor() { return null == this.backgroundColor ? this._backgroundColor : this.backgroundColor.get(); }
    public void setBackgroundColor(final Color backgroundColor) {
        if (null == this.backgroundColor) {
            this.axisGradient              = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, backgroundColor.darker()), new Stop(0.23, backgroundColor.brighter().brighter()), new Stop(1, backgroundColor.darker()));
            this._backgroundColor          = backgroundColor;
            this.topBackgroundColor        = darker(backgroundColor);
            this.bottomBackgroundColor     = brighter(backgroundColor);
            this.topFlapBackgroundColor    = darker(darker(topBackgroundColor));
            this.bottomFlapBackgroundColor = darker(darker(bottomBackgroundColor));
            redraw();
        } else {
            this.backgroundColor.set(backgroundColor);
        }
    }
    public ObjectProperty<Color> backgroundColorProperty() {
        if (null == this.backgroundColor) {
            this.backgroundColor = new ObjectPropertyBase<>(_backgroundColor) {
                @Override protected void invalidated() {
                    axisGradient              = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, get().darker()), new Stop(0.23, get().brighter().brighter()), new Stop(1, get().darker()));
                    topBackgroundColor        = darker(get());
                    bottomBackgroundColor     = brighter(get());
                    topFlapBackgroundColor    = darker(darker(topBackgroundColor));
                    bottomFlapBackgroundColor = darker(darker(bottomBackgroundColor));

                    redraw();
                }
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
            this._textColor      = textColor;
            this.topTextColor    = darker(textColor);
            this.bottomTextColor = brighter(textColor);
            redraw();
        } else {
            this.textColor.set(textColor);
        }
    }
    public ObjectProperty<Color> textColorProperty() {
        if (null == this.textColor) {
            this.textColor = new ObjectPropertyBase<>(_textColor) {
                @Override protected void invalidated() {
                    topTextColor    = darker(get());
                    bottomTextColor = brighter(get());
                    redraw();
                }
                @Override public Object getBean() { return SplitFlap.this; }
                @Override public String getName() { return "textColor"; }
            };
            this._textColor = null;
        }
        return this.textColor;
    }

    public boolean isAxisVisible() { return null == axisVisible ? _axisVisible : axisVisible.get(); }
    public void setAxisVisible(final boolean axisVisible) {
        if (null == this.axisVisible) {
            backCanvas.setVisible(axisVisible);
            backCanvas.setManaged(axisVisible);
            redraw();
        } else {
            this.axisVisible.set(axisVisible);
        }
    }
    public BooleanProperty axisVisibleProperty() {
        if (null == this.axisVisible) {
            this.axisVisible = new BooleanPropertyBase(_axisVisible) {
                @Override protected void invalidated() {
                    backCanvas.setVisible(get());
                    backCanvas.setManaged(get());
                    redraw();
                }
                @Override public Object getBean() { return SplitFlap.this; }
                @Override public String getName() { return "axisVisible"; }
            };
        }
        return this.axisVisible;
    }

    public boolean isShaded() {return null == this.shaded ? this._shaded : this.shaded.get(); }
    public void setShaded(final boolean shaded) {
        if (null == this.shaded) {
            this._shaded = shaded;
            redraw();
        } else {
            this.shaded.set(shaded);
        }
    }
    public BooleanProperty shadedProperty() {
        if (null == this.shaded) {
            this.shaded = new BooleanPropertyBase(_shaded) {
                @Override protected void invalidated() { redraw(); }
                @Override public Object getBean() { return SplitFlap.this; }
                @Override public String getName() { return "shaded"; }
            };
        }
        return shaded;
    }

    public SplitFlapFont getFont() { return null == this.splitFlapFont ? this._splitFlapFont : this.splitFlapFont.get(); }
    public void setFont(final SplitFlapFont splitFlapFont) {
        if (null == this.splitFlapFont) {
            this._splitFlapFont = splitFlapFont;
            this.font           = SplitFlapFont.getFontAtSize(splitFlapFont, this.fontSize);
            redraw();
        } else {
            this.splitFlapFont.set(splitFlapFont);
        }
    }
    public ObjectProperty<SplitFlapFont> fontProperty() {
        if (null == this.splitFlapFont) {
            this.splitFlapFont = new ObjectPropertyBase<>(_splitFlapFont) {
                @Override protected void invalidated() {
                    font = SplitFlapFont.getFontAtSize(get(), fontSize);
                    redraw();
                }
                @Override public Object getBean() { return SplitFlap.this; }
                @Override public String getName() { return "font"; }
            };
            this._splitFlapFont = null;
        }
        return this.splitFlapFont;
    }

    public boolean canFlip() { return !flipping.get(); }

    public void setCharacter(final String character) {
        if (!characterSetContainsCharacter(character)) { throw new IllegalArgumentException("Character " +  character + " is not in current character set ( " + getCharacterSet().name() + ")"); }
        if (flipping.get()) { return; }
        this.targetCharacter = character;
        fireEvent(new FlipEvent(SplitFlap.this, null, FlipEvent.FLIP_STARTED));
        flip();
    }

    public void setFlipTime(final double flipTime) {
        if (flipTime < 10 || flipTime > 5000) { throw new IllegalArgumentException("Flip time must be within 10-5000 ms"); }
        this.halfFlipTime = flipTime * 0.5;
    }
    public double getFlipTime() { return this.halfFlipTime * 2; }

    public void reset() { setCharacter(" "); }

    public void selfTest() {
        flipping.addListener(this.flippingListener);
        reset();
        setCharacter("Z");
    }

    private void flip() {
        if (this.currentCharacter.equals(this.targetCharacter) || flipping.get()) {
            fireEvent(new FlipEvent(SplitFlap.this, null, FlipEvent.FLIP_FINISHED));
            return;
        }

        timelineTopFlap.stop();
        timelineBottomFlap.stop();

        KeyValue keyValueTopFlap0    = new KeyValue(rotateTopFlap.angleProperty(), 90, Interpolator.EASE_IN);
        KeyFrame keyFrameTopFlap0    = new KeyFrame(Duration.millis(halfFlipTime), keyValueTopFlap0);
        timelineTopFlap.getKeyFrames().setAll(keyFrameTopFlap0);

        KeyValue keyValueBottomFlap0 = new KeyValue(rotateBottomFlap.angleProperty(), 180, Interpolator.LINEAR);
        KeyFrame keyFrameBottomFlap0 = new KeyFrame(Duration.millis(halfFlipTime), keyValueBottomFlap0);
        timelineBottomFlap.getKeyFrames().setAll(keyFrameBottomFlap0);

        flipping.set(true);
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

    private Color darker(final Color color) {
        return color.deriveColor(0, 1.0, 0.975, 1.0);
    }
    private Color brighter(final Color color) {
        return color.deriveColor(0, 1.0, 1.0 / 0.975, 1.0);
    }


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
            final double        flapOffset  = Math.min(width, height) * 0.01;
            final double        flapOffset1 = flapOffset * 4;
            final SplitFlapFont sff         = getFont();
            flapWidth         = width * 0.98;
            flapHeight        = height / 1.87 * 0.9;
            flapCenterX       = flapWidth * 0.5;
            fontOffsetY       = height * sff.fontOffsetYTop;
            fontOffsetYBottom = flapHeight * sff.fontOffsetYBottom;
            fontSize          = height * sff.sizeFactor;
            font              = SplitFlapFont.getFontAtSize(sff, fontSize);

            rotateTopFlap.setPivotY(height * 0.5);
            rotateBottomFlap.setPivotY(height * 0.5);

            backCanvas.setWidth(width);
            backCanvas.setHeight(height);

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
            flapBottomCanvas.relocate(flapOffset, flapOffset);

            pane.setMaxSize(width, height);
            pane.setPrefSize(width, height);
            pane.relocate((getWidth() - width) * 0.5, (getHeight() - height) * 0.5);

            redraw();
        }
    }

    private void redraw() {
        if (isAxisVisible()) { drawBack(); }
        drawBackTop();
        drawBackBottom();
        drawFlapTop();
        drawFlapBottom();
    }

    private void drawBack() {
        backCtx.clearRect(0, 0, width, height);
        backCtx.setFill(axisGradient);
        backCtx.fillRect(width * 0.0041666666666, height * 0.4525, width * 0.03333333333333, height * 0.095);
        backCtx.fillRect(width * 0.9625, height * 0.4525, width * 0.03333333333333, height * 0.095);
        backCtx.fillRect(width * 0.0375, 0.4925 * height, 0.925 * width, 0.015 * 200);
    }

    private void drawBackTop() {
        backTopCtx.setFont(font);
        backTopCtx.clearRect(0, 0, width, height);
        backTopCtx.setFill(isShaded() ? topBackgroundColor : getBackgroundColor());
        backTopCtx.beginPath();
        backTopCtx.moveTo(flapWidth * 0.965987288135593, flapHeight);
        backTopCtx.lineTo(flapWidth * 0.034012711864406, flapHeight);
        backTopCtx.lineTo(flapWidth * 0.0340127118644068, flapHeight * 0.917794871794872);
        backTopCtx.lineTo(0, flapHeight * 0.917794871794872);
        backTopCtx.lineTo(0, flapHeight * 0.0770666666666667);
        backTopCtx.bezierCurveTo(0, flapHeight * 0.0345333333333333, flapWidth * 0.0285762711864407, 0, flapWidth * 0.0637754237288136, 0);
        backTopCtx.lineTo(flapWidth * 0.936072033898305, 0);
        backTopCtx.bezierCurveTo(flapWidth * 0.971351694915254, 0, flapWidth, flapHeight * 0.0346205128205128, flapWidth, flapHeight * 0.0772512820512821);
        backTopCtx.lineTo(flapWidth, flapHeight * 0.917794871794872);
        backTopCtx.lineTo(flapWidth * 0.965987288135593, flapHeight * 0.917794871794872);
        backTopCtx.lineTo(flapWidth * 0.965987288135593, flapHeight);
        backTopCtx.closePath();
        backTopCtx.fill();
        backTopCtx.setFill(isShaded() ? topTextColor : getTextColor());
        backTopCtx.fillText(selectedCharacterSet[nextIndex], flapCenterX, fontOffsetY + flapHeight);
    }

    private void drawBackBottom() {
        backBottomCtx.setFont(font);
        backBottomCtx.clearRect(0, 0, width, height);
        backBottomCtx.setFill(isShaded() ? bottomBackgroundColor : getBackgroundColor());
        backBottomCtx.beginPath();
        backBottomCtx.moveTo(flapWidth * 0.965987288135593, 0);
        backBottomCtx.lineTo(flapWidth * 0.0340127118644068, 0);
        backBottomCtx.lineTo(flapWidth * 0.0340127118644068, flapHeight * 0.0822051282051282);
        backBottomCtx.lineTo(0, flapHeight * 0.0822051282051282);
        backBottomCtx.lineTo(0, flapHeight * 0.922933333333333);
        backBottomCtx.bezierCurveTo(0, flapHeight * 0.965466666666667, flapWidth * 0.0285762711864407, flapHeight, flapWidth * 0.0637754237288136, flapHeight);
        backBottomCtx.lineTo(flapWidth * 0.936072033898305, flapHeight);
        backBottomCtx.bezierCurveTo(flapWidth * 0.971351694915254, flapHeight, flapWidth, flapHeight * 0.965379487179487, flapWidth, flapHeight * 0.922748717948718);
        backBottomCtx.lineTo(flapWidth, flapHeight * 0.0822051282051282);
        backBottomCtx.lineTo(flapWidth * 0.965987288135593, flapHeight * 0.0822051282051282);
        backBottomCtx.lineTo(flapWidth * 0.965987288135593, 0);
        backBottomCtx.closePath();
        backBottomCtx.fill();
        backBottomCtx.setFill(isShaded() ? bottomTextColor : getTextColor());
        backBottomCtx.fillText(selectedCharacterSet[selectedIndex], flapCenterX, fontOffsetY);
    }

    private void drawFlapTop() {
        flapTopCtx.setFont(font);
        flapTopCtx.clearRect(0, 0, width, height);
        flapTopCtx.setFill(isShaded() ? topFlapBackgroundColor : getBackgroundColor());
        flapTopCtx.beginPath();
        flapTopCtx.moveTo(flapWidth * 0.965987288135593, flapHeight);
        flapTopCtx.lineTo(flapWidth * 0.034012711864406, flapHeight);
        flapTopCtx.lineTo(flapWidth * 0.0340127118644068, flapHeight * 0.917794871794872);
        flapTopCtx.lineTo(0, flapHeight * 0.917794871794872);
        flapTopCtx.lineTo(0, flapHeight * 0.0770666666666667);
        flapTopCtx.bezierCurveTo(0, flapHeight * 0.0345333333333333, flapWidth * 0.0285762711864407, 0, flapWidth * 0.0637754237288136, 0);
        flapTopCtx.lineTo(flapWidth * 0.936072033898305, 0);
        flapTopCtx.bezierCurveTo(flapWidth * 0.971351694915254, 0, flapWidth, flapHeight * 0.0346205128205128, flapWidth, flapHeight * 0.0772512820512821);
        flapTopCtx.lineTo(flapWidth, flapHeight * 0.917794871794872);
        flapTopCtx.lineTo(flapWidth * 0.965987288135593, flapHeight * 0.917794871794872);
        flapTopCtx.lineTo(flapWidth * 0.965987288135593, flapHeight);
        flapTopCtx.closePath();
        flapTopCtx.fill();
        flapTopCtx.setFill(isShaded() ? topTextColor : getTextColor());
        flapTopCtx.fillText(selectedCharacterSet[selectedIndex], flapCenterX, fontOffsetY + flapHeight);
    }

    private void drawFlapBottom() {
        flapBottomCtx.setFont(font);
        flapBottomCtx.clearRect(0, 0, width, height);
        flapBottomCtx.setFill(isShaded() ? bottomFlapBackgroundColor : getBackgroundColor());
        flapBottomCtx.beginPath();
        flapBottomCtx.moveTo(flapWidth * 0.965987288135593, flapHeight);
        flapBottomCtx.lineTo(flapWidth * 0.034012711864406, flapHeight);
        flapBottomCtx.lineTo(flapWidth * 0.0340127118644068, flapHeight * 0.917794871794872);
        flapBottomCtx.lineTo(0, flapHeight * 0.917794871794872);
        flapBottomCtx.lineTo(0, flapHeight * 0.0770666666666667);
        flapBottomCtx.bezierCurveTo(0, flapHeight * 0.0345333333333333, flapWidth * 0.0285762711864407, 0, flapWidth * 0.0637754237288136, 0);
        flapBottomCtx.lineTo(flapWidth * 0.936072033898305, 0);
        flapBottomCtx.bezierCurveTo(flapWidth * 0.971351694915254, 0, flapWidth, flapHeight * 0.0346205128205128, flapWidth, flapHeight * 0.0772512820512821);
        flapBottomCtx.lineTo(flapWidth, flapHeight * 0.917794871794872);
        flapBottomCtx.lineTo(flapWidth * 0.965987288135593, flapHeight * 0.917794871794872);
        flapBottomCtx.lineTo(flapWidth * 0.965987288135593, flapHeight);
        flapBottomCtx.closePath();
        flapBottomCtx.fill();
        flapBottomCtx.setFill(isShaded() ? bottomTextColor : getTextColor());
        flapBottomCtx.save();
        flapBottomCtx.scale(1.0, -1.0);
        flapBottomCtx.fillText(selectedCharacterSet[nextIndex], flapCenterX, fontOffsetYBottom);
        flapBottomCtx.restore();
    }
}