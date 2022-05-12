package task;


import manager.TypeTask;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Subtask> listSubtask = new ArrayList<>();

    public Epic(String nameTask, String description, long id, StatusTask statusTask) {
        super(nameTask, description, id, statusTask);
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    public ArrayList<Subtask> getListSubtask() {

        return listSubtask;
    }

    public void setListSubtask(ArrayList<Subtask> listSubtask) {

        this.listSubtask = listSubtask;
    }
}