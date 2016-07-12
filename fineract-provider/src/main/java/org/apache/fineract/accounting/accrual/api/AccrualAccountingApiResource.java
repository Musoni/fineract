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
package org.apache.fineract.accounting.accrual.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.fineract.accounting.accrual.service.AccrualAccountingWritePlatformService;
import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.DefaultToApiJsonSerializer;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;

@Path("/runaccruals")
@Component
@Scope("singleton")
public class AccrualAccountingApiResource {

    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final DefaultToApiJsonSerializer<String> apiJsonSerializerService;
    private final AccrualAccountingWritePlatformService accrualAccountingwritePlatformService;
    private final FromJsonHelper fromApiJsonHelper;
    private final static Logger logger = LoggerFactory.getLogger(AccrualAccountingApiResource.class);



    @Autowired
    public AccrualAccountingApiResource(final DefaultToApiJsonSerializer<String> apiJsonSerializerService,  final FromJsonHelper fromApiJsonHelper,
                                        final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService, final AccrualAccountingWritePlatformService accrualAccountingWritePlatformService) {
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
        this.apiJsonSerializerService = apiJsonSerializerService;
        this.accrualAccountingwritePlatformService = accrualAccountingWritePlatformService;
        this.fromApiJsonHelper = fromApiJsonHelper;
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String executePeriodicAccrualAccounting(final String jsonRequestBody) {

        logger.info("Starting Periodic Accrual Accounting");
        final CommandWrapper commandRequest = new CommandWrapperBuilder().excuteAccrualAccounting().withJson(jsonRequestBody).build();

        JsonCommand command = null;
        final JsonElement parsedCommand = this.fromApiJsonHelper.parse(jsonRequestBody);
        command = JsonCommand.from(jsonRequestBody, parsedCommand, this.fromApiJsonHelper, commandRequest.getEntityName(), commandRequest.getEntityId(),
                commandRequest.getSubentityId(), commandRequest.getGroupId(), commandRequest.getClientId(), commandRequest.getLoanId(), commandRequest.getSavingsId(),
                commandRequest.getTransactionId(), commandRequest.getHref(), commandRequest.getProductId());
        
        //final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        final CommandProcessingResult result = this.accrualAccountingwritePlatformService.executeLoansPeriodicAccrual(command);

        logger.info("Periodic Accrual completed");

        return this.apiJsonSerializerService.serialize(result);
    }

}