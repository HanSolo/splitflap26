package eu.hansolo.fx.splitflap26.clock;

import eu.hansolo.fx.splitflap26.CharacterSet;
import eu.hansolo.fx.splitflap26.SplitFlap;
import eu.hansolo.fx.splitflap26.SplitFlapFont;
import javafx.beans.DefaultProperty;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;


@DefaultProperty("children")
public class Minutes extends HBox {
    private SplitFlap splitFlapMinutesLeft;
    private SplitFlap splitFlapMinutesRight;
    private Color     backgroundColor;
    private Color     textColor;
    private double    flipTime;


    // ******************** Constructors **************************************
    public Minutes(final Color backgroundColor, final Color textColor, final double flipTime) {
        this.backgroundColor = backgroundColor;
        this.textColor       = textColor;
        this.flipTime        = flipTime;
        initGraphics();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        splitFlapMinutesLeft  = new SplitFlap(CharacterSet.TIME_0_TO_5, SplitFlapFont.BEBAS, backgroundColor, textColor, flipTime, true, true);
        splitFlapMinutesRight = new SplitFlap(CharacterSet.TIME_0_TO_9, SplitFlapFont.BEBAS, backgroundColor, textColor, flipTime, true, true);
        setSpacing(5);
        getChildren().addAll(splitFlapMinutesLeft, splitFlapMinutesRight);
    }


    // ******************** Methods *******************************************
    public void update(final String minutesLeft, final String minutesRight) {
        splitFlapMinutesLeft.setCharacter(minutesLeft);
        splitFlapMinutesRight.setCharacter(minutesRight);
    }
}
