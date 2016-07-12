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
package org.apache.fineract.portfolio.savings.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SavingsAccountTransactionRepository extends JpaRepository<SavingsAccountTransaction, Long>,
        JpaSpecificationExecutor<SavingsAccountTransaction> {

    SavingsAccountTransaction findOneByIdAndSavingsAccountId(Long transactionId, Long savingsId);


    public static final String FIND_ALL_TRANSACTIONS_AFTER_CLIENT_TRANSFER = "from SavingsAccountTransaction t1 where t1.savingsAccount.id = :savingsAccountId and t1.reversed=false and " +
            "t1.id > (select max(t2.id) from SavingsAccountTransaction  t2 where t2.savingsAccount.id = :savingsAccountId and t2.typeOf = :enumType and t2.reversed=false)";

    public static final String FIND_THE_CURRENT_TRANSFER_TRANSACTIONS = "select * from m_savings_account_transaction t1 where t1.savings_account_id = :savingsAccountId and " +
            "t1.transaction_type_enum in (12,13) and t1.is_reversed=0  order by t1.id desc limit 2";

    public static final String FIND_THE_LAST_APPROVED_TRANSFER_TRANSACTIONS= "select * from m_savings_account_transaction t1 where t1.savings_account_id = :savingsAccountId and " +
            "t1.transaction_type_enum in (13) and t1.is_reversed=0  order by t1.id desc limit 1";


    @Query(FIND_ALL_TRANSACTIONS_AFTER_CLIENT_TRANSFER)
    List<SavingsAccountTransaction> transactionsAfterClientTransfer(@Param("savingsAccountId") Long savingsAccountId,@Param("enumType") Integer enumType);

    @Query(value=FIND_THE_LAST_APPROVED_TRANSFER_TRANSACTIONS,nativeQuery = true)
    SavingsAccountTransaction lastApprovedTransfer(@Param("savingsAccountId") Long savingsAccountId);

    @Query(value=FIND_THE_CURRENT_TRANSFER_TRANSACTIONS,nativeQuery = true)
    List<SavingsAccountTransaction> currentTransferTransaction(@Param("savingsAccountId") Long savingsAccountId);


}