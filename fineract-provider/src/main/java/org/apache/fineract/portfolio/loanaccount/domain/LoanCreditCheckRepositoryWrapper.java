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
package org.apache.fineract.portfolio.loanaccount.domain;

import org.apache.fineract.portfolio.loanaccount.exception.LoanCreditCheckNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 
 * A wrapper class for the LoanCreditCheckRepository that provides a method that returns a LoanCreditCheck entity if it exists, 
 * else throws "LoanCreditCheckNotFoundException" exception if the loan credit check does not exist
 **/
@Service
public class LoanCreditCheckRepositoryWrapper {
    private final LoanCreditCheckRepository loanCreditCheckRepository;
    
    @Autowired
    public LoanCreditCheckRepositoryWrapper(final LoanCreditCheckRepository loanCreditCheckRepository) {
        this.loanCreditCheckRepository = loanCreditCheckRepository;
    }
    
    public LoanCreditCheck findOneThrowExceptionIfNotFound(final Long id) {
        final LoanCreditCheck loanCreditCheck = this.loanCreditCheckRepository.findOne(id);
        
        if (loanCreditCheck == null) {
            throw new LoanCreditCheckNotFoundException(id);
        }
        
        return loanCreditCheck;
    }
    
    public LoanCreditCheckRepository getLoanCreditCheckRepository() {
        return this.loanCreditCheckRepository;
    }
}
