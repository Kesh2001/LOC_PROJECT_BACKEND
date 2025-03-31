package com.backend.LOC_Backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Application {
    @Id
    private String applicantId; // Changed to String, no auto-generation

    private double selfReportedExpenses;
    private int creditScore;
    private double annualIncome;
    private double selfReportedDebt;
    private double requestedAmount;
    private int age;
    private String province;
    private String employmentStatus;
    private int monthsEmployed;
    private double creditUtilization;
    private int numOpenAccounts;
    private int numCreditInquiries;
    private String paymentHistory;
    private double currentCreditLimit;
    private double monthlyExpenses;
    private int approved; // 0 = No, 1 = Yes
    private double approvedAmount;
    private double estimatedDebt;
    private double interestRate;

    // Default constructor
    public Application() {}

    // Getters and Setters
    public String getApplicantId() { return applicantId; }
    public void setApplicantId(String applicantId) { this.applicantId = applicantId; } // Fixed setter
    public double getSelfReportedExpenses() { return selfReportedExpenses; }
    public void setSelfReportedExpenses(double selfReportedExpenses) { this.selfReportedExpenses = selfReportedExpenses; }
    public int getCreditScore() { return creditScore; }
    public void setCreditScore(int creditScore) { this.creditScore = creditScore; }
    public double getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(double annualIncome) { this.annualIncome = annualIncome; }
    public double getSelfReportedDebt() { return selfReportedDebt; }
    public void setSelfReportedDebt(double selfReportedDebt) { this.selfReportedDebt = selfReportedDebt; }
    public double getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(double requestedAmount) { this.requestedAmount = requestedAmount; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }
    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }
    public int getMonthsEmployed() { return monthsEmployed; }
    public void setMonthsEmployed(int monthsEmployed) { this.monthsEmployed = monthsEmployed; }
    public double getCreditUtilization() { return creditUtilization; }
    public void setCreditUtilization(double creditUtilization) { this.creditUtilization = creditUtilization; }
    public int getNumOpenAccounts() { return numOpenAccounts; }
    public void setNumOpenAccounts(int numOpenAccounts) { this.numOpenAccounts = numOpenAccounts; }
    public int getNumCreditInquiries() { return numCreditInquiries; }
    public void setNumCreditInquiries(int numCreditInquiries) { this.numCreditInquiries = numCreditInquiries; }
    public String getPaymentHistory() { return paymentHistory; }
    public void setPaymentHistory(String paymentHistory) { this.paymentHistory = paymentHistory; }
    public double getCurrentCreditLimit() { return currentCreditLimit; }
    public void setCurrentCreditLimit(double currentCreditLimit) { this.currentCreditLimit = currentCreditLimit; }
    public double getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(double monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }
    public int getApproved() { return approved; }
    public void setApproved(int approved) { this.approved = approved; }
    public double getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(double approvedAmount) { this.approvedAmount = approvedAmount; }
    public double getEstimatedDebt() { return estimatedDebt; }
    public void setEstimatedDebt(double estimatedDebt) { this.estimatedDebt = estimatedDebt; }
    public double getInterestRate() { return interestRate; }
    public void setInterestRate(double interestRate) { this.interestRate = interestRate; }
}