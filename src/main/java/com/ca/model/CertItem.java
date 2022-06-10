package com.ca.model;

public class CertItem {

    private int id;
    private String serial_number;
    private String organization;
    private String registration_number;
    private String file_path;
    private String start_time;
    private String end_time;
    private String juridical_person;
    private String charge_person;
    private String charge_phone;
    private String username;

    public CertItem(int id, String serial_number, String organization, String registration_number, String file_path, String start_time, String end_time, String juridical_person, String charge_person, String charge_phone, String username) {
        this.id = id;
        this.serial_number = serial_number;
        this.organization = organization;
        this.registration_number = registration_number;
        this.file_path = file_path;
        this.start_time = start_time;
        this.end_time = end_time;
        this.juridical_person = juridical_person;
        this.charge_person = charge_person;
        this.charge_phone = charge_phone;
        this.username = username;
    }

    public CertItem() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getJuridical_person() {
        return juridical_person;
    }

    public void setJuridical_person(String juridical_person) {
        this.juridical_person = juridical_person;
    }

    public String getCharge_person() {
        return charge_person;
    }

    public void setCharge_person(String charge_person) {
        this.charge_person = charge_person;
    }

    public String getCharge_phone() {
        return charge_phone;
    }

    public void setCharge_phone(String charge_phone) {
        this.charge_phone = charge_phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CertItem{" + "id=" + id + ", serial_number='" + serial_number + '\''
                + ", organization='" + organization + '\'' + ", registration_number='"
                + registration_number + '\'' + ", file_path='" + file_path + '\'' + ", start_time='"
                + start_time + '\'' + ", end_time='" + end_time + '\'' + ", juridical_person='"
                + juridical_person + '\'' + ", charge_person='" + charge_person + '\''
                + ", charge_phone='" + charge_phone + '\'' + ", username='" + username + '\'' + '}';
    }
}

