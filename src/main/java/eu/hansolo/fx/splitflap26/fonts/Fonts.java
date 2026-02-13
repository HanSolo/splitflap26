package eu.hansolo.fx.splitflap26.fonts;

import javafx.scene.text.Font;


public final class Fonts {
    private static final String BEBAS_NEUE_NAME;
    private static final String BEBAS_NEUE_ROUNDED_NAME;
    private static final String SWAG_URBANO_REGULAR_NAME;
    private static final String DIN_CONDENSED_BOLD_NAME;

    private static String bebasNeueName;
    private static String bebasNeueRoundedName;
    private static String swagURBanoRegularName;
    private static String dinCondensedBoldName;

    static {
        try {
            bebasNeueName         = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/splitflap26/fonts/bebasneue.otf"), 10).getName();
            bebasNeueRoundedName  = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/splitflap26/fonts/BebasNeueSemiRounded-Regular.otf"), 10).getName();
            swagURBanoRegularName = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/splitflap26/fonts/SwagUrbanoRegular.otf"), 10).getName();
            dinCondensedBoldName  = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/splitflap26/fonts/D-DINCondensed-Bold.ttf"), 10).getName();
        } catch (Exception exception) { }

        BEBAS_NEUE_NAME          = bebasNeueName;
        BEBAS_NEUE_ROUNDED_NAME  = bebasNeueRoundedName;
        SWAG_URBANO_REGULAR_NAME = swagURBanoRegularName;
        DIN_CONDENSED_BOLD_NAME  = dinCondensedBoldName;
    }


    // ******************** Methods *******************************************
    public static Font bebasNeue(final double size) { return new Font(BEBAS_NEUE_NAME, size); }

    public static Font bebasNeueRounded(final double size) { return new Font(BEBAS_NEUE_ROUNDED_NAME, size); }

    public static Font swagURBanoRegular(final double size) { return new Font(SWAG_URBANO_REGULAR_NAME, size); }

    public static Font dinCondensedBold(final double size) { return new Font(DIN_CONDENSED_BOLD_NAME, size); }
}