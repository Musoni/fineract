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
package org.apache.fineract.portfolio.loanaccount.guarantor.domain;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.ObjectUtils;
import org.apache.fineract.infrastructure.core.service.DateUtils;
import org.apache.fineract.portfolio.savings.domain.SavingsAccount;
import org.apache.fineract.portfolio.savings.domain.SavingsAccountTransaction;
import org.joda.time.LocalDate;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;


@Entity
@Table(name = "m_guarantor_loan_interest_payment")
public class GuarantorInterestPayment extends AbstractPersistableCustom<Long> {

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "guarantor_id", nullable = false)
    private Guarantor guarantor;

    @ManyToOne
    @JoinColumn(name = "guarantor_interest_allocation_id", nullable = false)
    private GuarantorInterestAllocation interestAllocation;


    @Column(name = "deposited_amount", scale = 6, precision = 19, nullable = false)
    private BigDecimal depositedAmount;

    @ManyToOne
    @JoinColumn(name = "savings_account_id", nullable = false)
    private SavingsAccount savingsAccount;

    @ManyToOne
    @JoinColumn(name = "savings_transaction_id", nullable = false)
    private SavingsAccountTransaction savingsAccountTransaction;

    @Column(name = "is_reversed", nullable = false)
    private boolean reversed;

    @Temporal(TemporalType.DATE)
    @Column(name = "submitted_on_date")
    private Date submittedOnDate;


    protected GuarantorInterestPayment() {
    }


    private GuarantorInterestPayment(final Guarantor guarantor, final GuarantorInterestAllocation guarantorInterestAllocation,
                                     final BigDecimal depositedAmount,final SavingsAccount savingsAccount ,final  SavingsAccountTransaction savingsAccountTransaction) {
        this.guarantor = guarantor;
        this.interestAllocation = guarantorInterestAllocation;
        this.depositedAmount = depositedAmount;
        this.savingsAccount = savingsAccount;
        this.savingsAccountTransaction = savingsAccountTransaction;
        this.submittedOnDate = DateUtils.getDateOfTenant();
    }


    public static GuarantorInterestPayment createNew(final Guarantor guarantor, final GuarantorInterestAllocation guarantorInterestAccumulated,
                                                     final BigDecimal depositedAmount,final SavingsAccount savingsAccount,
                                                     final SavingsAccountTransaction savingsAccountTransaction){
        return new GuarantorInterestPayment(guarantor,guarantorInterestAccumulated,depositedAmount,savingsAccount,savingsAccountTransaction);
    }


    public Guarantor getGuarantor() {return this.guarantor;}

    public GuarantorInterestAllocation getInterestAllocation() {
        return this.interestAllocation;
    }

    public BigDecimal getDepositedAmount() {return this.depositedAmount;}

    public SavingsAccountTransaction getSavingsAccountTransaction() {return this.savingsAccountTransaction;}

    public SavingsAccount getSavingsAccount() {return this.savingsAccount;}

    public LocalDate getSubmittedOnDate() {
        return (LocalDate) ObjectUtils.defaultIfNull(new LocalDate(this.submittedOnDate), null);
    }

    public boolean isReversed() {return this.reversed;}

    public void reverse(){this.reversed = true;}
}
