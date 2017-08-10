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
package org.apache.fineract.portfolio.loanaccount.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.dataqueries.data.GenericResultsetData;
import org.apache.fineract.infrastructure.dataqueries.data.ReportData;
import org.apache.fineract.infrastructure.dataqueries.data.ResultsetRowData;
import org.apache.fineract.infrastructure.dataqueries.service.GenericDataService;
import org.apache.fineract.infrastructure.dataqueries.service.ReadReportingService;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.creditcheck.data.CreditCheckData;
import org.apache.fineract.portfolio.creditcheck.data.CreditCheckEnumerations;
import org.apache.fineract.portfolio.creditcheck.service.CreditCheckDropdownReadPlatformService;
import org.apache.fineract.portfolio.creditcheck.service.CreditCheckReadPlatformService;
import org.apache.fineract.portfolio.creditcheck.service.CreditCheckReportParamReadPlatformService;
import org.apache.fineract.portfolio.loanaccount.data.LoanAccountData;
import org.apache.fineract.portfolio.loanaccount.data.LoanCreditCheckData;
import org.apache.fineract.portfolio.loanaccount.data.LoanCreditCheckGenericResultsetData;
import org.apache.fineract.portfolio.loanaccount.data.LoanStatusEnumData;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanCreditCheckHelper;
import org.apache.fineract.portfolio.loanaccount.domain.LoanStatus;
import org.apache.fineract.portfolio.loanaccount.exception.LoanCreditCheckNotFoundException;
import org.apache.fineract.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class LoanCreditCheckReadPlatformServiceImpl implements LoanCreditCheckReadPlatformService {
    private final JdbcTemplate jdbcTemplate;
    private final CreditCheckDropdownReadPlatformService creditCheckDropdownReadPlatformService;
    private final CreditCheckReadPlatformService creditCheckReadPlatformService;
    private final ReadReportingService readReportingService;
    private final LoanCreditCheckHelper loanCreditCheckHelper;
    private final PlatformSecurityContext platformSecurityContext;
    
    @Autowired
    public LoanCreditCheckReadPlatformServiceImpl(final RoutingDataSource dataSource, 
            final CreditCheckDropdownReadPlatformService creditCheckDropdownReadPlatformService, 
            final CreditCheckReadPlatformService creditCheckReadPlatformService, 
            final ReadReportingService readReportingService, 
            final CreditCheckReportParamReadPlatformService creditCheckReportParamReadPlatformService, 
            final GenericDataService genericDataService, 
            final PlatformSecurityContext platformSecurityContext) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.creditCheckDropdownReadPlatformService = creditCheckDropdownReadPlatformService;
        this.creditCheckReadPlatformService = creditCheckReadPlatformService;
        this.readReportingService = readReportingService;
        this.loanCreditCheckHelper = new LoanCreditCheckHelper(genericDataService, creditCheckReportParamReadPlatformService);
        this.platformSecurityContext = platformSecurityContext;
    }

    @Override
    public Collection<LoanCreditCheckData> retrieveLoanCreditChecks(Long loanId) {
        final LoanCreditCheckMapper mapper = new LoanCreditCheckMapper();
        final String sql = "select " + mapper.loanCreditCheckSchema() + " where lcc.loan_id = ? and lcc.is_deleted = 0";
        
        return this.jdbcTemplate.query(sql, mapper, new Object[] { loanId });
    }

    @Override
    public LoanCreditCheckData retrieveLoanCreditCheckEnumOptions() {
        final List<EnumOptionData> severityLevelOptions = this.creditCheckDropdownReadPlatformService.retrieveSeverityLevelOptions();
        
        return LoanCreditCheckData.instance(severityLevelOptions);
    }

    @Override
    public LoanCreditCheckData retrieveLoanCreditCheck(Long loanId, Long loanCreditCheckId) {
        try {
            final LoanCreditCheckMapper mapper = new LoanCreditCheckMapper();
            final String sql = "select " + mapper.loanCreditCheckSchema() + " where lcc.loan_id = ? and lcc.id = ? and lcc.is_deleted = 0";
            
            return this.jdbcTemplate.queryForObject(sql, mapper, new Object[] { loanId, loanCreditCheckId });
        }
        
        catch (final EmptyResultDataAccessException e) {
            throw new LoanCreditCheckNotFoundException(loanCreditCheckId);
        }
    }
    
    private static final class LoanCreditCheckMapper implements RowMapper<LoanCreditCheckData> {
        
        public String loanCreditCheckSchema() {
            return "lcc.id, lcc.credit_check_id as creditCheckId, cc.name as name, "
                    + "lcc.expected_result as expectedResult, lcc.actual_result as actualResult, "
                    + "lcc.severity_level_enum_value as severityLevelEnumValue, "
                    + "lcc.message, lcc.is_deleted as isDeleted, lcc.sqlStatement as sqlStatement "
                    + "from m_loan_credit_check lcc "
                    + "inner join m_credit_check cc "
                    + "on cc.id = lcc.credit_check_id";
        }

        @Override
        public LoanCreditCheckData mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Long id = rs.getLong("id");
            final Long creditCheckId = rs.getLong("creditCheckId");
            final String name = rs.getString("name");
            final String expectedResult = rs.getString("expectedResult");
            final String actualResult = rs.getString("actualResult");
            final Integer severityLevelEnumValue = JdbcSupport.getInteger(rs, "severityLevelEnumValue");
            final String message = rs.getString("message");
            final boolean isDeleted = rs.getBoolean("isDeleted");
            final String sqlStatement = rs.getString("sqlStatement");
            EnumOptionData severityLevelEnum = null;
            
            if (severityLevelEnumValue != null) {
                severityLevelEnum = CreditCheckEnumerations.severityLevel(severityLevelEnumValue);
            }
            
            return LoanCreditCheckData.instance(id, creditCheckId, name, expectedResult, actualResult, 
                    severityLevelEnum, message, isDeleted, sqlStatement);
        }
    }
    
    @Override
    public Collection<LoanCreditCheckData> triggerLoanCreditChecks(final LoanAccountData loanAccountData) {
        final AppUser appUser = this.platformSecurityContext.authenticatedUser();
        final Long loanId = loanAccountData.getId();
        Collection<LoanCreditCheckData> loanCreditCheckDataList = new ArrayList<>();
        final LoanStatusEnumData loanStatusEnumData = loanAccountData.getStatus();

        if ((loanStatusEnumData != null) && (loanStatusEnumData.isPendingApprovalOrPendingDisbursement())) {
            loanCreditCheckDataList = triggerLoanCreditChecks(loanId, loanAccountData.loanProductId(), appUser.getId(), loanAccountData.isGroupLoan());
        }
        
        return loanCreditCheckDataList;
    }
    
    @Override
    public Collection<LoanCreditCheckData> triggerLoanCreditChecks(final Loan loan) {
        final AppUser appUser = this.platformSecurityContext.authenticatedUser();
        Collection<LoanCreditCheckData> loanCreditCheckDataList = new ArrayList<>();
        final LoanStatus loanStatus = LoanStatus.fromInt(loan.getStatus());
        
        if ((loanStatus != null) && (loanStatus.isPendingApprovalOrPendingDisbursement())) {
            loanCreditCheckDataList = triggerLoanCreditChecks(loan.getId(), loan.productId(), appUser.getId(), loan.isGroupLoan());
        }
        
        return loanCreditCheckDataList;
    }
    
    /** 
     * Run all credit check SQLs, creating a list of loan credit check data objects 
     * 
     * @param loanId -- the identifier of the loan to be checked
     * @param loanProductId -- loan product identifier
     * @param appUserId -- the identifier of the app user making the request
     * @return collection of loan credit check data
     **/
    private Collection<LoanCreditCheckData> triggerLoanCreditChecks(final Long loanId, final Long loanProductId, 
            final Long appUserId, final Boolean isGroupLoan) {
        final Collection<LoanCreditCheckData> loanCreditCheckDataList = new ArrayList<>();
        final Collection<CreditCheckData> creditCheckDataList = this.creditCheckReadPlatformService.retrieveLoanProductCreditChecks(loanProductId);
        
        if (creditCheckDataList != null && !creditCheckDataList.isEmpty()) {
            for (CreditCheckData creditCheckData : creditCheckDataList) {
                ReportData reportData = this.readReportingService.retrieveReport(creditCheckData.getStretchyReportId());
                String creditCheckResult = null;
                final LoanCreditCheckGenericResultsetData loanCreditCheckGenericResultsetData = this.loanCreditCheckHelper.
                        retrieveGenericResultsetForCreditCheck(reportData, loanId, appUserId, isGroupLoan);
                final GenericResultsetData genericResultsetData = loanCreditCheckGenericResultsetData.getGenericResultsetData();
                final List<ResultsetRowData> resultsetData = genericResultsetData.getData();
                List<String> resultsetRow = null;
                final String sqlStatement = loanCreditCheckGenericResultsetData.getSqlStatement();
                
                if (resultsetData != null && (resultsetData.get(0) != null)) {
                    resultsetRow = resultsetData.get(0).getRow();
                    creditCheckResult = resultsetRow.get(0);
                }
                
                final Long creditCheckId = creditCheckData.getId();
                final String creditCheckName = creditCheckData.getName();
                final String expectedResult = creditCheckData.getExpectedResult();
                final EnumOptionData severityLevel = creditCheckData.getSeverityLevel();
                final String message = creditCheckData.getMessage();
                
                LoanCreditCheckData loanCreditCheckData = LoanCreditCheckData.instance(null, creditCheckId, creditCheckName, expectedResult, 
                        creditCheckResult, severityLevel, message, false, sqlStatement);
                
                loanCreditCheckDataList.add(loanCreditCheckData);
            }
        }
        
        return loanCreditCheckDataList;
    }
}
