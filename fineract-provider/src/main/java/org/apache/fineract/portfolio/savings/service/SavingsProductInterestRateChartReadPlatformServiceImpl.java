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
package org.apache.fineract.portfolio.savings.service;

import org.apache.fineract.portfolio.interestratechart.domain.InterestRateChartFields;
import org.apache.fineract.portfolio.savings.data.InterestRateCharts;
import org.apache.fineract.portfolio.savings.domain.SavingsProductInterestRateChart;
import org.apache.fineract.portfolio.savings.domain.SavingsProductInterestRateChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class SavingsProductInterestRateChartReadPlatformServiceImpl  implements SavingsProductInterestRateChartReadPlatformService{

    final private SavingsProductInterestRateChartRepository savingsProductInterestRateChartRepository;

    @Autowired
    private SavingsProductInterestRateChartReadPlatformServiceImpl(final SavingsProductInterestRateChartRepository savingsProductInterestRateChartRepository) {
        this.savingsProductInterestRateChartRepository = savingsProductInterestRateChartRepository;
    }

    @Override
    public Collection<InterestRateCharts> retrieveOne(Long productId) {
        final Collection<SavingsProductInterestRateChart> interestRateCharts = this.savingsProductInterestRateChartRepository.findBySavingsProduct(productId);
        final Collection<InterestRateCharts> interestRateChartData = new ArrayList<>();
        if(!interestRateCharts.isEmpty()){
            for(SavingsProductInterestRateChart interest : interestRateCharts){
                InterestRateChartFields fields = interest.getChartFields();
                interestRateChartData.add(InterestRateCharts.createNew(interest.getId(),fields.getName(),fields.getDescription(),fields.getFromDate(),fields.getEndDate(),interest.getAnnualInterestRate(),interest.isApplyToExistingSavingsAccount()));
            }
        }
        return interestRateChartData;
    }
}
