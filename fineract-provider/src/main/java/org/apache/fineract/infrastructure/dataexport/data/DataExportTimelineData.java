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

import org.joda.time.DateTime;

public class DataExportTimelineData {
    @SuppressWarnings("unused")
    private final String createdByUsername;
    @SuppressWarnings("unused")
    private final String createdByFirstname;
    @SuppressWarnings("unused")
    private final String createdByLastname;
    @SuppressWarnings("unused")
    private final DateTime createdOnDate;
    @SuppressWarnings("unused")
    private final String modifiedByUsername;
    @SuppressWarnings("unused")
    private final String modifiedByFirstname;
    @SuppressWarnings("unused")
    private final String modifiedByLastname;
    @SuppressWarnings("unused")
    private final DateTime modifiedOnDate;
    
    /**
     * @param createdByUsername
     * @param createdByFirstname
     * @param createdByLastname
     * @param createdOnDate
     * @param modifiedByUsername
     * @param modifiedByFirstname
     * @param modifiedByLastname
     * @param modifiedOnDate
     */
    private DataExportTimelineData(final String createdByUsername, final String createdByFirstname, 
            final String createdByLastname, final DateTime createdOnDate, 
            final String modifiedByUsername, final String modifiedByFirstname,
            final String modifiedByLastname, final DateTime modifiedOnDate) {
        this.createdByUsername = createdByUsername;
        this.createdByFirstname = createdByFirstname;
        this.createdByLastname = createdByLastname;
        this.createdOnDate = createdOnDate;
        this.modifiedByUsername = modifiedByUsername;
        this.modifiedByFirstname = modifiedByFirstname;
        this.modifiedByLastname = modifiedByLastname;
        this.modifiedOnDate = modifiedOnDate;
    }
    
    /**
     * Creates a new instances of the {@link DataExportTimelineData} object
     * 
     * @param createdByUsername
     * @param createdByFirstname
     * @param createdByLastname
     * @param createdOnDate
     * @param modifiedByUsername
     * @param modifiedByFirstname
     * @param modifiedByLastname
     * @param modifiedOnDate
     * @return {@link DataExportTimelineData} object
     */
    public static DataExportTimelineData newInstance(final String createdByUsername, final String createdByFirstname, 
            final String createdByLastname, final DateTime createdOnDate, 
            final String modifiedByUsername, final String modifiedByFirstname,
            final String modifiedByLastname, final DateTime modifiedOnDate) {
        return new DataExportTimelineData(createdByUsername, createdByFirstname, createdByLastname, 
                createdOnDate, modifiedByUsername, modifiedByFirstname, modifiedByLastname, modifiedOnDate);
    }
}
