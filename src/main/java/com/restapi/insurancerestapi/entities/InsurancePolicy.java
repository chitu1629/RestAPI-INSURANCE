package com.restapi.insurancerestapi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePolicy {
    @Id
    @NotNull
    private Integer policyNumber;
    @NotNull
    private Integer clientId;
    @Size(min = 5, message = "PolicyType length must be 5")
    private String policyType;
    @NotNull
    private Long coverageAmount;
    @NotNull
    private Long premium;
    @PastOrPresent
    private LocalDate startDate;
    @Future
    private LocalDate endDate;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(Integer policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    public Long getCoverageAmount() {
        return coverageAmount;
    }

    public void setCoverageAmount(Long coverageAmount) {
        this.coverageAmount = coverageAmount;
    }

    public Long getPremium() {
        return premium;
    }

    public void setPremium(Long premium) {
        this.premium = premium;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "InsurancePolicy{" +
                "clientID=" + clientId +
                ", policyNumber=" + policyNumber +
                ", policyType='" + policyType + '\'' +
                ", coverageAmount=" + coverageAmount +
                ", premium=" + premium +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
