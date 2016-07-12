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
package org.apache.fineract.portfolio.loanaccount.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.fineract.portfolio.client.domain.Client;
import org.apache.fineract.portfolio.loanaccount.data.GroupLoanMembersAllocationData;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_group_loan_member_allocation")
public class GroupLoanMemberAllocation extends AbstractPersistable<Long> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(name = "amount", scale = 6, precision = 19, nullable = false)
    private BigDecimal amount;


    public static GroupLoanMemberAllocation createNew(final Loan loan,Client client, final BigDecimal amount) {

        return new GroupLoanMemberAllocation(loan,client,amount);

    }

    public static GroupLoanMemberAllocation from(final Client client, final BigDecimal amount) {
        return new GroupLoanMemberAllocation(null,client,amount);
    }

    private GroupLoanMemberAllocation(final Loan loan, final Client client, final BigDecimal amount) {
        this.loan = loan;
        this.client = client;
        this.amount = amount;
    }


    public void update(final BigDecimal amount) {
        this.amount = amount;
    }

    public void associateWith(final Loan loan) {
        this.loan = loan;
    }

    public Loan loan(){
        return this.loan;
    }

    public Client client(){
        return this.client;
    }


    public BigDecimal amount() {
        return this.amount;
    }

    protected GroupLoanMemberAllocation() {
        //
    }


    public GroupLoanMembersAllocationData toData() {

        return GroupLoanMembersAllocationData.newOne(null,null,this.client.toData(),this.amount());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }
        if (obj.getClass() != getClass()) { return false; }
        final GroupLoanMemberAllocation rhs = (GroupLoanMemberAllocation) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)) //
                .append(getId(), rhs.getId()) //
                .append(this.client.getId(), rhs.client.getId()) //
                .append(this.amount, this.amount)//
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 5) //
                .append(getId()) //
                .append(this.client.getId()) //
                .append(this.amount)//
                .toHashCode();
    }
}
