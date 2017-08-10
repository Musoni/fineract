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
package org.apache.fineract.infrastructure.dataqueries.domain;
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

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Entity
@Table(name = "m_entity_datatable_check")

public class EntityDatatableChecks extends AbstractPersistable<Long> {

    @Column(name="application_table_name", nullable = false)
    private String entity;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="x_registered_table_id",insertable=false, updatable=false)
    private RegisteredTable datatable;

    @Column(name="x_registered_table_id", nullable = false)
    private Long datatableId;

    @Column(name = "status_enum",nullable = false)
    private Long status;

    @Column(name="system_defined")
    private boolean systemDefined;


    @Column(name="product_loan_id", nullable = false)
    private Long productLoanId;



    public EntityDatatableChecks() {
    }

    public EntityDatatableChecks(final String entity, final Long datatableId, final Long status, final boolean systemDefined, final Long productLoanId) {

        this.entity = entity;
        this.status = status;
        this.datatableId = datatableId;
        this.systemDefined = systemDefined;
        this.productLoanId = productLoanId;
    }

    public static EntityDatatableChecks fromJson(final JsonCommand command){

        final String entity = command.stringValueOfParameterNamed("entity");
        final Long status = command.longValueOfParameterNamed("status");
        final Long datatableId=command.longValueOfParameterNamed("datatableId");

        boolean systemDefined = false;

        if(command.parameterExists("systemDefined")){
             systemDefined = command.booleanObjectValueOfParameterNamed("systemDefined");
        }else{
            systemDefined = false;
        }

        Long productLoanId =null;

        if(command.parameterExists("productId")){
           productLoanId = command.longValueOfParameterNamed("productId");
        }

        return new EntityDatatableChecks(entity,datatableId,status,systemDefined,productLoanId);

    }

    public String getEntity() {
        return this.entity;
    }

    public Long getStatus() {
        return this.status;
    }

    public Long getDatatableId() {
        return this.datatableId;
    }

    public boolean isSystemDefined() {return this.systemDefined;}

    public RegisteredTable getDatatable(){return this.datatable;}

    public Long getProductLoanId() {
        return productLoanId;
    }
}
