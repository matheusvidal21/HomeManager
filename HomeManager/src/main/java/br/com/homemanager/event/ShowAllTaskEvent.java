package br.com.homemanager.event;

import javafx.event.Event;
import javafx.event.EventType;


/**
 * Classe de evento personalizado para atualização da lista de tarefas na HomePage.
 */
public class ShowAllTaskEvent extends Event {
    /**
     * Tipo de evento personalizado para mostrar todas as tarefas.
     */
    public static final EventType<ShowAllTaskEvent> SHOW_ALL_TASK_EVENT=
            new EventType<>(Event.ANY, "SHOW_ALL_TASK_EVENT");

    /**
     * Construtor da classe que cria um novo evento para mostrar todas as tarefas.
     */
    public ShowAllTaskEvent() {
        super(SHOW_ALL_TASK_EVENT);
    }
}