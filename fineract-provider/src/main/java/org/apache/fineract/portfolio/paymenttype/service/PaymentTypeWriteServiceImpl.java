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
package org.apache.fineract.portfolio.paymenttype.service;

import java.util.Map;

import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.portfolio.paymenttype.api.PaymentTypeApiResourceConstants;
import org.apache.fineract.portfolio.paymenttype.data.PaymentTypeDataValidator;
import org.apache.fineract.portfolio.paymenttype.domain.PaymentType;
import org.apache.fineract.portfolio.paymenttype.domain.PaymentTypeRepository;
import org.apache.fineract.portfolio.paymenttype.domain.PaymentTypeRepositoryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PaymentTypeWriteServiceImpl implements PaymentTypeWriteService {

    private final PaymentTypeRepository repository;
    private final PaymentTypeRepositoryWrapper repositoryWrapper;
    private final PaymentTypeDataValidator fromApiJsonDeserializer;
    private final static Logger logger = LoggerFactory.getLogger(PaymentTypeWriteServiceImpl.class);

    @Autowired
    public PaymentTypeWriteServiceImpl(PaymentTypeRepository repository, PaymentTypeRepositoryWrapper repositoryWrapper,
            PaymentTypeDataValidator fromApiJsonDeserializer) {
        this.repository = repository;
        this.repositoryWrapper = repositoryWrapper;
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;

    }

    @Override
    public CommandProcessingResult createPaymentType(JsonCommand command) {
        try {
            this.fromApiJsonDeserializer.validateForCreate(command.json());
            String name = command.stringValueOfParameterNamed(PaymentTypeApiResourceConstants.NAME);
            String description = command.stringValueOfParameterNamed(PaymentTypeApiResourceConstants.DESCRIPTION);
            Boolean isCashPayment = command.booleanObjectValueOfParameterNamed(PaymentTypeApiResourceConstants.ISCASHPAYMENT);
            Long position = command.longValueOfParameterNamed(PaymentTypeApiResourceConstants.POSITION);

            PaymentType newPaymentType = PaymentType.create(name, description, isCashPayment, position);
            this.repository.save(newPaymentType);
            
            return new CommandProcessingResultBuilder().
                    withCommandId(command.commandId()).
                    withEntityId(newPaymentType.getId()).
                    build();
        } catch (final DataIntegrityViolationException dve) {
            handleCodeValueDataIntegrityIssues(command, dve);
            return new CommandProcessingResultBuilder() //
                    .withCommandId(command.commandId()) //
                    .build();
        }
        
    }

    @Override
    public CommandProcessingResult updatePaymentType(Long paymentTypeId, JsonCommand command) {

        try {
            this.fromApiJsonDeserializer.validateForUpdate(command.json());
            final PaymentType paymentType = this.repositoryWrapper.findOneWithNotFoundDetection(paymentTypeId);
            final Map<String, Object> changes = paymentType.update(command);

            if (!changes.isEmpty()) {
                this.repository.save(paymentType);
            }

            return new CommandProcessingResultBuilder().
                    withCommandId(command.commandId()).
                    withEntityId(command.entityId()).
                    build();
        } catch (final DataIntegrityViolationException dve) {
            handleCodeValueDataIntegrityIssues(command, dve);
            return new CommandProcessingResultBuilder() //
                    .withCommandId(command.commandId()) //
                    .build();
        }
    }

    @Override
    public CommandProcessingResult deletePaymentType(Long paymentTypeId) {
        final PaymentType paymentType = this.repositoryWrapper.findOneWithNotFoundDetection(paymentTypeId);
        
        // delete the entity by setting the "deleted" flag to 1
        paymentType.delete();
        
        this.repository.save(paymentType);
        
        return new CommandProcessingResultBuilder().withEntityId(paymentType.getId()).build();
    }
    
    /*
     * Guaranteed to throw an exception no matter what the data integrity issue
     * is.
     */
    private void handleCodeValueDataIntegrityIssues(final JsonCommand command, final DataIntegrityViolationException dve) {
        final Throwable realCause = dve.getMostSpecificCause();
        if (realCause.getMessage().contains("unique_payment_type")) {
            final String name = command.stringValueOfParameterNamed("name");
            throw new PlatformDataIntegrityException("error.msg.payment.type.duplicate.name", "A payment type with name '" + name
                    + "' already exists", "name", name);
        }

        logger.error(dve.getMessage(), dve);
        throw new PlatformDataIntegrityException("error.msg.code.value.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource: " + realCause.getMessage());
    }
}
