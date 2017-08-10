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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.LocalDate;

public class GLClosureAccountBalanceReportData {
    private final String accountNumber;
    private final String accountName;
    private final Long closureId;
    private final LocalDate transactionDate;
    private final LocalDate postedDate;
    private final GLClosureAccountBalanceReportTransactionType transactionType;
    private final BigDecimal amount;
    private final String reference;
    
    /**
     * @param accountNumber
     * @param transactionDate
     * @param postedDate
     * @param transactionType
     * @param amount
     * @param reference
     */
    private GLClosureAccountBalanceReportData(final String accountNumber, final LocalDate transactionDate, 
            final LocalDate postedDate, final GLClosureAccountBalanceReportTransactionType transactionType, 
            final BigDecimal amount, final String reference, final Long closureId, final String accountName) {
        this.accountNumber = accountNumber;
        this.transactionDate = transactionDate;
        this.postedDate = postedDate;
        this.transactionType = transactionType;
        this.amount = amount;
        this.reference = reference;
        this.closureId = closureId;
        this.accountName = accountName;
    }
    
    /**
     * Creates a new {@link GLClosureAccountBalanceReportData} object
     * 
     * @param accountNumber
     * @param transactionDate
     * @param postedDate
     * @param amount
     * @param reference
     * @return {@link GLClosureAccountBalanceReportData} object
     */
    public static GLClosureAccountBalanceReportData instance(final String accountNumber, final LocalDate transactionDate, 
            final LocalDate postedDate, final BigDecimal amount, final String reference, final Long closureId,
            final String accountName) {
        return new GLClosureAccountBalanceReportData(accountNumber, transactionDate, postedDate, 
                GLClosureAccountBalanceReportTransactionType.NEW, amount, reference, closureId, accountName);
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @return the transactionDate
     */
    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    /**
     * @return the postedDate
     */
    public LocalDate getPostedDate() {
        return postedDate;
    }

    /**
     * @return the transactionType
     */
    public GLClosureAccountBalanceReportTransactionType getTransactionType() {
        return transactionType;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @return the closureId
     */
    public Long getClosureId() { return closureId; }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
