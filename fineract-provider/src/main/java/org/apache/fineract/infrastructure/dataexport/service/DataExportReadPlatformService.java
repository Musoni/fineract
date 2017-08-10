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
package org.apache.fineract.infrastructure.dataexport.service;

import org.apache.fineract.infrastructure.dataexport.data.DataExportData;
import org.apache.fineract.infrastructure.dataexport.data.DataExportEntityData;

import javax.ws.rs.core.Response;

import java.util.Collection;

public interface DataExportReadPlatformService {
    /**
     * Retrieves {@link DataExportData} representation of all {@link DataExport} entities
     * 
     * @return {@link DataExportData} objects
     */
    Collection<DataExportData> retrieveAll();
    
    /**
     * Retrieves {@link DataExportData} representation of the {@link DataExport} entity with id similar to the 
     * parameter passed
     * 
     * @param id entity identifier
     * @return {@link DataExportData} object
     */
    DataExportData retrieveOne(final Long id);
    
    /**
     * Retrieves a template data for the specified base entity
     * 
     * @param baseEntityName
     * @return
     */
    DataExportEntityData retrieveTemplate(final String baseEntityName);

    /**
     * Creates a file with the data export entity data
     * 
     * @param id data export entity identifier
     * @param fileFormat file format (xml, xls, csv)
     * @return
     */
    Response downloadDataExportFile(final Long id, final String fileFormat);
    
    /**
     * Retrieves all base entities
     * 
     * @return {@link DataExportEntityData} objects
     */
    Collection<DataExportEntityData> retrieveAllBaseEntities();
}
