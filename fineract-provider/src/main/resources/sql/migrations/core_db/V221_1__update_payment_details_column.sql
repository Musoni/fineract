--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

update acc_gl_journal_entry as je
join m_loan_transaction as mlt on je.`loan_transaction_id` = mlt.`id`
set je.`payment_details_id` = mlt.`payment_detail_id`
where je.`loan_transaction_id` is not null;

update acc_gl_journal_entry as je
join `m_savings_account_transaction` as mlt on je.`savings_transaction_id` = mlt.`id`
set je.`payment_details_id` = mlt.`payment_detail_id`
where je.`savings_transaction_id` is not null;
