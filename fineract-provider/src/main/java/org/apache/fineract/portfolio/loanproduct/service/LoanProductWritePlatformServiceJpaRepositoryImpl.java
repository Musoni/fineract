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
package org.apache.fineract.portfolio.loanproduct.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.fineract.accounting.producttoaccountmapping.service.ProductToGLAccountMappingWritePlatformService;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.infrastructure.entityaccess.domain.FineractEntityAccessType;
import org.apache.fineract.infrastructure.entityaccess.service.FineractEntityAccessUtil;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.charge.domain.Charge;
import org.apache.fineract.portfolio.charge.domain.ChargeRepositoryWrapper;
import org.apache.fineract.portfolio.creditcheck.CreditCheckConstants;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheck;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckRepositoryWrapper;
import org.apache.fineract.portfolio.floatingrates.domain.FloatingRate;
import org.apache.fineract.portfolio.floatingrates.domain.FloatingRateRepositoryWrapper;
import org.apache.fineract.portfolio.fund.domain.Fund;
import org.apache.fineract.portfolio.fund.domain.FundRepository;
import org.apache.fineract.portfolio.fund.exception.FundNotFoundException;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepositoryWrapper;
import org.apache.fineract.portfolio.loanaccount.domain.LoanTransactionProcessingStrategyRepository;
import org.apache.fineract.portfolio.loanaccount.exception.LoanTransactionProcessingStrategyNotFoundException;
import org.apache.fineract.portfolio.loanaccount.loanschedule.domain.AprCalculator;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProduct;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductRepository;
import org.apache.fineract.portfolio.loanproduct.domain.LoanTransactionProcessingStrategy;
import org.apache.fineract.portfolio.loanproduct.exception.InvalidCurrencyException;
import org.apache.fineract.portfolio.loanproduct.exception.InvalidInstallmentAmountInMultiplesOfException;
import org.apache.fineract.portfolio.loanproduct.exception.LoanProductCannotBeModifiedDueToNonClosedLoansException;
import org.apache.fineract.portfolio.loanproduct.exception.LoanProductDateException;
import org.apache.fineract.portfolio.loanproduct.exception.LoanProductNotFoundException;
import org.apache.fineract.portfolio.loanproduct.serialization.LoanProductDataValidator;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class LoanProductWritePlatformServiceJpaRepositoryImpl implements LoanProductWritePlatformService {

    private final static Logger logger = LoggerFactory.getLogger(LoanProductWritePlatformServiceJpaRepositoryImpl.class);
    private final PlatformSecurityContext context;
    private final LoanProductDataValidator fromApiJsonDeserializer;
    private final LoanProductRepository loanProductRepository;
    private final AprCalculator aprCalculator;
    private final FundRepository fundRepository;
    private final CodeValueRepositoryWrapper codeValueRepository;
    private final LoanTransactionProcessingStrategyRepository loanTransactionProcessingStrategyRepository;
    private final ChargeRepositoryWrapper chargeRepository;
    private final ProductToGLAccountMappingWritePlatformService accountMappingWritePlatformService;
    private final FineractEntityAccessUtil FineractEntityAccessUtil;
    private final FloatingRateRepositoryWrapper floatingRateRepository;
    private final LoanRepositoryWrapper loanRepositoryWrapper;
    private final CreditCheckRepositoryWrapper creditCheckRepositoryWrapper;

    @Autowired
    public LoanProductWritePlatformServiceJpaRepositoryImpl(final PlatformSecurityContext context,
            final LoanProductDataValidator fromApiJsonDeserializer, final LoanProductRepository loanProductRepository,
            final AprCalculator aprCalculator, final FundRepository fundRepository,
            final LoanTransactionProcessingStrategyRepository loanTransactionProcessingStrategyRepository,
            final ChargeRepositoryWrapper chargeRepository,
            final ProductToGLAccountMappingWritePlatformService accountMappingWritePlatformService,
            final FineractEntityAccessUtil FineractEntityAccessUtil,
            final FloatingRateRepositoryWrapper floatingRateRepository,
            final LoanRepositoryWrapper loanRepositoryWrapper, 
            final CreditCheckRepositoryWrapper creditCheckRepositoryWrapper,
            final CodeValueRepositoryWrapper codeValueRepository) {
        this.context = context;
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;
        this.loanProductRepository = loanProductRepository;
        this.aprCalculator = aprCalculator;
        this.fundRepository = fundRepository;
        this.loanTransactionProcessingStrategyRepository = loanTransactionProcessingStrategyRepository;
        this.chargeRepository = chargeRepository;
        this.accountMappingWritePlatformService = accountMappingWritePlatformService;
        this.FineractEntityAccessUtil = FineractEntityAccessUtil;
        this.floatingRateRepository = floatingRateRepository;
        this.loanRepositoryWrapper = loanRepositoryWrapper;
        this.creditCheckRepositoryWrapper = creditCheckRepositoryWrapper;
        this.codeValueRepository = codeValueRepository;
    }

    @Transactional
    @Override
    public CommandProcessingResult createLoanProduct(final JsonCommand command) {

        try {

            this.context.authenticatedUser();

            this.fromApiJsonDeserializer.validateForCreate(command.json());
            validateInputDates(command);
            validateInMultiplesOf(command);

            final Fund fund = findFundByIdIfProvided(command.longValueOfParameterNamed("fundId"));
            final CodeValue productGroup = this.codeValueRepository.findOneWithNotFoundDetection(command.longValueOfParameterNamed("productGroupId"));

            final Long transactionProcessingStrategyId = command.longValueOfParameterNamed("transactionProcessingStrategyId");
            final LoanTransactionProcessingStrategy loanTransactionProcessingStrategy = findStrategyByIdIfProvided(transactionProcessingStrategyId);

            final String currencyCode = command.stringValueOfParameterNamed("currencyCode");
            final List<Charge> charges = assembleListOfProductCharges(command, currencyCode);
            final List<CreditCheck> creditChecks = assembleListOfProductCreditChecks(command);

            FloatingRate floatingRate = null;
            if(command.parameterExists("floatingRatesId")){
            	floatingRate = this.floatingRateRepository
            			.findOneWithNotFoundDetection(command.longValueOfParameterNamed("floatingRatesId"));
            }
            final LoanProduct loanproduct = LoanProduct.assembleFromJson(fund, loanTransactionProcessingStrategy, charges, command,
                    this.aprCalculator, floatingRate, creditChecks, productGroup);
            
            loanproduct.updateLoanProductInRelatedClasses();

            this.loanProductRepository.save(loanproduct);

            // save accounting mappings
            this.accountMappingWritePlatformService.createLoanProductToGLAccountMapping(loanproduct.getId(), command);
            // check if the office specific products are enabled. If yes, then save this savings product against a specific office
            // i.e. this savings product is specific for this office.
            FineractEntityAccessUtil.checkConfigurationAndAddProductResrictionsForUserOffice(
            		FineractEntityAccessType.OFFICE_ACCESS_TO_LOAN_PRODUCTS, 
            		loanproduct.getId());
            
            return new CommandProcessingResultBuilder() //
                    .withCommandId(command.commandId()) //
                    .withEntityId(loanproduct.getId()) //
                    .build();

        } catch (final DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve);
            return CommandProcessingResult.empty();
        }

    }

    private LoanTransactionProcessingStrategy findStrategyByIdIfProvided(final Long transactionProcessingStrategyId) {
        LoanTransactionProcessingStrategy strategy = null;
        if (transactionProcessingStrategyId != null) {
            strategy = this.loanTransactionProcessingStrategyRepository.findOne(transactionProcessingStrategyId);
            if (strategy == null) { throw new LoanTransactionProcessingStrategyNotFoundException(transactionProcessingStrategyId); }
        }
        return strategy;
    }

    private Fund findFundByIdIfProvided(final Long fundId) {
        Fund fund = null;
        if (fundId != null) {
            fund = this.fundRepository.findOne(fundId);
            if (fund == null) { throw new FundNotFoundException(fundId); }
        }
        return fund;
    }

    @Transactional
    @Override
    public CommandProcessingResult updateLoanProduct(final Long loanProductId, final JsonCommand command) {

        try {
            this.context.authenticatedUser();

            final LoanProduct product = this.loanProductRepository.findOne(loanProductId);
            if (product == null) { throw new LoanProductNotFoundException(loanProductId); }

            this.fromApiJsonDeserializer.validateForUpdate(command.json(), product);
            validateInputDates(command);
            validateInMultiplesOf(command);

            if(anyChangeInCriticalFloatingRateLinkedParams(command, product) 
            		&& this.loanRepositoryWrapper.doNonClosedLoanAccountsExistForProduct(product.getId())){
            	throw new LoanProductCannotBeModifiedDueToNonClosedLoansException(product.getId());
            }
            
            FloatingRate floatingRate = null;
            if(command.parameterExists("floatingRatesId")){
            	floatingRate = this.floatingRateRepository
            			.findOneWithNotFoundDetection(command.longValueOfParameterNamed("floatingRatesId"));
            }

            final Map<String, Object> changes = product.update(command, this.aprCalculator, floatingRate);

            if (changes.containsKey("fundId")) {
                final Long fundId = (Long) changes.get("fundId");
                final Fund fund = findFundByIdIfProvided(fundId);
                product.update(fund);
            }

            if (changes.containsKey("productGroupId")) {
                final Long productGroupId = (Long) changes.get("productGroupId");
                final CodeValue productGroup = this.codeValueRepository.findOneWithNotFoundDetection(productGroupId);
                product.update(productGroup);
            }

            if (changes.containsKey("transactionProcessingStrategyId")) {
                final Long transactionProcessingStrategyId = (Long) changes.get("transactionProcessingStrategyId");
                final LoanTransactionProcessingStrategy loanTransactionProcessingStrategy = findStrategyByIdIfProvided(transactionProcessingStrategyId);
                product.update(loanTransactionProcessingStrategy);
            }

            if (changes.containsKey("charges")) {
                final List<Charge> productCharges = assembleListOfProductCharges(command, product.getCurrency().getCode());
                final boolean updated = product.update(productCharges);
                if (!updated) {
                    changes.remove("charges");
                }
            }
            
            if (changes.containsKey(CreditCheckConstants.CREDIT_CHECKS_PARAM_NAME)) {
                final List<CreditCheck> creditChecks = assembleListOfProductCreditChecks(command);
                final boolean updated = product.updateCreditChecks(creditChecks);
                
                if (!updated) {
                    changes.remove(CreditCheckConstants.CREDIT_CHECKS_PARAM_NAME);
                }
            }

            // accounting related changes
            final boolean accountingTypeChanged = changes.containsKey("accountingRule");
            final Map<String, Object> accountingMappingChanges = this.accountMappingWritePlatformService
                    .updateLoanProductToGLAccountMapping(product.getId(), command, accountingTypeChanged, product.getAccountingType());
            changes.putAll(accountingMappingChanges);

            if (!changes.isEmpty()) {
                this.loanProductRepository.saveAndFlush(product);
            }

            return new CommandProcessingResultBuilder() //
                    .withCommandId(command.commandId()) //
                    .withEntityId(loanProductId) //
                    .with(changes) //
                    .build();

        } catch (final DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve);
            return new CommandProcessingResult(Long.valueOf(-1));
        }

    }

    private boolean anyChangeInCriticalFloatingRateLinkedParams(JsonCommand command, LoanProduct product) {
        final boolean isChangeFromFloatingToFlatOrViceVersa = command.isChangeInBooleanParameterNamed("isLinkedToFloatingInterestRates", product.isLinkedToFloatingInterestRate());
    	final boolean isChangeInCriticalFloatingRateParams = product.getFloatingRates() != null
    			&& (command.isChangeInLongParameterNamed("floatingRatesId", product.getFloatingRates().getFloatingRate().getId())
    					|| command.isChangeInBigDecimalParameterNamed("interestRateDifferential", product.getFloatingRates().getInterestRateDifferential()));
		return isChangeFromFloatingToFlatOrViceVersa || isChangeInCriticalFloatingRateParams;
	}

	private List<Charge> assembleListOfProductCharges(final JsonCommand command, final String currencyCode) {

        final List<Charge> charges = new ArrayList<>();

        String loanProductCurrencyCode = command.stringValueOfParameterNamed("currencyCode");
        if (loanProductCurrencyCode == null) {
            loanProductCurrencyCode = currencyCode;
        }

        if (command.parameterExists("charges")) {
            final JsonArray chargesArray = command.arrayOfParameterNamed("charges");
            if (chargesArray != null) {
                for (int i = 0; i < chargesArray.size(); i++) {

                    final JsonObject jsonObject = chargesArray.get(i).getAsJsonObject();
                    if (jsonObject.has("id")) {
                        final Long id = jsonObject.get("id").getAsLong();

                        final Charge charge = this.chargeRepository.findOneWithNotFoundDetection(id);

                        if (!loanProductCurrencyCode.equals(charge.getCurrencyCode())) {
                            final String errorMessage = "Charge and Loan Product must have the same currency.";
                            throw new InvalidCurrencyException("charge", "attach.to.loan.product", errorMessage);
                        }
                        charges.add(charge);
                    }
                }
            }
        }

        return charges;
    }
    
    /** 
     * get list of credit check entities from the JsonCommand object 
     * 
     * @param jsonCommand -- JsonCommand object
     * @return list of CreditCheck objects
     **/
    private List<CreditCheck> assembleListOfProductCreditChecks(final JsonCommand jsonCommand) {
        final List<CreditCheck> creditChecks = new ArrayList<>();
        
        if (jsonCommand.parameterExists(CreditCheckConstants.CREDIT_CHECKS_PARAM_NAME)) {
            final JsonArray jsonArray = jsonCommand.arrayOfParameterNamed(CreditCheckConstants.CREDIT_CHECKS_PARAM_NAME);
            
            if (jsonArray != null) {
                for (int i=0; i<jsonArray.size(); i++) {
                    final JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    
                    if (jsonObject.has(CreditCheckConstants.ID_PARAM_NAME)) {
                        final Long id = jsonObject.get(CreditCheckConstants.ID_PARAM_NAME).getAsLong();
                        final CreditCheck creditCheck = this.creditCheckRepositoryWrapper.findOneThrowExceptionIfNotFound(id);
                        
                        creditChecks.add(creditCheck);
                    }
                }
            }
        }
        
        return creditChecks;
    }

    /*
     * Guaranteed to throw an exception no matter what the data integrity issue
     * is.
     */
    private void handleDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {

        final Throwable realCause = dve.getMostSpecificCause();

        if (realCause.getMessage().contains("external_id")) {

            final String externalId = command.stringValueOfParameterNamed("externalId");
            throw new PlatformDataIntegrityException("error.msg.product.loan.duplicate.externalId", "Loan Product with externalId `"
                    + externalId + "` already exists", "externalId", externalId);
        } else if (realCause.getMessage().contains("unq_name")) {

            final String name = command.stringValueOfParameterNamed("name");
            throw new PlatformDataIntegrityException("error.msg.product.loan.duplicate.name", "Loan product with name `" + name
                    + "` already exists", "name", name);
        } else if (realCause.getMessage().contains("unq_short_name")) {

            final String shortName = command.stringValueOfParameterNamed("shortName");
            throw new PlatformDataIntegrityException("error.msg.product.loan.duplicate.short.name", "Loan product with short name `"
                    + shortName + "` already exists", "shortName", shortName);
        } else if (realCause.getMessage().contains("Duplicate entry")) {
            final Object[] args = null;
            throw new PlatformDataIntegrityException("error.msg.product.loan.duplicate.charge",
                    "Loan product may only have one charge of each type.`", "charges", args);
        }

        logAsErrorUnexpectedDataIntegrityException(dve);
        throw new PlatformDataIntegrityException("error.msg.product.loan.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource.");
    }

    private void validateInputDates(final JsonCommand command) {
        final LocalDate startDate = command.localDateValueOfParameterNamed("startDate");
        final LocalDate closeDate = command.localDateValueOfParameterNamed("closeDate");

        if (startDate != null && closeDate != null) {
            if (closeDate.isBefore(startDate)) { throw new LoanProductDateException(startDate.toString(), closeDate.toString()); }
        }
    }

    /*
    * Validates if installmentAmountInMultiplesOf is equal to or bigger than and divisible by inMultiplesOf
    */
    private void validateInMultiplesOf(final JsonCommand command){
        final Integer inMultiplesOf = command.integerValueOfParameterNamed("inMultiplesOf");
        final Integer installmentAmountInMultiplesOf = command.integerValueOfParameterNamed("installmentAmountInMultiplesOf");

        if(inMultiplesOf != null && installmentAmountInMultiplesOf != null){
            if(inMultiplesOf > installmentAmountInMultiplesOf){
                throw new InvalidInstallmentAmountInMultiplesOfException("Loan product Installment Amount In Multiples Of value must be equal to or larger than In Multiples Of value",
                        inMultiplesOf.toString(), installmentAmountInMultiplesOf.toString());
            }
            if((installmentAmountInMultiplesOf % inMultiplesOf) != 0){
                throw new InvalidInstallmentAmountInMultiplesOfException("Loan product Installment Amount In Multiples Of value must be divisible by In Multiples Of value",
                        inMultiplesOf.toString(), installmentAmountInMultiplesOf.toString());
            }
        }
    }

    private void logAsErrorUnexpectedDataIntegrityException(final DataIntegrityViolationException dve) {
        logger.error(dve.getMessage(), dve);
    }
}