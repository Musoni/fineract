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
package org.apache.fineract.portfolio.loanaccount.data;

import org.apache.fineract.infrastructure.dataqueries.data.GenericResultsetData;

/**  
 * immutable object that represents a loan credit check generic resultset data
 **/
public class LoanCreditCheckGenericResultsetData {
    private final GenericResultsetData genericResultsetData;
    private final String sqlStatement;

    /** 
     * LoanCreditCheckGenericResultsetData constructor
     * 
     * @return None
     **/
    private LoanCreditCheckGenericResultsetData(final GenericResultsetData genericResultsetData, 
            final String sqlStatement) {
        this.genericResultsetData = genericResultsetData;
        this.sqlStatement = sqlStatement;
    }
    
    /**
     * @param sql -- SQL string with variables replaced by string values
     * @param genericResultsetData -- GenericResultsetData object
     * @return LoanCreditCheckGenericResultsetData object
     **/
    public static LoanCreditCheckGenericResultsetData instance(final GenericResultsetData genericResultsetData, 
            final String sqlStatement) {
        return new LoanCreditCheckGenericResultsetData(genericResultsetData, sqlStatement);
    }

    /**
     * @return the genericResultsetData
     */
    public GenericResultsetData getGenericResultsetData() {
        return genericResultsetData;
    }

    /**
     * @return the SQL statement
     */
    public String getSqlStatement() {
        return sqlStatement;
    }
}
