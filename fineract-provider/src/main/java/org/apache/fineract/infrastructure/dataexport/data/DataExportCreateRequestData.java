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

import java.util.Map;

public class DataExportCreateRequestData {
	private final String name;
    private final String baseEntityName;
    private final Map<String, String> filters;
    private final String[] datatables;
    private final String[] columns;
    
    /**
     * @param name
     * @param baseEntityName
     * @param filters
     * @param datatables
     * @param columns
     */
    public DataExportCreateRequestData(final String name, final String baseEntityName, 
    		final Map<String, String> filters, final String[] datatables, final String[] columns) {
    	this.name = name;
        this.baseEntityName = baseEntityName;
        this.filters = filters;
        this.datatables = datatables;
        this.columns = columns;
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
     * @return the filters
     */
    public Map<String, String> getFilters() {
        return filters;
    }

    /**
     * @return the datatables
     */
    public String[] getDatatables() {
        return datatables;
    }

    public String[] getColumns() {
        return columns;
    }
}
