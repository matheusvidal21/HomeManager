package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Classe de evento personalizado para atualização dos botões dos membros.
 */
public class ShowMemberButtonsEvent extends Event {
    /**
     * Tipo de evento personalizado para mostrar os botões dos membros.
     */
    public static final EventType<ShowMemberButtonsEvent> SHOW_MEMBER_BUTTONS_EVENT=
            new EventType<>(Event.ANY, "SHOW_MEMBER_BUTTONS_EVENT");

    /**
     * Construtor da classe que cria um novo evento para mostrar os botões dos membros.
     */
    public ShowMemberButtonsEvent() {
        super(SHOW_MEMBER_BUTTONS_EVENT);
    }
}