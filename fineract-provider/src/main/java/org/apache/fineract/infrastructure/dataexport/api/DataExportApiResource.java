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
package org.apache.fineract.infrastructure.dataexport.api;

import java.util.Collection;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiRequestParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.ApiRequestJsonSerializationSettings;
import org.apache.fineract.infrastructure.core.serialization.ToApiJsonSerializer;
import org.apache.fineract.infrastructure.dataexport.data.DataExportData;
import org.apache.fineract.infrastructure.dataexport.data.DataExportEntityData;
import org.apache.fineract.infrastructure.dataexport.service.DataExportReadPlatformService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Path(DataExportApiConstants.DATA_EXPORT_URI_PATH_VALUE)
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DataExportApiResource {
    private final PlatformSecurityContext platformSecurityContext;
    private final DataExportReadPlatformService dataExportReadPlatformService;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final ToApiJsonSerializer<Object> toApiJsonSerializer;
    private final ApiRequestParameterHelper apiRequestParameterHelper;

    @Autowired
    public DataExportApiResource(final DataExportReadPlatformService dataExportReadPlatformService, 
            final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService, 
            final PlatformSecurityContext platformSecurityContext, 
            final ToApiJsonSerializer<Object> toApiJsonSerializer, 
            final ApiRequestParameterHelper apiRequestParameterHelper) {
        this.platformSecurityContext = platformSecurityContext;
        this.dataExportReadPlatformService = dataExportReadPlatformService;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.apiRequestParameterHelper = apiRequestParameterHelper;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public String retrieveAllDataExports(@Context final UriInfo uriInfo){

        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(DataExportApiConstants.DATA_EXPORT_ENTITY_NAME);

        final Collection<DataExportData> dataExports = this.dataExportReadPlatformService.retrieveAll();
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        return this.toApiJsonSerializer.serialize(settings, dataExports);
    }

    @GET
    @Path("{resourceId}")
    @Consumes({ MediaType.APPLICATION_JSON})
    @Produces({ MediaType.APPLICATION_JSON})
    public String retrieveOneDataExport(@PathParam("resourceId") final Long resourceId, @Context final UriInfo uriInfo){

        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(DataExportApiConstants.DATA_EXPORT_ENTITY_NAME);

        final DataExportData dataExport = this.dataExportReadPlatformService.retrieveOne(resourceId);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());

        return this.toApiJsonSerializer.serialize(settings, dataExport);
    }

    @GET
    @Path("{entityName}/template")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveDataExportTemplate(@PathParam("entityName") final String entityName, 
            @Context final UriInfo uriInfo) {

        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(entityName);
        
        final DataExportEntityData dataExportEntityData = this.dataExportReadPlatformService.retrieveTemplate(entityName);
        final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
        
        return this.toApiJsonSerializer.serialize(settings, dataExportEntityData);
    }
    
    @GET
    @Path("baseentities")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveDataExportBaseEntities(@Context final UriInfo uriInfo) {
    	this.platformSecurityContext.authenticatedUser().validateHasReadPermission(DataExportApiConstants.DATA_EXPORT_ENTITY_NAME);
    	
    	final Collection<DataExportEntityData> dataExportEntityDataList = this.dataExportReadPlatformService.retrieveAllBaseEntities();
    	final ApiRequestJsonSerializationSettings settings = this.apiRequestParameterHelper.process(uriInfo.getQueryParameters());
    	
    	return this.toApiJsonSerializer.serialize(settings, dataExportEntityDataList);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createDataExport(final String apiRequestBodyAsJson) {
    	final CommandWrapper commandWrapper = new CommandWrapperBuilder().
        		createDataExport().withJson(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);

        return this.toApiJsonSerializer.serialize(result);
    }

    @GET
    @Path("{resourceId}/download")
    @Produces({ MediaType.APPLICATION_OCTET_STREAM, MediaType.APPLICATION_JSON })
    public Response downloadDataExportFile(@PathParam("resourceId") final Long resourceId, 
            @QueryParam(DataExportApiConstants.BASE_ENTITY_NAME_PARAM_NAME) final String baseEntityName, 
            @QueryParam(DataExportApiConstants.FILE_FORMAT_PARAM_NAME) final String fileFormat) {

        this.platformSecurityContext.authenticatedUser().validateHasReadPermission(baseEntityName);

        return this.dataExportReadPlatformService.downloadDataExportFile(resourceId, fileFormat);
    }
    
    @PUT
    @Path("{entityId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String updateDataExport(@PathParam("entityId") final Long entityId, final String apiRequestBodyAsJson) {
    	final CommandWrapper commandWrapper = new CommandWrapperBuilder().
    			updateDataExport(entityId).withJson(apiRequestBodyAsJson).build();
    	final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);
    	
    	return this.toApiJsonSerializer.serialize(result);
    }
    
    @DELETE
    @Path("{entityId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteDataExport(@PathParam("entityId") final Long entityId, final String apiRequestBodyAsJson) {
    	final CommandWrapper commandWrapper = new CommandWrapperBuilder().
    			deleteDataExport(entityId).withJson(apiRequestBodyAsJson).build();
    	final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandWrapper);
    	
    	return this.toApiJsonSerializer.serialize(result);
    }
}
