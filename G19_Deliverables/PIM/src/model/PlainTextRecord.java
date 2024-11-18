package model;

import java.util.Objects;

public class PlainTextRecord extends PersonalInformationRecord {
    private String content;

    public PlainTextRecord(String title, String content) {
        super(title);
        this.content = content;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public String getInfo() {
        return "PlainText: " + getTitle() + "\nContent: " + getContent();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PlainTextRecord that = (PlainTextRecord) obj;
        return Objects.equals(content, that.content);
    }
}
