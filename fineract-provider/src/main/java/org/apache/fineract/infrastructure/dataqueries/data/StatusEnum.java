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

public enum StatusEnum {

    CREATE("create",100),
    APPROVE("approve",200),
    ACTIVATE("activate",300),
    WITHDRAWN("withdraw",400),
    REJECTED("reject",500),
    CLOSE("close",600),
    WRITE_OFF("write off",601),
    RESCHEDULE("reschedule",602),
    OVERPAY("overpay",700);

    private String name;

    public Integer getCode() {
        return code;
    }

    private Integer code;

    private StatusEnum(String name, Integer code){

        this.name = name;
        this.code = code;

    }

    public static List<DatatableCheckStatusData> getStatusList(){

        List<DatatableCheckStatusData> data = new ArrayList<DatatableCheckStatusData>();

        for(StatusEnum status : StatusEnum.values()){
            data.add(new DatatableCheckStatusData(status.name,status.code));
        }

        return data;

    }

}
