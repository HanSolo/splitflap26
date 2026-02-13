package eu.hansolo.fx.splitflap26;

import eu.hansolo.fx.splitflap26.fonts.Fonts;
import javafx.scene.text.Font;


public enum SplitFlapFont {
    BEBAS(Fonts.bebasNeue(SplitFlap.PREFERRED_HEIGHT), 0.85, 0.053, -0.92),
    BEBAS_ROUNDED(Fonts.bebasNeueRounded(SplitFlap.PREFERRED_HEIGHT), 0.85, 0.053, -0.92),
    SWAG_URBANO(Fonts.swagURBanoRegular(SplitFlap.PREFERRED_HEIGHT), 0.85, 0.06, -0.9),
    DIN_CONDENSED(Fonts.dinCondensedBold(SplitFlap.PREFERRED_HEIGHT), 0.85, 0.01, -1.01);

    public final Font   font;
    public final double sizeFactor;
    public final double fontOffsetYTop;
    public final double fontOffsetYBottom;


    SplitFlapFont(final Font font,  final double sizeFactor, final double fontOffsetYTop,  final double fontOffsetYBottom) {
        this.font              = font;
        this.sizeFactor        = sizeFactor;
        this.fontOffsetYTop    = fontOffsetYTop;
        this.fontOffsetYBottom = fontOffsetYBottom;
    }


    public static final Font getFontAtSize(final SplitFlapFont splitFlapFont, final double size) {
        switch (splitFlapFont) {
            case DIN_CONDENSED -> { return Fonts.dinCondensedBold(size);  }
            case SWAG_URBANO   -> { return Fonts.swagURBanoRegular(size); }
            case BEBAS_ROUNDED -> { return Fonts.bebasNeueRounded(size);  }
            default            -> { return Fonts.bebasNeue(size);         }
        }
    }
}
