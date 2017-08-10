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

public enum DataExportCoreDatatable {
	GUARANTORS("m_guarantor", "Loan Guarantors", DataExportBaseEntity.LOAN),
	LOAN_CHARGES("m_loan_charge", "Loan Charges", DataExportBaseEntity.LOAN), 
	SAVINGS_ACCOUNT_CHARGES("m_savings_account_charge", "Savings Account Charges", DataExportBaseEntity.SAVINGS_ACCOUNT), 
	LOAN_COLLATERALS("m_loan_collateral", "Loan Collateral", DataExportBaseEntity.LOAN);
	
	private final String tableName;
	private final String displayName;
	private final DataExportBaseEntity baseEntity;
	
	/**
	 * @param baseEntity
	 * @param tableName
	 * @param displayName
	 */
	private DataExportCoreDatatable(final String tableName, final String displayName, 
			final DataExportBaseEntity baseEntity) {
		this.baseEntity = baseEntity;
		this.tableName = tableName;
		this.displayName = displayName;
	}
	
	/**
	 * Creates a new {@link DataExportCoreDatatable} object
	 * 
	 * @param tableName
	 * @return {@link DataExportCoreDatatable} enum object
	 */
	public static DataExportCoreDatatable newInstance(final String tableName) {
		DataExportCoreDatatable dataExportCoreDatatable = null;
		
		for (DataExportCoreDatatable datatable : DataExportCoreDatatable.values()) {
			if (datatable.tableName.equalsIgnoreCase(tableName)) {
				dataExportCoreDatatable = datatable;
				
				break;
			}
		}
		
		return dataExportCoreDatatable;
	}

	/**
	 * @return the baseEntity
	 */
	public DataExportBaseEntity getBaseEntity() {
		return baseEntity;
	}

	/**
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
}
