package task;


import manager.TypeTask;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> listSubtask = new ArrayList<>();

    public Epic(String nameTask, String description, long id, StatusTask statusTask, long duration,
                LocalDateTime startTime) {
        super(nameTask, description, id, statusTask,duration, startTime);
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }


    @Override
    public LocalDateTime getEndTime() {
        LocalDateTime startTime = getStartTime();
        Long durationEpics = getDuration();
        endTime = startTime.plusMinutes(durationEpics);
        return endTime;
    }

    //LocalDateTime startTime = getStartTime();
    //Long between = Duration.between(startTime, maxEndTime).toMinutes();
    //duration = between;

    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime minStartTime = LocalDateTime.of(3000, 1, 1, 0, 0);
        for (Subtask subtask : getListSubtask()) {
            if (minStartTime.isAfter(subtask.getStartTime())) {
                minStartTime = subtask.getStartTime();
            }
            //startTime = minStartTime;
        }
        return minStartTime;
    }

    @Override
    public long getDuration() {
        LocalDateTime maxEndTime = LocalDateTime.of(1980, 1, 1, 0, 0);
        for (Subtask subtaskEpic : getListSubtask()) {
            if (maxEndTime.isBefore(subtaskEpic.getStartTime().plusMinutes(subtaskEpic.getDuration()))) {
                maxEndTime = subtaskEpic.getStartTime().plusMinutes(subtaskEpic.getDuration());
            }
        }
        return Duration.between(getStartTime(), maxEndTime).toMinutes();
    }

    public ArrayList<Subtask> getListSubtask() {
        return listSubtask;
    }

    public void setListSubtask(ArrayList<Subtask> listSubtask) {
        this.listSubtask = listSubtask;
    }
}