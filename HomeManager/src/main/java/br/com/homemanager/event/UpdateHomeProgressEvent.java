package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Classe de evento personalizado para atualização da barra de progresso na HomePage.
 */
public class UpdateHomeProgressEvent extends Event {
    /**
     * Tipo de evento personalizado para atualizar a barra de progresso na HomePage.
     */
    public static final EventType<UpdateHomeProgressEvent> UPDATE_HOME_PROGRESS_EVENT =
            new EventType<>(Event.ANY, "UPDATE_HOME_PROGRESS_EVENT");

    /**
     * Construtor da classe que cria um novo evento para atualizar a barra de progresso na HomePage.
     */
    public UpdateHomeProgressEvent() {
        super(UPDATE_HOME_PROGRESS_EVENT);
    }
}