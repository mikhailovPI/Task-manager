package task;


import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> listSubtask;

    public Epic(String nameTask, String description, int id, String status, ArrayList<Subtask> listSubtask) {
        super(nameTask, description, id, status);
        this.listSubtask = listSubtask;
    }

    public ArrayList<Subtask> getListSubtask() {

        return listSubtask;
    }

    public void setListSubtask(ArrayList<Subtask> listSubtask) {

        this.listSubtask = listSubtask;
    }
}