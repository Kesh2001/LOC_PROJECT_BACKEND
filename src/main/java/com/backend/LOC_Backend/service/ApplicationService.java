package com.backend.LOC_Backend.service;

import com.backend.LOC_Backend.entity.Application;
import com.backend.LOC_Backend.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public Application saveApplication(Application application) {
        return applicationRepository.save(application);
    }

    public Page<Application> getApplications(
            Pageable pageable,
            String applicantId,
            String employmentStatus,
            String province
    ) {
        return applicationRepository.findApplications(
                applicantId,
                employmentStatus,
                province,
                pageable
        );
    }

    public Application getApplicationById(String applicantId) {
        return applicationRepository.findById(applicantId).orElse(null);
    }
}