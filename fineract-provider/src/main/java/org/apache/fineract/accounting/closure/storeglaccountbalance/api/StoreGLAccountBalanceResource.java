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
package org.apache.fineract.accounting.closure.storeglaccountbalance.api;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.fineract.accounting.closure.storeglaccountbalance.service.GLClosureJournalEntryBalanceReadPlatformService;
import org.apache.fineract.infrastructure.security.exception.NoAuthorizationException;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path("/glclosureaccountbalance")
@Component
@Scope("singleton")
public class StoreGLAccountBalanceResource {
    private final GLClosureJournalEntryBalanceReadPlatformService glClosureJournalEntryBalanceReadPlatformService;
    private final PlatformSecurityContext platformSecurityContext;
    public static final String CLOSURE_ACCOUNT_BALANCE_REPORT_ENTITY_NAME = "CLOSUREACCOUNTBALANCEREPORT";
    public static final String CLOSURE_ACCOUNT_BALANCE_REPORT_PERMISSION_ENTITY_NAME = "GLClosureAccountBalanceReport";
    
    /**
     * @param glClosureJournalEntryBalanceReadPlatformService
     * @param platformSecurityContext
     */
    @Autowired
    private StoreGLAccountBalanceResource(
            final GLClosureJournalEntryBalanceReadPlatformService glClosureJournalEntryBalanceReadPlatformService, 
            final PlatformSecurityContext platformSecurityContext) {
        this.glClosureJournalEntryBalanceReadPlatformService = glClosureJournalEntryBalanceReadPlatformService;
        this.platformSecurityContext = platformSecurityContext;
    }

    @GET
    @Path("/report")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_OCTET_STREAM })
    public Response report(@Context final UriInfo uriInfo){
        final AppUser appUser = this.platformSecurityContext.authenticatedUser();
        
        // ensure that application user has the right to read the report
        if (appUser.hasNotPermissionForReport(CLOSURE_ACCOUNT_BALANCE_REPORT_PERMISSION_ENTITY_NAME)) {
            throw new NoAuthorizationException("Not authorized to run report: "
                    + CLOSURE_ACCOUNT_BALANCE_REPORT_PERMISSION_ENTITY_NAME);
        }
        
        final MultivaluedMap<String, String> uriQueryParameters = uriInfo.getQueryParameters();
        final File file = this.glClosureJournalEntryBalanceReadPlatformService.
                generateGLClosureAccountBalanceReport(uriQueryParameters);
        
        // this will change to an ok response if and only if a File object is returned above
        ResponseBuilder response = Response.serverError();
        
        if (file != null) {
            response = Response.ok(file);
            
            response.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
        }
        
        return response.build();
    }
}
