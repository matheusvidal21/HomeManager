package br.com.homemanager.model;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

public class DailyTask extends Task implements Serializable {

    private AtomicInteger progress;
    public DailyTask(String taskName) {
        super(taskName);
        this.progress = new AtomicInteger(0);
    }
    public AtomicInteger getProgress() {
        return progress;
    }

    public void setProgress(AtomicInteger progress) {
        this.progress = progress;
    }
    @Override
    public String toString() {
        return "DailyTask = " + getTaskName();
    }
}
