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

import static org.apache.fineract.portfolio.interestratechart.InterestRateChartSlabApiConstants.annualInterestRateParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.applyToExistingSavingsAccountParamName;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.portfolio.interestratechart.domain.InterestRateChartFields;
import org.joda.time.LocalDate;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "m_savings_product_interest_rate_chart")
public class SavingsProductInterestRateChart extends AbstractPersistable<Long> {

    @ManyToOne
    @JoinColumn(name = "savings_product_id", nullable = false)
    private SavingsProduct savingsProduct;

    @Embedded
    private InterestRateChartFields chartFields;

    @Column(name = "annual_interest_rate", scale = 6, precision = 19, nullable = false)
    private BigDecimal annualInterestRate;

    @Column(name = "apply_to_existing_savings_account", nullable = false)
    private boolean applyToExistingSavingsAccount;

    protected SavingsProductInterestRateChart() {
    }

    private SavingsProductInterestRateChart(final SavingsProduct savingsProduct, final InterestRateChartFields chartFields,
                                            final BigDecimal annualInterestRate, final boolean applyToExistingSavingsAccount) {
        this.savingsProduct = savingsProduct;
        this.chartFields = chartFields;
        this.annualInterestRate = annualInterestRate;
        this.applyToExistingSavingsAccount = applyToExistingSavingsAccount;
    }

    public void updateSavingsProduct(final SavingsProduct product){
        this.savingsProduct = product;
    }

    public static SavingsProductInterestRateChart createNewFromJson(final SavingsProduct savingsProduct, final JsonCommand command){

        final String name = command.stringValueOfParameterNamed("name");

        final String description = command.stringValueOfParameterNamed("description");

        final LocalDate fromDate = command.localDateValueOfParameterNamed("fromDate");

        final LocalDate endDate = command.localDateValueOfParameterNamed("endDate");

        final BigDecimal annualInterestRate = command.bigDecimalValueOfParameterNamed("annualInterestRate");

        final boolean applyToExistingSavingsAccount = command.booleanPrimitiveValueOfParameterNamed("applyToExistingSavingsAccount");

        final InterestRateChartFields interestRateChartFields = InterestRateChartFields.createNew(name,description,fromDate,endDate, false);

        return new SavingsProductInterestRateChart(savingsProduct,interestRateChartFields,annualInterestRate,applyToExistingSavingsAccount);
    }
    public static SavingsProductInterestRateChart createNew(final SavingsProduct savingsProduct,final String name, final String description, final LocalDate fromDate,final LocalDate endDate,
                                                            final BigDecimal annualInterestRate, final boolean applyToExistingSavingsAccount){
        final InterestRateChartFields interestRateChartFields = InterestRateChartFields.createNew(name,description,fromDate,endDate, false);

        return new SavingsProductInterestRateChart(savingsProduct,interestRateChartFields,annualInterestRate,applyToExistingSavingsAccount);
    }
    public void update(JsonCommand command, final Map<String, Object> actualChanges, final DataValidatorBuilder baseDataValidator) {

        this.chartFields.update(command, actualChanges, baseDataValidator);

        if (command.isChangeInBigDecimalParameterNamed(annualInterestRateParamName, this.annualInterestRate, command.extractLocale())) {
            final BigDecimal newValue = command.bigDecimalValueOfParameterNamed(annualInterestRateParamName, command.extractLocale());
            actualChanges.put(annualInterestRateParamName, newValue);
            this.annualInterestRate = newValue;
        }

        if(command.isChangeInBooleanParameterNamed(applyToExistingSavingsAccountParamName,this.applyToExistingSavingsAccount)){
            final boolean newValue = command.booleanPrimitiveValueOfParameterNamed(applyToExistingSavingsAccountParamName);
            actualChanges.put(applyToExistingSavingsAccountParamName, newValue);
            this.applyToExistingSavingsAccount = newValue;
        }

    }

    public SavingsProduct getSavingsProduct() {return savingsProduct;}

    public InterestRateChartFields getChartFields() {return this.chartFields;}

    public BigDecimal getAnnualInterestRate() {return this.annualInterestRate;}

    public boolean isApplyToExistingSavingsAccount() {return this.applyToExistingSavingsAccount;}

    public void updateAnnualInterestRate(final BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public void updateApplyToExistingSavingsAccount(boolean applyToExistingSavingsAccount) {
        this.applyToExistingSavingsAccount = applyToExistingSavingsAccount;
    }
}
