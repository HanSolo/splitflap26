package eu.hansolo.fx.splitflap26;

import javafx.scene.paint.Color;


public class Constants {
    public static final Color    GRAY              = Color.rgb(31, 31, 31);
    public static final Color    WHITE             = Color.color(0.95, 0.95, 0.95);
    public static final Color    AZUL_BLUE         = Color.web("#152241");
    public static final Color    AZUL_BLUE_BRIGHT  = AZUL_BLUE.brighter();
    public static final Color    AZUL_LIGHTER_BLUE = Color.web("#3E8FBB");
    public static final Color    AZUL_LIGHT_BLUE   = Color.web("#A9D9EF");
    public static final Color    AZUL_PINK         = Color.web("#FF2B60");

    public static final double   DEFAULT_FLIP_TIME = 100;


    public static final String[] ALPHA             = { " ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                                                       "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
                                                       "V", "W", "X", "Y", "Z" };
    public static final String[] ALPHANUMERIC      = { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                                                       "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                                                       "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                                                       "W", "X", "Y", "Z" };
    public static final String[] EXTENDED          = { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                                                       "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                                                       "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                                                       "W", "X", "Y", "Z", "-", "/", ":", ",", "", ";", "@",
                                                       "#", "+", "?", "!", "%", "$", "=", "<", ">" };
    public static final String[] EXTENDED_UMLAUTE  = { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
                                                       "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                                                       "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
                                                       "W", "X", "Y", "Z", "-", "/", ":", ",", "", ";", "@",
                                                       "#", "+", "?", "!", "%", "$", "=", "<", ">", "Ä", "Ö",
                                                       "Ü", "ß"};
}
