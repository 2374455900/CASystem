package com.kuugae.ca.repository;

import com.kuugae.ca.entity.Crl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrlRepository extends JpaRepository<Crl, Integer> {

    Optional<Crl> findBySerialNumber(String serialNumber);

    List<Crl> findAll();

    @Query("SELECT COUNT(c) FROM Crl c WHERE c.organization = :organization")
    long countByOrganization(@Param("organization") String organization);

    @Query("SELECT COUNT(c) FROM Crl c WHERE c.registrationNumber = :registrationNumber")
    long countByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    boolean existsBySerialNumber(String serialNumber);
}
