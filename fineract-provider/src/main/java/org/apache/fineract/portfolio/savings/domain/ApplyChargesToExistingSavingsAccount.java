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
package org.apache.fineract.portfolio.savings.domain;

import org.apache.fineract.portfolio.charge.domain.Charge;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;

@Entity
@Table(name="m_savings_product_add_charge_to_existing_accounts")
public class ApplyChargesToExistingSavingsAccount extends AbstractPersistable<Long> {

    @ManyToOne
    @JoinColumn(name = "savings_product_id", nullable = false)
    private SavingsProduct savingsProduct;

    @ManyToOne(optional = false)
    @JoinColumn(name = "charge_id", nullable = false)
    private Charge productCharge;

    @Column(name = "apply_charge_to_existing_savings_account", nullable = false)
    private boolean applyChargeToExistingSavingsAccount;

    protected ApplyChargesToExistingSavingsAccount() {
    }

    public ApplyChargesToExistingSavingsAccount(final SavingsProduct savingsProduct, final Charge productCharge, final boolean applyChargeToExistingSavingsAccount) {
        this.savingsProduct = savingsProduct;
        this.productCharge = productCharge;
        this.applyChargeToExistingSavingsAccount = applyChargeToExistingSavingsAccount;
    }

    public SavingsProduct getSavingsProduct() {
        return this.savingsProduct;
    }

    public Charge getProductCharge() {
        return this.productCharge;
    }

    public boolean isApplyChargeToExistingSavingsAccount() {
        return this.applyChargeToExistingSavingsAccount;
    }

    public void updateSavingsProduct(final SavingsProduct product){
        this.savingsProduct = product;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ApplyChargesToExistingSavingsAccount that = (ApplyChargesToExistingSavingsAccount) o;

        if (applyChargeToExistingSavingsAccount != that.applyChargeToExistingSavingsAccount) return false;
        if (savingsProduct != null ? !savingsProduct.equals(that.savingsProduct) : that.savingsProduct != null)
            return false;
        return productCharge != null ? productCharge.equals(that.productCharge) : that.productCharge == null;

    }


}
