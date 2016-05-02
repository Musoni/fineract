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
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.addProductChargeToExistingAccountsParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.allowOverdraftParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.applyToExistingSavingsAccountParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.chargesParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.currencyCodeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.daysToDormancyParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.daysToEscheatParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.daysToInactiveParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.descriptionParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.digitsAfterDecimalParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.endDateParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.enforceMinRequiredBalanceParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.fromDateParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.idParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.inMultiplesOfParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestCalculationDaysInYearTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestCalculationTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestCompoundingPeriodTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestPostingPeriodTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.interestRateCharts;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.isDormancyTrackingActiveParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.lockinPeriodFrequencyParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.lockinPeriodFrequencyTypeParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.minBalanceForInterestCalculationParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.minOverdraftForInterestCalculationParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.minRequiredBalanceParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.minRequiredOpeningBalanceParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.nameParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.nominalAnnualInterestRateOverdraftParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.nominalAnnualInterestRateParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.overdraftLimitParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.productGroupIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.shortNameParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.taxGroupIdParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.withHoldTaxParamName;
import static org.apache.fineract.portfolio.savings.SavingsApiConstants.withdrawalFeeForTransfersParamName;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.fineract.accounting.common.AccountingRuleType;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.organisation.monetary.domain.MonetaryCurrency;
import org.apache.fineract.portfolio.charge.domain.Charge;
import org.apache.fineract.portfolio.charge.domain.ChargeRepositoryWrapper;
import org.apache.fineract.portfolio.charge.exception.ChargeCannotBeAppliedToException;
import org.apache.fineract.portfolio.loanproduct.exception.InvalidCurrencyException;
import org.apache.fineract.portfolio.savings.SavingsCompoundingInterestPeriodType;
import org.apache.fineract.portfolio.savings.SavingsInterestCalculationDaysInYearType;
import org.apache.fineract.portfolio.savings.SavingsInterestCalculationType;
import org.apache.fineract.portfolio.savings.SavingsPeriodFrequencyType;
import org.apache.fineract.portfolio.savings.SavingsPostingInterestPeriodType;
import org.apache.fineract.portfolio.tax.domain.TaxGroup;
import org.apache.fineract.portfolio.tax.domain.TaxGroupRepositoryWrapper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class SavingsProductAssembler {

    private final ChargeRepositoryWrapper chargeRepository;
    private final CodeValueRepositoryWrapper codeValueRepository;
    private final FromJsonHelper fromApiJsonHelper;
    private final SavingsProductInterestRateChartRepository savingsProductInterestRateChartRepository;
    private final ApplyChargesToExistingSavingsAccountRepository applyChargesToExistingSavingsAccountRepository;
    private final TaxGroupRepositoryWrapper taxGroupRepository;

    @Autowired
    public SavingsProductAssembler(final ChargeRepositoryWrapper chargeRepository, 
    		final CodeValueRepositoryWrapper codeValueRepository, final FromJsonHelper fromApiJsonHelper, 
    		final SavingsProductInterestRateChartRepository savingsProductInterestRateChartRepository, 
    		final ApplyChargesToExistingSavingsAccountRepository applyChargesToExistingSavingsAccountRepository, 
    		final TaxGroupRepositoryWrapper taxGroupRepository) {
        this.chargeRepository = chargeRepository;
        this.codeValueRepository = codeValueRepository;
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.savingsProductInterestRateChartRepository = savingsProductInterestRateChartRepository;
        this.applyChargesToExistingSavingsAccountRepository = applyChargesToExistingSavingsAccountRepository;
        this.taxGroupRepository = taxGroupRepository;
    }

    public SavingsProduct assemble(final JsonCommand command) {

        final String name = command.stringValueOfParameterNamed(nameParamName);
        final String shortName = command.stringValueOfParameterNamed(shortNameParamName);
        final String description = command.stringValueOfParameterNamed(descriptionParamName);

        final String currencyCode = command.stringValueOfParameterNamed(currencyCodeParamName);
        final Integer digitsAfterDecimal = command.integerValueOfParameterNamed(digitsAfterDecimalParamName);
        final Integer inMultiplesOf = command.integerValueOfParameterNamed(inMultiplesOfParamName);
        final MonetaryCurrency currency = new MonetaryCurrency(currencyCode, digitsAfterDecimal, inMultiplesOf);

        final BigDecimal interestRate = command.bigDecimalValueOfParameterNamed(nominalAnnualInterestRateParamName);

        SavingsCompoundingInterestPeriodType interestCompoundingPeriodType = null;
        final Integer interestPeriodTypeValue = command.integerValueOfParameterNamed(interestCompoundingPeriodTypeParamName);
        if (interestPeriodTypeValue != null) {
            interestCompoundingPeriodType = SavingsCompoundingInterestPeriodType.fromInt(interestPeriodTypeValue);
        }

        SavingsPostingInterestPeriodType interestPostingPeriodType = null;
        final Integer interestPostingPeriodTypeValue = command.integerValueOfParameterNamed(interestPostingPeriodTypeParamName);
        if (interestPostingPeriodTypeValue != null) {
            interestPostingPeriodType = SavingsPostingInterestPeriodType.fromInt(interestPostingPeriodTypeValue);
        }

        SavingsInterestCalculationType interestCalculationType = null;
        final Integer interestCalculationTypeValue = command.integerValueOfParameterNamed(interestCalculationTypeParamName);
        if (interestCalculationTypeValue != null) {
            interestCalculationType = SavingsInterestCalculationType.fromInt(interestCalculationTypeValue);
        }

        SavingsInterestCalculationDaysInYearType interestCalculationDaysInYearType = null;
        final Integer interestCalculationDaysInYearTypeValue = command
                .integerValueOfParameterNamed(interestCalculationDaysInYearTypeParamName);
        if (interestCalculationDaysInYearTypeValue != null) {
            interestCalculationDaysInYearType = SavingsInterestCalculationDaysInYearType.fromInt(interestCalculationDaysInYearTypeValue);
        }

        final BigDecimal minRequiredOpeningBalance = command
                .bigDecimalValueOfParameterNamedDefaultToNullIfZero(minRequiredOpeningBalanceParamName);

        final Integer lockinPeriodFrequency = command.integerValueOfParameterNamedDefaultToNullIfZero(lockinPeriodFrequencyParamName);
        SavingsPeriodFrequencyType lockinPeriodFrequencyType = null;
        final Integer lockinPeriodFrequencyTypeValue = command.integerValueOfParameterNamed(lockinPeriodFrequencyTypeParamName);
        if (lockinPeriodFrequencyTypeValue != null) {
            lockinPeriodFrequencyType = SavingsPeriodFrequencyType.fromInt(lockinPeriodFrequencyTypeValue);
        }

        boolean iswithdrawalFeeApplicableForTransfer = false;
        if (command.parameterExists(withdrawalFeeForTransfersParamName)) {
            iswithdrawalFeeApplicableForTransfer = command.booleanPrimitiveValueOfParameterNamed(withdrawalFeeForTransfersParamName);
        }

        final AccountingRuleType accountingRuleType = AccountingRuleType.fromInt(command.integerValueOfParameterNamed("accountingRule"));

        CodeValue productGroup = null;
        if(command.parameterExists(productGroupIdParamName)){
            Long productGroupId = command.longValueOfParameterNamed(productGroupIdParamName);
            productGroup = this.codeValueRepository.findOneWithNotFoundDetection(productGroupId);
        }

        // Savings product charges
        final Set<Charge> charges = assembleListOfSavingsProductCharges(command, currencyCode);

        final Set<ApplyChargesToExistingSavingsAccount> applyChargesToExistingSavingsAccounts = assembleApplyChargesToExistingSavingsAccount(command);

        boolean allowOverdraft = false;
        if (command.parameterExists(allowOverdraftParamName)) {
            allowOverdraft = command.booleanPrimitiveValueOfParameterNamed(allowOverdraftParamName);
        }

        BigDecimal overdraftLimit = BigDecimal.ZERO;
        if(command.parameterExists(overdraftLimitParamName)){
            overdraftLimit = command.bigDecimalValueOfParameterNamed(overdraftLimitParamName);
        }

        BigDecimal nominalAnnualInterestRateOverdraft = BigDecimal.ZERO;
        if(command.parameterExists(nominalAnnualInterestRateOverdraftParamName)){
        	nominalAnnualInterestRateOverdraft = command.bigDecimalValueOfParameterNamed(nominalAnnualInterestRateOverdraftParamName);
        }
        
        BigDecimal minOverdraftForInterestCalculation = BigDecimal.ZERO;
        if(command.parameterExists(minOverdraftForInterestCalculationParamName)){
        	minOverdraftForInterestCalculation = command.bigDecimalValueOfParameterNamed(minOverdraftForInterestCalculationParamName);
        }

        boolean enforceMinRequiredBalance = false;
        if (command.parameterExists(enforceMinRequiredBalanceParamName)) {
            enforceMinRequiredBalance = command.booleanPrimitiveValueOfParameterNamed(enforceMinRequiredBalanceParamName);
        }

        BigDecimal minRequiredBalance = BigDecimal.ZERO;
        if(command.parameterExists(minRequiredBalanceParamName)){
            minRequiredBalance = command.bigDecimalValueOfParameterNamed(minRequiredBalanceParamName);
        }
        final BigDecimal minBalanceForInterestCalculation = command
                .bigDecimalValueOfParameterNamedDefaultToNullIfZero(minBalanceForInterestCalculationParamName);

        Set<SavingsProductInterestRateChart> savingsProductInterestRateChart = null;

        if(command.parameterExists(interestRateCharts)){
            savingsProductInterestRateChart= this.assembleSetOfInterestRateCharts(command);
        }
        
        boolean withHoldTax = command.booleanPrimitiveValueOfParameterNamed(withHoldTaxParamName);
        final TaxGroup taxGroup = assembleTaxGroup(command);
        
        final Boolean isDormancyTrackingActive = command.booleanObjectValueOfParameterNamed(isDormancyTrackingActiveParamName);
        final Long daysToInactive = command.longValueOfParameterNamed(daysToInactiveParamName);
        final Long daysToDormancy = command.longValueOfParameterNamed(daysToDormancyParamName);
        final Long daysToEscheat = command.longValueOfParameterNamed(daysToEscheatParamName);

        return SavingsProduct.createNew(name, shortName, description, currency, interestRate, productGroup, interestCompoundingPeriodType,
                interestPostingPeriodType, interestCalculationType, interestCalculationDaysInYearType, minRequiredOpeningBalance,
                lockinPeriodFrequency, lockinPeriodFrequencyType, iswithdrawalFeeApplicableForTransfer, accountingRuleType, charges,
                allowOverdraft, overdraftLimit, enforceMinRequiredBalance, minRequiredBalance, minBalanceForInterestCalculation,
                nominalAnnualInterestRateOverdraft, minOverdraftForInterestCalculation,savingsProductInterestRateChart, 
                applyChargesToExistingSavingsAccounts, withHoldTax, taxGroup, 
                isDormancyTrackingActive, daysToInactive, daysToDormancy, daysToEscheat);
    }

	public Set<ApplyChargesToExistingSavingsAccount> assembleApplyChargesToExistingSavingsAccount(final JsonCommand command){

        final Set<ApplyChargesToExistingSavingsAccount> applyChargesToExistingSavingsAccounts = new HashSet<>();

        if (command.parameterExists(chargesParamName)) {
            final JsonArray chargesArray = command.arrayOfParameterNamed(chargesParamName);
            if (chargesArray != null) {
                for (int i = 0; i < chargesArray.size(); i++) {

                    final JsonObject jsonObject = chargesArray.get(i).getAsJsonObject();
                    if (jsonObject.has(idParamName)) {
                        final Long id = jsonObject.get(idParamName).getAsLong();
                        if(jsonObject.has(addProductChargeToExistingAccountsParamName)){
                            final boolean addChargeToExistingSavingsAccount = jsonObject.get(addProductChargeToExistingAccountsParamName).getAsBoolean();
                            final Charge charge = this.chargeRepository.findOneWithNotFoundDetection(id);
                            if(addChargeToExistingSavingsAccount){
                                if(charge.isAnnualFee() || charge.isMonthlyFee() || charge.isWithdrawalFee()){
                                    ApplyChargesToExistingSavingsAccount applyChargesToExistingSavingsAccount = new ApplyChargesToExistingSavingsAccount(null,charge,addChargeToExistingSavingsAccount);
                                    applyChargesToExistingSavingsAccounts.add(applyChargesToExistingSavingsAccount);
                                }
                            }
                        }
                    }
                }
            }
        }
        return applyChargesToExistingSavingsAccounts;
    }

    public Set<Charge> assembleListOfSavingsProductCharges(final JsonCommand command, final String savingsProductCurrencyCode) {

        final Set<Charge> charges = new HashSet<>();

        if (command.parameterExists(chargesParamName)) {
            final JsonArray chargesArray = command.arrayOfParameterNamed(chargesParamName);
            if (chargesArray != null) {
                for (int i = 0; i < chargesArray.size(); i++) {

                    final JsonObject jsonObject = chargesArray.get(i).getAsJsonObject();
                    if (jsonObject.has(idParamName)) {
                        final Long id = jsonObject.get(idParamName).getAsLong();

                        final Charge charge = this.chargeRepository.findOneWithNotFoundDetection(id);

                        if (!charge.isSavingsCharge()) {
                            final String errorMessage = "Charge with identifier " + charge.getId()
                                    + " cannot be applied to Savings product.";
                            throw new ChargeCannotBeAppliedToException("savings.product", errorMessage, charge.getId());
                        }

                        if (!savingsProductCurrencyCode.equals(charge.getCurrencyCode())) {
                            final String errorMessage = "Charge and Savings Product must have the same currency.";
                            throw new InvalidCurrencyException("charge", "attach.to.savings.product", errorMessage);
                        }
                        charges.add(charge);
                    }
                }
            }
        }

        return charges;
    }

    public Set<SavingsProductInterestRateChart> assembleSetOfInterestRateCharts(final JsonCommand command){

        final Set<SavingsProductInterestRateChart> savingsProductInterestRateChart = new HashSet<>();

        if(command.parameterExists(interestRateCharts)){
            final JsonArray interestRateChartArray = command.arrayOfParameterNamed(interestRateCharts);
            if(interestRateChartArray != null){
                for (int i = 0; i <interestRateChartArray.size(); i++) {
                    final JsonObject interestRateChartElement = interestRateChartArray.get(i).getAsJsonObject();
                    final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(interestRateChartElement);
                    SavingsProductInterestRateChart interestRateChart = this.assembleFrom(interestRateChartElement,locale);
                    savingsProductInterestRateChart.add(interestRateChart);
                }
            }
        }

        return savingsProductInterestRateChart;

    }

    public SavingsProductInterestRateChart assembleFrom(final JsonElement element, Locale locale) {

        final String name = this.fromApiJsonHelper.extractStringNamed(nameParamName, element);

        String description = "";

        if(this.fromApiJsonHelper.parameterExists(descriptionParamName, element)){
            description = this.fromApiJsonHelper.extractStringNamed(descriptionParamName, element);
        }
        final LocalDate fromDate = this.fromApiJsonHelper.extractLocalDateNamed(fromDateParamName, element);
        final LocalDate endDate = this.fromApiJsonHelper.extractLocalDateNamed(endDateParamName, element);

        final BigDecimal annualInterestRate = this.fromApiJsonHelper.extractBigDecimalNamed(annualInterestRateParamName,element,locale);

        boolean applyToExistingSavings = false;

        if(this.fromApiJsonHelper.parameterExists(applyToExistingSavingsAccountParamName,element)){
            applyToExistingSavings = this.fromApiJsonHelper.extractBooleanNamed(applyToExistingSavingsAccountParamName,element);
        }

        final SavingsProductInterestRateChart savingsProductInterestRateChart =  SavingsProductInterestRateChart.createNew(null,name,description,fromDate,endDate,annualInterestRate,applyToExistingSavings);

        return savingsProductInterestRateChart;
    }
    
    public TaxGroup assembleTaxGroup(final JsonCommand command) {
        final Long taxGroupId = command.longValueOfParameterNamed(taxGroupIdParamName);
        TaxGroup taxGroup = null;
        if (taxGroupId != null) {
            taxGroup = this.taxGroupRepository.findOneWithNotFoundDetection(taxGroupId);
        }
        return taxGroup;
    }
}