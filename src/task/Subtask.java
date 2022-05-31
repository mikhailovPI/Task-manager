package task;

import manager.saveToFile.TypeTask;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private long epicId;

    public Subtask(String nameTask, String description, long id, StatusTask statusTask, long duration,
                   LocalDateTime startTime, long epicId) {
        super(nameTask, description, id, statusTask, duration, startTime);
        this.epicId = epicId;
    }


    @Override
    public TypeTask getTypeTask() {
        return TypeTask.SUBTASK;
    }

    public long getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
    
}
