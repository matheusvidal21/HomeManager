package br.com.homemanager.event;

import javafx.event.EventHandler;

// Classe para gerenciar os eventos entre controladores
public class EventManager {
    private static EventManager instance;
    private EventHandler<UpdateProgressEvent> updateProgressEventHandler;

    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    public void setUpdateProgressEventHandler(EventHandler<UpdateProgressEvent> handler) {
        this.updateProgressEventHandler = handler;
    }

    public void fireEvent(UpdateProgressEvent event) {
        if (updateProgressEventHandler != null) {
            updateProgressEventHandler.handle(event);
        }
    }
}
