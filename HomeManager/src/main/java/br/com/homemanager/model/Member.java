package br.com.homemanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Representa um membro com tarefas semanais e diárias.
 */
public class Member implements Serializable {

    private String name;
    private List<WeeklyTask> currentWTasks;
    private List<DailyTask> currentDTasks;

    /**
     * Construtor da classe Member.
     *
     * @param name O nome do membro.
     */
    public Member(String name) {
        this.name = name;
        this.currentDTasks = new ArrayList<>();
        this.currentWTasks = new ArrayList<>();
    }

    /**
     * Obtém o nome do membro.
     *
     * @return O nome do membro.
     */
    public String getName() {
        return name;
    }

    /**
     * Define o nome do membro.
     *
     * @param name O nome do membro.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Obtém a lista de tarefas semanais do membro.
     *
     * @return A lista de tarefas semanais.
     */
    public List<WeeklyTask> getWeeklyTasks() {
        return currentWTasks;
    }

    /**
     * Remove todas as tarefas semanais do membro.
     */
    public void removeWeeklyTasks() {
        currentWTasks.clear();
    }

    /**
     * Adiciona todas as tarefas semanais fornecidas à lista de tarefas do membro.
     *
     * @param tasks Lista de tarefas semanais a serem adicionadas.
     */
    public void addAllWeeklyTasks(List<WeeklyTask> tasks) {
        currentWTasks.addAll(tasks);
    }

    /**
     * Obtém a lista de tarefas diárias do membro.
     *
     * @return A lista de tarefas diárias.
     */
    public List<DailyTask> getDailyTasks(){
        return currentDTasks;
    }

    /**
     * Remove todas as tarefas diárias do membro.
     */
    public void removeDailyTasks(){
        currentDTasks.clear();
    }

    /**
     * Adiciona todas as tarefas diárias fornecidas à lista de tarefas do membro.
     *
     * @param tasks Lista de tarefas diárias a serem adicionadas.
     */
    public void addAllDailyTasks(List<DailyTask> tasks){
        currentDTasks.addAll(tasks);
    }

    /**
     * Retorna uma representação em string do membro.
     *
     * @return Uma string representando o membro.
     */
    @Override
    public String toString() {
        return "Member {" +
                "name ='" + name + '\'' + currentWTasks + "}";
    }

    /**
     * Verifica se dois membros são iguais com base no nome.
     *
     * @param o Objeto a ser comparado com o membro.
     * @return true se os membros forem iguais, caso contrário, false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(name, member.name);
    }

    /**
     * Gera um código de hash para o membro com base no nome.
     *
     * @return O código de hash gerado.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}