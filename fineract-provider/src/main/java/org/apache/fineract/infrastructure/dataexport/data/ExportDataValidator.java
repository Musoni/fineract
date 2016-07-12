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
package org.apache.fineract.infrastructure.dataexport.data;

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
import org.apache.fineract.infrastructure.dataexport.api.DataExportApiConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

@Component
public class ExportDataValidator {

    private final FromJsonHelper fromJsonHelper;

    @Autowired
    public ExportDataValidator(final FromJsonHelper fromJsonHelper) {
        this.fromJsonHelper = fromJsonHelper;
    }

    /** 
     * validate the request to create a new data export tool
     * 
     * @param jsonCommand -- the JSON command object (instance of the JsonCommand class)
     * @return None
     **/
    public void validateCreateDataExportRequest(final JsonCommand jsonCommand) {
        final String jsonString = jsonCommand.json();
        final JsonElement jsonElement = jsonCommand.parsedJson();

        if (StringUtils.isBlank(jsonString)) { 
            throw new InvalidJsonException(); 
        }
        
        final Type typeToken = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromJsonHelper.checkForUnsupportedParameters(typeToken, jsonString, 
                DataExportApiConstants.CREATE_DATA_EXPORT_REQUEST_PARAMETERS);
        
        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder dataValidatorBuilder = new DataValidatorBuilder(dataValidationErrors).
                resource(StringUtils.lowerCase(DataExportApiConstants.DATA_EXPORT_ENTITY_NAME));
        
        final String name = this.fromJsonHelper.extractStringNamed(
                DataExportApiConstants.NAME_PARAM_NAME, jsonElement);
        dataValidatorBuilder.reset().parameter(DataExportApiConstants.BASE_ENTITY_NAME_PARAM_NAME).value(name).notBlank();
        
        final String baseEntity = this.fromJsonHelper.extractStringNamed(
                DataExportApiConstants.BASE_ENTITY_NAME_PARAM_NAME, jsonElement);
        dataValidatorBuilder.reset().parameter(DataExportApiConstants.BASE_ENTITY_NAME_PARAM_NAME).value(baseEntity).notBlank();
        
        final String[] columns = this.fromJsonHelper.extractArrayNamed(DataExportApiConstants.COLUMNS_PARAM_NAME, 
                jsonElement);
        dataValidatorBuilder.reset().parameter(DataExportApiConstants.COLUMNS_PARAM_NAME).value(columns).notBlank();
        
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
