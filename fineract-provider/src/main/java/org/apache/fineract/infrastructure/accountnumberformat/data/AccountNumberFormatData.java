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
package org.apache.fineract.infrastructure.accountnumberformat.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;

public class AccountNumberFormatData implements Serializable {

    private final Long id;

    private final EnumOptionData accountType;
    private final EnumOptionData prefixType;

    // template options
    private List<EnumOptionData> accountTypeOptions;
    private Map<String, List<EnumOptionData>> prefixTypeOptions;

    private final String customPattern;
    private  final Integer zeroPadding;

    private Map<String,Set<String>> customPrefixOptions;


    public AccountNumberFormatData(final Long id, final EnumOptionData accountType, final EnumOptionData prefixType,
                                   final String customPattern, final Integer zeroPadding,final Map<String,Set<String>> customPrefixOptions) {
        this(id, accountType, prefixType, null, null,customPattern,zeroPadding,customPrefixOptions);
    }

    public AccountNumberFormatData(final List<EnumOptionData> accountTypeOptions, Map<String, List<EnumOptionData>> prefixTypeOptions,Map<String,Set<String>> customPrefixOptions) {
        this(null, null, null, accountTypeOptions, prefixTypeOptions,null,null,customPrefixOptions);
    }

    public void templateOnTop(List<EnumOptionData> accountTypeOptions, Map<String, List<EnumOptionData>> prefixTypeOptions,Map<String,Set<String>> customPrefixOptions ) {
        this.accountTypeOptions = accountTypeOptions;
        this.prefixTypeOptions = prefixTypeOptions;
        this.customPrefixOptions = customPrefixOptions;
    }

    private AccountNumberFormatData(final Long id, final EnumOptionData accountType, final EnumOptionData prefixType,
            final List<EnumOptionData> accountTypeOptions, Map<String, List<EnumOptionData>> prefixTypeOptions,
            final String customPattern, final Integer zeroPadding,final Map<String,Set<String>> customPrefixOptions) {
        this.id = id;
        this.accountType = accountType;
        this.prefixType = prefixType;
        this.accountTypeOptions = accountTypeOptions;
        this.prefixTypeOptions = prefixTypeOptions;
        this.customPattern = customPattern;
        this.zeroPadding  =zeroPadding;
        this.customPrefixOptions = customPrefixOptions;
    }

    public Long getId() {
        return this.id;
    }

    public EnumOptionData getAccountType() {
        return this.accountType;
    }

    public EnumOptionData getPrefixType() {
        return this.prefixType;
    }

    public List<EnumOptionData> getAccountTypeOptions() {
        return this.accountTypeOptions;
    }

    public Map<String, List<EnumOptionData>> getPrefixTypeOptions() {
        return this.prefixTypeOptions;
    }

    public String getCustomPattern() {return this.customPattern;}

    public Integer getZeroPadding() {return this.zeroPadding;}
}
