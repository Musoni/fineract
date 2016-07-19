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
package org.apache.fineract.accounting.closure.storeglaccountbalance.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.fineract.accounting.closure.bookoffincomeandexpense.domain.IncomeAndExpenseBooking;
import org.apache.fineract.accounting.closure.domain.GLClosure;
import org.apache.fineract.accounting.closure.storeglaccountbalance.data.GLClosureJournalEntryData;
import org.apache.fineract.accounting.closure.storeglaccountbalance.domain.GLClosureJournalEntryBalance;
import org.apache.fineract.accounting.closure.storeglaccountbalance.domain.GLClosureJournalEntryBalanceRepository;
import org.apache.fineract.accounting.glaccount.domain.GLAccount;
import org.apache.fineract.accounting.glaccount.domain.GLAccountRepository;
import org.apache.fineract.accounting.glaccount.domain.GLAccountType;
import org.apache.fineract.accounting.journalentry.data.JournalEntryData;
import org.apache.fineract.accounting.journalentry.domain.JournalEntryType;
import org.apache.fineract.accounting.journalentry.service.JournalEntryReadPlatformService;
import org.apache.fineract.infrastructure.configuration.domain.ConfigurationDomainService;
import org.apache.fineract.infrastructure.core.service.Page;
import org.apache.fineract.infrastructure.core.service.SearchParameters;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GLClosureJournalEntryBalanceWritePlatformServiceImp implements
        GLClosureJournalEntryBalanceWritePlatformService {
    private final GLClosureJournalEntryBalanceRepository glClosureJournalEntryBalanceRepository;
    private final GLClosureJournalEntryBalanceReadPlatformService glClosureJournalEntryReadPlatformService;
    private final GLAccountRepository glAccountRepository;
    private final ConfigurationDomainService configurationDomainService;
    private final JournalEntryReadPlatformService journalEntryReadPlatformService;

    /**
     * @param glClosureJournalEntryBalanceRepository
     */
    @Autowired
    private GLClosureJournalEntryBalanceWritePlatformServiceImp(
            final GLClosureJournalEntryBalanceRepository glClosureJournalEntryBalanceRepository, 
            final GLClosureJournalEntryBalanceReadPlatformService glClosureJournalEntryReadPlatformService, 
            final GLAccountRepository glAccountRepository, 
            final ConfigurationDomainService configurationDomainService, 
            final JournalEntryReadPlatformService journalEntryReadPlatformService) {
        this.glClosureJournalEntryBalanceRepository = glClosureJournalEntryBalanceRepository;
        this.glClosureJournalEntryReadPlatformService = glClosureJournalEntryReadPlatformService;
        this.glAccountRepository = glAccountRepository;
        this.configurationDomainService = configurationDomainService;
        this.journalEntryReadPlatformService = journalEntryReadPlatformService;
    }

    @Override
    @Transactional
    public void storeJournalEntryRunningBalance(final GLClosure glClosure, 
            final IncomeAndExpenseBooking incomeAndExpenseBooking) {
        if (glClosure != null && glClosure.getOffice() != null && glClosure.getClosingDate() != null && 
                this.configurationDomainService.storeJournalEntryBalanceAtPeriodClosure()) {
            final Long officeId = glClosure.getOffice().getId();
            final Date closureClosingDate = glClosure.getClosingDate();
            final LocalDate maxEntryDate = LocalDate.fromDateFields(closureClosingDate);
            final Collection<GLClosureJournalEntryData> journalEntriesData = 
                    this.glClosureJournalEntryReadPlatformService.retrieveAllJournalEntries(officeId, maxEntryDate);
            final Map<Long, GLAccount> glAccountMap = this.retrieveAllActiveGlAccounts();
            
            if (journalEntriesData != null) {
                // will hold array of GL account ids that have already been processed 
                // (GLClosureJournalEntryBalance entity already created and stored in the database)
                Long[] processedGLAccountIds = new Long[journalEntriesData.size()];
                int longArrayIndex = 0;
                
                final Map<Long, JournalEntryData> journalEntryDataMap = 
                        this.getIncomeAndExpenseBookingJournalEntryDataMap(incomeAndExpenseBooking);
                
                for (GLClosureJournalEntryData journalEntryData : journalEntriesData) {
                    Long glAccountId = journalEntryData.getAccountId();
                    GLAccount glAccount = glAccountMap.get(glAccountId);
                    
                    // calculate the running balance for the GL account
                    BigDecimal runningBalance = this.calculateAccountRunningBalance(glAccount, 
                            journalEntryData.getOfficeRunningBalance(), journalEntryDataMap);
                    
                    // create new GLClosureJournalEntryBalance entity
                    GLClosureJournalEntryBalance journalEntryBalance = GLClosureJournalEntryBalance.
                            newGLClosureBalance(glClosure, glAccount, runningBalance);
                    
                    // add the id of the GL account to the array of ids of already processed GL accounts
                    processedGLAccountIds[longArrayIndex] = glAccountId;
                    
                    // increment the array index
                    longArrayIndex++;
                    
                    // save entity and flush changes instantly
                    this.glClosureJournalEntryBalanceRepository.saveAndFlush(journalEntryBalance);
                }
                
                Iterator<Map.Entry<Long, GLAccount>> glAccounts = glAccountMap.entrySet().iterator();
                
                while (glAccounts.hasNext()) {
                    Map.Entry<Long, GLAccount> glAccountEntry = glAccounts.next();
                    
                    // if GL account has not been processed, go ahead and create a new GLClosureJournalEntryBalance
                    // with a zero amount (the GL account has no journal entry)
                    if (!Arrays.asList(processedGLAccountIds).contains(glAccountEntry.getKey())) {
                        GLAccount glAccount = glAccountEntry.getValue();
                        
                        // calculate the running balance for the GL account
                        BigDecimal runningBalance = this.calculateAccountRunningBalance(glAccount, 
                                BigDecimal.ZERO, journalEntryDataMap);
                        
                        // create new GLClosureJournalEntryBalance entity
                        GLClosureJournalEntryBalance journalEntryBalance = GLClosureJournalEntryBalance.
                                newGLClosureBalance(glClosure, glAccount, runningBalance);
                        
                        // save entity and flush changes instantly
                        this.glClosureJournalEntryBalanceRepository.saveAndFlush(journalEntryBalance);
                    }
                }
            }
        }
    }
    
    /**
     * Calculates the running balance for the specified account
     * 
     * @param glAccount
     * @param currentRunningBalance
     * @param journalEntryDataMap
     * @return running balance of the {@link GLAccount} object
     */
    private BigDecimal calculateAccountRunningBalance(final GLAccount glAccount, 
            final BigDecimal currentRunningBalance, final Map<Long, JournalEntryData> journalEntryDataMap) {
        BigDecimal runningBalance = currentRunningBalance;
        
        if (journalEntryDataMap != null && glAccount != null) {
            if (journalEntryDataMap.containsKey(glAccount.getId())) {
                JournalEntryData journalEntryData = journalEntryDataMap.get(glAccount.getId());
                GLAccountType accountType = GLAccountType.fromInt(
                        journalEntryData.getGlAccountType().getId().intValue());
                JournalEntryType entryType = JournalEntryType.fromInt(
                        journalEntryData.getEntryType().getId().intValue());
                boolean addToRunningBalance = false;
                
                switch (accountType) {
                    case ASSET:
                        if (entryType.isDebitType()) {
                            addToRunningBalance = true;
                        }
                    break;
                    case EQUITY:
                        if (entryType.isCreditType()) {
                            addToRunningBalance = true;
                        }
                    break;
                    case EXPENSE:
                        if (entryType.isDebitType()) {
                            addToRunningBalance = true;
                        }
                    break;
                    case INCOME:
                        if (entryType.isCreditType()) {
                            addToRunningBalance = true;
                        }
                    break;
                    case LIABILITY:
                        if (entryType.isCreditType()) {
                            addToRunningBalance = true;
                        }
                    break;
                }
                
                if (addToRunningBalance) {
                    runningBalance = currentRunningBalance.add(journalEntryData.getAmount());
                } 
                
                else {
                    runningBalance = currentRunningBalance.subtract(journalEntryData.getAmount());
                }
            }
        }
        
        return runningBalance;
    }
    
    /**
     * Returns a map of GL account ids to {@link JournalEntryData} objects
     * 
     * @param incomeAndExpenseBooking {@link IncomeAndExpenseBooking} object
     * @return map of GL account ids to {@link JournalEntryData} objects
     */
    private Map<Long, JournalEntryData> getIncomeAndExpenseBookingJournalEntryDataMap(
            final IncomeAndExpenseBooking incomeAndExpenseBooking) {
        Map<Long, JournalEntryData> journalEntryDataMap = new HashMap<Long, JournalEntryData>();
        
        if (incomeAndExpenseBooking != null) {
            final SearchParameters searchParameters = SearchParameters.from(null, null, null, null, null);
            final Page<JournalEntryData> journalEntryDataPage = this.journalEntryReadPlatformService.
                    retrieveAll(searchParameters, null, null, null, null, incomeAndExpenseBooking.getTransactionId(), 
                            null, null, null);
            
            if (journalEntryDataPage != null && journalEntryDataPage.getTotalFilteredRecords() > 0) {
                final Collection<JournalEntryData> journalEntriesData = journalEntryDataPage.getPageItems();
                
                for (JournalEntryData journalEntryData : journalEntriesData) {
                    journalEntryDataMap.put(journalEntryData.getGlAccountId(), journalEntryData);
                }
            }
        }
        
        return journalEntryDataMap;
    }
    
    /**
     * Retrieve all active gl accounts as a map of the id to the {@link GLAccount} entity
     * 
     * @return map of id to {@link GLAccount} entity
     */
    private Map<Long, GLAccount> retrieveAllActiveGlAccounts() {
        Map<Long, GLAccount> glAccountMap = new HashMap<Long, GLAccount>();
        
        final Collection<GLAccount> glAccounts = this.glAccountRepository.findAll();
        
        if (glAccounts != null) {
            for (GLAccount glAccount : glAccounts) {
                if (!glAccount.isDisabled()) {
                    glAccountMap.put(glAccount.getId(), glAccount);
                }
            }
        }
        
        return glAccountMap;
    }

    @Override
    @Transactional
    public void deleteJournalEntryRunningBalances(final GLClosure glClosure) {
        final Collection<GLClosureJournalEntryBalance> glClosureJournalEntryBalances = 
                this.glClosureJournalEntryBalanceRepository.findByGlClosureAndDeletedIsFalse(glClosure);
        
        if (glClosureJournalEntryBalances != null) {
            for (GLClosureJournalEntryBalance glClosureJournalEntryBalance : glClosureJournalEntryBalances) {
                glClosureJournalEntryBalance.delete();
                
                this.glClosureJournalEntryBalanceRepository.saveAndFlush(glClosureJournalEntryBalance);
            }
        }
    }
}
