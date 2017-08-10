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

import org.apache.fineract.infrastructure.dataqueries.data.GenericResultsetData;
import org.apache.fineract.infrastructure.dataqueries.data.ReportData;
import org.apache.fineract.infrastructure.dataqueries.service.GenericDataService;
import org.apache.fineract.portfolio.creditcheck.CreditCheckConstants;
import org.apache.fineract.portfolio.creditcheck.data.CreditCheckReportParamData;
import org.apache.fineract.portfolio.creditcheck.service.CreditCheckReportParamReadPlatformService;
import org.apache.fineract.portfolio.loanaccount.data.LoanCreditCheckGenericResultsetData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * Loan credit check helper class 
 **/
public class LoanCreditCheckHelper {
    private GenericDataService genericDataService = null;
    private CreditCheckReportParamReadPlatformService creditCheckReportParamReadPlatformService = null;
    private final static Logger logger = LoggerFactory.getLogger(LoanCreditCheckHelper.class);

    public LoanCreditCheckHelper(final GenericDataService genericDataService, 
            final CreditCheckReportParamReadPlatformService creditCheckReportParamReadPlatformService) { 
        this.genericDataService = genericDataService;
        this.creditCheckReportParamReadPlatformService = creditCheckReportParamReadPlatformService;
    }
    
    /** 
     * @param stretchyReport -- Report object
     * @param loanId -- loan identifier
     * @param userId -- app user identifier
     * @return GenericResultsetData object
     **/
    public LoanCreditCheckGenericResultsetData retrieveGenericResultsetForCreditCheck(final ReportData reportData, final Long loanId, 
            final Long userId, final Boolean isGroupLoan) {
        final long startTime = System.currentTimeMillis();
        logger.info("STARTING REPORT: " + reportData.getReportName() + "   Type: " + reportData.getReportType());
        
        final String sqlStatement = searchAndReplaceParamsInSQLString(loanId, userId, reportData.getReportSql(), isGroupLoan);
        final GenericResultsetData genericResultsetData = this.genericDataService.fillGenericResultSet(sqlStatement);
        
        logger.info("SQL: " + sqlStatement);
        
        final long elapsed = System.currentTimeMillis() - startTime;
        logger.info("FINISHING REPORT: " + reportData.getReportName() + " - " + reportData.getReportType() 
                + "     Elapsed Time: " + elapsed + " milliseconds");
        
        return LoanCreditCheckGenericResultsetData.instance(genericResultsetData, sqlStatement);
    }
    
    /** 
     * @param loanId -- loan identifier
     * @param userId -- app user identifier
     * @param sql -- the initial SQL string containing variables
     * @return SQL string with variables replaced by string values 
     **/
    private String searchAndReplaceParamsInSQLString(final Long loanId, final Long userId, String sql, final Boolean isGroupLoan) {
        CreditCheckReportParamData creditCheckReportParamData = this.creditCheckReportParamReadPlatformService
                .retrieveCreditCheckReportParameters(loanId, userId, isGroupLoan);
        
        sql = this.genericDataService.replace(sql, CreditCheckConstants.CLIENT_ID_PARAM_PATTERN, 
                creditCheckReportParamData.getClientId().toString());
        sql = this.genericDataService.replace(sql, CreditCheckConstants.GROUP_ID_PARAM_PATTERN, 
                creditCheckReportParamData.getGroupId().toString());
        sql = this.genericDataService.replace(sql, CreditCheckConstants.LOAN_ID_PARAM_PATTERN, 
                creditCheckReportParamData.getLoanId().toString());
        sql = this.genericDataService.replace(sql, CreditCheckConstants.USER_ID_PARAM_PATTERN, 
                creditCheckReportParamData.getUserId().toString());
        sql = this.genericDataService.replace(sql, CreditCheckConstants.STAFF_ID_PARAM_PATTERN, 
                creditCheckReportParamData.getStaffId().toString());
        sql = this.genericDataService.replace(sql, CreditCheckConstants.OFFICE_ID_PARAM_PATTERN, 
                creditCheckReportParamData.getOfficeId().toString());
        sql = this.genericDataService.replace(sql, CreditCheckConstants.PRODUCT_ID_PARAM_PATTERN, 
                creditCheckReportParamData.getProductId().toString());
        
        return this.genericDataService.wrapSQL(sql);
    }
}
