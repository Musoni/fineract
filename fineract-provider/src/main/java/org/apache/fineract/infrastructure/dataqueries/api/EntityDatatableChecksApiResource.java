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
package org.apache.fineract.infrastructure.dataqueries.api;

import org.apache.fineract.commands.domain.CommandWrapper;
import org.apache.fineract.commands.service.CommandWrapperBuilder;
import org.apache.fineract.commands.service.PortfolioCommandSourceWritePlatformService;
import org.apache.fineract.infrastructure.core.api.ApiParameterHelper;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.serialization.ToApiJsonSerializer;
import org.apache.fineract.infrastructure.dataqueries.data.EntityDataTableChecksData;
import org.apache.fineract.infrastructure.dataqueries.data.EntityDataTableChecksTemplateData;
import org.apache.fineract.infrastructure.dataqueries.data.GenericResultsetData;
import org.apache.fineract.infrastructure.dataqueries.service.EntityDatatableChecksReadService;
import org.apache.fineract.infrastructure.dataqueries.service.GenericDataService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;

//import org.slf4j.Logger;

@Path("/entityDatatableChecks")
@Component
@Scope("singleton")
public class EntityDatatableChecksApiResource {

    private final PlatformSecurityContext context;
    private final GenericDataService genericDataService;
    private final EntityDatatableChecksReadService readEntityDatatableChecksService;
    private final ToApiJsonSerializer<GenericResultsetData> toApiJsonSerializer;
    private final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService;
    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(EntityDatatableChecksApiResource.class);

    @Autowired
    public EntityDatatableChecksApiResource(final PlatformSecurityContext context, final GenericDataService genericDataService,
                                            final EntityDatatableChecksReadService readEntityDatatableChecksService,
                                            final ToApiJsonSerializer<GenericResultsetData> toApiJsonSerializer,
                                            final PortfolioCommandSourceWritePlatformService commandsSourceWritePlatformService) {
        this.context = context;
        this.genericDataService = genericDataService;
        this.readEntityDatatableChecksService = readEntityDatatableChecksService;
        this.toApiJsonSerializer = toApiJsonSerializer;
        this.commandsSourceWritePlatformService = commandsSourceWritePlatformService;
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String retrieveAll(@Context final UriInfo uriInfo,@QueryParam("status") final Long status,@QueryParam("entity") final String entity,@QueryParam("productLoanId") final Long productLoanId) {



        final List<EntityDataTableChecksData> result = this.readEntityDatatableChecksService.retrieveAll(status,entity,productLoanId);

        final boolean prettyPrint = ApiParameterHelper.prettyPrint(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serializePretty(prettyPrint, result);
    }

    @GET
    @Path("template")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String getTemplate(@Context final UriInfo uriInfo) {

        final EntityDataTableChecksTemplateData result = this.readEntityDatatableChecksService.retrieveTemplate();

        final boolean prettyPrint = ApiParameterHelper.prettyPrint(uriInfo.getQueryParameters());
        return this.toApiJsonSerializer.serializePretty(prettyPrint, result);
    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String createEntityDatatableCheck(final String apiRequestBodyAsJson) {

        final CommandWrapper commandRequest = new CommandWrapperBuilder().createEntityDatatableChecks(apiRequestBodyAsJson).build();
        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }
//
//    @PUT
//    @Path("{datatableName}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String updateDatatable(@PathParam("datatableName") final String datatableName, final String apiRequestBodyAsJson) {
//
//        final CommandWrapper commandRequest = new CommandWrapperBuilder().updateDBDatatable(datatableName, apiRequestBodyAsJson).build();
//
//        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
//        return this.toApiJsonSerializer.serialize(result);
//    }
//
    @DELETE
    @Path("{entityDatatableCheckId}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public String deleteDatatable(@PathParam("entityDatatableCheckId") final long entityDatatableCheckId, final String apiRequestBodyAsJson) {

        final CommandWrapper commandRequest = new CommandWrapperBuilder().deleteEntityDatatableChecks(entityDatatableCheckId, apiRequestBodyAsJson).build();

        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
        return this.toApiJsonSerializer.serialize(result);
    }
//
//    @POST
//    @Path("register/{datatable}/{apptable}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String registerDatatable(@PathParam("datatable") final String datatable, @PathParam("apptable") final String apptable,
//            final String apiRequestBodyAsJson) {
//
//        final CommandWrapper commandRequest = new CommandWrapperBuilder().registerDBDatatable(datatable, apptable)
//                .withJson(apiRequestBodyAsJson).build();
//
//        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
//
//        return this.toApiJsonSerializer.serialize(result);
//    }
//
//    @POST
//    @Path("deregister/{datatable}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String deregisterDatatable(@PathParam("datatable") final String datatable) {
//
//        this.readWriteNonCoreDataService.deregisterDatatable(datatable);
//
//        final CommandProcessingResult result = new CommandProcessingResultBuilder().withResourceIdAsString(datatable).build();
//
//        return this.toApiJsonSerializer.serialize(result);
//    }
//
//    @GET
//    @Path("{datatable}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String getDatatable(@PathParam("datatable") final String datatable, @Context final UriInfo uriInfo) {
//
//        final DatatableData result = this.readWriteNonCoreDataService.retrieveDatatable(datatable);
//
//        final boolean prettyPrint = ApiParameterHelper.prettyPrint(uriInfo.getQueryParameters());
//        return this.toApiJsonSerializer.serializePretty(prettyPrint, result);
//    }
//
//    @GET
//    @Path("{datatable}/{apptableId}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String getDatatable(@PathParam("datatable") final String datatable, @PathParam("apptableId") final Long apptableId,
//            @QueryParam("order") final String order, @Context final UriInfo uriInfo) {
//
//        this.context.authenticatedUser().validateHasDatatableReadPermission(datatable);
//
//        final GenericResultsetData results = this.readWriteNonCoreDataService.retrieveDataTableGenericResultSet(datatable, apptableId,
//                order, null);
//
//        String json = "";
//        final boolean genericResultSet = ApiParameterHelper.genericResultSet(uriInfo.getQueryParameters());
//        if (genericResultSet) {
//            final boolean prettyPrint = ApiParameterHelper.prettyPrint(uriInfo.getQueryParameters());
//            json = this.toApiJsonSerializer.serializePretty(prettyPrint, results);
//        } else {
//            json = this.genericDataService.generateJsonFromGenericResultsetData(results);
//        }
//
//        return json;
//    }
//
//    @GET
//    @Path("{datatable}/{apptableId}/{datatableId}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String getDatatableManyEntry(@PathParam("datatable") final String datatable, @PathParam("apptableId") final Long apptableId,
//            @PathParam("datatableId") final Long datatableId, @QueryParam("order") final String order, @Context final UriInfo uriInfo) {
//
//        logger.debug("::1 we came in the getDatatbleManyEntry apiRessource method");
//
//        this.context.authenticatedUser().validateHasDatatableReadPermission(datatable);
//
//        final GenericResultsetData results = this.readWriteNonCoreDataService.retrieveDataTableGenericResultSet(datatable, apptableId,
//                order, datatableId);
//
//        String json = "";
//        final boolean genericResultSet = ApiParameterHelper.genericResultSet(uriInfo.getQueryParameters());
//        if (genericResultSet) {
//            final boolean prettyPrint = ApiParameterHelper.prettyPrint(uriInfo.getQueryParameters());
//            json = this.toApiJsonSerializer.serializePretty(prettyPrint, results);
//        } else {
//            json = this.genericDataService.generateJsonFromGenericResultsetData(results);
//        }
//
//        return json;
//    }
//
//    @POST
//    @Path("{datatable}/{apptableId}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String createDatatableEntry(@PathParam("datatable") final String datatable, @PathParam("apptableId") final Long apptableId,
//            final String apiRequestBodyAsJson) {
//
//        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
//                .createDatatable(datatable, apptableId, null) //
//                .withJson(apiRequestBodyAsJson) //
//                .build();
//
//        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
//
//        return this.toApiJsonSerializer.serialize(result);
//    }
//
//    @PUT
//    @Path("{datatable}/{apptableId}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String updateDatatableEntryOnetoOne(@PathParam("datatable") final String datatable,
//            @PathParam("apptableId") final Long apptableId, final String apiRequestBodyAsJson) {
//
//        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
//                .updateDatatable(datatable, apptableId, null) //
//                .withJson(apiRequestBodyAsJson) //
//                .build();
//
//        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
//
//        return this.toApiJsonSerializer.serialize(result);
//    }
//
//    @PUT
//    @Path("{datatable}/{apptableId}/{datatableId}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String updateDatatableEntryOneToMany(@PathParam("datatable") final String datatable,
//            @PathParam("apptableId") final Long apptableId, @PathParam("datatableId") final Long datatableId,
//            final String apiRequestBodyAsJson) {
//
//        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
//                .updateDatatable(datatable, apptableId, datatableId) //
//                .withJson(apiRequestBodyAsJson) //
//                .build();
//
//        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
//
//        return this.toApiJsonSerializer.serialize(result);
//    }
//
//    @DELETE
//    @Path("{datatable}/{apptableId}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String deleteDatatableEntries(@PathParam("datatable") final String datatable, @PathParam("apptableId") final Long apptableId) {
//
//        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
//                .deleteDatatable(datatable, apptableId, null) //
//                .build();
//
//        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
//
//        return this.toApiJsonSerializer.serialize(result);
//    }
//
//    @DELETE
//    @Path("{datatable}/{apptableId}/{datatableId}")
//    @Consumes({ MediaType.APPLICATION_JSON })
//    @Produces({ MediaType.APPLICATION_JSON })
//    public String deleteDatatableEntries(@PathParam("datatable") final String datatable, @PathParam("apptableId") final Long apptableId,
//            @PathParam("datatableId") final Long datatableId) {
//
//        final CommandWrapper commandRequest = new CommandWrapperBuilder() //
//                .deleteDatatable(datatable, apptableId, datatableId) //
//                .build();
//
//        final CommandProcessingResult result = this.commandsSourceWritePlatformService.logCommandSource(commandRequest);
//
//        return this.toApiJsonSerializer.serialize(result);
//    }
}