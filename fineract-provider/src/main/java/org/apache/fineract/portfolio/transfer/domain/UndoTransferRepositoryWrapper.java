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
package org.apache.fineract.portfolio.transfer.domain;

import org.apache.fineract.portfolio.transfer.exception.UndoTransferNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UndoTransferRepositoryWrapper {

    private final UndoTransferRepository undoTransferRepository;

    @Autowired
    public UndoTransferRepositoryWrapper(final UndoTransferRepository undoTransferRepository) {
        this.undoTransferRepository = undoTransferRepository;
    }


    public void save(final UndoTransfer undoTransfer){
        this.undoTransferRepository.save(undoTransfer);
    }
    public void saveAndFlush(final UndoTransfer undoTransfer) {
        this.undoTransferRepository.saveAndFlush(undoTransfer);
    }

    public boolean doesClientExistInUndoTransfer(final Long clientId){
        boolean clientExist  = false;
        final UndoTransfer undoTransfer = this.undoTransferRepository.findClientUndoTransfer(clientId);
        if(undoTransfer !=null){
            clientExist = true;
        }
        return clientExist;
    }


    public boolean doesGroupExistInUndoTransfer(final Long groupId){
        boolean groupExist = false;
        final UndoTransfer undoTransfer = this.undoTransferRepository.findGroupUndoTransfer(groupId);
        if(undoTransfer !=null){
            groupExist = true;
        }
        return groupExist;
    }


    public UndoTransfer findOneWithNotFoundDetection(final Long id){
        final UndoTransfer entity = this.undoTransferRepository.findOne(id);
        if (entity == null) { throw new UndoTransferNotFoundException(id); }
        return entity;
    }
}
