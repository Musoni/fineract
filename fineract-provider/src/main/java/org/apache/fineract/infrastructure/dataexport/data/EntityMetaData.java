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

import java.util.List;

public class EntityMetaData {
    private final String name;
    private final List<EntityColumnMetaData> columns;
    
    /**
     * @param name
     * @param columns
     */
    private EntityMetaData(final String name, final List<EntityColumnMetaData> columns) {
        this.name = name;
        this.columns = columns;
    }
    
    /**
     * Creates a new {@link EntityMetaData} object
     * 
     * @param name
     * @param columns
     * @return {@link EntityMetaData} object
     */
    public static EntityMetaData newInstance(final String name, final List<EntityColumnMetaData> columns) {
        return new EntityMetaData(name, columns);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the columns
     */
    public List<EntityColumnMetaData> getColumns() {
        return columns;
    }
}
