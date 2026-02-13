package eu.hansolo.fx.splitflap26;

public class Helper {

    public static final double clamp(final double min, final double max, final double value) {
        if (value < min) { return min; }
        if (value > max) { return max; }
        return value;
    }

    public static final long clamp(final long min, final long max, final long value) {
        if (value < min) { return min; }
        if (value > max) { return max; }
        return value;
    }
}
