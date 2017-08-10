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
package org.apache.fineract.portfolio.loanaccount.api;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.exception.UnrecognizedQueryParamException;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.loanaccount.data.LoanCreditCheckData;
import org.apache.fineract.portfolio.loanaccount.service.LoanCreditCheckReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/loans/{loanId}/creditchecks")
@Component
@Scope("singleton")
public class LoanCreditChecksApiResource {
    private final PlatformSecurityContext platformSecurityContext;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final DefaultToApiJsonSerializer<LoanCreditCheckData> loanCreditCheckToApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final LoanCreditCheckReadPlatformService loanCreditCheckReadPlatformService;
    
    @Autowired
    public LoanCreditChecksApiResource(final PlatformSecurityContext platformSecurityContext, 
            final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService, 
            final DefaultToApiJsonSerializer<LoanCreditCheckData> loanCreditCheckToApiJsonSerializer, 
            final ApiRequestParameterHelper apiRequestParameterHelper, 
            final LoanCreditCheckReadPlatformService loanCreditCheckReadPlatformService) {
        this.platformSecurityContext = platformSecurityContext;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
        this.loanCreditCheckToApiJsonSerializer = loanCreditCheckToApiJsonSerializer;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.loanCreditCheckReadPlatformService = loanCreditCheckReadPlatformService;
    }
    
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllLoanCreditChecks(@PathParam("loanId") final Long loanId, @Context final UriInfo uriInfo) {
        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(LoanApiConstants.LOAN_CREDIT_CHECK_ENTITY_NAME);
        
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        final Collection<LoanCreditCheckData> loanCreditCheckData = this.loanCreditCheckReadPlatformService.retrieveLoanCreditChecks(loanId);
        
        return this.loanCreditCheckToApiJsonSerializer.serialize(settings, loanCreditCheckData, LoanApiConstants.LOAN_CREDIT_CHECK_RESPONSE_DATA);
    }
    
    @GET
    @Path("template")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveLoanCreditCheckTemplate(@PathParam("loanId") final Long loanId, @Context final UriInfo uriInfo) {
        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(LoanApiConstants.LOAN_CREDIT_CHECK_ENTITY_NAME);
        
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        final LoanCreditCheckData loanCreditCheckData = this.loanCreditCheckReadPlatformService.retrieveLoanCreditCheckEnumOptions();
        
        return this.loanCreditCheckToApiJsonSerializer.serialize(settings, loanCreditCheckData, LoanApiConstants.LOAN_CREDIT_CHECK_RESPONSE_DATA);
    }
    
    @GET
    @Path("{loanCreditCheckId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveLoanCreditCheck(@PathParam("loanId") final Long loanId, @PathParam("loanCreditCheckId") final Long loanCreditCheckId,
            @Context final UriInfo uriInfo) {
        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(LoanApiConstants.LOAN_CREDIT_CHECK_ENTITY_NAME);
        
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        final LoanCreditCheckData loanCreditCheckData = this.loanCreditCheckReadPlatformService.retrieveLoanCreditCheck(loanId, loanCreditCheckId);
        
        return this.loanCreditCheckToApiJsonSerializer.serialize(settings, loanCreditCheckData, LoanApiConstants.LOAN_CREDIT_CHECK_RESPONSE_DATA);
    }
}
