package task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String nameTask, String description, int id, StatusTask statusTask, int epicId) {
        super(nameTask, description, id, statusTask);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
    
}
