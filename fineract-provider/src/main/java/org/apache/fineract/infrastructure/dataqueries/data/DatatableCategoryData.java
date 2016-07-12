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
package org.apache.fineract.infrastructure.dataqueries.data;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Cieyou on 2/19/2015.
 */
public class DatatableCategoryData {

    private final Long id;
    private final String category;
    private final List<String> datatables;

    private final static Logger logger = LoggerFactory.getLogger(DatatableData.class);

    public DatatableCategoryData(final Long id, final String name, final List<String> tables){

        this.id = id;
        this.category = name;
        this.datatables = tables;
    }

    public static DatatableCategoryData datatableCategoryData(final Long id, final String name){

        return new DatatableCategoryData(id,name,new ArrayList<String>());
    }

    public void addTable(String table){
        datatables.add(table);
    }

    public Long getId(){return this.id;}
}
