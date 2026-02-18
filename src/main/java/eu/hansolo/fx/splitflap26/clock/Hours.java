package eu.hansolo.fx.splitflap26.clock;

import eu.hansolo.fx.splitflap26.CharacterSet;
import eu.hansolo.fx.splitflap26.Constants;
import eu.hansolo.fx.splitflap26.SplitFlap;
import eu.hansolo.fx.splitflap26.SplitFlapBuilder;
import eu.hansolo.fx.splitflap26.SplitFlapFont;
import javafx.beans.DefaultProperty;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.util.Arrays;


@DefaultProperty("children")
public class Hours extends HBox {
    private SplitFlap splitFlapHourLeft;
    private SplitFlap splitFlapHourRight;
    private Color     backgroundColor;
    private Color     textColor;
    private double    flipTime;


    // ******************** Constructors **************************************
    public Hours(final Color backgroundColor, final Color textColor, final double flipTime) {
        this.backgroundColor = backgroundColor;
        this.textColor       = textColor;
        this.flipTime        = flipTime;
        initGraphics();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        splitFlapHourLeft  = new SplitFlap(CharacterSet.TIME_0_TO_5, SplitFlapFont.BEBAS, backgroundColor, textColor, flipTime, true, true);
        splitFlapHourRight = new SplitFlap(CharacterSet.TIME_0_TO_9, SplitFlapFont.BEBAS, backgroundColor, textColor, flipTime, true, true);
        setSpacing(5);
        getChildren().addAll(splitFlapHourLeft, splitFlapHourRight);
    }


    // ******************** Methods *******************************************
    public void update(final String hourLeft, final String hourRight) {
        splitFlapHourLeft.setCharacter(hourLeft);
        splitFlapHourRight.setCharacter(hourRight);
    }
}