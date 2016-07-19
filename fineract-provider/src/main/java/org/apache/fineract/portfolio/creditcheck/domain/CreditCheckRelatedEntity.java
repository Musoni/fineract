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
package org.apache.fineract.portfolio.creditcheck.domain;

public enum CreditCheckRelatedEntity {

    INVALID(0, "creditCheckRelatedEntity.invalid"),
    LOAN(1, "creditCheckRelatedEntity.loan"),
    SAVINGS(2, "creditCheckRelatedEntity.savings");

    private final Integer value;
    private final String code;

    private CreditCheckRelatedEntity(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    public Integer getValue() {
        return this.value;
    }

    public String getCode() {
        return this.code;
    }

    public static CreditCheckRelatedEntity fromInt(final Integer creditCheckRelatedEntity) {
        CreditCheckRelatedEntity appliesToType = INVALID;

        if (creditCheckRelatedEntity != null) {
            switch (creditCheckRelatedEntity) {
                case 1:
                    appliesToType = LOAN;
                break;
                
                case 2:
                    appliesToType = SAVINGS;
                break;
                
                default:
                    appliesToType = INVALID;
                break;
            }
        }

        return appliesToType;
    }

    public boolean isLoanCreditCheck() {
        return this.value.equals(CreditCheckRelatedEntity.LOAN.getValue());
    }

    public boolean isSavingsCreditCheck() {
        return this.value.equals(CreditCheckRelatedEntity.SAVINGS.getValue());
    }

    public static Object[] validValues() {
        return new Object[] { CreditCheckRelatedEntity.LOAN.getValue(), CreditCheckRelatedEntity.SAVINGS.getValue() };
    }
}