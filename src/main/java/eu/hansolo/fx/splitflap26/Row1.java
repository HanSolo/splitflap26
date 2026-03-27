package eu.hansolo.fx.splitflap26;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;


public class Row1 extends HBox {
    private CharacterSet                  _characterSet;
    private ObjectProperty<CharacterSet>  characterSet;
    private SplitFlapFont                 _splitFlapFont;
    private ObjectProperty<SplitFlapFont> splitFlapFont;
    private int                           noOfCharacters;
    private Color                         _backgroundColor;
    private ObjectProperty<Color>         backgroundColor;
    private Color                         _textColor;
    private ObjectProperty<Color>         textColor;
    private double                        flipTime;
    private boolean                       _axisVisible;
    private BooleanProperty               axisVisible;
    private boolean                       _shaded;
    private BooleanProperty               shaded;
    private Map<SplitFlap,Boolean>        flipMap;
    private EventHandler<FlipEvent>       flipFinishedHandler;


    public Row1(final CharacterSet characterSet, final int _noOfCharacters) {
        this(characterSet, SplitFlapFont.BEBAS, _noOfCharacters, SplitFlap.DEFAULT_FLIP_TIME, Constants.GRAY, Constants.WHITE, true, true);
    }
    public Row1(final CharacterSet characterSet, final int _noOfCharacters, final double flipTime, final boolean axisVisible, final boolean shaded) {
        this(characterSet, SplitFlapFont.BEBAS, _noOfCharacters, flipTime, Constants.GRAY, Constants.WHITE, axisVisible, shaded);
    }
    public Row1(final CharacterSet characterSet, final SplitFlapFont splitFlapFont, final int noOfCharacters, final double flipTime) {
        this(characterSet, splitFlapFont, noOfCharacters, flipTime, Constants.GRAY, Constants.WHITE, true, true);
    }
    public Row1(final CharacterSet characterSet, final SplitFlapFont splitFlapFont, final int noOfCharacters, final double flipTime, final boolean axisVisible, final boolean shaded) {
        this(characterSet, splitFlapFont, noOfCharacters, flipTime, Constants.GRAY, Constants.WHITE, axisVisible, shaded);
    }
    public Row1(final CharacterSet characterSet, final int noOfCharacters, final double flipTime, final Color backgroundColor, final Color textColor, final boolean axisVisible, final boolean shaded) {
        this(characterSet, SplitFlapFont.BEBAS, noOfCharacters, flipTime, backgroundColor, textColor, axisVisible, shaded);
    }
    public Row1(final CharacterSet characterSet, final SplitFlapFont splitFlapFont, final int noOfCharacters, final double flipTime, final Color backgroundColor, final Color textColor, final boolean axisVisible, final boolean shaded) {
        this._characterSet    = characterSet;
        this._splitFlapFont   = splitFlapFont;
        this.noOfCharacters   = noOfCharacters;
        this._backgroundColor = backgroundColor;
        this._textColor       = textColor;
        this.flipTime         = flipTime;
        this._axisVisible     = axisVisible;
        this._shaded          = shaded;
        this.flipMap          = new HashMap<>(noOfCharacters);
        this.flipFinishedHandler      = e -> {
            this.flipMap.put((SplitFlap) e.getSource(), true);
            if (!this.flipMap.values().contains(false)) {
                fireEvent(new FlipEvent(Row1.this, null, FlipEvent.FLIP_FINISHED));
            };
        };

        initGraphics();
        registerListeners();
    }


    private void initGraphics() {
        for (int i = 0 ; i < this.noOfCharacters ; i++) {
            final SplitFlap splitFlap = new SplitFlap(getCharacterSet(), getSplitFlapFont(), getBackgroundColor(), getTextColor(), this.flipTime, isAxisVisible(), isShaded());
            this.flipMap.put(splitFlap, false);
            //this.splitFlaps.add(new SplitFlap(getCharacterSet(), getSplitFlapFont(), getBackgroundColor(), getTextColor(), this.flipTime, isAxisVisible(), isShaded()));
        }
        getChildren().setAll(this.flipMap.keySet());
    }

    private void registerListeners() {
        this.flipMap.keySet().forEach(splitFlap -> splitFlap.addEventHandler(FlipEvent.FLIP_FINISHED, this.flipFinishedHandler));
    }

    public void dispose() { this.flipMap.keySet().forEach(splitFlap -> splitFlap.removeEventHandler(FlipEvent.FLIP_FINISHED, this.flipFinishedHandler));}

    public CharacterSet getCharacterSet() { return null == characterSet ? _characterSet :  characterSet.get(); }
    public void setCharacterSet(final CharacterSet characterSet) {
        if (null == this.characterSet) {
            this._characterSet = characterSet;
            this.udpate();
        } else {
            this.characterSet.set(characterSet);
        }
    }
    public ObjectProperty<CharacterSet> characterSetProperty() {
        if (null == this.characterSet) {
            this.characterSet = new ObjectPropertyBase<>(_characterSet) {
                @Override protected void invalidated() { udpate(); }
                @Override public Object getBean() { return Row1.this; }
                @Override public String getName() { return "characterSet"; }
            };
            this._characterSet = null;
        }
        return characterSet;
    }

    public SplitFlapFont getSplitFlapFont() { return null == splitFlapFont ? _splitFlapFont : splitFlapFont.get(); }
    public void setSplitFlapFont(final SplitFlapFont splitFlapFont) {
        if (null == this.splitFlapFont) {
            this._splitFlapFont = splitFlapFont;
            this.udpate();
        } else {
            this.splitFlapFont.set(splitFlapFont);
        }
    }
    public ObjectProperty<SplitFlapFont> splitFlapFontProperty() {
        if (null == this.splitFlapFont) {
            this.splitFlapFont = new ObjectPropertyBase<>(_splitFlapFont) {
                @Override protected void invalidated() { udpate(); }
                @Override public Object getBean() { return Row1.this; }
                @Override public String getName() { return "splitFlapFont"; }
            };
            this._splitFlapFont = null;
        }
        return splitFlapFont;
    }

    public Color getBackgroundColor() { return null == backgroundColor ? _backgroundColor : backgroundColor.get(); }
    public void setBackgroundColor(final Color backgroundColor) {
        if (null == this.backgroundColor) {
            this._backgroundColor = backgroundColor;
            this.udpate();
        } else  {
            this.backgroundColor.set(backgroundColor);
        }
    }
    public ObjectProperty<Color> backgroundColorProperty() {
        if (null == this.backgroundColor) {
            this.backgroundColor = new ObjectPropertyBase<>(_backgroundColor) {
                @Override protected void invalidated() { udpate(); }
                @Override public Object getBean() { return Row1.this; }
                @Override public String getName() { return "backgroundColor"; }
            };
            this._backgroundColor = null;
        }
        return backgroundColor;
    }

    public Color getTextColor() { return null == textColor ? _textColor : textColor.get(); }
    public void setTextColor(final Color textColor) {
        if (null == this.textColor) {
            this._textColor = textColor;
            this.udpate();
        } else   {
            this.textColor.set(textColor);
        }
    }
    public ObjectProperty<Color> textColorProperty() {
        if (null == this.textColor) {
            this.textColor = new ObjectPropertyBase<>(_textColor) {
                @Override protected void invalidated() { udpate(); }
                @Override public Object getBean() { return Row1.this; }
                @Override public String getName() { return "textColor"; }
            };
            this._textColor = null;
        }
        return textColor;
    }

    public boolean isAxisVisible() { return null == axisVisible ? _axisVisible : axisVisible.get(); }
    public void setAxisVisible(final boolean axisVisible) {
        if (null == this.axisVisible) {
            this.axisVisible.set(axisVisible);
            this.udpate();
        } else {
            this.axisVisible.set(axisVisible);
        }
    }
    public BooleanProperty axisVisibleProperty() {
        if (null == this.axisVisible) {
            this.axisVisible = new BooleanPropertyBase(_axisVisible) {
                @Override protected void invalidated() { udpate(); }
                @Override public Object getBean() { return Row1.this; }
                @Override public String getName() { return "axisVisible"; }
            };
        }
        return axisVisible;
    }

    public boolean isShaded() { return null == shaded ? _shaded : shaded.get(); }
    public void setShaded(final boolean shaded) {
        if (null == this.shaded) {
            this._shaded = shaded;
            this.udpate();
        } else {
            this.shaded.set(shaded);
        }
    }
    public BooleanProperty shadedProperty() {
        if (null == this.shaded) {
            this.shaded = new BooleanPropertyBase(_shaded) {
                @Override protected void invalidated() { udpate(); }
                @Override public Object getBean() { return Row1.this; }
                @Override public String getName() { return "shaded"; }
            };
        }
        return shaded;
    }

    public void setText(final String text) {
        if (text.length() > this.noOfCharacters) { throw new IllegalArgumentException("Text too long (max " + this.flipMap.size() + " characters)"); }
        //List<SplitFlap> splitFlaps = new ArrayList<>(this.flipMap.keySet());
        //splitFlaps.forEach(splitFlap -> this.flipMap.put(splitFlap, false));
        this.flipMap.keySet().forEach(splitFlap -> this.flipMap.put(splitFlap, false));
        for (int i = 0 ; i < text.length(); i++) { this.flipMap.keySet().stream().toList().get(i).setCharacter(String.valueOf(text.charAt(i))); }
        fireEvent(new FlipEvent(Row1.this, null, FlipEvent.FLIP_STARTED));
    }

    public void reset() {
        for (int i = 0 ; i < this.noOfCharacters; i++) { flipMap.keySet().stream().toList().get(i).reset(); }
    }

    public void selfCheck() {
        for (int i = 0 ; i < noOfCharacters; i++) { flipMap.keySet().stream().toList().get(i).selfCheck(); }
    }

    private void udpate() {
        flipMap.keySet().forEach(splitFlap -> {
            splitFlap.setCharacterSet(getCharacterSet());
            splitFlap.setFont(getSplitFlapFont());
            splitFlap.setBackgroundColor(getBackgroundColor());
            splitFlap.setTextColor(getTextColor());
            splitFlap.setAxisVisible(isAxisVisible());
            splitFlap.setShaded(isShaded());
        });
    }
}
