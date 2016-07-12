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
import org.apache.fineract.commands.exception.UnsupportedCommandException;
import org.apache.fineract.portfolio.group.api.GroupsApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This command strategy is used to assign a role to a group.
 *
 * @author Ishan Khanna
 */
@Component
public class AssignRoleToGroupCommandStrategy implements CommandStrategy {

    /**
     * Command expected in the batch request to assign the role to a group.
     */
    private static final String ASSIGN_ROLE_TO_GROUP_COMMAND = "assignRole";

    /**
     * The API Resource that allows us to execute the command to assign role.
     */
    private final GroupsApiResource groupsApiResource;

    @Autowired
    public AssignRoleToGroupCommandStrategy(final GroupsApiResource groupsApiResource) {
        this.groupsApiResource = groupsApiResource;
    }

    @Override
    public BatchResponse execute(final BatchRequest batchRequest, @SuppressWarnings("unused") UriInfo uriInfo) {

        final BatchResponse batchResponse = new BatchResponse();
        final String responseBody;

        batchResponse.setRequestId(batchRequest.getRequestId());
        batchResponse.setHeaders(batchRequest.getHeaders());

        final String[] pathParameters = batchRequest.getRelativeUrl().split("/");

        // Fetching the group ID and command from the path which should look something like this
        // /groups/{groupID}?command=assignRole

        Long groupId = Long.parseLong(pathParameters[1].substring(0, pathParameters[1].indexOf("?")));

        final String command = pathParameters[1].substring(pathParameters[1].indexOf("=")+1, pathParameters[1].length());

        try {

            if (command.equals(ASSIGN_ROLE_TO_GROUP_COMMAND)) {

                    responseBody = groupsApiResource.activateOrGenerateCollectionSheet(groupId, command, null,
                            batchRequest.getBody(), uriInfo);
                    batchResponse.setStatusCode(200);
                    batchResponse.setBody(responseBody);

            } else {

                // Maybe the command was not provided or an invalid command was provided.
                throw new UnsupportedCommandException(command);

            }

        } catch (RuntimeException e) {

            // Gets an object of type ErrorInfo, containing information about
            // raised exception
            ErrorInfo ex = ErrorHandler.handler(e);

            batchResponse.setStatusCode(ex.getStatusCode());
            batchResponse.setBody(ex.getMessage());

        }

        return batchResponse;
    }
}
