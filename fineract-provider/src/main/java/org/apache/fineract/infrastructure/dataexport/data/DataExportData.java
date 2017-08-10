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

import org.apache.fineract.infrastructure.dataexport.domain.DataExport;

/**
 * Immutable object representing a {@link DataExport} entity data
 */
public class DataExportData {
    private final Long id;
    private final String name;
    private final String baseEntityName;
    private final String userRequestMap;
    private final String filename;
    private final Integer fileDownloadCount;
    private final DataExportTimelineData timeline;
    
    /**
     * @param id
     * @param name
     * @param baseEntityName
     * @param userRequestMap
     * @param fileDownloadCount
     * @param timeline
     * @param filename
     */
    private DataExportData(final Long id, final String name, final String baseEntityName, final String userRequestMap, 
            final Integer fileDownloadCount, final DataExportTimelineData timeline, final String filename) {
        this.id = id;
        this.name = name;
        this.baseEntityName = baseEntityName;
        this.userRequestMap = userRequestMap;
        this.fileDownloadCount = fileDownloadCount;
        this.timeline = timeline;
        this.filename = filename;
    }
    
    /**
     * Creates a new {@link DataExportData} object
     * 
     * @param id
     * @param name
     * @param baseEntityName
     * @param userRequestMap
     * @param fileDownloadCount
     * @param timeline
     * @param filename
     * @return {@link DataExportData} object
     */
    public static DataExportData newInstance(final Long id, final String name, final String baseEntityName, 
    		final String userRequestMap, final Integer fileDownloadCount, final DataExportTimelineData timeline, 
    		final String filename) {
        return new DataExportData(id, name, baseEntityName, userRequestMap, fileDownloadCount, timeline, filename);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
     * @return the baseEntityName
     */
    public String getBaseEntityName() {
        return baseEntityName;
    }

    /**
     * @return the userRequestMap
     */
    public String getUserRequestMap() {
        return userRequestMap;
    }

    /**
     * @return the fileDownloadCount
     */
    public Integer getFileDownloadCount() {
        return fileDownloadCount;
    }

    /**
     * @return the timeline
     */
    public DataExportTimelineData getTimeline() {
        return timeline;
    }

    /**
     * @return filename
     */
    public String getFilename() {
        return filename;
    }
}
