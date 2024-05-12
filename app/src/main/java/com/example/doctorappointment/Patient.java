package com.example.doctorappointment;

public class Patient {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String TAJnumber;

    public Patient(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = null;
        this.TAJnumber = null;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", TAJnumber='" + TAJnumber + '\'' +
                '}';
    }

    public Patient(){}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getTAJnumber() {
        return TAJnumber;
    }
}
