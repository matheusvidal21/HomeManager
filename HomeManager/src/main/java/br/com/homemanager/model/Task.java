package br.com.homemanager.model;

import br.com.homemanager.model.enums.TaskStatus;

import java.io.Serializable;

/**
 * Classe abstrata que representa uma tarefa genérica.
 * As classes específicas de tarefa devem estender esta classe.
 */
public abstract class Task implements Serializable {

    private String taskName;
    private TaskStatus taskStatus;

    /**
     * Construtor da classe Task.
     *
     * @param taskName O nome da tarefa.
     */
    public Task(String taskName) {
        this.taskName = taskName;
        this.taskStatus = TaskStatus.NOT_DONE;
    }

    /**
     * Obtém o nome da tarefa.
     *
     * @return O nome da tarefa.
     */
    public String getTaskName() {
        return taskName;
    }

    /**
     * Define o nome da tarefa.
     *
     * @param taskName O nome da tarefa.
     */
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Obtém o status da tarefa.
     *
     * @return O status da tarefa.
     */
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    /**
     * Define o status da tarefa.
     *
     * @param taskStatus O status da tarefa.
     */
    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * Retorna uma representação em string da Task.
     *
     * @return Uma string representando a Task.
     */
    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'';
    }
}