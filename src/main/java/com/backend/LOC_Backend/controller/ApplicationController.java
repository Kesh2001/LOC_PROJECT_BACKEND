package com.backend.LOC_Backend.controller;

import com.backend.LOC_Backend.entity.Application;
import com.backend.LOC_Backend.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.OPTIONS}, allowedHeaders = "*")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        Application savedApplication = applicationService.saveApplication(application);
        return ResponseEntity.ok(savedApplication);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String applicantId,
            @RequestParam(required = false) String employmentStatus,
            @RequestParam(required = false) String province) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Application> applicationPage = applicationService.getApplications(
                pageable,
                applicantId,
                employmentStatus,
                province
        );

        Map<String, Object> response = new HashMap<>();
        response.put("data", applicationPage.getContent().stream().map(app -> {
            Map<String, Object> filteredData = new HashMap<>();
            filteredData.put("applicantId", app.getApplicantId());
            filteredData.put("province", app.getProvince());
            filteredData.put("employmentStatus", app.getEmploymentStatus());
            filteredData.put("annualIncome", app.getAnnualIncome());
            filteredData.put("estimatedDebt", app.getEstimatedDebt());
            filteredData.put("creditScore", app.getCreditScore());
            filteredData.put("currentCreditLimit", app.getCurrentCreditLimit());
            filteredData.put("approved", app.getApproved());
            filteredData.put("interestRate", app.getInterestRate());


            return filteredData;
        }).toList());
        response.put("totalElements", applicationPage.getTotalElements());
        response.put("totalPages", applicationPage.getTotalPages());
        response.put("currentPage", applicationPage.getNumber());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{applicantId}")
    public ResponseEntity<Application> getApplicationById(@PathVariable String applicantId) {
        Application application = applicationService.getApplicationById(applicantId);
        if (application != null) {
            return ResponseEntity.ok(application);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{applicantId}")
    public ResponseEntity<Application> updateApplication(
            @PathVariable String applicantId,
            @RequestBody Application application) {
        application.setApplicantId(applicantId); // Ensure ID matches
        Application updatedApplication = applicationService.saveApplication(application);
        return ResponseEntity.ok(updatedApplication);
    }
}