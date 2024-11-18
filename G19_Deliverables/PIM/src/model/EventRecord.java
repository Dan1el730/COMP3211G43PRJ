package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class EventRecord extends PersonalInformationRecord {
    private String description;
    private LocalDateTime startTime;
    private boolean hasAlarm;

    public EventRecord(String title, String description, LocalDateTime startTime, boolean hasAlarm) {
        super(title);
        this.description = description;
        this.startTime = startTime;
        this.hasAlarm = hasAlarm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public boolean hasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(boolean hasAlarm) {
        this.hasAlarm = hasAlarm;
    }

    @Override
    public String getInfo() {
        return "Event: " + getTitle() + "\nDescription: " + getDescription() + "\nStart Time: " + getStartTime() + "\nAlarm: " + hasAlarm();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        EventRecord that = (EventRecord) obj;
        return Objects.equals(description, that.description) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(hasAlarm, that.hasAlarm);
    }
}