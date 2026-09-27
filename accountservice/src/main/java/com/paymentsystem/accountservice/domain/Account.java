package com.paymentsystem.accountservice.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity 
@Table (name = "accounts")
public class Account {
    @Id
    private UUID id;

    @Column (name = "owner_user", nullable = false)
    private String ownerUser;

    @Column (name = "balance", nullable = false)
    private BigDecimal balance;

    @Column (name = "currency", nullable = false)
    private String currency;

    @Version
    private Long version;

    @Column (name = "created_at", nullable = false)
    private Instant createdAt;

    @Column (name = "updated_at", nullable = false)
    private Instant updatedAt;


    public UUID getId(){ return  id ; }
    public void setId(UUID id) { this.id = id; }

    public String getOwnerUser() { return ownerUser; }
    public void setOwnerUser(String ownerUser) { this.ownerUser = ownerUser; }

    public BigDecimal getBalance() { return  balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getCurrency(){ return  currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Long getVersion() {return version;}
    public void setVersion(Long version) { this.version = version; }
    
    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt){ this.createdAt = createdAt; }

    public Instant getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(Instant updatedAt){ this.updatedAt = updatedAt; }
}