package com.kuugae.ca.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "certificate")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "serial_number", unique = true, nullable = false)
    private String serialNumber;

    @Column(name = "organization", nullable = false)
    private String organization;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "start_time")
    private String startTime;

    @Column(name = "end_time")
    private String endTime;

    @Column(name = "juridical_person")
    private String juridicalPerson;

    @Column(name = "charge_person")
    private String chargePerson;

    @Column(name = "charge_phone")
    private String chargePhone;

    @Column(name = "username", length = 255)
    private String username;

    @Column(name = "state")
    private String state;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "user_public_key", columnDefinition = "TEXT")
    private String userPublicKey;

    @Column(name = "user_private_key_encrypted", columnDefinition = "TEXT")
    private String userPrivateKeyEncrypted;

    @Column(name = "key_algorithm")
    private String keyAlgorithm = "RSA";

    @Column(name = "key_size")
    private Integer keySize = 2048;

    public Certificate() {
    }

    public Certificate(String serialNumber, String organization, String registrationNumber,
                       String filePath, String startTime, String endTime, String juridicalPerson,
                       String chargePerson, String chargePhone, String username, String state) {
        this.serialNumber = serialNumber;
        this.organization = organization;
        this.registrationNumber = registrationNumber;
        this.filePath = filePath;
        this.startTime = startTime;
        this.endTime = endTime;
        this.juridicalPerson = juridicalPerson;
        this.chargePerson = chargePerson;
        this.chargePhone = chargePhone;
        this.username = username;
        this.state = state;
    }
}
