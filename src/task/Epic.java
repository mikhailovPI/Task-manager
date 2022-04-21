package task;


import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> listSubtask = new ArrayList<>();

    public Epic(String nameTask, String description, long id, StatusTask statusTask) {
        super(nameTask, description, id, statusTask);
    }

    public ArrayList<Subtask> getListSubtask() {

        return listSubtask;
    }

    public void setListSubtask(ArrayList<Subtask> listSubtask) {

        this.listSubtask = listSubtask;
    }
}