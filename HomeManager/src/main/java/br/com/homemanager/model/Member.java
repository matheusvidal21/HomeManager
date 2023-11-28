package br.com.homemanager.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    public List<WeeklyTask> getWeeklyTasks() {
        return currentWTasks;
    }

    public void removeWeeklyTasks() {
        currentWTasks.clear();
    }

    public void addAllWeeklyTasks(List<WeeklyTask> tasks) {
        currentWTasks.addAll(tasks);
    }

    public List<DailyTask> getDailyTasks(){
        return currentDTasks;
    }

    public void removeDailyTasks(){
        currentDTasks.clear();
    }

    public void addAllDailyTasks(List<DailyTask> tasks){
        currentDTasks.addAll(tasks);
    }

    @Override
    public String toString() {
        return "Member {" +
                "name ='" + name + '\'' + currentWTasks + "}";
    }
}
