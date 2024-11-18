package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class TaskRecord extends PersonalInformationRecord {
    private String description;
    private LocalDateTime deadline;

    public TaskRecord(String title, String description, LocalDateTime deadline) {
        super(title);
        this.description = description;
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getInfo() {
        return "Task: " + getTitle() + "\nDescription: " + getDescription() + "\nDeadline: " + getDeadline();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        TaskRecord that = (TaskRecord) obj;
        return Objects.equals(description, that.description) &&
                Objects.equals(deadline, that.deadline);
    }
}