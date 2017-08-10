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
package org.apache.fineract.infrastructure.entityaccess.service;

import java.util.Collection;
import java.util.Iterator;

import org.apache.fineract.infrastructure.configuration.domain.GlobalConfigurationProperty;
import org.apache.fineract.infrastructure.configuration.domain.GlobalConfigurationRepositoryWrapper;


import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.infrastructure.codes.domain.CodeValueRepositoryWrapper;
import org.apache.fineract.infrastructure.codes.service.CodeValueReadPlatformService;
import org.apache.fineract.infrastructure.entityaccess.FineractEntityAccessConstants;
import org.apache.fineract.infrastructure.entityaccess.domain.FineractEntityAccessType;
import org.apache.fineract.infrastructure.entityaccess.domain.FineractEntityType;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FineractEntityAccessUtil {
    
    private final PlatformSecurityContext context;
    private final GlobalConfigurationRepositoryWrapper globalConfigurationRepository;
    private final CodeValueReadPlatformService codeValueReadPlatformService;
    private final CodeValueRepositoryWrapper codeValueRepository;
    private final FineractEntityAccessWriteService FineractEntityAccessWriteService;
    private final FineractEntityAccessReadService FineractEntityAccessReadService;

    @Autowired
    public FineractEntityAccessUtil (
    		final PlatformSecurityContext context,
    		final GlobalConfigurationRepositoryWrapper globalConfigurationRepository,
            final FineractEntityAccessWriteService FineractEntityAccessWriteService,
            final CodeValueReadPlatformService codeValueReadPlatformService,
            final CodeValueRepositoryWrapper codeValueRepository,
            final FineractEntityAccessReadService FineractEntityAccessReadService) {
    	this.context = context;
        this.globalConfigurationRepository = globalConfigurationRepository;
        this.FineractEntityAccessWriteService = FineractEntityAccessWriteService;
        this.codeValueReadPlatformService = codeValueReadPlatformService;
        this.codeValueRepository = codeValueRepository;
        this.FineractEntityAccessReadService = FineractEntityAccessReadService;
    }

	
	@Transactional
	public void checkConfigurationAndAddProductResrictionsForUserOffice (
			final FineractEntityAccessType FineractEntityAccessType,
			final FineractEntityType FineractEntityType,
			final Long productOrChargeId) {
		
		AppUser thisUser = this.context.authenticatedUser();
		
		// check if the office specific products are enabled. If yes, then save this product or charge against a specific office
        // i.e. this product or charge is specific for this office.
		
        final GlobalConfigurationProperty property = this.globalConfigurationRepository
        		.findOneByNameWithNotFoundDetection(
        				FineractEntityAccessConstants.GLOBAL_CONFIG_FOR_OFFICE_SPECIFIC_PRODUCTS);
        if (property.isEnabled() ) {
        	// If this property is enabled, then Fineract need to restrict access to this loan product to only the office of the current user            	
            final GlobalConfigurationProperty restrictToUserOfficeProperty = this.globalConfigurationRepository
            		.findOneByNameWithNotFoundDetection(
            				FineractEntityAccessConstants.GLOBAL_CONFIG_FOR_RESTRICT_PRODUCTS_TO_USER_OFFICE);
            
            if (restrictToUserOfficeProperty.isEnabled() ) {
            	final Long officeId = thisUser.getOffice().getId();
            	Collection<CodeValueData> codevalues = codeValueReadPlatformService.retrieveCodeValuesByCode(
            			FineractEntityAccessConstants.ENTITY_ACCESS_CODENAME);
            	if (codevalues != null) {
            		Iterator<CodeValueData> iterator = codevalues.iterator();
            		while(iterator.hasNext()) {
            			CodeValueData oneCodeValue = iterator.next();
            			if ( (oneCodeValue != null) &&
            					(oneCodeValue.getName().equals(FineractEntityAccessType.toStr())) ) {
            				CodeValue cv = codeValueRepository.findOneByCodeNameAndLabelWithNotFoundDetection(
            						FineractEntityAccessConstants.ENTITY_ACCESS_CODENAME,
            						FineractEntityAccessType.toStr()
            						);
            				if (cv != null) {
            					FineractEntityAccessWriteService.addNewEntityAccess(
            							FineractEntityType.OFFICE.getType(), officeId,
            							cv,
            							FineractEntityType.getType(), productOrChargeId);
            				}
            			}
            		}
            	}
            }
        }
		
	}
	
	public String getSQLWhereClauseForProductIDsForUserOffice_ifGlobalConfigEnabled (
			FineractEntityType FineractEntityType) {
		String inClause = "";
		
		final GlobalConfigurationProperty property = this.globalConfigurationRepository
        		.findOneByNameWithNotFoundDetection(
        				FineractEntityAccessConstants.GLOBAL_CONFIG_FOR_OFFICE_SPECIFIC_PRODUCTS);
		
        if (property.isEnabled() ) {
        	// Get 'SQL In Clause' for fetching only products/charges that are relevant for current user's office
        	if (FineractEntityType.equals(FineractEntityType.SAVINGS_PRODUCT)) {
        		inClause = FineractEntityAccessReadService.
        				getSQLQueryInClauseIDList_ForSavingsProductsForOffice (
        				this.context.authenticatedUser().getOffice().getId(), false);
        	} else if (FineractEntityType.equals(FineractEntityType.LOAN_PRODUCT)) {
        		inClause = FineractEntityAccessReadService.
        				getSQLQueryInClauseIDList_ForLoanProductsForOffice (
        				this.context.authenticatedUser().getOffice().getId(), false);
        	} else if (FineractEntityType.equals(FineractEntityType.CHARGE)) {
        		inClause = FineractEntityAccessReadService.
        				getSQLQueryInClauseIDList_ForChargesForOffice(
        				this.context.authenticatedUser().getOffice().getId(), false);
        	}
        }
		return inClause;
	}
	
}