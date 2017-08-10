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

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

/**
 * Immutable object that represents a Journal entry with maximum entry date at close of a financial period
 */
public class GLClosureJournalEntryData {
    private final Long id;
    private final Long accountId;
    private final Long officeId;
    private final LocalDate entryDate;
    private final LocalDateTime createdDate;
    private final BigDecimal amount;
    private final BigDecimal organisationRunningBalance;
    private final BigDecimal officeRunningBalance;
    private final String description;
    
    /**
     * @param id
     * @param accountId
     * @param officeId
     * @param entryDate
     * @param createdDate
     * @param amount
     * @param organisationRunningBalance
     * @param officeRunningBalance
     * @param description
     */
    private GLClosureJournalEntryData(final Long id, final Long accountId, final Long officeId, 
            final LocalDate entryDate, final LocalDateTime createdDate, final BigDecimal amount, 
            final BigDecimal organisationRunningBalance, final BigDecimal officeRunningBalance, 
            final String description) {
        this.id = id;
        this.accountId = accountId;
        this.officeId = officeId;
        this.entryDate = entryDate;
        this.createdDate = createdDate;
        this.amount = amount;
        this.organisationRunningBalance = organisationRunningBalance;
        this.officeRunningBalance = officeRunningBalance;
        this.description = description;
    }
    
    /**
     * Creates a new {@link GLClosureJournalEntryData} object
     * 
     * @param id
     * @param accountId
     * @param officeId
     * @param entryDate
     * @param createdDate
     * @param amount
     * @param organisationRunningBalance
     * @param officeRunningBalance
     * @param description
     * @return {@link GLClosureJournalEntryData} object
     */
    public static GLClosureJournalEntryData instance(final Long id, final Long accountId, final Long officeId, 
            final LocalDate entryDate, final LocalDateTime createdDate, final BigDecimal amount, 
            final BigDecimal organisationRunningBalance, final BigDecimal officeRunningBalance, 
            final String description) {
        return new GLClosureJournalEntryData(id, accountId, officeId, entryDate, createdDate, amount, 
                organisationRunningBalance, officeRunningBalance, description);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the accountId
     */
    public Long getAccountId() {
        return accountId;
    }

    /**
     * @return the officeId
     */
    public Long getOfficeId() {
        return officeId;
    }

    /**
     * @return the entryDate
     */
    public LocalDate getEntryDate() {
        return entryDate;
    }

    /**
     * @return the createdDate
     */
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return the organisationRunningBalance
     */
    public BigDecimal getOrganisationRunningBalance() {
        return organisationRunningBalance;
    }

    /**
     * @return the officeRunningBalance
     */
    public BigDecimal getOfficeRunningBalance() {
        return officeRunningBalance;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
