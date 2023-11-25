package br.com.homemanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {

    private String name;
    private List<WeeklyTask> currentWTasks;
    private List<DailyTask> currentDTasks;

    public Member(String name) {
        this.name = name;
        this.currentDTasks = new ArrayList<>();
        this.currentWTasks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WeeklyTask> getTarefasSemanais() {
        return currentWTasks;
    }

    public void removerTarefasSemanais() {
        currentWTasks.clear();
    }

    public void adicionarTarefasSemanais(List<WeeklyTask> tarefas) {
        currentWTasks.addAll(tarefas);
    }

    public List<DailyTask> getTarefasDiarias(){
        return currentDTasks;
    }

    public void removerTarefasDiarias(){
        currentDTasks.clear();
    }

    public void adicionarTarefasDiarias(List<DailyTask> tarefas){
        currentDTasks.addAll(tarefas);
    }

    @Override
    public String toString() {
        return "Member {" +
                "name ='" + name + '\'' + currentWTasks + "}";
    }
}
