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
package org.apache.fineract.accounting.closure.storeglaccountbalance.helper;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;

public class UriQueryParameterHelper {
    public static final String AGGREGATE_BALANCE_OF_SUB_OFFICES_PARAMETER_NAME = "aggregateBalanceOfSubOffices";
    public static final String OFFICE_ID_PARAMETER_NAME = "officeId";
    public static final String START_CLOSURE_ID_PARAMETER_NAME = "startClosureId";
    public static final String END_CLOSURE_ID_PARAMETER_NAME = "endClosureId";
    public static final String REFERENCE_PARAMETER_NAME = "reference";
    public static final String BOOLEAN_TRUE_AS_STRING = "true";
    public static final String FILE_FORMAT_PARAMETER_NAME = "fileFormat";
    
    /**
     * Checks if the parameter "aggregateBalanceOfSubOffices" is part of the query string and set to true
     * 
     * @param uriQueryParameters
     * @return boolean true/false
     */
    public static boolean aggregateBalanceOfSubOffices(final MultivaluedMap<String, String> uriQueryParameters) {
        boolean aggregate = false;
        
        if (uriQueryParameters.getFirst(AGGREGATE_BALANCE_OF_SUB_OFFICES_PARAMETER_NAME) != null) {
            final String parameterValue = uriQueryParameters.getFirst(AGGREGATE_BALANCE_OF_SUB_OFFICES_PARAMETER_NAME);
            
            aggregate = BOOLEAN_TRUE_AS_STRING.equalsIgnoreCase(parameterValue);
        }
        
        return aggregate;
    }
    
    /**
     * Get the value of the "officeId" query string parameter
     * 
     * @param uriQueryParameters
     * @return office id
     */
    public static Long getOfficeId(final MultivaluedMap<String, String> uriQueryParameters) {
        Long officeId = null;
        
        if (uriQueryParameters.getFirst(OFFICE_ID_PARAMETER_NAME) != null) {
            final String parameterValue = uriQueryParameters.getFirst(OFFICE_ID_PARAMETER_NAME);
            
            if (StringUtils.isNotBlank(parameterValue)) {
                officeId = Long.valueOf(parameterValue);
            }
        }
        
        return officeId;
    }
    
    /**
     * Get the value of the "startClosureId" query string parameter
     * 
     * @param uriQueryParameters
     * @return closure id
     */
    public static Long getStartClosureId(final MultivaluedMap<String, String> uriQueryParameters) {
        Long officeId = null;
        
        if (uriQueryParameters.getFirst(START_CLOSURE_ID_PARAMETER_NAME) != null) {
            final String parameterValue = uriQueryParameters.getFirst(START_CLOSURE_ID_PARAMETER_NAME);
            
            if (StringUtils.isNotBlank(parameterValue)) {
                officeId = Long.valueOf(parameterValue);
            }
        }
        
        return officeId;
    }
    
    /**
     * Get the value of the "endClosureId" query string parameter
     * 
     * @param uriQueryParameters
     * @return closure id
     */
    public static Long getEndClosureId(final MultivaluedMap<String, String> uriQueryParameters) {
        Long officeId = null;
        
        if (uriQueryParameters.getFirst(END_CLOSURE_ID_PARAMETER_NAME) != null) {
            final String parameterValue = uriQueryParameters.getFirst(END_CLOSURE_ID_PARAMETER_NAME);
            
            if (StringUtils.isNotBlank(parameterValue)) {
                officeId = Long.valueOf(parameterValue);
            }
        }
        
        return officeId;
    }
    
    /**
     * Get the value of the "reference" query string parameter
     * 
     * @param uriQueryParameters
     * @return reference string
     */
    public static String getReference(final MultivaluedMap<String, String> uriQueryParameters) {
        return uriQueryParameters.getFirst(REFERENCE_PARAMETER_NAME);
    }

    /**
     * Get the value of the "reference" query string parameter
     *
     * @param uriQueryParameters
     * @return reference string
     */
    public static String getFileFormat(final MultivaluedMap<String, String> uriQueryParameters) {
        return uriQueryParameters.getFirst(FILE_FORMAT_PARAMETER_NAME);
    }
}
