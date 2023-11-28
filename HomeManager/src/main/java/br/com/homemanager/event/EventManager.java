package br.com.homemanager.event;

import javafx.event.EventHandler;

// Classe para gerenciar os eventos entre controladores
public class EventManager {
    private static EventManager instance;
    private EventHandler<UpdateProgressEvent> updateProgressEventHandler;
    private EventHandler<UpdateHomeProgressEvent> updateHomeProgressEventHandler;
    private EventHandler<EditTaskListEvent> editTaskListEventEventHandler;
    private EventHandler<ShowAllTaskEvent> showAllTaskEventEventHandler;

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void setUpdateProgressEventHandler(EventHandler<UpdateProgressEvent> handler) {
        this.updateProgressEventHandler = handler;
    }

    public void setUpdateHomeProgressEventHandler(EventHandler<UpdateHomeProgressEvent> handler) {
        this.updateHomeProgressEventHandler = handler;
    }

    public void setEditTaskListEventHandler(EventHandler<EditTaskListEvent> handler) {
        this.editTaskListEventEventHandler = handler;
    }
    public void setShowAllTaskEventEventHandler(EventHandler<ShowAllTaskEvent> handler) {
        this.showAllTaskEventEventHandler = handler;
    }

    public void fireShowAllTaksEvent(ShowAllTaskEvent event) {
        if (showAllTaskEventEventHandler != null) {
            showAllTaskEventEventHandler.handle(event);
        }
    }

    public void fireEditTaskListEvent(EditTaskListEvent event) {
        if (editTaskListEventEventHandler != null) {
            editTaskListEventEventHandler.handle(event);
        }
    }

    public void fireHomeEvent(UpdateHomeProgressEvent event) {
        if (updateHomeProgressEventHandler != null) {
            updateHomeProgressEventHandler.handle(event);
        }
    }

    public void fireProgressEvent(UpdateProgressEvent event) {
        if (updateProgressEventHandler != null) {
            updateProgressEventHandler.handle(event);
        }
    }
}
