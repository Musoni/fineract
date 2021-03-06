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

create table m_credit_check (
id bigint primary key auto_increment,
name varchar(100) not null,
related_entity_enum_value smallint(2) not null,
expected_result varchar(20) not null,
severity_level_enum_value smallint(2) not null,
stretchy_report_id int not null,
stretchy_report_param_map varchar(200) null,
message varchar(500) not null,
is_active tinyint(1) default 1 not null,
is_deleted tinyint(1) default 0 not null,
foreign key (stretchy_report_id) references stretchy_report(id),
constraint unique_name unique (name)
);

create table m_product_loan_credit_check (
id bigint primary key auto_increment,
credit_check_id bigint not null,
product_loan_id bigint not null,
foreign key (product_loan_id) references m_product_loan(id),
foreign key (credit_check_id) references m_credit_check(id)
);

create table m_loan_credit_check (
id bigint primary key auto_increment,
credit_check_id bigint not null,
loan_id bigint not null,
expected_result varchar(20) not null,
actual_result varchar(20) null,
severity_level_enum_value smallint(2) not null,
has_been_triggered tinyint(1) not null default '0',
triggered_on_date date null,
triggered_by_user_id bigint null,
message varchar(500) not null,
is_active tinyint(1) default 1 not null,
foreign key (triggered_by_user_id) references m_appuser(id),
foreign key (credit_check_id) references m_credit_check(id),
foreign key (loan_id) references m_loan(id)
);

insert into `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) values ('organisation', 'READ_CREDITCHECK', 'CREDITCHECK', 'READ', '0');

insert into `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) values ('organisation', 'CREATE_CREDITCHECK', 'CREDITCHECK', 'CREATE', '0');

insert into `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) values ('organisation', 'UPDATE_CREDITCHECK', 'CREDITCHECK', 'UPDATE', '0');

insert into `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) values ('organisation', 'DELETE_CREDITCHECK', 'CREDITCHECK', 'DELETE', '0');
