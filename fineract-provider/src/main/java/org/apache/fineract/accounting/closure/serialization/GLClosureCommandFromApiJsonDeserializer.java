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
package org.apache.fineract.accounting.closure.serialization;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.apache.fineract.accounting.closure.api.GLClosureJsonInputParams;
import org.apache.fineract.accounting.closure.command.GLClosureCommand;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.serialization.AbstractFromApiJsonDeserializer;
import org.apache.fineract.infrastructure.core.serialization.FromApiJsonDeserializer;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.loanaccount.guarantor.command.GuarantorCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of {@link FromApiJsonDeserializer} for
 * {@link GuarantorCommand}'s.
 */
@Component
public final class GLClosureCommandFromApiJsonDeserializer extends AbstractFromApiJsonDeserializer<GLClosureCommand> {

    private final FromJsonHelper fromApiJsonHelper;

    @Autowired
    public GLClosureCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonfromApiJsonHelper) {
        this.fromApiJsonHelper = fromApiJsonfromApiJsonHelper;
    }

    @Override
    public GLClosureCommand commandFromApiJson(final String json) {
        if (StringUtils.isBlank(json)) { throw new InvalidJsonException(); }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        final Set<String> supportedParameters = GLClosureJsonInputParams.getAllValues();
        this.fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        final JsonElement element = this.fromApiJsonHelper.parse(json);

        final Long id = this.fromApiJsonHelper.extractLongNamed(GLClosureJsonInputParams.ID.getValue(), element);
        final Long officeId = this.fromApiJsonHelper.extractLongNamed(GLClosureJsonInputParams.OFFICE_ID.getValue(), element);
        final String comments = this.fromApiJsonHelper.extractStringNamed(GLClosureJsonInputParams.COMMENTS.getValue(), element);
        final LocalDate closingDate = this.fromApiJsonHelper.extractLocalDateNamed(GLClosureJsonInputParams.CLOSING_DATE.getValue(),
                element);
        final Boolean bookOffIncomeAndExpense = this.fromApiJsonHelper.extractBooleanNamed(GLClosureJsonInputParams.BOOK_OFF_INCOME_AND_EXPENSE.getValue(),element);
        final Long equityGlAccountId = this.fromApiJsonHelper.extractLongNamed(GLClosureJsonInputParams.EQUITY_GL_ACCOUNT_ID.getValue(),element);
        final String currencyCode = this.fromApiJsonHelper.extractStringNamed(GLClosureJsonInputParams.CURRENCY_CODE.getValue(), element);
        final Boolean subBranches = this.fromApiJsonHelper.extractBooleanNamed(GLClosureJsonInputParams.SUB_BRANCHES.getValue(),element);
        final Boolean reverseIncomeAndExpenseBooking = this.fromApiJsonHelper.extractBooleanNamed(GLClosureJsonInputParams.REVERSE_INCOME_AND_EXPENSE_BOOKING.getValue(),element);
        final String incomeAndExpenseComments   = this.fromApiJsonHelper.extractStringNamed(GLClosureJsonInputParams.INCOME_AND_EXPENSE_COMMENTS.getValue(),element);

        return new GLClosureCommand(id, officeId, closingDate, comments,bookOffIncomeAndExpense,equityGlAccountId,currencyCode,reverseIncomeAndExpenseBooking,subBranches,incomeAndExpenseComments);
    }
}