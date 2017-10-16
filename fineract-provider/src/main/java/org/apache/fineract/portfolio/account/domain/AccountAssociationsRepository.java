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
package org.apache.fineract.portfolio.account.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountAssociationsRepository extends JpaRepository<AccountAssociations, Long>,
        JpaSpecificationExecutor<AccountAssociations> {
    
    @Query("select aa from AccountAssociations aa where aa.loanAccount.id= :loanId")
    AccountAssociations findByLoanId(@Param("loanId") Long loanId);

    @Query("select aa from AccountAssociations aa where aa.loanAccount.id= :loanId and aa.associationType = :associationType")
    AccountAssociations findByLoanIdAndType(@Param("loanId") Long loanId, @Param("associationType") Integer accountAssociationType);

    @Query("select aa from AccountAssociations aa where aa.savingsAccount.id= :savingsId and aa.associationType = :associationType")
    AccountAssociations findBySavingsIdAndType(@Param("savingsId") Long savingsId, @Param("associationType") Integer accountAssociationType);
}
