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
package org.apache.fineract.portfolio.creditcheck.api;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.creditcheck.CreditCheckConstants;
import org.apache.fineract.portfolio.creditcheck.data.CreditCheckData;
import org.apache.fineract.portfolio.creditcheck.service.CreditCheckReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/creditchecks")
@Component
@Scope("singleton")
public class CreditCheckApiResource {
    
    private final PlatformSecurityContext platformSecurityContext;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final DefaultToApiJsonSerializer<CreditCheckData> creditCheckToApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;
    private final CreditCheckReadPlatformService creditCheckReadPlatformService;

    @Autowired
    public CreditCheckApiResource(final PlatformSecurityContext platformSecurityContext, 
            final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService, 
            final DefaultToApiJsonSerializer<CreditCheckData> creditCheckToApiJsonSerializer, 
            final ApiRequestParameterHelper apiRequestParameterHelper, 
            final CreditCheckReadPlatformService creditCheckReadPlatformService) {
        this.platformSecurityContext = platformSecurityContext;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
        this.creditCheckToApiJsonSerializer = creditCheckToApiJsonSerializer;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.creditCheckReadPlatformService = creditCheckReadPlatformService;
    }
    
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createCreditCheck(final String apiRequestBodyAsJson) {
        final CommandWrapper commandWrapper = new CommandWrapperBuilder()
                .createCreditCheck(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME).withJson(apiRequestBodyAsJson).build();
        
        final CommandProcessingResult commandProcessingResult = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);
        
        return this.creditCheckToApiJsonSerializer.serialize(commandProcessingResult);
    }
    
    @PUT
    @Path("{entityId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateCreditCheck(@PathParam("entityId") final Long entityId, final String apiRequestBodyAsJson) {
        final CommandWrapper commandWrapper = new CommandWrapperBuilder()
                .updateCreditCheck(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME, entityId).withJson(apiRequestBodyAsJson).build();
        
        final CommandProcessingResult commandProcessingResult = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);
        
        return this.creditCheckToApiJsonSerializer.serialize(commandProcessingResult);
    }
    
    @DELETE
    @Path("{entityId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteCreditCheck(@PathParam("entityId") final Long entityId, final String apiRequestBodyAsJson) {
        final CommandWrapper commandWrapper = new CommandWrapperBuilder()
                .deleteCreditCheck(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME, entityId).withJson(apiRequestBodyAsJson).build();
        
        final CommandProcessingResult commandProcessingResult = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);
        
        return this.creditCheckToApiJsonSerializer.serialize(commandProcessingResult);
    }
    
    @GET
    @Path("{creditCheckId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveCreditCheck(@PathParam("creditCheckId") final Long creditCheckId, @Context final UriInfo uriInfo) {
        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME);
        
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        CreditCheckData creditCheckData = this.creditCheckReadPlatformService.retrieveCreditCheck(creditCheckId);
        
        if (settings.isTemplate()) {
            final CreditCheckData creditCheckEnumOptions = this.creditCheckReadPlatformService.retrieveCreditCheckEnumOptions();
            creditCheckData = CreditCheckData.instance(creditCheckData, creditCheckEnumOptions);
        }
        
        return this.creditCheckToApiJsonSerializer.serialize(settings, creditCheckData, CreditCheckConstants.CREDIT_CHECK_DATA_PARAMETERS);
    }
    
    @GET
    @Path("template")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveCreditCheckTemplate(@Context final UriInfo uriInfo) {
        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME);
        
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        final CreditCheckData creditCheckEnumOptions = this.creditCheckReadPlatformService.retrieveCreditCheckEnumOptions();
        
        return this.creditCheckToApiJsonSerializer.serialize(settings, creditCheckEnumOptions, CreditCheckConstants.CREDIT_CHECK_DATA_PARAMETERS);
    }
    
    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllCreditChecks(@Context final UriInfo uriInfo) {
        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME);
        
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        final Collection<CreditCheckData> creditCheckDataCollection = this.creditCheckReadPlatformService.retrieveAllCreditChecks();
        
        return this.creditCheckToApiJsonSerializer.serialize(settings, creditCheckDataCollection, CreditCheckConstants.CREDIT_CHECK_DATA_PARAMETERS);
    }
}
