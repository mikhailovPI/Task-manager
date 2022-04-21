package task;

public class Subtask extends Task {

    private long epicId;

    public Subtask(String nameTask, String description, long id, StatusTask statusTask, long epicId) {
        super(nameTask, description, id, statusTask);
        this.epicId = epicId;
    }

    public long getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
    
}
