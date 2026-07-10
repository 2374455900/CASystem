package com.kuugae.ca.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "crl")
public class Crl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "serial_number", unique = true, nullable = false)
    private String serialNumber;

    @Column(name = "organization")
    private String organization;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    public Crl() {
    }

    public Crl(String serialNumber, String organization, String registrationNumber,
               String startTime, String endTime) {
        this.serialNumber = serialNumber;
        this.organization = organization;
        this.registrationNumber = registrationNumber;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
