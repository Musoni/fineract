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
package org.apache.fineract.accounting.closure.storeglaccountbalance.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.apache.fineract.accounting.closure.data.GLClosureData;
import org.apache.fineract.accounting.closure.exception.GLClosureNotFoundException;
import org.apache.fineract.accounting.closure.storeglaccountbalance.api.StoreGLAccountBalanceResource;
import org.apache.fineract.accounting.closure.storeglaccountbalance.helper.UriQueryParameterHelper;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.springframework.stereotype.Component;

@Component
public class GLClosureJournalEntryBalanceValidator {
    /**
     * Validates the request to generate a closure journal entry account balance report
     * 
     * @param officeId
     * @param startClosure
     * @param endClosure
     */
    public void validateGenerateReportRequest(final Long officeId, final GLClosureData startClosure, 
            final GLClosureData endClosure) {
        
        final Long endClosureId = (endClosure != null) ? endClosure.getId() : null;
        
        final String resourceNameToLowerCase = StringUtils.lowerCase(StoreGLAccountBalanceResource.
                CLOSURE_ACCOUNT_BALANCE_REPORT_ENTITY_NAME);
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder dataValidatorBuilder = new DataValidatorBuilder(dataValidationErrors).
                resource(resourceNameToLowerCase);
        
        dataValidatorBuilder.reset().parameter(UriQueryParameterHelper.OFFICE_ID_PARAMETER_NAME).
                value(officeId).notBlank();
        dataValidatorBuilder.reset().parameter(UriQueryParameterHelper.END_CLOSURE_ID_PARAMETER_NAME).
                value(endClosureId).notBlank();
        
        if (startClosure != null && startClosure.isDeleted()) {
            throw new GLClosureNotFoundException(startClosure.getId());
        }
        
        if (endClosure != null && endClosure.isDeleted()) {
            throw new GLClosureNotFoundException(endClosureId);
        }
        
        if (startClosure != null && endClosure != null) {
            final LocalDate startClosureClosingDate = startClosure.getClosingDate();
            final LocalDate endClosureClosingDate = endClosure.getClosingDate();
            
            if (endClosureClosingDate.isBefore(startClosureClosingDate)) {
                dataValidatorBuilder.failWithCodeNoParameterAddedToErrorCode("error.msg." + resourceNameToLowerCase
                        + ".end.closure.closing.date.cannot.be.before.start.closure.closing.date", "Closing "
                                + "date of end closure must be after closing date of start closure.");
            }
            
            if (startClosure.getId().equals(endClosure.getId())) {
                dataValidatorBuilder.failWithCodeNoParameterAddedToErrorCode("error.msg." + resourceNameToLowerCase
                        + ".end.closure.cannot.be.equal.to.start.closure", "End closure cannot be equal "
                                + "to start closure.");
            }
        }
        
        if (officeId != null && startClosure != null && !startClosure.getOfficeId().equals(officeId)) {
            dataValidatorBuilder.failWithCodeNoParameterAddedToErrorCode("error.msg." + resourceNameToLowerCase 
                    + ".start.closure.office.id.must.be.equal.to.provided.office.id", "The start closure "
                            + "office id is different from provided office id");
        }
        
        if (officeId != null && endClosure != null && !endClosure.getOfficeId().equals(officeId)) {
            dataValidatorBuilder.failWithCodeNoParameterAddedToErrorCode("error.msg." + resourceNameToLowerCase 
                    + ".end.closure.office.id.must.be.equal.to.provided.office.id", "The end closure "
                            + "office id is different from provided office id");
        }
        
        // throw data validation exception if there are any validation errors 
        this.throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
    
    /** 
     * throw a PlatformApiDataValidationException exception if there are validation errors
     * 
     * @param dataValidationErrors -- list of ApiParameterError objects
     * @return None
     **/
    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { 
            throw new PlatformApiDataValidationException(dataValidationErrors); 
        }
    }
}
