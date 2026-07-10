package com.kuugae.ca.repository;

import com.kuugae.ca.entity.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Optional<Certificate> findBySerialNumber(String serialNumber);

    List<Certificate> findByUsername(String username);

    Page<Certificate> findAll(Pageable pageable);

    List<Certificate> findByUsernameOrderByCreatedAtDesc(String username);

    @Query("SELECT COUNT(c) FROM Certificate c WHERE c.organization = :organization")
    long countByOrganization(@Param("organization") String organization);

    @Query("SELECT COUNT(c) FROM Certificate c WHERE c.registrationNumber = :registrationNumber")
    long countByRegistrationNumber(@Param("registrationNumber") String registrationNumber);

    boolean existsByOrganizationAndState(String organization, String state);

    boolean existsByRegistrationNumberAndState(String registrationNumber, String state);
}
