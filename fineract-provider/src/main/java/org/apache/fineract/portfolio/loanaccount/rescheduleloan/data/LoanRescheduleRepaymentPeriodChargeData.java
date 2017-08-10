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
package org.apache.fineract.portfolio.loanaccount.rescheduleloan.data;

import org.apache.fineract.portfolio.loanaccount.domain.LoanCharge;

/**
 * Object contains the id and a {@link LoanCharge} object linked to a loan installment
 */
public class LoanRescheduleRepaymentPeriodChargeData {
	private final int installmentNumber;
	private final LoanCharge loanCharge;
	
	/**
	 * @param installmentNumber
	 * @param loanCharge
	 */
	private LoanRescheduleRepaymentPeriodChargeData(final int installmentNumber, final LoanCharge loanCharge) {
		this.installmentNumber = installmentNumber;
		this.loanCharge = loanCharge;
	}
	
	/**
	 * Creates a new {@link LoanRescheduleRepaymentPeriodChargeData} object
	 * 
	 * @param installmentNumber
	 * @param loanCharge
	 * @return {@link LoanRescheduleRepaymentPeriodChargeData} object
	 */
	public static LoanRescheduleRepaymentPeriodChargeData instance(final int installmentNumber, 
			final LoanCharge loanCharge) {
		return new LoanRescheduleRepaymentPeriodChargeData(installmentNumber, loanCharge);
	}

	/**
	 * @return the installmentNumber
	 */
	public int getInstallmentNumber() {
		return installmentNumber;
	}

	/**
	 * @return the loanCharge
	 */
	public LoanCharge getLoanCharge() {
		return loanCharge;
	}
}
