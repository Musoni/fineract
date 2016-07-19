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
package org.apache.fineract.portfolio.transfer.data;

import org.joda.time.LocalDate;

public class UndoTransferGroupData {

    private final Long id;
    private final Long groupId;
    private final String groupName;
    private final boolean isTransferUndone;
    private final Long transferFromOfficeId;
    private final String transferFromOfficeName;
    private final Long transferFromGroupId;
    private final String transferFromGroupName;
    private final Long transferFromStaffId;   
    private final String transferFromStaffName;
    private final LocalDate submittedOnDate;
    private final String approvedUser;
    private final String transferToOfficeName;

    private UndoTransferGroupData(Long id, Long groupId, String groupName, boolean transferUndone, Long transferFromOfficeId,
            String transferFromOfficeName, Long transferFromGroupId, String transferFromGroupName, Long transferFromStaffId, String transferFromStaffName,
            LocalDate submittedOnDate, String approvedUser,String transferToOfficeName) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        isTransferUndone = transferUndone;
        this.transferFromOfficeId = transferFromOfficeId;
        this.transferFromOfficeName = transferFromOfficeName;
        this.transferFromGroupId = transferFromGroupId;
        this.transferFromGroupName = transferFromGroupName;
        this.transferFromStaffId = transferFromStaffId;
        this.transferFromStaffName = transferFromStaffName;
        this.submittedOnDate = submittedOnDate;
        this.approvedUser = approvedUser;
        this.transferToOfficeName = transferToOfficeName;

    }
    
    public static UndoTransferGroupData instance(final Long id,final Long groupId, final String groupName,
            boolean transferUndone,final Long transferFromOfficeId,final String transferFromOfficeName,
            final Long transferFromGroupId,final String transferFromGroupName,final Long transferFromStaffId,
            final String transferFromStaffName,final LocalDate submittedOnDate,final String approvedUser, final String transferToOfficeName){
        return new UndoTransferGroupData(id,groupId,groupName,transferUndone,transferFromOfficeId,transferFromOfficeName,transferFromGroupId,
                transferFromGroupName,transferFromStaffId,transferFromStaffName,submittedOnDate,approvedUser,transferToOfficeName);
    }
}
