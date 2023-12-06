package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Classe de evento personalizado para atualização da barra de progresso no MemberPage.
 */
public class UpdateProgressEvent extends Event {
    /**
     * Tipo de evento personalizado para atualizar a barra de progresso no MemberPage.
     */
    public static final EventType<UpdateProgressEvent> UPDATE_PROGRESS_EVENT =
            new EventType<>(Event.ANY, "UPDATE_PROGRESS_EVENT");

    /**
     * Construtor da classe que cria um novo evento para atualizar a barra de progresso no MemberPage.
     */
    public UpdateProgressEvent() {
        super(UPDATE_PROGRESS_EVENT);
    }
}