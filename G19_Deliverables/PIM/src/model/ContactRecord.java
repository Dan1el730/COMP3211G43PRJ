package model;

import java.util.Objects;

public class ContactRecord extends PersonalInformationRecord {
    private String name;
    private String address;
    private String mobileNumber;

    public ContactRecord(String title, String name, String address, String mobileNumber) {
        super(title);
        this.name = name;
        this.address = address;
        this.mobileNumber = mobileNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String getInfo() {
        return "Contact: " + getTitle() + "\nName: " + getName() + "\nAddress: " + getAddress() + "\nMobile Number: " + getMobileNumber();
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContactRecord that = (ContactRecord) obj;
        return Objects.equals(name, that.name) &&
                Objects.equals(address, that.address) &&
                Objects.equals(mobileNumber, that.mobileNumber);
    }
}