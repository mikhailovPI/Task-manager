package task;


import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> listSubtask;

    public Epic(String nameTask, String description, int id, String status, ArrayList<Integer> listSubtask) {
        super(nameTask, description, id, status);
        this.listSubtask = listSubtask;
    }

    public ArrayList<Integer> getListSubtask() {

        return listSubtask;
    }

    public void setListSubtask(ArrayList<Integer> listSubtask) {

        this.listSubtask = listSubtask;
    }
}