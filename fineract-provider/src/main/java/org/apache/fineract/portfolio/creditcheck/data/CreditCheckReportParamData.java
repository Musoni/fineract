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

/** 
 * Immutable object representing the credit check stretchy report parameters
 * Each stretchy report can have one or more of these parameters
 **/
public class CreditCheckReportParamData {
    private final Long clientId;
    private final Long loanId;
    private final Long groupId;
    private final Long userId;
    private final Long staffId;
    private final Long officeId;
    private final Long productId;

    private CreditCheckReportParamData(final Long clientId, final Long loanId, final Long groupId, 
            final Long userId, final Long staffId, final Long officeId, final Long productId) {
        this.clientId = clientId;
        this.loanId = loanId;
        this.groupId = groupId;
        this.userId = userId;
        this.staffId = staffId;
        this.officeId = officeId;
        this.productId = productId;
    }
    
    public static CreditCheckReportParamData instance(final Long clientId, final Long loanId, final Long groupId, 
            final Long userId, final Long staffId, final Long officeId, final Long productId) {
        return new CreditCheckReportParamData(clientId, loanId, groupId, userId, staffId, officeId, productId);
    }

    /**
     * @return the clientId
     */
    public Long getClientId() {
        return clientId;
    }

    /**
     * @return the loanId
     */
    public Long getLoanId() {
        return loanId;
    }

    /**
     * @return the groupId
     */
    public Long getGroupId() {
        return groupId;
    }

    /**
     * @return the userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * @return the staffId
     */
    public Long getStaffId() {
        return staffId;
    }

    /**
     * @return the officeId
     */
    public Long getOfficeId() {
        return officeId;
    }

    /**
     * @return the productId
     */
    public Long getProductId() {
        return productId;
    }
}
