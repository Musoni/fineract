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
package org.apache.fineract.accounting.journalentry.data;

public class JournalEntryAssociationParametersData {

    private final boolean transactionDetailsRequired;
    private final boolean runningBalanceRequired;
    private final boolean paymentDetailsRequired;
    private final boolean notesRequired;
    private final boolean glClosureRequired;
    private final boolean unReconciledBalanceRequired;
    private final boolean tellerRequired;

    public JournalEntryAssociationParametersData() {
        this.transactionDetailsRequired = false;
        this.runningBalanceRequired = false;
        this.paymentDetailsRequired = false;
        this.notesRequired = false;
        this.glClosureRequired = false;
        this.unReconciledBalanceRequired = false;
        this.tellerRequired = false;
    }

    public JournalEntryAssociationParametersData(final boolean transactionDetailsRequired, final boolean runningBalanceRequired, final boolean paymentDetails,
                                                 final boolean glClosureRequired, final boolean unReconciledBalanceRequired,final boolean tellerRequired) {

        
        Boolean notesRequired = false;
        Boolean paymentDetailsRequired = paymentDetails;
        
        if(transactionDetailsRequired)
        {
            notesRequired = transactionDetailsRequired;
            paymentDetailsRequired = transactionDetailsRequired;
        }
        
        this.notesRequired = notesRequired;
        this.transactionDetailsRequired = transactionDetailsRequired;
        this.paymentDetailsRequired = paymentDetailsRequired;
        this.runningBalanceRequired = runningBalanceRequired;
        this.glClosureRequired = glClosureRequired;
        this.unReconciledBalanceRequired = unReconciledBalanceRequired;
        this.tellerRequired = tellerRequired;

    }

    public boolean isTransactionDetailsRequired() {
        return this.transactionDetailsRequired;
    }

    public boolean isRunningBalanceRequired() {
        return this.runningBalanceRequired;
    }

    public boolean isUnReconciledBalanceRequired() {
        return this.unReconciledBalanceRequired;
    }


    
    public boolean isPaymentDetailsRequired() {
        return this.paymentDetailsRequired;
    }
    
    public boolean isNotesRequired() {
        return this.notesRequired;
    }
    public boolean isGlClosureRequired (){ return this.glClosureRequired;}

    public boolean isTellerRequired(){return this.tellerRequired;}
}
