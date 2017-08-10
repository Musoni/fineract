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
package org.apache.fineract.portfolio.search.data;

import org.apache.fineract.infrastructure.core.data.EnumOptionData;

public class SearchData {

    private final Long entityId;
    private final String entityAccountNo;
    private final String entityExternalId;
    private final String entityName;
    private final String entityType;
    private final Long parentId;
    private final String parentName;
    private final String entityMobileNo;
    private final EnumOptionData entityStatus;
    private final String parentType;
    private final Long groupId;
    private final String groupName;
    private final Long officeId;
    private final String officeName;
    private final String parentAccountNo;

    public SearchData(final Long entityId, final String entityAccountNo, final String entityExternalId, final String entityName,
            final String entityType, final Long parentId, final String parentName, final String parentType, 
            final String entityMobileNo, final EnumOptionData entityStatus, final Long groupId, final String groupName, 
            final Long officeId, final String officeName, final String parentAccountNo) {

        this.entityId = entityId;
        this.entityAccountNo = entityAccountNo;
        this.entityExternalId = entityExternalId;
        this.entityName = entityName;
        this.entityType = entityType;
        this.parentId = parentId;
        this.parentName = parentName;
        this.parentType = parentType;
        this.entityMobileNo=entityMobileNo;
        this.entityStatus = entityStatus;
        this.groupId = groupId;
        this.groupName = groupName;
        this.officeId = officeId;
        this.officeName = officeName;
        this.parentAccountNo = parentAccountNo;
    }

    public Long getEntityId() {
        return this.entityId;
    }

    public String getEntityAccountNo() {
        return this.entityAccountNo;
    }

    public String getEntityExternalId() {
        return this.entityExternalId;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public String getParentName() {
        return this.parentName;
    }
    
    public String getParentType() {
        return this.parentType;
    }

    public String getEntityMobileNo() {
		return this.entityMobileNo;
	}

	public EnumOptionData getEntityStatus() {
        return this.entityStatus;
    }

	/**
	 * @return the groupId
	 */
	public Long getGroupId() {
		return groupId;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return the officeId
	 */
	public Long getOfficeId() {
		return officeId;
	}

	/**
	 * @return the officeName
	 */
	public String getOfficeName() {
		return officeName;
	}

	/**
	 * @return the parentAccountNo
	 */
	public String getParentAccountNo() {
		return parentAccountNo;
	}

}
