package br.com.homemanager.event;

import javafx.event.EventHandler;

// Classe para gerenciar os eventos entre controladores
public class EventManager {
    private static EventManager instance;
    private EventHandler<UpdateProgressEvent> updateProgressEventHandler;
    private EventHandler<UpdateHomeProgressEvent> updateHomeProgressEventHandler;
    private EventHandler<EditTaskListEvent> editTaskListEventHandler;
    private EventHandler<ShowAllTaskEvent> showAllTaskEventHandler;
    private EventHandler<EditMemberListEvent> editMemberListEventHandler;
    private EventHandler<ShowMemberButtonsEvent> showMemberButtonsEventHandler;

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
        this.editTaskListEventHandler = handler;
    }
    public void setShowAllTaskEventHandler(EventHandler<ShowAllTaskEvent> handler) {
        this.showAllTaskEventHandler = handler;
    }

    public void setEditMemberListEventHandler(EventHandler<EditMemberListEvent> handler) {
        this.editMemberListEventHandler = handler;
    }
    public void setShowMemberButtonsEventHandler(EventHandler<ShowMemberButtonsEvent> handler) {
        this.showMemberButtonsEventHandler = handler;
    }

    public void fireShowMemberButtonsEvent(ShowMemberButtonsEvent event) {
        if (showMemberButtonsEventHandler != null) {
            showMemberButtonsEventHandler.handle(event);
        }
    }
    public void fireEditMemberListEvent(EditMemberListEvent event) {
        if (editMemberListEventHandler != null) {
            editMemberListEventHandler.handle(event);
        }
    }

    public void fireShowAllTaksEvent(ShowAllTaskEvent event) {
        if (showAllTaskEventHandler != null) {
            showAllTaskEventHandler.handle(event);
        }
    }

    public void fireEditTaskListEvent(EditTaskListEvent event) {
        if (editTaskListEventHandler != null) {
            editTaskListEventHandler.handle(event);
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
