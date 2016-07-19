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

public enum CreditCheckSeverityLevel {
    INVALID(0, "creditCheckSeverityLevel.invalid"),
    ERROR(1, "creditCheckSeverityLevel.error"),
    WARNING(2, "creditCheckSeverityLevel.warning"),
    NOTICE(3, "creditCheckSeverityLevel.notice");
    
    private final Integer value;
    private final String code;
    
    public static CreditCheckSeverityLevel fromInt(final Integer levelValue) {
        CreditCheckSeverityLevel severityLevel = CreditCheckSeverityLevel.INVALID;
        
        switch (levelValue) {
            case 1:
                severityLevel = CreditCheckSeverityLevel.ERROR;
                break;
                
            case 2:
                severityLevel = CreditCheckSeverityLevel.WARNING;
                break;
                
            case 3:
                severityLevel = CreditCheckSeverityLevel.NOTICE;
                break;
        }
        
        return severityLevel;
    }

    private CreditCheckSeverityLevel(final Integer value, final String code) {
        this.value = value;
        this.code = code;
    }

    /**
     * @return the value
     */
    public Integer getValue() {
        return value;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }
    
    /** 
     * @return true/false 
     **/
    public boolean isError() {
        return this.value.equals(CreditCheckSeverityLevel.ERROR.getValue());
    }
    
    /** 
     * @return true/false 
     **/
    public boolean isWarning() {
        return this.value.equals(CreditCheckSeverityLevel.WARNING.getValue());
    }
    
    /** 
     * @return true/false 
     **/
    public boolean isNotice() {
        return this.value.equals(CreditCheckSeverityLevel.NOTICE.getValue());
    }
    
    public static Object[] validValues() {
        return new Object[] { CreditCheckSeverityLevel.ERROR.getValue(), CreditCheckSeverityLevel.WARNING.getValue(), 
                CreditCheckSeverityLevel.NOTICE.getValue()};
    }
}
