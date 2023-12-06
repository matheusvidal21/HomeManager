package br.com.homemanager.model;

/**
 * Classe que representa uma tarefa semanal.
 * Esta classe estende a classe Task e herda suas propriedades e comportamentos.
 */
public class WeeklyTask extends Task{

    /**
     * Construtor da classe WeeklyTask.
     *
     * @param taskName O nome da tarefa semanal.
     */
    public WeeklyTask(String taskName) {
        super(taskName);
    }

    /**
     * Retorna uma representação em string da WeeklyTask.
     *
     * @return Uma string representando a WeeklyTask.
     */
    @Override
    public String toString() {
        return "WeeklyTask = " + getTaskName();
    }
}