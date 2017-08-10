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
import java.util.Map;

public class ResultsetRowData {

    private final List<String> row;
    private final Map<String, String> rowWithColumnName;

    public static ResultsetRowData create(final List<String> rowValues) {
        return new ResultsetRowData(rowValues);
    }

    public static ResultsetRowData createWithColumnName(final List<String> rowValues,final Map<String, String>  rowValuesWithColumn) {
        return new ResultsetRowData(rowValues,rowValuesWithColumn);
    }

    private ResultsetRowData(final List<String> rowValues) {
        this.row = rowValues;
        this.rowWithColumnName = null;
    }

    private ResultsetRowData(final List<String> rowValues,final Map<String, String>  rowValuesWithColumn) {
        this.row = rowValues;
        this.rowWithColumnName = rowValuesWithColumn;
    }

    public List<String> getRow() {
        return this.row;
    }

    public Map<String, String> getRowWithColumnName() {return this.rowWithColumnName;}
}
