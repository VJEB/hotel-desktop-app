package org.example.Model;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "guests")
public class Guest {
    @Id
    @Column(name = "national_id")
    private String national_id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "nationality")
    @Enumerated(EnumType.STRING)
    private GuestNationalities nationality;

    public Guest(){}

    public Guest(String national_id, String firstName, String lastName, LocalDate dateOfBirth, String phoneNumber, String email, GuestNationalities nationality){
        this.national_id = national_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.nationality = nationality;
    }


    public String getNational_id() {
        return national_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public GuestNationalities getNationality() {
        return nationality;
    }

    public void setNationality(GuestNationalities nationality) {
        this.nationality = nationality;
    }
}
