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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.fineract.infrastructure.core.service.DateUtils;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.useradministration.domain.AppUser;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;

@Entity
@Table(name = "m_guarantor_interest_allocation")
public class GuarantorInterestAllocation extends AbstractPersistableCustom<Long> {

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;



    @Column(name = "allocated_interest_paid", scale = 6, precision = 19, nullable = false)
    private BigDecimal allocatedInterestPaid;

    @ManyToOne
    @JoinColumn(name = "submitted_user_id", nullable = true)
    private AppUser submittedUserId;

    @Column(name = "is_reversed", nullable = false)
    private boolean reversed;

    @Temporal(TemporalType.DATE)
    @Column(name = "submitted_on_date")
    private Date submittedOnDate;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_on_date", nullable = false)
    private  Date createdDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "interestAllocation", orphanRemoval = true, fetch=FetchType.EAGER)
    protected final List<GuarantorInterestPayment> guarantorInterestPayments = new ArrayList<>();


    protected GuarantorInterestAllocation() {
    }

    private GuarantorInterestAllocation(final BigDecimal allocatedInterestPaid,final Loan loan, final AppUser submittedUserId) {
        this.allocatedInterestPaid = allocatedInterestPaid;
        this.loan = loan;
        this.submittedUserId = submittedUserId;
        this.submittedOnDate = DateUtils.getDateOfTenant();
        this.createdDate = new Date();
    }

    public static GuarantorInterestAllocation createNew(final BigDecimal allocatedInterestPaid,final Loan loan, final AppUser submittedUserId){
        return new GuarantorInterestAllocation(allocatedInterestPaid,loan,submittedUserId);
    }

    public BigDecimal getAllocatedInterestPaid() {return this.allocatedInterestPaid;}

    public AppUser getSubmittedUserId() {return this.submittedUserId;}

    public Date getCreatedDate() {return this.createdDate;}

    public Date getSubmittedOnDate() {return this.submittedOnDate;}

    public boolean isReversed() {return this.reversed;}

    public void reverse(){this.reversed = true;}

    public void addGuarantorInterestPayment(final List<GuarantorInterestPayment> guarantorInterestPayments){
        this.guarantorInterestPayments.addAll(guarantorInterestPayments);
    }

    public List<GuarantorInterestPayment> getGuarantorInterestPayments() {
        return this.guarantorInterestPayments;
    }
}
