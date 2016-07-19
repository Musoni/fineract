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
package org.apache.fineract.infrastructure.sms.data;

import java.util.Map;

public class SmsBusinessRulesData {

    @SuppressWarnings("unused")
    private final Long reportId;

    @SuppressWarnings("unused")
    private final String reportName;

    @SuppressWarnings("unused")
    private final String reportType;

    @SuppressWarnings("unused")
    private final String reportDescription;

    @SuppressWarnings("unused")
    private final Map<String,Object> reportParamName;



    public SmsBusinessRulesData(final Long reportId, final String reportName,final String reportType, final Map<String,Object> reportParamName,
                                final String reportDescription) {
        this.reportId = reportId;
        this.reportName = reportName;
        this.reportType = reportType;
        this.reportParamName = reportParamName;
        this.reportDescription = reportDescription;
    }


    public static SmsBusinessRulesData instance(final Long reportId, final String reportName,final String reportType,final Map<String,Object> reportParamName,
                                                final String reportDescription){
        return new SmsBusinessRulesData(reportId,reportName,reportType,reportParamName,reportDescription);
    }

    public Map<String, Object> getReportParamName() {
        return reportParamName;
    }

    public String getReportType() {
        return reportType;
    }

    public String getReportName() {
        return reportName;
    }

    public Long getReportId() {
        return reportId;
    }

    public String getReportDescription() {
        return reportDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmsBusinessRulesData that = (SmsBusinessRulesData) o;

        if (reportId != null ? !reportId.equals(that.reportId) : that.reportId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return reportId != null ? reportId.hashCode() : 0;
    }
}
