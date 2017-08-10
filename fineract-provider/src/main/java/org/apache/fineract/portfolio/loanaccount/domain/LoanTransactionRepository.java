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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanTransactionRepository extends JpaRepository<LoanTransaction, Long>, JpaSpecificationExecutor<LoanTransaction> {

    // no added behaviour

    public static final String FIND_ALL_TRANSACTIONS_AFTER_CLIENT_TRANSFER = "from LoanTransaction t1 where t1.loan.id = :loanId and t1.reversed=false and " +
            "t1.id > (select max(t2.id) from LoanTransaction t2 where t2.loan.id = :loanId and t2.typeOf = :enumType and t2.reversed=false)";

    public static final String FIND_THE_CURRENT_TRANSFER_TRANSACTIONS = "select * from m_loan_transaction t1 where t1.loan_id = :loanId and " +
            "t1.transaction_type_enum in (12,13) and t1.is_reversed=0 order by t1.id desc limit 2";

    public static final String FIND_THE_LAST_TRANSFER_TRANSACTIONS = "select * from m_loan_transaction t1 where t1.loan_id = :loanId and " +
            "t1.transaction_type_enum in (13) and t1.is_reversed=0 order by t1.id desc limit 1";

    @Query(value=FIND_THE_CURRENT_TRANSFER_TRANSACTIONS,nativeQuery = true)
    List<LoanTransaction> currentTransferTransaction(@Param("loanId") Long loanId);

    @Query(value=FIND_THE_LAST_TRANSFER_TRANSACTIONS,nativeQuery = true)
    LoanTransaction lastApprovedTransfer(@Param("loanId") Long loanId);

    @Query(FIND_ALL_TRANSACTIONS_AFTER_CLIENT_TRANSFER)
    List<LoanTransaction> transactionsAfterClientTransfer(@Param("loanId") Long loanId,@Param("enumType") Integer enumType);

}