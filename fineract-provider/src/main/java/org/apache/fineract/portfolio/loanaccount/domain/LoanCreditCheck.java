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
package org.apache.fineract.portfolio.loanaccount.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.fineract.portfolio.creditcheck.domain.CreditCheck;
import org.springframework.data.jpa.domain.AbstractPersistable;

@SuppressWarnings("serial")
@Entity
@Table(name = "m_loan_credit_check")
public class LoanCreditCheck extends AbstractPersistable<Long> {
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "credit_check_id", nullable = false)
    private CreditCheck creditCheck;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
    
    @Column(name = "expected_result", nullable = false)
    private String expectedResult;
    
    @Column(name = "actual_result", nullable = true)
    private String actualResult;
    
    @Column(name = "severity_level_enum_value", nullable = false)
    private Integer severityLevelEnumValue;
    
    @Column(name = "message", nullable = false)
    private String message;
    
    @Column(name = "sqlStatement", nullable = true)
    private String sqlStatement;
    
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    protected LoanCreditCheck() {}
    
    private LoanCreditCheck(final CreditCheck creditCheck, final Loan loan, final String expectedResult, 
            final String actualResult, final Integer severityLevelEnumValue, final String message, 
            final boolean isDeleted, final String sqlStatement) {
        this.creditCheck = creditCheck;
        this.loan = loan;
        this.expectedResult = expectedResult;
        this.actualResult = actualResult;
        this.severityLevelEnumValue = severityLevelEnumValue;
        this.message = message;
        this.isDeleted = isDeleted;
        this.sqlStatement = sqlStatement;
    }
    
    public static LoanCreditCheck instance(final CreditCheck creditCheck, final Loan loan, final String expectedResult, 
            final String actualResult, final Integer severityLevelEnumValue, final String message, 
            final boolean isDeleted, final String sqlStatement) {
        return new LoanCreditCheck(creditCheck, loan, expectedResult, actualResult, severityLevelEnumValue, 
                message, isDeleted, sqlStatement);
    }
    
    public static LoanCreditCheck instance(final CreditCheck creditCheck) {
        return new LoanCreditCheck(creditCheck, null, creditCheck.getExpectedResult(), null, creditCheck.getSeverityLevelEnumValue(), 
                creditCheck.getMessage(), false, null);
    }
    
    public void update(final Loan loan) {
        this.loan = loan;
    }
    
    /** 
     * @return the creditCheck 
     **/
    public CreditCheck getCreditCheck() {
        return this.creditCheck;
    }
    
    /** 
     * @return the loan 
     **/
    public Loan getLoan() {
        return this.loan;
    }
    
    /** 
     * @return the expectedResult 
     **/
    public String getExpectedResult() {
        return this.expectedResult;
    }
    
    /** 
     * @return the actualResult 
     **/
    public String getActualResult() {
        return this.actualResult;
    }
    
    /** 
     * @return the severityLevelEnumValue 
     **/
    public Integer getSeverityLevelEnumValue() {
        return this.severityLevelEnumValue;
    }
    
    /** 
     * @return the message 
     **/
    public String getMessage() {
        return this.message;
    }
    
    /** 
     * @return the isDeleted 
     **/
    public boolean isDeleted() {
        return this.isDeleted;
    }
    
    /** 
     * @return true if actual result equals expected result 
     **/
    public boolean actualResultEqualsExpectedResult() {
        boolean result = false;
        
        if (this.actualResult != null && this.expectedResult != null) {
            result = this.actualResult.trim().equals(this.expectedResult.trim());
        }
        
        return result;
    }

    /** 
     * @return the SQL statement 
     **/
    public String getSqlStatement() {
        return this.sqlStatement;
    }
    
    /** 
     * Update the "isDelete" property
     * 
     * @param isDeleted -- boolean true/false
     * @return None 
     **/
    public void updateIsDeleted(final boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
