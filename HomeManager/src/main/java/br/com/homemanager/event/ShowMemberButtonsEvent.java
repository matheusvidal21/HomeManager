package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;

// Classe de evento personalizado para atualização da lista de tarefas na EditTaskListPage
public class ShowMemberButtonsEvent extends Event {
    public static final EventType<ShowMemberButtonsEvent> SHOW_MEMBER_BUTTONS_EVENT=
            new EventType<>(Event.ANY, "SHOW_MEMBER_BUTTONS_EVENT");

    public ShowMemberButtonsEvent() {
        super(SHOW_MEMBER_BUTTONS_EVENT);
    }
}
