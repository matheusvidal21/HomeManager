package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;

// Classe de evento personalizado para atualização da EditMemberListPage
public class EditMemberListEvent extends Event {
    public static final EventType<EditMemberListEvent> EDIT_MEMBER_LIST_EVENT =
            new EventType<>(Event.ANY, "EDIT_MEMBER_LIST_EVENT");

    public EditMemberListEvent() {
        super(EDIT_MEMBER_LIST_EVENT);
    }
}
