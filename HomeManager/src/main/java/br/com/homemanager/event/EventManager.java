package br.com.homemanager.event;

import javafx.event.EventHandler;

/**
 * Classe para gerenciar os eventos entre controladores.
 */
public class EventManager {
    private static EventManager instance;
    private EventHandler<UpdateProgressEvent> updateProgressEventHandler;
    private EventHandler<UpdateHomeProgressEvent> updateHomeProgressEventHandler;
    private EventHandler<EditTaskListEvent> editTaskListEventHandler;
    private EventHandler<ShowAllTaskEvent> showAllTaskEventHandler;
    private EventHandler<EditMemberListEvent> editMemberListEventHandler;
    private EventHandler<ShowMemberButtonsEvent> showMemberButtonsEventHandler;

    /**
     * Obtém uma instância única da classe EventManager.
     *
     * @return A instância única da classe EventManager.
     */
    public static synchronized EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }


    /**
     * Define o manipulador de evento de atualização de progresso do membro.
     *
     * @param handler O manipulador de evento de atualização de progresso do membro.
     */
    public void setUpdateProgressEventHandler(EventHandler<UpdateProgressEvent> handler) {
        this.updateProgressEventHandler = handler;
    }

    /**
     * Define o manipulador de evento de atualização de progresso da página inicial.
     *
     * @param handler O manipulador de evento de atualização de progresso da página inicial.
     */
    public void setUpdateHomeProgressEventHandler(EventHandler<UpdateHomeProgressEvent> handler) {
        this.updateHomeProgressEventHandler = handler;
    }


    /**
     * Define o manipulador de evento da página de edição da lista de tarefas.
     *
     * @param handler O manipulador de evento da página de edição da lista de tarefas.
     */
    public void setEditTaskListEventHandler(EventHandler<EditTaskListEvent> handler) {
        this.editTaskListEventHandler = handler;
    }

    /**
     * Define o manipulador de evento de exibição de todas as tarefas.
     *
     * @param handler O manipulador de evento de exibição de todas as tarefas.
     */
    public void setShowAllTaskEventHandler(EventHandler<ShowAllTaskEvent> handler) {
        this.showAllTaskEventHandler = handler;
    }

    /**
     * Define o manipulador de evento da página de edição da lista de membros.
     *
     * @param handler O manipulador de evento da página de edição da lista de membros.
     */
    public void setEditMemberListEventHandler(EventHandler<EditMemberListEvent> handler) {
        this.editMemberListEventHandler = handler;
    }

    /**
     * Define o manipulador de evento de exibição de botões de membro.
     *
     * @param handler O manipulador de evento de exibição de botões de membro.
     */
    public void setShowMemberButtonsEventHandler(EventHandler<ShowMemberButtonsEvent> handler) {
        this.showMemberButtonsEventHandler = handler;
    }

    /**
     * Dispara o evento para mostrar botões de membros.
     *
     * @param event O evento para mostrar botões de membros.
     */
    public void fireShowMemberButtonsEvent(ShowMemberButtonsEvent event) {
        if (showMemberButtonsEventHandler != null) {
            showMemberButtonsEventHandler.handle(event);
        }
    }

    /**
     * Dispara o evento da página de edição da lista de membros.
     *
     * @param event O evento da página de edição da lista de membros.
     */
    public void fireEditMemberListEvent(EditMemberListEvent event) {
        if (editMemberListEventHandler != null) {
            editMemberListEventHandler.handle(event);
        }
    }

    /**
     * Dispara o evento de exibição de todas as tarefas.
     *
     * @param event O evento de exibição de todas as tarefas.
     */
    public void fireShowAllTaksEvent(ShowAllTaskEvent event) {
        if (showAllTaskEventHandler != null) {
            showAllTaskEventHandler.handle(event);
        }
    }

    /**
     * Dispara o evento da página de edição da lista de tarefas.
     *
     * @param event O evento da página de edição da lista de tarefas.
     */
    public void fireEditTaskListEvent(EditTaskListEvent event) {
        if (editTaskListEventHandler != null) {
            editTaskListEventHandler.handle(event);
        }
    }

    /**
     * Dispara o evento de progresso da página inicial.
     *
     * @param event O evento de progresso da página inicial.
     */
    public void fireHomeEvent(UpdateHomeProgressEvent event) {
        if (updateHomeProgressEventHandler != null) {
            updateHomeProgressEventHandler.handle(event);
        }
    }

    /**
     * Dispara o evento de progresso do membro.
     *
     * @param event O evento de progresso do membro.
     */
    public void fireProgressEvent(UpdateProgressEvent event) {
        if (updateProgressEventHandler != null) {
            updateProgressEventHandler.handle(event);
        }
    }
}