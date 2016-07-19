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

import java.util.Collection;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.dataqueries.service.GenericDataService;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheck;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckSeverityLevel;
import org.apache.fineract.portfolio.creditcheck.service.CreditCheckReportParamReadPlatformService;
import org.apache.fineract.portfolio.loanaccount.data.LoanCreditCheckData;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanCreditCheck;
import org.apache.fineract.portfolio.loanaccount.domain.LoanCreditCheckRepository;
import org.apache.fineract.portfolio.loanaccount.exception.LoanCreditCheckFailedException;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoanCreditCheckWritePlatformServiceImpl implements LoanCreditCheckWritePlatformService {
    private final LoanCreditCheckRepository loanCreditCheckRepository;
    private final LoanCreditCheckReadPlatformService loanCreditCheckReadPlatformService;
    
    @Autowired
    public LoanCreditCheckWritePlatformServiceImpl(final CreditCheckReportParamReadPlatformService creditCheckReportParamReadPlatformService, 
            final LoanCreditCheckRepository loanCreditCheckRepository, 
            final GenericDataService genericDataService, 
            final LoanCreditCheckReadPlatformService loanCreditCheckReadPlatformService) {
        this.loanCreditCheckRepository = loanCreditCheckRepository;
        this.loanCreditCheckReadPlatformService = loanCreditCheckReadPlatformService;
    }
    
    /** 
     * Run all credit checks associated with the specified loan, throw an exception if any credit check fails
     * 
     * @param loan -- loan object
     * @return None
     **/
    @Override
    public void runLoanCreditChecks(final Loan loan) {
        final Collection<LoanCreditCheckData> loanCreditCheckDataList = this.loanCreditCheckReadPlatformService.triggerLoanCreditChecks(loan);
        
        if (loanCreditCheckDataList != null && !loanCreditCheckDataList.isEmpty()) {
            for (LoanCreditCheckData loanCreditCheckData : loanCreditCheckDataList) {
                final EnumOptionData severityLevelEnumOptionData = loanCreditCheckData.getSeverityLevel();
                final CreditCheckSeverityLevel severityLevel = CreditCheckSeverityLevel.fromInt(severityLevelEnumOptionData.getId().intValue());
                
                if (severityLevel.isError() && !loanCreditCheckData.actualResultEqualsExpectedResult()) {
                    throw new LoanCreditCheckFailedException(loan.getId(), loanCreditCheckData.getCreditCheckId(), loanCreditCheckData.getMessage());
                }
            }
        }
    }

    /** 
     * Run the credit checks, throw an exception if anyone fails, else add to the "m_loan_credit_check" table 
     * 
     * @param loanId -- loan object
     * @return None
     **/
    @Override
    @Transactional
    public void addLoanCreditChecks(final Loan loan) {
        final Collection<LoanCreditCheckData> loanCreditCheckDataList = this.loanCreditCheckReadPlatformService.triggerLoanCreditChecks(loan);
        final LoanProduct loanProduct = loan.loanProduct();
        
        if (loanCreditCheckDataList != null && !loanCreditCheckDataList.isEmpty()) {
            for (LoanCreditCheckData loanCreditCheckData : loanCreditCheckDataList) {
                final EnumOptionData severityLevelEnumOptionData = loanCreditCheckData.getSeverityLevel();
                final CreditCheckSeverityLevel severityLevel = CreditCheckSeverityLevel.fromInt(severityLevelEnumOptionData.getId().intValue());
                
                if (severityLevel.isError() && !loanCreditCheckData.actualResultEqualsExpectedResult()) {
                    throw new LoanCreditCheckFailedException(loan.getId(), loanCreditCheckData.getCreditCheckId(), loanCreditCheckData.getMessage());
                }
                
                CreditCheck creditCheck = getCreditCheckFromList(loanProduct.getCreditChecks(), loanCreditCheckData.getCreditCheckId());
                
                if (creditCheck != null) {
                    final Integer severityLevelIntValue = loanCreditCheckData.getSeverityLevel().getId().intValue();
                    
                    LoanCreditCheck loanCreditCheck = LoanCreditCheck.instance(creditCheck, loan, loanCreditCheckData.getExpectedResult(), 
                            loanCreditCheckData.getActualResult(), severityLevelIntValue, loanCreditCheckData.getMessage(), 
                            false, loanCreditCheckData.getSqlStatement());
                    
                    this.loanCreditCheckRepository.save(loanCreditCheck);
                }
            }
        } 
    }
    
    /** 
     * Set the "is_deleted" property of all credit checks associated with loan to 1 
     * 
     * @param loanId -- the identifier of the loan
     * @return None
     **/
    @Override
    @Transactional
    public void deleteLoanCreditChecks(final Loan loan) {
        Collection<LoanCreditCheck> loanCreditCheckList = loan.getCreditChecks();
        
        if (loanCreditCheckList != null && !loanCreditCheckList.isEmpty()) {
            for (LoanCreditCheck loanCreditCheck : loanCreditCheckList) {
                loanCreditCheck.updateIsDeleted(true);
                
                this.loanCreditCheckRepository.save(loanCreditCheck);
            }
        }
    }
    
    /** 
     * get credit check by id from a list of credit check objects 
     * 
     * @param creditChecks -- list of credit check objects
     * @param creditCheckId -- the identifier of the credit check to be retrieved
     * @return CreditCheck object if found, else null
     **/
    private CreditCheck getCreditCheckFromList(final Collection<CreditCheck> creditChecks, final Long creditCheckId) {
        CreditCheck creditCheckFound = null;
        
        if (creditChecks != null && !creditChecks.isEmpty()) {
            for (CreditCheck creditCheck : creditChecks) {
                if (creditCheck.getId().equals(creditCheckId)) {
                    creditCheckFound = creditCheck;
                    break;
                }
            }
        }
        
        return creditCheckFound;
    }
}
