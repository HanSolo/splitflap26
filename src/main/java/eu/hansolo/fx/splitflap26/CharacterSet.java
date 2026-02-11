package eu.hansolo.fx.splitflap26;

public enum CharacterSet {
    TIME_0_TO_5("1", "2", "3", "4", "5", "0"),
    TIME_0_TO_9("1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
    NUMERIC(" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"),
    ALPHA(" ", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"),
    ALPHA_NUMERIC(" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"),
    ALPHA_NUMERIC_EXTENDED(" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "-", "/", ":", ",", "", ";", "@", "#", "+", "?", "!", "%", "$", "=", "<", ">");

    public final String[] characters;


    CharacterSet(final String... characters) {
        this.characters = characters;
    }
}
