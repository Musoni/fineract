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

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface GuarantorInterestPaymentRepository  extends JpaRepository<GuarantorInterestPayment, Long>,
        JpaSpecificationExecutor<GuarantorInterestPayment> {

    public static String FIND_ACTIVE_INTEREST_ALLOCATION = "select gI from GuarantorInterestPayment gI inner join gI.interestAllocation m where m.loan.id =:loanId " +
            "and gI.guarantor.id =:guarantorId and m.reversed=false ";


    @Query( FIND_ACTIVE_INTEREST_ALLOCATION)
    List<GuarantorInterestPayment> findByLoanAndReverseFalse(@Param("loanId") Long loanId, @Param("guarantorId") Long guarantorId);


    /*
    select *
    from m_guarantor_loan_interest_payment mg
    inner join m_guarantor_interest_allocation ml on ml.id = mg.guarantor_interest_allocation_id
    where ml.loan_id = 7 and ml.is_reversed is false */

    Collection<GuarantorInterestPayment> findByInterestAllocationOrderByIdDesc(GuarantorInterestAllocation guarantorInterestAllocation);
}
