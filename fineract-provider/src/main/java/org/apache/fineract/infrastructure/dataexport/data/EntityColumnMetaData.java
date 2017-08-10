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

public class EntityColumnMetaData {
    private final String name;
    private String label;
    private final String type;
    private final boolean isNullable;
    
    /**
     * @param name
     * @param label
     * @param type
     * @param isNullable
     */
    private EntityColumnMetaData(final String name, final String label, final String type, 
            final boolean isNullable) {
        this.name = name;
        this.label = label;
        this.type = type;
        this.isNullable = isNullable;
    }
    
    /**
     * Creates a new {@link EntityColumnMetaData} object
     * 
     * @param name
     * @param label
     * @param type
     * @param isNullable
     * @return {@link EntityColumnMetaData} object
     */
    public static EntityColumnMetaData newInstance(final String name, final String label, final String type, 
            final boolean isNullable) {
        return new EntityColumnMetaData(name, label, type, isNullable);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the isNullable
     */
    public boolean isNullable() {
        return isNullable;
    }
    
    /**
     * Updates the value of the label property
     * 
     * @param label
     */
    public void updateLabel(final String label) {
    	this.label = label;
    }
}
