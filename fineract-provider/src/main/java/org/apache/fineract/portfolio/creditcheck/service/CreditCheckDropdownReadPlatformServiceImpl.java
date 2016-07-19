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
package org.apache.fineract.portfolio.creditcheck.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.portfolio.creditcheck.data.CreditCheckEnumerations;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckRelatedEntity;
import org.apache.fineract.portfolio.creditcheck.domain.CreditCheckSeverityLevel;
import org.springframework.stereotype.Service;

@Service
public class CreditCheckDropdownReadPlatformServiceImpl implements CreditCheckDropdownReadPlatformService {

    @Override
    public List<EnumOptionData> retrieveRelatedEntityOptions() {
        final List<EnumOptionData> relatedEntityOptions = new ArrayList<>();
        
        for (final CreditCheckRelatedEntity creditCheckRelatedEntity : CreditCheckRelatedEntity.values()) {
            
            if (CreditCheckRelatedEntity.INVALID.equals(creditCheckRelatedEntity)) {
                continue;
            }
            
            relatedEntityOptions.add(CreditCheckEnumerations.CreditCheckRelatedEntity(creditCheckRelatedEntity));
        }
        
        return relatedEntityOptions;
    }

    @Override
    public List<EnumOptionData> retrieveSeverityLevelOptions() {
        final List<EnumOptionData> severityLevelOptions = new ArrayList<>();
        
        for (final CreditCheckSeverityLevel creditCheckSeverityLevel : CreditCheckSeverityLevel.values()) {
            
            if (CreditCheckSeverityLevel.INVALID.equals(creditCheckSeverityLevel)) {
                continue;
            }
            
            severityLevelOptions.add(CreditCheckEnumerations.severityLevel(creditCheckSeverityLevel));
        }
        
        return severityLevelOptions;
    }
}
