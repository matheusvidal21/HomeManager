package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;

// Classe de evento personalizado para atualização da lista de tarefas na EditTaskListPage
public class EditTaskListEvent extends Event {
    public static final EventType<EditTaskListEvent> EDIT_TASK_LIST_EVENT =
            new EventType<>(Event.ANY, "EDIT_TASK_LIST_EVENT");

    public EditTaskListEvent() {
        super(EDIT_TASK_LIST_EVENT);
    }
}
