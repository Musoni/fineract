/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.accounting.closure.storeglaccountbalance.data;

import java.math.BigDecimal;

import org.joda.time.LocalDateTime;
import org.apache.fineract.accounting.closure.storeglaccountbalance.domain.GLClosureJournalEntryBalance;

/**
 * Immutable object representing a {@link GLClosureJournalEntryBalance} entity
 */
public class GLClosureJournalEntryBalanceData {
    private final Long id;
    private final Long glClosureId;
    private final Long glAccountId;
    private final BigDecimal amount;
    private final LocalDateTime createdDate;
    private final Long createdByUserId;
    private final String createdByUsername;
    private final LocalDateTime lastModifiedDate;
    private final Long lastModifiedByUserId;
    private final String lastModifiedByUsername;
    private final boolean deleted;
    
    /**
     * @param id
     * @param glClosure
     * @param glAccount
     * @param amount
     * @param createdDate
     * @param createdByUserId
     * @param createdByUsername
     * @param lastModifiedDate
     * @param lastModifiedByUserId
     * @param lastModifiedByUsername
     * @param deleted
     */
    private GLClosureJournalEntryBalanceData(Long id, Long glClosureId, Long glAccountId,
            BigDecimal amount, LocalDateTime createdDate, Long createdByUserId, String createdByUsername,
            LocalDateTime lastModifiedDate, Long lastModifiedByUserId, String lastModifiedByUsername, boolean deleted) {
        this.id = id;
        this.glClosureId = glClosureId;
        this.glAccountId = glAccountId;
        this.amount = amount;
        this.createdDate = createdDate;
        this.createdByUserId = createdByUserId;
        this.createdByUsername = createdByUsername;
        this.lastModifiedDate = lastModifiedDate;
        this.lastModifiedByUserId = lastModifiedByUserId;
        this.lastModifiedByUsername = lastModifiedByUsername;
        this.deleted = deleted;
    }
    
    /**
     * Creates a new {@link GLClosureJournalEntryBalanceData} object
     * 
     * @param glClosureJournalEntryBalance {@link GLClosureJournalEntryBalance} object
     * @return {@link GLClosureJournalEntryBalanceData} object
     */
    public static GLClosureJournalEntryBalanceData newGLClosureBalanceData(
            final GLClosureJournalEntryBalance glClosureJournalEntryBalance) {
        LocalDateTime createdDate = null;
        LocalDateTime lastModifiedDate = null;
        String createdByUsername = null;
        String lastModifiedByUsername = null;
        Long createdByUserId = null;
        Long lastModifiedByUserId = null;
        Long glClosureId = null;
        Long glAccountId = null;
        
        if (glClosureJournalEntryBalance.getCreatedBy() != null) {
            createdByUsername = glClosureJournalEntryBalance.getCreatedBy().getUsername();
            createdByUserId = glClosureJournalEntryBalance.getCreatedBy().getId();
        }
        
        if (glClosureJournalEntryBalance.getLastModifiedBy() != null) {
            lastModifiedByUsername = glClosureJournalEntryBalance.getLastModifiedBy().getUsername();
            lastModifiedByUserId = glClosureJournalEntryBalance.getLastModifiedBy().getId();
        }
        
        if (glClosureJournalEntryBalance.getCreatedDate() != null) {
            createdDate = new LocalDateTime(glClosureJournalEntryBalance.getCreatedDate());
        }
        
        if (glClosureJournalEntryBalance.getLastModifiedDate() != null) {
            lastModifiedDate = new LocalDateTime(glClosureJournalEntryBalance.getLastModifiedDate());
        }
        
        if (glClosureJournalEntryBalance.getGlAccount() != null) {
            glAccountId = glClosureJournalEntryBalance.getGlAccount().getId();
        }
        
        if (glClosureJournalEntryBalance.getGlClosure() != null) {
            glClosureId = glClosureJournalEntryBalance.getGlClosure().getId();
        }
        
        return new GLClosureJournalEntryBalanceData(glClosureJournalEntryBalance.getId(), 
                glClosureId, glAccountId, glClosureJournalEntryBalance.getAmount(), createdDate, 
                createdByUserId, createdByUsername, lastModifiedDate, lastModifiedByUserId, 
                lastModifiedByUsername, glClosureJournalEntryBalance.isDeleted());
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the glClosureId
     */
    public Long getGlClosureId() {
        return glClosureId;
    }

    /**
     * @return the glAccountId
     */
    public Long getGlAccountId() {
        return glAccountId;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return the createdDate
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * @return the createdByUserId
     */
    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    /**
     * @return the createdByUsername
     */
    public String getCreatedByUsername() {
        return createdByUsername;
    }

    /**
     * @return the lastModifiedDate
     */
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    /**
     * @return the lastModifiedByUserId
     */
    public Long getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    /**
     * @return the lastModifiedByUsername
     */
    public String getLastModifiedByUsername() {
        return lastModifiedByUsername;
    }

    /**
     * @return the deleted
     */
    public boolean isDeleted() {
        return deleted;
    }
}
