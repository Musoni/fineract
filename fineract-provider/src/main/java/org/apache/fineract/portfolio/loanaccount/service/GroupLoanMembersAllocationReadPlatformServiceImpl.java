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

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.client.data.ClientData;
import org.apache.fineract.portfolio.loanaccount.data.GroupLoanMembersAllocationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class GroupLoanMembersAllocationReadPlatformServiceImpl implements GroupLoanMembersAllocationReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformSecurityContext context;

    @Autowired
    public GroupLoanMembersAllocationReadPlatformServiceImpl(
            final PlatformSecurityContext context, final RoutingDataSource dataSource) {
        this.context = context;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class GroupLoanMembersAllocationMapper implements RowMapper<GroupLoanMembersAllocationData> {

        private final StringBuilder sqlBuilder = new StringBuilder(
                "al.id as id, al.amount as amount, al.loan_id as loanId, al.client_id as clientId, ")
        .append("c.firstname as firstname, c.account_no as accountNo, c.middlename as middlename, c.lastname as lastname, ")
        .append("c.fullname as fullname, c.display_name as displayName ")
                .append(" FROM m_group_loan_member_allocation al") //
                .append(" JOIN m_client c on c.id = al.client_id");

        public String schema() {
            return this.sqlBuilder.toString();
        }

        @Override
        public GroupLoanMembersAllocationData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = rs.getLong("id");
            final Long loanId = rs.getLong("loanId");
            final Long clientId = rs.getLong("clientId");
            final BigDecimal amount = JdbcSupport.getBigDecimalDefaultToNullIfZero(rs, "amount");
            final String displayName = rs.getString("displayName");
            final String accountNo = rs.getString("accountNo");

            final ClientData clientData =  ClientData.instance(accountNo, null, null, null, null, null, null, clientId, null, null, null, null, displayName, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);

            return  GroupLoanMembersAllocationData.newOne(id, loanId, clientData, amount);

        }
    }

    @Override
    public List<GroupLoanMembersAllocationData> retrieveGroupLoanMembersAllocation (final Long loanId) {
        this.context.authenticatedUser();

        final GroupLoanMembersAllocationMapper rm = new GroupLoanMembersAllocationMapper();

        final String sql = "select " + rm.schema() + " where al.loan_id=? order by id ASC";

        return this.jdbcTemplate.query(sql, rm, new Object[] { loanId });
    }


}