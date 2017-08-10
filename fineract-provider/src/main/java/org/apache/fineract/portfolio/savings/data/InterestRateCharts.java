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
package org.apache.fineract.portfolio.savings.data;

import org.joda.time.LocalDate;

import java.math.BigDecimal;

public class InterestRateCharts {

    private Long id;

    private String name;

    private String description;

    private LocalDate fromDate;

    private LocalDate endDate;

    private BigDecimal annualInterestRate;

    private boolean applyToExistingSavingsAccount;


    public InterestRateCharts(final Long id, final String name, final String description, final LocalDate fromDate, final LocalDate endDate,
                              final BigDecimal annualInterestRate, boolean applyToExistingSavingsAccount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.fromDate = fromDate;
        this.endDate = endDate;
        this.annualInterestRate = annualInterestRate;
        this.applyToExistingSavingsAccount = applyToExistingSavingsAccount;
    }

    public static InterestRateCharts createNew(final Long id , final String name, final String description, final LocalDate fromDate, final LocalDate endDate,
                                               final BigDecimal annualInterestRate, boolean applyToExistingSavingsAccount){
        return new InterestRateCharts(id, name,description,fromDate,endDate,annualInterestRate,applyToExistingSavingsAccount);

    }

    public Long getId() {return id;}

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public boolean isApplyToExistingSavingsAccount() {
        return applyToExistingSavingsAccount;
    }
}
