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
package org.apache.fineract.portfolio.creditcheck.data;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.exception.PlatformApiDataValidationException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.creditcheck.CreditCheckConstants;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckRelatedEntity;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckSeverityLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class CreditCheckDataValidator {
    
    private final FromJsonHelper fromJsonHelper;

    @Autowired
    public CreditCheckDataValidator(final FromJsonHelper fromJsonHelper) {
        this.fromJsonHelper = fromJsonHelper;
    }
    
    /** 
     * validate the request to create a new credit check 
     * 
     * @param jsonCommand -- the JSON command object (instance of the JsonCommand class)
     * @return None
     **/
    public void validateForCreateAction(final JsonCommand jsonCommand) {
        final String jsonString = jsonCommand.json();
        final JsonElement jsonElement = jsonCommand.parsedJson();

        if (StringUtils.isBlank(jsonString)) { 
            throw new InvalidJsonException(); 
        }
        
        final Type typeToken = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromJsonHelper.checkForUnsupportedParameters(typeToken, jsonString, 
                CreditCheckConstants.CREATE_REQUEST_PARAMETERS);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder dataValidatorBuilder = new DataValidatorBuilder(dataValidationErrors)
                .resource(StringUtils.lowerCase(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME));
        
        final String name = this.fromJsonHelper.extractStringNamed(CreditCheckConstants.NAME_PARAM_NAME, jsonElement);
        dataValidatorBuilder.reset().parameter(CreditCheckConstants.NAME_PARAM_NAME).value(name)
                .notBlank().notExceedingLengthOf(100);
        
        final Integer creditCheckRelatedEntity = this.fromJsonHelper.extractIntegerSansLocaleNamed(
                CreditCheckConstants.RELATED_ENTITY_PARAM_NAME, jsonElement);
        dataValidatorBuilder.reset().parameter(CreditCheckConstants.RELATED_ENTITY_PARAM_NAME)
                .value(creditCheckRelatedEntity).notNull();

        if (creditCheckRelatedEntity != null) {
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.RELATED_ENTITY_PARAM_NAME).value(creditCheckRelatedEntity)
                    .isOneOfTheseValues(CreditCheckRelatedEntity.validValues());
        }
        
        if (this.fromJsonHelper.parameterExists(CreditCheckConstants.IS_ACTIVE_PARAM_NAME, jsonElement)) {
            final Boolean isActive = this.fromJsonHelper.extractBooleanNamed(CreditCheckConstants.IS_ACTIVE_PARAM_NAME, 
                    jsonElement);
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.IS_ACTIVE_PARAM_NAME).value(isActive).notNull();
        }
        
        final String expectedResult = this.fromJsonHelper.extractStringNamed(CreditCheckConstants.EXPECTED_RESULT_PARAM_NAME, 
                jsonElement);
        dataValidatorBuilder.reset().parameter(CreditCheckConstants.EXPECTED_RESULT_PARAM_NAME).value(expectedResult)
                .notBlank().notExceedingLengthOf(20);
        
        final Integer severityLevel = this.fromJsonHelper.extractIntegerSansLocaleNamed(CreditCheckConstants.SEVERITY_LEVEL_PARAM_NAME, 
                jsonElement);
        dataValidatorBuilder.reset().parameter(CreditCheckConstants.SEVERITY_LEVEL_PARAM_NAME).value(severityLevel).notNull();
        
        if (severityLevel != null) {
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.SEVERITY_LEVEL_PARAM_NAME).value(severityLevel)
                    .isOneOfTheseValues(CreditCheckSeverityLevel.validValues());
        }
        
        final Integer stretchyReportId = this.fromJsonHelper.extractIntegerWithLocaleNamed(CreditCheckConstants.STRETCHY_REPORT_ID_PARAM_NAME, 
                jsonElement);
        dataValidatorBuilder.reset().parameter(CreditCheckConstants.STRETCHY_REPORT_ID_PARAM_NAME).value(stretchyReportId).notNull();
        
        final String message = this.fromJsonHelper.extractStringNamed(CreditCheckConstants.MESSAGE_PARAM_NAME, jsonElement);
        dataValidatorBuilder.reset().parameter(CreditCheckConstants.MESSAGE_PARAM_NAME).value(message).notBlank().notExceedingLengthOf(500);
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
    
    /** 
     * validate the request to update a credit check 
     * 
     * @param jsonCommand -- the JSON command object (instance of the JsonCommand class)
     * @return None
     **/
    public void validateForUpdateAction(final JsonCommand jsonCommand) {
        final String jsonString = jsonCommand.json();
        final JsonElement jsonElement = jsonCommand.parsedJson();

        if (StringUtils.isBlank(jsonString)) { throw new InvalidJsonException(); }
        
        final Type typeToken = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromJsonHelper.checkForUnsupportedParameters(typeToken, jsonString, 
                CreditCheckConstants.UPDATE_REQUEST_PARAMETERS);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder dataValidatorBuilder = new DataValidatorBuilder(dataValidationErrors)
                .resource(StringUtils.lowerCase(CreditCheckConstants.CREDIT_CHECK_ENTITY_NAME));
        
        if (this.fromJsonHelper.parameterExists(CreditCheckConstants.NAME_PARAM_NAME, jsonElement)) {
            final String name = this.fromJsonHelper.extractStringNamed(CreditCheckConstants.NAME_PARAM_NAME, jsonElement);
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.NAME_PARAM_NAME).value(name).notBlank().notExceedingLengthOf(100);
        }
        
        if (this.fromJsonHelper.parameterExists(CreditCheckConstants.IS_ACTIVE_PARAM_NAME, jsonElement)) {
            final Boolean isActive = this.fromJsonHelper.extractBooleanNamed(CreditCheckConstants.IS_ACTIVE_PARAM_NAME, jsonElement);
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.IS_ACTIVE_PARAM_NAME).value(isActive).notNull();
        }
        
        if (this.fromJsonHelper.parameterExists(CreditCheckConstants.EXPECTED_RESULT_PARAM_NAME, jsonElement)) {
            final String expectedResult = this.fromJsonHelper.extractStringNamed(CreditCheckConstants.EXPECTED_RESULT_PARAM_NAME, 
                    jsonElement);
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.EXPECTED_RESULT_PARAM_NAME).value(expectedResult)
                    .notBlank().notExceedingLengthOf(20);
        }
        
        if (this.fromJsonHelper.parameterExists(CreditCheckConstants.SEVERITY_LEVEL_PARAM_NAME, jsonElement)) {
            final Integer severityLevel = this.fromJsonHelper.extractIntegerSansLocaleNamed(CreditCheckConstants.SEVERITY_LEVEL_PARAM_NAME, 
                    jsonElement);
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.SEVERITY_LEVEL_PARAM_NAME).value(severityLevel).notNull()
                    .isOneOfTheseValues(CreditCheckSeverityLevel.validValues());
        }
        
        if (this.fromJsonHelper.parameterExists(CreditCheckConstants.STRETCHY_REPORT_ID_PARAM_NAME, jsonElement)) {
            final Integer stretchyReportId = this.fromJsonHelper.extractIntegerWithLocaleNamed(CreditCheckConstants.STRETCHY_REPORT_ID_PARAM_NAME, 
                    jsonElement);
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.STRETCHY_REPORT_ID_PARAM_NAME).value(stretchyReportId).notNull();
        }
        
        if (this.fromJsonHelper.parameterExists(CreditCheckConstants.MESSAGE_PARAM_NAME, jsonElement)) {
            final String message = this.fromJsonHelper.extractStringNamed(CreditCheckConstants.MESSAGE_PARAM_NAME, jsonElement);
            dataValidatorBuilder.reset().parameter(CreditCheckConstants.MESSAGE_PARAM_NAME).value(message).notBlank()
                    .notExceedingLengthOf(500);
        }
        
        throwExceptionIfValidationWarningsExist(dataValidationErrors);
    }
    
    /** 
     * throw a PlatformApiDataValidationException exception if there are validation errors
     * 
     * @param dataValidationErrors -- list of ApiParameterError objects
     * @return None
     **/
    private void throwExceptionIfValidationWarningsExist(final List<ApiParameterError> dataValidationErrors) {
        if (!dataValidationErrors.isEmpty()) { 
            throw new PlatformApiDataValidationException(dataValidationErrors); 
        }
    }
}
