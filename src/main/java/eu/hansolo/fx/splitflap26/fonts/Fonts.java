package eu.hansolo.fx.splitflap26.fonts;

import javafx.scene.text.Font;


public final class Fonts {
    private static final String BEBAS_NEUE_NAME;

    private static String bebasNeueName;

    static {
        try {
            bebasNeueName = Font.loadFont(Fonts.class.getResourceAsStream("/eu/hansolo/fx/splitflap26/fonts/bebasneue.otf"), 10).getName();
        } catch (Exception exception) { }
        BEBAS_NEUE_NAME = bebasNeueName;
    }


    // ******************** Methods *******************************************
    public static Font bebasNeue(final double size) {
        return new Font(BEBAS_NEUE_NAME, size);
    }
}