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
package org.apache.fineract.portfolio.transfer.api;

import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.ToApiJsonSerializer;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.transfer.data.UndoTransferClientData;
import org.apache.fineract.portfolio.transfer.data.UndoTransferGroupData;
import org.apache.fineract.portfolio.transfer.service.UndoTransferReadPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

@Path("/undoTransfer")
@Component
@Scope("singleton")
public class UndoTransferApiResource {

    private final String resourceNameForPermissions = "UNDOTRANSFER";


    private final PlatformSecurityContext context;
    private final ToApiJsonSerializer<UndoTransferClientData> toApiJsonSerializer;
    private final ToApiJsonSerializer<UndoTransferGroupData>  undoTransferGroupDataToApiJsonSerializer;
    private final UndoTransferReadPlatformService undoTransferReadPlatformService;
    private final ApiRequestParameterHelper apiRequestParameterHelper;

    @Autowired
    public UndoTransferApiResource(final PlatformSecurityContext context,final ToApiJsonSerializer<UndoTransferClientData> toApiJsonSerializer,
                                   final ToApiJsonSerializer<UndoTransferGroupData> undoTransferGroupDataToApiJsonSerializer,
                                   final UndoTransferReadPlatformService undoTransferReadPlatformService,final ApiRequestParameterHelper apiRequestParameterHelper) {
        this.context = context;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.undoTransferGroupDataToApiJsonSerializer = undoTransferGroupDataToApiJsonSerializer;
        this.undoTransferReadPlatformService = undoTransferReadPlatformService;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
    }

    @GET()
    @Path("clients/")
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllUndoTransferClientData(@PathParam("clientId") final Long clientId,@Context final UriInfo uriInfo){
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final Collection<UndoTransferClientData> undoTransferClientData = this.undoTransferReadPlatformService.retrieveAllTransferredClients();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings,undoTransferClientData);
    }



    @GET()
    @Path("clients/{clientId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveUndoTransferClientData(@PathParam("clientId") final Long clientId,@Context final UriInfo uriInfo){
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final UndoTransferClientData undoTransferClientData = this.undoTransferReadPlatformService.retrieveUndoTransferClientData(clientId);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serialize(settings,undoTransferClientData);
    }

    @GET()
    @Path("groups/{groupId}")
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveUndoTransferGroupData(@PathParam("groupId") final Long groupId,@Context final UriInfo uriInfo){
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final UndoTransferGroupData undoTransferGroupData = this.undoTransferReadPlatformService.retrieveUndoTransferGroupData(groupId);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.undoTransferGroupDataToApiJsonSerializer.serialize(settings,undoTransferGroupData);
    }

    @GET()
    @Path("groups")
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAllUndoTransferGroupData(@PathParam("groupId") final Long groupId,@Context final UriInfo uriInfo){
        this.context.authenticatedUser().validateHasReadPermission(this.resourceNameForPermissions);
        final Collection<UndoTransferGroupData> undoTransferGroupData = this.undoTransferReadPlatformService.retrieveAllTransferredGroups();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        return this.undoTransferGroupDataToApiJsonSerializer.serialize(settings,undoTransferGroupData);
    }
}
