package com.backend.LOC_Backend.entity;
 
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
 
@Entity
@Table(name = "applications")  // <--- DB table name is "applications"
public class Application {
 
    @Id
    @Column(name = "applicant_id", nullable = false)
    private String applicantId;
 
    @Column(name = "self_reported_expenses")
    private double selfReportedExpenses;
 
    @Column(name = "credit_score")
    private int creditScore;
 
    @Column(name = "annual_income")
    private double annualIncome;
 
    @Column(name = "self_reported_debt")
    private double selfReportedDebt;
 
    @Column(name = "requested_amount")
    private double requestedAmount;
 
    @Column(name = "age")
    private int age;
 
    @Column(name = "province")
    private String province;
 
    @Column(name = "employment_status")
    private String employmentStatus;
 
    @Column(name = "months_employed")
    private int monthsEmployed;
 
    @Column(name = "credit_utilization")
    private double creditUtilization;
 
    @Column(name = "num_open_accounts")
    private int numOpenAccounts;
 
    @Column(name = "num_credit_inquiries")
    private int numCreditInquiries;
 
    @Column(name = "payment_history")
    private String paymentHistory;
 
    @Column(name = "current_credit_limit")
    private double currentCreditLimit;
 
    @Column(name = "monthly_expenses")
    private double monthlyExpenses;
 
    @Column(name = "approved")
    private int approved; // 0 = No, 1 = Yes
 
    @Column(name = "approved_amount")
    private double approvedAmount;
 
    @Column(name = "estimated_debt")
    private double estimatedDebt;
 
    @Column(name = "interest_rate")
    private double interestRate;
 
    // Match your Python code if you also track DTI in the DB:
    @Column(name = "dti")
    private double dti;
 
    public Application() {}
 
    public String getApplicantId() {
        return applicantId;
    }
    public void setApplicantId(String applicantId) {
        this.applicantId = applicantId;
    }
 
    public double getSelfReportedExpenses() {
        return selfReportedExpenses;
    }
    public void setSelfReportedExpenses(double selfReportedExpenses) {
        this.selfReportedExpenses = selfReportedExpenses;
    }
 
    public int getCreditScore() {
        return creditScore;
    }
    public void setCreditScore(int creditScore) {
        this.creditScore = creditScore;
    }
 
    public double getAnnualIncome() {
        return annualIncome;
    }
    public void setAnnualIncome(double annualIncome) {
        this.annualIncome = annualIncome;
    }
 
    public double getSelfReportedDebt() {
        return selfReportedDebt;
    }
    public void setSelfReportedDebt(double selfReportedDebt) {
        this.selfReportedDebt = selfReportedDebt;
    }
 
    public double getRequestedAmount() {
        return requestedAmount;
    }
    public void setRequestedAmount(double requestedAmount) {
        this.requestedAmount = requestedAmount;
    }
 
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
 
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
 
    public String getEmploymentStatus() {
        return employmentStatus;
    }
    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }
 
    public int getMonthsEmployed() {
        return monthsEmployed;
    }
    public void setMonthsEmployed(int monthsEmployed) {
        this.monthsEmployed = monthsEmployed;
    }
 
    public double getCreditUtilization() {
        return creditUtilization;
    }
    public void setCreditUtilization(double creditUtilization) {
        this.creditUtilization = creditUtilization;
    }
 
    public int getNumOpenAccounts() {
        return numOpenAccounts;
    }
    public void setNumOpenAccounts(int numOpenAccounts) {
        this.numOpenAccounts = numOpenAccounts;
    }
 
    public int getNumCreditInquiries() {
        return numCreditInquiries;
    }
    public void setNumCreditInquiries(int numCreditInquiries) {
        this.numCreditInquiries = numCreditInquiries;
    }
 
    public String getPaymentHistory() {
        return paymentHistory;
    }
    public void setPaymentHistory(String paymentHistory) {
        this.paymentHistory = paymentHistory;
    }
 
    public double getCurrentCreditLimit() {
        return currentCreditLimit;
    }
    public void setCurrentCreditLimit(double currentCreditLimit) {
        this.currentCreditLimit = currentCreditLimit;
    }
 
    public double getMonthlyExpenses() {
        return monthlyExpenses;
    }
    public void setMonthlyExpenses(double monthlyExpenses) {
        this.monthlyExpenses = monthlyExpenses;
    }
 
    public int getApproved() {
        return approved;
    }
    public void setApproved(int approved) {
        this.approved = approved;
    }
 
    public double getApprovedAmount() {
        return approvedAmount;
    }
    public void setApprovedAmount(double approvedAmount) {
        this.approvedAmount = approvedAmount;
    }
 
    public double getEstimatedDebt() {
        return estimatedDebt;
    }
    public void setEstimatedDebt(double estimatedDebt) {
        this.estimatedDebt = estimatedDebt;
    }
 
    public double getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
 
    public double getDti() {
        return dti;
    }
    public void setDti(double dti) {
        this.dti = dti;
    }
}