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
    private UUID id;
    private String ownerUser;
    private BigDecimal balance;
    private String currency;
    private Long version;
    private Instant createdAt;
    private Instant updatedAt;

    @Id
    public UUID getId(){ return  id ; }
    public void setId(UUID id) { this.id = id; }

    @Column (name = "owner_user", nullable = false)
    public String getOwnerUser() { return ownerUser; }
    public void setOwnerUser(String ownerUser) { this.ownerUser = ownerUser; }

    @Column (name = "balance", nullable = false)
    public BigDecimal getBalance() { return  balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    @Column (name = "currency", nullable = false)
    public String getCurrency(){ return  currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    @Version
    public Long getVersion() {return version;}
    public void setVersion(Long version) { this.version = version; }
    
    @Column (name = "created_at", nullable = false)
    public Instant getCreatedAt() {return createdAt;}
    public void setCreatedAt(Instant createdAt){ this.createdAt = createdAt; }

    @Column (name = "updated_at", nullable = false)
    public Instant getUpdatedAt() {return updatedAt;}
    public void setUpdatedAt(Instant updatedAt){ this.updatedAt = updatedAt; }
}


