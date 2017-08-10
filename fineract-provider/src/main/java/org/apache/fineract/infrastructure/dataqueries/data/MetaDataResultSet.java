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

public class MetaDataResultSet {

    private Long id;
    private String columnName;
    private String labelName;
    private Long order;
    private boolean systemDefined;
    private String displayCondition;
    private String formulaExpression;
    private String type;


    public static MetaDataResultSet createMetaDataResultSet(final Long id,final String columnName,final String labelName, final Long order,final boolean systemDefined, final String displayCondition, final String formulaExpression){

        return new MetaDataResultSet(id,columnName,labelName,order,systemDefined, displayCondition, formulaExpression,null);
    }

    public static MetaDataResultSet createMetaDataResultSet(final Long id,final String columnName,final String labelName, final Long order,final boolean systemDefined, final String displayCondition, final String formulaExpression, final String type){
        return new MetaDataResultSet(id,columnName,labelName,order,systemDefined, displayCondition, formulaExpression,type);
    }

    private MetaDataResultSet(final Long id,final String columnName, final String labelName, final Long order,final boolean systemDefined, final String displayCondition, final String formulaExpression, final String type) {
        this.id = id;
        this.columnName = columnName;
        this.labelName = labelName;
        this.order = order;
        this.systemDefined = systemDefined;
        this.displayCondition = displayCondition;
        this.formulaExpression = formulaExpression;
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getLabelName() {
        return labelName;
    }

    public Long getOrder() {
        return order;
    }

    public Long getId() {
        return this.id;
    }

    public boolean isSystemDefined() {return this.systemDefined;}

    public String getDisplayCondition() { return displayCondition; }

    public String getFormulaExpression() { return formulaExpression; }

    public String getType() {
        return type;
    }
}
