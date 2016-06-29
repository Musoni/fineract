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

ALTER TABLE `acc_gl_account` ADD `organization_running_balance_derived` DECIMAL(19,6)  NULL  DEFAULT NULL  AFTER `reconciliation_enabled`;
ALTER TABLE `acc_gl_account` ADD `last_entry_id_derived` BIGINT  NULL  DEFAULT NULL  AFTER `organization_running_balance_derived`;

-- Provide initial values:
update acc_gl_account as ac join
(
select je.account_id,
SUM(IF(type_enum = 1, IF(accounts.classification_enum IN (1,5), amount *-1, amount), IF(accounts.classification_enum IN (1,5), amount, amount *-1))) as movement,
max(je.id) as last_entry_id_derived
from acc_gl_journal_entry as je
left join acc_gl_account as accounts ON accounts.id = je.account_id
WHERE je.id > IFNULL(accounts.last_entry_id_derived,0)
group by je.account_id) as upd on ac.id = upd.account_id
set `organization_running_balance_derived` = IFNULL(organization_running_balance_derived,0) + IFNULL(upd.movement,0),
	ac.`last_entry_id_derived` = IFNULL(upd.last_entry_id_derived,0);