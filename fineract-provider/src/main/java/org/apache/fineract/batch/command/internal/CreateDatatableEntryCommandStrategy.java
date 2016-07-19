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
package org.apache.fineract.batch.command.internal;

import org.apache.fineract.batch.command.CommandStrategy;
import org.apache.fineract.batch.domain.BatchRequest;
import org.apache.fineract.batch.domain.BatchResponse;
import org.apache.fineract.batch.exception.ErrorHandler;
import org.apache.fineract.batch.exception.ErrorInfo;
import org.apache.fineract.infrastructure.dataqueries.api.DatatablesApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriInfo;

/**
 * Implements {@link CommandStrategy} to handle
 * activation of a pending client. It passes the contents of the body from the
 * BatchRequest to
 * {@link DatatablesApiResource} and gets
 * back the response. This class will also catch any errors raised by
 * {@link DatatablesApiResource} and map
 * those errors to appropriate status codes in BatchResponse.
 *
 * @author Sander van der Heijden
 *
 * @see CommandStrategy
 * @see BatchRequest
 * @see BatchResponse
 */
@Component
public class CreateDatatableEntryCommandStrategy implements CommandStrategy {

    private final DatatablesApiResource datatablesApiResource;

    @Autowired
    public CreateDatatableEntryCommandStrategy(final DatatablesApiResource datatablesApiResource) {
        this.datatablesApiResource = datatablesApiResource;
    }

    @Override
    public BatchResponse execute(final BatchRequest request, @SuppressWarnings("unused") UriInfo uriInfo) {

        final BatchResponse response = new BatchResponse();
        final String responseBody;

        response.setRequestId(request.getRequestId());
        response.setHeaders(request.getHeaders());

        final String[] pathParameters = request.getRelativeUrl().split("/");
        final String datatable = pathParameters[1];
        final Long appTableId = Long.parseLong(pathParameters[2]);

        // Try-catch blocks to map exceptions to appropriate status codes
        try {

            // Calls 'create' function from 'DatatablesApiResource' to create a client identifier
            responseBody = datatablesApiResource.createDatatableEntry (datatable, appTableId, request.getBody());

            response.setStatusCode(200);
            // Sets the body of the response after the successful activation of
            // the client
            response.setBody(responseBody);

        } catch (RuntimeException e) {

            // Gets an object of type ErrorInfo, containing information about
            // raised exception
            ErrorInfo ex = ErrorHandler.handler(e);

            response.setStatusCode(ex.getStatusCode());
            response.setBody(ex.getMessage());
        }

        return response;
    }

}
