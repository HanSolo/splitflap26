package eu.hansolo.fx.splitflap26.board;

import eu.hansolo.fx.splitflap26.CharacterSet;
import eu.hansolo.fx.splitflap26.Constants;
import eu.hansolo.fx.splitflap26.SplitFlap;
import eu.hansolo.fx.splitflap26.SplitFlapFont;
import javafx.beans.DefaultProperty;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Arrays;


@DefaultProperty("children")
public class Row extends HBox {
    private static final int          NO_OF_CHARACTERS = 11;
    private              SplitFlap[]  splitFlaps;
    private              CharacterSet characterSet;
    private              Color        backgroundColor;
    private              Color        textColor;
    private              double       flipTime;


    // ******************** Constructors **************************************
    public Row() {
        this(CharacterSet.ALPHA_NUMERIC, Constants.GRAY, Constants.WHITE, SplitFlap.DEFAULT_FLIP_TIME);
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
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        splitFlaps = new SplitFlap[NO_OF_CHARACTERS];
        for (int i = 0; i < NO_OF_CHARACTERS; i++) {
            splitFlaps[i] = new SplitFlap(characterSet, SplitFlapFont.BEBAS, backgroundColor, textColor, flipTime, true, true);
        }
        setSpacing(5);
        getChildren().addAll(Arrays.asList(splitFlaps));
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
    public void selfTest() {
        for (int i = 0 ; i < NO_OF_CHARACTERS; i++) {
            splitFlaps[i].selfTest();
        }
    }
}