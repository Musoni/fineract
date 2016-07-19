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
package org.apache.fineract.portfolio.loanaccount.data;

import org.apache.fineract.portfolio.client.data.ClientData;

import java.math.BigDecimal;

/**
 * Immutable data object for loan charge data.
 */
public class GroupLoanMembersAllocationData {

    private final Long id;
    private final Long loanId;
    private final ClientData clientData;
    private final BigDecimal amount;

    /**
     * used when populating with details from charge definition (for crud on
     * charges)
     */
    public static GroupLoanMembersAllocationData newOne(final Long id, final Long loanId, final ClientData clientData, final BigDecimal amount) {
        return new GroupLoanMembersAllocationData(id, loanId, clientData, amount);
    }

    public GroupLoanMembersAllocationData(final Long id, final Long loanId, final ClientData clientData, final BigDecimal amount) {
        this.id = id;
        this.loanId = loanId;
        this.clientData = clientData;
        this.amount = amount;
    }

    public Long getId() {
        return this.id;
    }

    public Long getLoanId() {
        return this.loanId;
    }

    public ClientData clientData() {

        return clientData;
    }


    public BigDecimal getAmount() {
        return this.amount;
    }
}
