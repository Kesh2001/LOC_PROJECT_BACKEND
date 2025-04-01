package com.backend.LOC_Backend.service;
 
import com.backend.LOC_Backend.entity.Application;
import com.backend.LOC_Backend.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
 
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
 
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
 
    // New method to validate the credit score using the CSV file.
    public int validateCreditScore(String applicantId, int inputCreditScore) {
        try (InputStream is = getClass().getResourceAsStream("/third_party_credit.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip header row if present
                if (line.startsWith("applicantId")) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String csvApplicantId = parts[0].trim();
                    int csvCreditScore = Integer.parseInt(parts[1].trim());
                    if (csvApplicantId.equals(applicantId)) {
                        return csvCreditScore;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Optionally log the error
        }
        return inputCreditScore;
    }
}