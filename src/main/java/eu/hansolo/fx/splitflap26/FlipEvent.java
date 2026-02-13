package eu.hansolo.fx.splitflap26;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;


public class FlipEvent extends Event {
    public static final EventType<FlipEvent> FLIP_STARTED  = new EventType(ANY, "FLIP_STARTED");
    public static final EventType<FlipEvent> FLIP_FINISHED = new EventType(ANY, "FLIP_FINISHED");


    // ******************* Constructors ***************************************
    public FlipEvent(final Object source, final EventTarget target, final EventType<FlipEvent> eventType) {
        super(source, target, eventType);
    }
}
