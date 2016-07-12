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

import javax.ws.rs.core.UriInfo;

import org.apache.fineract.batch.command.CommandStrategy;
import org.apache.fineract.batch.domain.BatchRequest;
import org.apache.fineract.batch.domain.BatchResponse;
import org.apache.fineract.batch.exception.ErrorHandler;
import org.apache.fineract.batch.exception.ErrorInfo;
import org.apache.fineract.portfolio.collateral.api.CollateralsApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ishan on 04/10/16.
 */
@Component
public class CreateCollateralCommandStrategy implements CommandStrategy {

    private final CollateralsApiResource collateralsApiResource;

    @Autowired
    public CreateCollateralCommandStrategy(final CollateralsApiResource collateralsApiResource) {
        this.collateralsApiResource = collateralsApiResource;
    }

    @Override
    public BatchResponse execute(BatchRequest batchRequest, UriInfo uriInfo) {

        final BatchResponse response = new BatchResponse();
        final String responseBody;

        response.setRequestId(batchRequest.getRequestId());
        response.setHeaders(batchRequest.getHeaders());

        final String[] pathParameters = batchRequest.getRelativeUrl().split("/");
        Long loanId = Long.parseLong(pathParameters[1]);

        try {

            responseBody = collateralsApiResource.createCollateral(loanId, batchRequest.getBody());
            response.setBody(responseBody);
            response.setStatusCode(200);

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
