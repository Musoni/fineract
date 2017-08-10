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
package org.apache.fineract.portfolio.loanaccount.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.client.domain.Client;
import org.apache.fineract.portfolio.client.domain.ClientRepositoryWrapper;
import org.apache.fineract.portfolio.loanaccount.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class GroupLoanMemberAllocationAssembler {

    private final FromJsonHelper fromApiJsonHelper;
    private final GroupLoanMemberAllocationRepository groupLoanMemberAllocationRepository;
    private final ClientRepositoryWrapper clientRepository;

    @Autowired
    public GroupLoanMemberAllocationAssembler(final FromJsonHelper fromApiJsonHelper, 
            final GroupLoanMemberAllocationRepository groupLoanMemberAllocationRepository, 
            final ClientRepositoryWrapper clientRepository) {
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.groupLoanMemberAllocationRepository = groupLoanMemberAllocationRepository;
        this.clientRepository = clientRepository;
    }

    public Set<GroupLoanMemberAllocation> fromParsedJson(final JsonElement element) {

        final Set<GroupLoanMemberAllocation> groupLoanMemberAllocations = new HashSet<>();

        if (element.isJsonObject()) {
            final JsonObject topLevelJsonElement = element.getAsJsonObject();
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);

            if (topLevelJsonElement.has("groupMemberAllocation") && topLevelJsonElement.get("groupMemberAllocation").isJsonArray()) {
                final JsonArray array = topLevelJsonElement.get("groupMemberAllocation").getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {

                    final JsonObject loanChargeElement = array.get(i).getAsJsonObject();

                    final Long id = this.fromApiJsonHelper.extractLongNamed("id", loanChargeElement);
                    final Long clientId = this.fromApiJsonHelper.extractLongNamed("client_id", loanChargeElement);
                    final BigDecimal amount = this.fromApiJsonHelper.extractBigDecimalNamed("amount", loanChargeElement, locale);

                    if (id == null) {
                        final Client client = this.clientRepository.findOneWithNotFoundDetection(clientId);

                        groupLoanMemberAllocations.add(GroupLoanMemberAllocation.from(client,amount));

                    } else {
                        final Long groupLoanMemberAllocationId = id;
                        final GroupLoanMemberAllocation groupLoanMemberAllocation = this.groupLoanMemberAllocationRepository.findOne(groupLoanMemberAllocationId);
                        if (groupLoanMemberAllocation == null) { /* throw new LoanChargeNotFoundException(loanChargeId);*/ }

                        groupLoanMemberAllocation.update(amount);

                        groupLoanMemberAllocations.add(groupLoanMemberAllocation);
                    }
                }
            }
        }

        return groupLoanMemberAllocations;
    }

    public Set<GroupLoanMemberAllocation> fromParsedJsonWithLoan(final JsonElement element,final Loan loan) {

        final Set<GroupLoanMemberAllocation> groupLoanMemberAllocations = new HashSet<>();

        if (element.isJsonObject()) {
            final JsonObject topLevelJsonElement = element.getAsJsonObject();
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);

            if (topLevelJsonElement.has("groupMemberAllocation") && topLevelJsonElement.get("groupMemberAllocation").isJsonArray()) {
                final JsonArray array = topLevelJsonElement.get("groupMemberAllocation").getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {

                    final JsonObject loanChargeElement = array.get(i).getAsJsonObject();

                    final Long id = this.fromApiJsonHelper.extractLongNamed("id", loanChargeElement);
                    final Long clientId = this.fromApiJsonHelper.extractLongNamed("client_id", loanChargeElement);
                    final BigDecimal amount = this.fromApiJsonHelper.extractBigDecimalNamed("amount", loanChargeElement, locale);

                    if (id == null) {
                        final Client client = this.clientRepository.findOneWithNotFoundDetection(clientId);

                        final GroupLoanMemberAllocation groupLoanMemberAllocation = GroupLoanMemberAllocation.createNew(loan,client,amount) ;
                        groupLoanMemberAllocations.add(groupLoanMemberAllocation);

                    } else {
                        final Long groupLoanMemberAllocationId = id;
                        final GroupLoanMemberAllocation groupLoanMemberAllocation = this.groupLoanMemberAllocationRepository.findOne(groupLoanMemberAllocationId);
                        if (groupLoanMemberAllocation == null) { /* throw new LoanChargeNotFoundException(loanChargeId);*/ }

                        groupLoanMemberAllocation.update(amount);

                        groupLoanMemberAllocations.add(groupLoanMemberAllocation);
                    }
                }
            }
        }

        return groupLoanMemberAllocations;
    }
}