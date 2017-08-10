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

public enum DataExportCoreTable {
	M_OFFICE("m_office"), 
	M_CLIENT("m_client"), 
	M_GROUP("m_group"), 
	M_GROUP_CLIENT("m_group_client"), 
	M_SAVINGS_ACCOUNT("m_savings_account"), 
	M_STAFF("m_staff"), 
	M_CODE_VALUE("m_code_value"), 
	M_CODE("m_code"), 
	M_CHARGE("m_charge"),
	M_LOAN("m_loan"),
	M_LOAN_TRANSACTION("m_loan_transaction"),
	M_SAVINGS_ACCOUNT_TRANSACTION("m_savings_account_transaction"),
	M_LOAN_REPAYMENT_SCHEDULE("m_loan_repayment_schedule"),
	M_PAYMENT_DETAIL("m_payment_detail"),
	M_PRODUCT_LOAN("m_product_loan"),
	M_SAVINGS_PRODUCT("m_savings_product"),
	M_APP_USER("m_appuser"),
	R_ENUM_VALUE("r_enum_value"),
	M_GUARANTOR("m_guarantor"),
	M_PAYMENT_TYPE("m_payment_type"),
	M_LOAN_ARREARS_AGING("m_loan_arrears_aging"),
	M_GROUP_LOAN_MEMBER_ALLOCATION("m_group_loan_member_allocation");
	
	private String name;

	/**
	 * @param name
	 */
	private DataExportCoreTable(final String name) {
		this.name = name;
	}
	
	/**
	 * Creates a new instance of {@link DataExportCoreTable} object
	 * 
	 * @param name table name
	 * @return {@link DataExportCoreTable} object
	 */
	public static DataExportCoreTable newInstance(final String name) {
		DataExportCoreTable dataExportCoreTable = null;
		
		for (DataExportCoreTable coreTable : DataExportCoreTable.values()) {
			if (coreTable.name.equalsIgnoreCase(name)) {
				dataExportCoreTable = coreTable;
				
				break;
			}
		}
		
		return dataExportCoreTable;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Creates a table alias
	 * 
	 * @param aliasPostfixNumber integer that would be appended to the table name
	 * @return
	 */
	public String getAlias(final int aliasPostfixNumber) {
		return this.name + aliasPostfixNumber;
	}
}
