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

import java.util.Collection;

import org.apache.fineract.infrastructure.dataqueries.data.DatatableData;

public class DataExportEntityData {
    private final String entityName;
    private final String tableName;
    private final Collection<DatatableData> datatables;
    private final Collection<EntityColumnMetaData> columns;
    
    /**
     * @param entityName
     * @param tableName
     * @param datatables
     * @param columns
     */
    private DataExportEntityData(final String entityName, final String tableName, final Collection<DatatableData> datatables,
            final Collection<EntityColumnMetaData> columns) {
        this.entityName = entityName;
        this.tableName = tableName;
        this.datatables = datatables;
        this.columns = columns;
    }
    
    /**
     * Creates a new {@link DataExportEntityData} object
     * 
     * @param entityName
     * @param tableName
     * @param datatables
     * @param columns
     * @return {@link DataExportEntityData} object
     */
    public static DataExportEntityData newInstance(final String entityName, final String tableName, final Collection<DatatableData> datatables,
            final Collection<EntityColumnMetaData> columns) {
        return new DataExportEntityData(entityName, tableName, datatables, columns);
    }

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @return the datatables
     */
    public Collection<DatatableData> getDatatables() {
        return datatables;
    }

    /**
     * @return the columns
     */
    public Collection<EntityColumnMetaData> getColumns() {
        return columns;
    }
}
