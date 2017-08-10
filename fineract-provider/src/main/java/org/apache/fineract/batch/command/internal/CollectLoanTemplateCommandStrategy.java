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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minidev.json.parser.JSONParser;
import org.apache.fineract.batch.command.CommandStrategy;
import org.apache.fineract.batch.domain.BatchRequest;
import org.apache.fineract.batch.domain.BatchResponse;
import org.apache.fineract.batch.exception.ErrorHandler;
import org.apache.fineract.batch.exception.ErrorInfo;
import org.apache.fineract.portfolio.loanaccount.api.LoansApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.UriInfo;
import java.text.ParseException;

@Component
public class CollectLoanTemplateCommandStrategy implements CommandStrategy {

    private final LoansApiResource loansApiResource;

    @Autowired
    private CollectLoanTemplateCommandStrategy(final LoansApiResource loansApiResource) {
        this.loansApiResource = loansApiResource;
    }

    @Override
    public BatchResponse execute(BatchRequest request, @SuppressWarnings("unused") UriInfo uriInfo) {
        final BatchResponse response = new BatchResponse();
        final String responseBody;

        response.setRequestId(request.getRequestId());
        response.setHeaders(request.getHeaders());


        // Pluck out the loanId out of the relative path
        final String body = request.getBody();
        JsonParser parse = new JsonParser();
        JsonObject jsonBody = (JsonObject) parse.parse(body);

        final Long clientId = (jsonBody.has("clientId")) ? jsonBody.get("clientId").getAsLong() : null ;
        final Long productId =  (jsonBody.has("productId")) ? jsonBody.get("productId").getAsLong() : null;
        final String templateType = (jsonBody.has("templateType")) ? jsonBody.get("templateType").getAsString() : null;
        final Long groupId  = (jsonBody.has("groupId")) ? jsonBody.get("groupId").getAsLong() : null;


        // Try-catch blocks to map exceptions to appropriate status codes
        try {

            // Calls 'retrieveAllLoanCharges' function from
            // 'LoanChargesApiResource' to Collect
            // Charges for a loan
            responseBody = loansApiResource.template(clientId,groupId,productId,templateType,false,true,uriInfo);

            response.setStatusCode(200);
            // Sets the body of the response after Charges have been
            // successfully collected
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
