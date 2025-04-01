package com.backend.LOC_Backend.repository;
 
import com.backend.LOC_Backend.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
 
@Repository
public interface ApplicationRepository extends JpaRepository<Application, String> {
 
    @Query("""
        SELECT a
        FROM Application a
        WHERE
            (:applicantId IS NULL OR a.applicantId = :applicantId)
            AND (:employmentStatus IS NULL OR a.employmentStatus = :employmentStatus)
            AND (:province IS NULL OR a.province = :province)
    """)
    Page<Application> findApplications(
            @Param("applicantId") String applicantId,
            @Param("employmentStatus") String employmentStatus,
            @Param("province") String province,
            Pageable pageable
    );
}