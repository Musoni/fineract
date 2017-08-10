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

public class DataExportFieldData {
    private final Long id;
    private final String entityTable;
    private final String entityFieldName;
    private final String entityJsonParam;
    private final String entityFieldLabel;
    private final String referenceTable;
    private final String referenceField;
    
    /**
     * @param id
     * @param entityTable
     * @param entityFieldName
     * @param entityJsonParam
     * @param entityFieldLabel
     * @param referenceTable
     * @param referenceField
     */
    private DataExportFieldData(final Long id, final String entityTable, final String entityFieldName, 
            final String entityJsonParam, final String entityFieldLabel, final String referenceTable, 
            final String referenceField) {
        this.id = id;
        this.entityTable = entityTable;
        this.entityFieldName = entityFieldName;
        this.entityJsonParam = entityJsonParam;
        this.entityFieldLabel = entityFieldLabel;
        this.referenceTable = referenceTable;
        this.referenceField = referenceField;
    }
    
    /**
     * Creates a new {@link DataExportFieldData} object
     * 
     * @param id
     * @param entityTable
     * @param entityFieldName
     * @param entityJsonParam
     * @param entityFieldLabel
     * @param referenceTable
     * @param referenceField
     * @return
     */
    public static DataExportFieldData newInstance(final Long id, final String entityTable, 
            final String entityFieldName, final String entityJsonParam, final String entityFieldLabel, 
            final String referenceTable, final String referenceField) {
        return new DataExportFieldData(id, entityTable, entityFieldName, entityJsonParam, 
                entityFieldLabel, referenceTable, referenceField);
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the entityTable
     */
    public String getEntityTable() {
        return entityTable;
    }

    /**
     * @return the entityFieldName
     */
    public String getEntityFieldName() {
        return entityFieldName;
    }

    /**
     * @return the entityJsonParam
     */
    public String getEntityJsonParam() {
        return entityJsonParam;
    }

    /**
     * @return the entityFieldLabel
     */
    public String getEntityFieldLabel() {
        return entityFieldLabel;
    }

    /**
     * @return the referenceTable
     */
    public String getReferenceTable() {
        return referenceTable;
    }

    /**
     * @return the referenceField
     */
    public String getReferenceField() {
        return referenceField;
    }
}
