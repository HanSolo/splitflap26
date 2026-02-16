package eu.hansolo.fx.splitflap26;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Dimension2D;
import javafx.scene.paint.Color;

import java.util.HashMap;


public class SplitFlapBuilder {
    private final HashMap<String, Property> properties = new HashMap<>();


    // ******************** Constructors **************************************
    protected SplitFlapBuilder() {}


    // ******************** Methods *******************************************
    public static SplitFlapBuilder create() {
        return new SplitFlapBuilder();
    }

    public final SplitFlapBuilder characterSet(final CharacterSet characterSet) {
        properties.put("characterSet", new SimpleObjectProperty<>(characterSet));
        return this;
    }

    public final SplitFlapBuilder font(final SplitFlapFont font) {
        properties.put("font", new SimpleObjectProperty<>(font));
        return this;
    }

    public final SplitFlapBuilder backgroundColor(final Color backgroundColor) {
        properties.put("backgroundColor", new SimpleObjectProperty<>(backgroundColor));
        return this;
    }

    public final SplitFlapBuilder textColor(final Color textColor) {
        properties.put("textColor", new SimpleObjectProperty<>(textColor));
        return this;
    }

    public final SplitFlapBuilder flipTime(final double flipTime) {
        properties.put("flipTime", new SimpleDoubleProperty(flipTime));
        return this;
    }

    public final SplitFlapBuilder axisVisible(final boolean axisVisible) {
        properties.put("axisVisible", new SimpleBooleanProperty(axisVisible));
        return this;
    }

    public final SplitFlapBuilder shaded(final boolean shaded) {
        properties.put("shaded", new SimpleBooleanProperty(shaded));
        return this;
    }

    public final SplitFlapBuilder prefSize(final double width, final double height) {
        properties.put("prefSize", new SimpleObjectProperty<>(new Dimension2D(width, height)));
        return this;
    }

    public final SplitFlap build() {
        final SplitFlap splitFlap = new SplitFlap();
        properties.keySet().forEach(key -> {
            switch (key) {
                case "characterSet"    -> splitFlap.setCharacterSet(((ObjectProperty<CharacterSet>) properties.get(key)).get());
                case "font"            -> splitFlap.setFont(((ObjectProperty<SplitFlapFont>) properties.get(key)).get());
                case "backgroundColor" -> splitFlap.setBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
                case "textColor"       -> splitFlap.setTextColor(((ObjectProperty<Color>) properties.get(key)).get());
                case "flipTime"        -> splitFlap.setFlipTime(((DoubleProperty) properties.get(key)).get());
                case "shaded"          -> splitFlap.setShaded(((BooleanProperty) properties.get(key)).get());
                case "axisVisible"     -> splitFlap.setAxisVisible(((BooleanProperty) properties.get(key)).get());
                case "prefSize"        -> {
                    final Dimension2D dim = ((ObjectProperty<Dimension2D>) properties.get(key)).get();
                    splitFlap.setPrefSize(dim.getWidth(), dim.getHeight());
                }
            }
        });
        return splitFlap;
    }
}
