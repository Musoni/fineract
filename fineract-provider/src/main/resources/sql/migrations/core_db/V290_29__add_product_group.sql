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

insert into m_code (code_name, code_label, is_system_defined)
values ('loanProductGroups', 'loanProductGroups', 1);

insert into m_code (code_name, code_label, is_system_defined)
values ('savingsProductGroups', 'savingsProductGroups', 1);

insert into m_code_value (code_id, code_value, order_position)
values (
	(select id from m_code where code_name = 'loanProductGroups'),
	'Individual Loan',  0);

insert into m_code_value (code_id, code_value, order_position)
values (
	(select id from m_code where code_name = 'loanProductGroups'),
	'Group Loan',  0);

insert into m_code_value (code_id, code_value, order_position)
values (
	(select id from m_code where code_name = 'savingsProductGroups'),
	'Individual Savings',  0);

insert into m_code_value (code_id, code_value, order_position)
values (
	(select id from m_code where code_name = 'savingsProductGroups'),
	'Group Savings',  0);

ALTER TABLE m_product_loan ADD COLUMN product_group bigint(20) NULL;
ALTER TABLE m_savings_product ADD COLUMN product_group bigint(20) NULL;