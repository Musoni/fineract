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
package org.apache.fineract.portfolio.creditcheck.data;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckRelatedEntity;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckSeverityLevel;

public class CreditCheckEnumerations {
    
    public static EnumOptionData severityLevel(final Integer severityLevel) {
        return severityLevel(CreditCheckSeverityLevel.fromInt(severityLevel));
    }

    public static EnumOptionData severityLevel(final CreditCheckSeverityLevel severityLevel) {
        EnumOptionData enumData = null;
        
        if (severityLevel != null) {
            switch (severityLevel) {
                case ERROR:
                    enumData = new EnumOptionData(severityLevel.getValue().longValue(), severityLevel.getCode(), 
                            "Error");
                    break;
                    
                case WARNING:
                    enumData = new EnumOptionData(severityLevel.getValue().longValue(), severityLevel.getCode(), 
                            "Warning");
                    break;
                    
                case NOTICE:
                    enumData = new EnumOptionData(severityLevel.getValue().longValue(), severityLevel.getCode(), 
                            "Notice");
                    break;
                    
                default:
                    break;
            }
        }
        
        return enumData;
    }
    
    public static EnumOptionData CreditCheckRelatedEntity(final Integer creditCheckRelatedEntity) {
        return CreditCheckRelatedEntity(CreditCheckRelatedEntity.fromInt(creditCheckRelatedEntity));
    }
    
    public static EnumOptionData CreditCheckRelatedEntity(final CreditCheckRelatedEntity creditCheckRelatedEntity) {
        EnumOptionData enumData = null;
        
        if (creditCheckRelatedEntity != null) {
            switch (creditCheckRelatedEntity) {
                case LOAN:
                    enumData = new EnumOptionData(creditCheckRelatedEntity.getValue().longValue(), creditCheckRelatedEntity.getCode(), 
                            "Loan");
                    break;
                    
                case SAVINGS:
                    enumData = new EnumOptionData(creditCheckRelatedEntity.getValue().longValue(), creditCheckRelatedEntity.getCode(), 
                            "Savings Account");
                    break;
                    
                default:
                    break;
            }
        }
        
        return enumData;
    }
}
