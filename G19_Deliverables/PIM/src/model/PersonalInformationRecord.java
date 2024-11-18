package model;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class PersonalInformationRecord {
    protected String title;

    public PersonalInformationRecord(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public abstract String getInfo();

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        PersonalInformationRecord that = (PersonalInformationRecord) obj;
        return Objects.equals(title, that.title);
    }

}


