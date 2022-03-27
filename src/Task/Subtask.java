package Task;

public class Subtask extends Task {

    int epicId;

    public Subtask(String nameTask, String description, int id, String status, int epicId) {
        super(nameTask, description, id, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
