package task;

import java.util.Objects;

public class Task {

    private String nameTask;
    private String description;
    private long id;
    private StatusTask statusTask;


    public Task(String nameTask, String description, long id, StatusTask statusTask) {
        this.nameTask = nameTask;
        this.description = description;
        this.id = id;
        this.statusTask = statusTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StatusTask getStatus() {
        return statusTask;
    }

    public void setStatus(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(nameTask, task.nameTask) &&
                Objects.equals(description, task.description)
                && Objects.equals(statusTask, task.statusTask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nameTask, description, id, statusTask);
    }

    @Override
    public String toString() {
        return "Task{" +
                "nameTask='" + nameTask + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + statusTask + '\'' +
                '}';
    }
}



