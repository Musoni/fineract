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

import org.apache.commons.lang.StringUtils;

/**
 * @see https://en.wikipedia.org/wiki/Foreign_key
 */
public class DataExportSqlJoin {
	private String id;
	private final DataExportCoreTable parentTable;
	private final DataExportCoreTable childTable;
	private final String sqlStatement;
	private final String parentTableAlias;
	private final String childTableAlias;
	
	/**
	 * @param id
	 * @param parentTable The table containing the candidate key
	 * @param childTable The table containing the foreign key
	 * @param sqlStatement
	 * @param parentTableAlias
	 * @param childTableAlias
	 */
	private DataExportSqlJoin(final String id, final DataExportCoreTable parentTable, 
			final DataExportCoreTable childTable, final String sqlStatement, 
			final String parentTableAlias, final String childTableAlias) {
		this.id = id;
		this.parentTable = parentTable;
		this.childTable = childTable;
		this.sqlStatement = sqlStatement;
		this.parentTableAlias = parentTableAlias;
		this.childTableAlias = childTableAlias;
	}
	
	/**
	 * Creates a new {@link DataExportSqlJoin} object
	 * 
	 * @param parentTable
	 * @param childTable
	 * @param sqlStatement
	 * @param parentTableAlias
	 * @param childTableAlias
	 * @return {@link DataExportSqlJoin} object
	 */
	public static final DataExportSqlJoin newInstance(final DataExportCoreTable parentTable, 
			final DataExportCoreTable childTable, final String sqlStatement, final String parentTableAlias, 
			final String childTableAlias) {
		final String id = createId(parentTable, childTable);
		
		return new DataExportSqlJoin(id, parentTable, childTable, sqlStatement, 
				parentTableAlias, childTableAlias);
	}
	
	/**
	 * Creates a new {@link DataExportSqlJoin} object
	 * 
	 * @param parentTable
	 * @param childTableName
	 * @param sqlStatement
	 * @param parentTableAlias
	 * @param childTableAlias
	 * @return {@link DataExportSqlJoin} object
	 */
	public static final DataExportSqlJoin newInstance(final DataExportCoreTable parentTable, 
			final String childTableName, final String sqlStatement, final String parentTableAlias, 
			final String childTableAlias) {
		final String id = createId(parentTable.getName(), childTableName);
		
		return new DataExportSqlJoin(id, parentTable, null, sqlStatement, 
				parentTableAlias, childTableAlias);
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the parentTable
	 */
	public DataExportCoreTable getParentTable() {
		return parentTable;
	}

	/**
	 * @return the childTable
	 */
	public DataExportCoreTable getChildTable() {
		return childTable;
	}

	/**
	 * @return the sqlStatement
	 */
	public String getSqlStatement() {
		return sqlStatement;
	}

	/**
	 * @return the parentTableAlias
	 */
	public String getParentTableAlias() {
		return parentTableAlias;
	}

	/**
	 * @return the childTableAlias
	 */
	public String getChildTableAlias() {
		return childTableAlias;
	}
	
	/**
	 * Creates the object id by concatenating the parentTable and the childTable values
	 * 
	 * @param parentTable The table containing the candidate key
	 * @param childTable The table containing the foreign key
	 * @return id
	 */
	public static String createId(final DataExportCoreTable parentTable, 
			final DataExportCoreTable childTable) {
		return createId(parentTable.getName(), childTable.getName());
	}
	
	/**
	 * Creates an id string by concatenating the parent table name and child table name
	 * 
	 * @param parentTableName
	 * @param childTableName
	 * @return id
	 */
	public static String createId(final String parentTableName, final String childTableName) {
		return parentTableName + "_" + childTableName;
	}
	
	/**
	 * Updates the id of the object
	 * 
	 * @param id
	 */
	public void updateId(final String id) {
		if (StringUtils.isNotBlank(id)) {
			this.id = id;
		}
	}
}
