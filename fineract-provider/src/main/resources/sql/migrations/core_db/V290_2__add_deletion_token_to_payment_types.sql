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

alter table m_payment_type
add deletion_token varchar(100) not null default 'NA';

update m_payment_type
set deletion_token = sha2(concat(id, '_', `value`), 224)
where is_deleted = 1;

update m_payment_type
set `value` = replace(`value`, concat( '_deleted_', id ) , '' )
where is_deleted = 1;

alter table m_payment_type
add constraint unique_payment_type unique (`value`, deletion_token); 