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

create table acc_gl_closure_journal_entry_balance(
id bigint primary key auto_increment,
closure_id bigint not null,
account_id bigint not null,
amount decimal(19,6) not null,
created_date datetime not null,
createdby_id bigint not null,
lastmodified_date datetime not null,
lastmodifiedby_id bigint not null,
is_deleted tinyint(1) default 0 not null,
foreign key (closure_id) references acc_gl_closure (id),
foreign key (account_id) references acc_gl_account (id),
foreign key (createdby_id) references m_appuser (id),
foreign key (lastmodifiedby_id) references m_appuser (id)
);

insert into c_configuration (name, enabled, description)
values ('store-journal-entry-balance-at-period-closure', 1, 'If enabled, the latest journal entry (entry date less than or equal to the period closure closing date) running balance will be stored per GL account.');

insert into m_permission (grouping, code, entity_name, action_name, can_maker_checker)
values ('report', 'READ_GLClosureAccountBalanceReport', 'GLClosureAccountBalanceReport', 'READ', 0);