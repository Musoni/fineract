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
package org.apache.fineract.portfolio.creditcheck.domain;

import org.apache.fineract.portfolio.creditcheck.exception.CreditCheckNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 
 *  A wrapper class for the CreditCheckRepository that provides a method that returns a CreditCheck entity if it exists, 
 *  else throws "CreditCheckNotFoundException" exception if the credit check does not exist or has been deleted 
 **/
@Service
public class CreditCheckRepositoryWrapper {
    private final CreditCheckRepository creditCheckRepository;
    
    @Autowired
    public CreditCheckRepositoryWrapper(final CreditCheckRepository creditCheckRepository) {
        this.creditCheckRepository = creditCheckRepository;
    }
    
    public CreditCheck findOneThrowExceptionIfNotFound(final Long id) {
        final CreditCheck creditCheck = this.creditCheckRepository.findOne(id);
        
        if (creditCheck == null || creditCheck.isDeleted()) {
            throw new CreditCheckNotFoundException(id);
        }
        
        return creditCheck;
    }
    
    public CreditCheckRepository getCreditCheckRepository() {
        return this.creditCheckRepository;
    }
}
