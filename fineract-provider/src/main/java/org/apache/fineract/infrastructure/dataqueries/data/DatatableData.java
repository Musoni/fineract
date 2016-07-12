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
package org.apache.fineract.infrastructure.dataqueries.data;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Immutable data object representing datatable data.
 */
public class DatatableData {

    @SuppressWarnings("unused")
    private final String applicationTableName;
    private final String registeredTableName;
    @SuppressWarnings("unused")
    private final Long category;

    private final String displayName;

    @SuppressWarnings("unused")
    private final boolean systemDefined;
    private final List<ResultsetColumnHeaderData> columnHeaderData;
    @SuppressWarnings("unused")
    private final List<MetaDataResultSet> metaDataResultSets;

    private final static Logger logger = LoggerFactory.getLogger(DatatableData.class);


    public static DatatableData create(final String applicationTableName, final String registeredTableName,
            final List<ResultsetColumnHeaderData> columnHeaderData,final Long category,final List<MetaDataResultSet> metaDataResultSets,final boolean systemDefined,final String displayName) {
        return new DatatableData(applicationTableName, registeredTableName, columnHeaderData,category,metaDataResultSets,systemDefined,displayName);
    }

    private DatatableData(final String applicationTableName, final String registeredTableName,final List<ResultsetColumnHeaderData> columnHeaderData,
                          final Long category,final List<MetaDataResultSet> metaDataResultSets,final boolean systemDefined, final String displayName) {
        this.applicationTableName = applicationTableName;
        this.registeredTableName = registeredTableName;
        this.columnHeaderData = columnHeaderData;
        this.category  = category;
        this.metaDataResultSets = metaDataResultSets;
        this.systemDefined = systemDefined;
        this.displayName = displayName;

    }

    public boolean hasColumn(final String columnName){

        for(ResultsetColumnHeaderData c : this.columnHeaderData){

            if(c.getColumnName().equals(columnName)) return true;

            logger.info(c.getColumnName()+"is it equal to"+ columnName);
        }

        return false;
    }

    public String getRegisteredTableName(){
        return registeredTableName;
    }
    
    /**
     * @return the displayName
     */
    public String getDisplayName() {
    	return this.displayName;
    }
}