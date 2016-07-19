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
package org.apache.fineract.infrastructure.dataqueries.service;

import org.apache.fineract.infrastructure.core.domain.JdbcSupport;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.infrastructure.dataqueries.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class EntityDatatableChecksReadPlatformServiceImpl implements EntityDatatableChecksReadService {

    private final JdbcTemplate jdbcTemplate;
    private final RegisterDataTableMapper registerDataTableMapper;
    private  final EntityDataTableChecksMapper entityDataTableChecksMapper;

    @Autowired
    public EntityDatatableChecksReadPlatformServiceImpl(final RoutingDataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.registerDataTableMapper = new RegisterDataTableMapper();
        this.entityDataTableChecksMapper = new EntityDataTableChecksMapper();
    }

    @Override
    public List<EntityDataTableChecksData> retrieveAll (final Long status){


        String sql = "select " + this.entityDataTableChecksMapper.schema();

        if(status !=null) sql +=" where status_enum ="+ status;

        return this.jdbcTemplate.query(sql, this.entityDataTableChecksMapper);

    }

    @Override
    public EntityDataTableChecksTemplateData retrieveTemplate (){

        List<DatatableChecksData> dataTables = getDataTables();
        List<String> entities = EntityTables.getEntitiesList();
        List<DatatableCheckStatusData> status = StatusEnum.getStatusList();

        return new EntityDataTableChecksTemplateData(entities,status,dataTables);

    }

    private List<DatatableChecksData> getDataTables(){
        final String sql = "select " + this.registerDataTableMapper.schema();

        return this.jdbcTemplate.query(sql, this.registerDataTableMapper);
    }


    protected static final class RegisterDataTableMapper implements RowMapper<DatatableChecksData> {

        @Override
        public DatatableChecksData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = JdbcSupport.getLong(rs, "id");
            final String tableName = rs.getString("tableName");

            return new DatatableChecksData(id, tableName);
        }

        public String schema() {
            return " t.id as id, t.registered_table_name as tableName from x_registered_table t where application_table_name IN( 'm_client','m_group','m_savings_account','m_loan')";
        }
    }

    protected static final class EntityDataTableChecksMapper implements RowMapper<EntityDataTableChecksData> {

        @Override
        public EntityDataTableChecksData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

            final Long id = JdbcSupport.getLong(rs, "id");
            final String entity = rs.getString("entity");
            final Long status = rs.getLong("status");
            final String datatableName = rs.getString("datatableName");
            final String displayName = rs.getString("displayName");
            final boolean systemDefined = rs.getBoolean("systemDefined");

            return new EntityDataTableChecksData(id,entity,status,datatableName,systemDefined,displayName);
        }

        public String schema() {
            return " rt.display_name as displayName, t.id as id,t.application_table_name as entity, t.status_enum as status, t.system_defined as systemDefined, rt.registered_table_name as datatableName from m_entity_datatable_check as t join x_registered_table rt on rt.id = t.x_registered_table_id";
        }
    }


}