package task;


import manager.saveToFile.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> listSubtask = new ArrayList<>();

    public Epic(String nameTask, String description, long id, StatusTask statusTask, long duration,
                LocalDateTime startTime) {
        super(nameTask, description, id, statusTask, duration, startTime);
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    @Override
    public long getDuration() {
        return super.getDuration();
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    public ArrayList<Subtask> getListSubtask() {
        return listSubtask;
    }

    public void setListSubtask(ArrayList<Subtask> listSubtask) {
        this.listSubtask = listSubtask;
    }
}