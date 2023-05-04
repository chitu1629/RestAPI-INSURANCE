package com.restapi.insurancerestapi.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restapi.insurancerestapi.utils.ClaimStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Claim {
    @Id
    @NotNull
    private Integer claimNumber;
    @NotNull
    private Integer policyNumber;
    @NotNull
    private Integer clientId;
    @Size(min = 15, message = "Description length must be 15")
    private String description;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate claimDate;
    private ClaimStatus claimStatus;

    public Integer getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(Integer claimNumber) {
        this.claimNumber = claimNumber;
    }

    public Integer getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(Integer policyNumber) {
        this.policyNumber = policyNumber;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    @Override
    public String toString() {
        return "Claim{" +
                "claimNumber=" + claimNumber +
                ", policyNumber=" + policyNumber +
                ", clientId=" + clientId +
                ", description='" + description + '\'' +
                ", claimDate=" + claimDate +
                ", claimStatus=" + claimStatus +
                '}';
    }
}
