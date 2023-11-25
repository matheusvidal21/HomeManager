package br.com.homemanager.model;

import br.com.homemanager.model.enums.TaskStatus;

import java.io.Serializable;

public abstract class Task implements Serializable {

    private String taskName;
    private TaskStatus taskStatus;

    public Task(String taskName) {
        this.taskName = taskName;
        this.taskStatus = TaskStatus.NOT_DONE;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'';
    }
}
