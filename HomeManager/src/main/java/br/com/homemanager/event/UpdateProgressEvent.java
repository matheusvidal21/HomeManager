package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;

// Classe de evento personalizado para atualização de progresso
public class UpdateProgressEvent extends Event {
    public static final EventType<UpdateProgressEvent> UPDATE_PROGRESS_EVENT =
            new EventType<>(Event.ANY, "UPDATE_PROGRESS_EVENT");

    public UpdateProgressEvent() {
        super(UPDATE_PROGRESS_EVENT);
    }
}
