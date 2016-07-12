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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

@Entity
@Table(name = "x_registered_table")
public class RegisteredTable extends AbstractPersistable<Long> {

    @Column(name="registered_table_name", nullable = false,unique=true)
    private String registeredTableName;

    @Column(name = "application_table_name", nullable = false)
    private String applicationTableName;

    @Column(name = "category")
    private Integer category;

    @Column(name = "display_name")
    private String displayName;

    @Column(name="system_defined")
    private boolean systemDefined;

    public RegisteredTable() {
    }

    public Integer getCategory() {
        return this.category;
    }

    public String getApplicationTableName() {
        return this.applicationTableName;
    }

    public String getRegisteredTableName() {
        return this.registeredTableName;
    }

    public void updateCategory(final Integer category){
        this.category = category;
    }

    public boolean isSystemDefined() {return this.systemDefined;}

    public void updateDisplayName(final String displayName){

        if(!this.displayName.equals(displayName)){
            this.displayName = displayName;
        }
    }
    public String getDisplayName(){
        return this.displayName;
    }
}
