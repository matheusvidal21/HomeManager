package br.com.homemanager.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Representa uma tarefa diária que estende a classe Task.
 * Possui um contador de progresso para controlar o andamento da tarefa.
 */
public class DailyTask extends Task implements Serializable {

    private AtomicInteger progress;

    /**
     * Construtor da classe DailyTask.
     *
     * @param taskName O nome da tarefa diária.
     */
    public DailyTask(String taskName) {
        super(taskName);
        this.progress = new AtomicInteger(0);
    }

    /**
     * Obtém o progresso da tarefa diária.
     *
     * @return O progresso da tarefa diária como um AtomicInteger.
     */
    public AtomicInteger getProgress() {
        return progress;
    }

    /**
     * Define o progresso da tarefa diária.
     *
     * @param progress O progresso da tarefa diária como um AtomicInteger.
     */
    public void setProgress(AtomicInteger progress) {
        this.progress = progress;
    }

    /**
     * Retorna uma representação em string da DailyTask.
     *
     * @return Uma string representando a DailyTask.
     */
    @Override
    public String toString() {
        return "DailyTask = " + getTaskName();
    }
}