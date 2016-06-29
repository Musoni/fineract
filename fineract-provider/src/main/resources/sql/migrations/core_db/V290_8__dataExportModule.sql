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

create table if not exists `m_data_export` (
id bigint(20) primary key auto_increment,
base_entity_name varchar(20) not null,
user_request_map text,
data_sql text not null,
is_deleted tinyint(1) default 0 not null,
filename varchar(20),
file_download_count int not null default 0,
createdby_id bigint not null,
created_date datetime not null,
lastmodifiedby_id bigint,
lastmodified_date datetime,
foreign key (createdby_id) references m_appuser(id),
foreign key (lastmodifiedby_id) references m_appuser(id));

INSERT INTO `m_permission`
(`grouping`,`code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES
('dataexport', 'CREATE_DATAEXPORT', 'DATAEXPORT', 'CREATE', 0),
('dataexport', 'READ_DATAEXPORT', 'DATAEXPORT', 'READ', 0),
('dataexport', 'DELETE_DATAEXPORT', 'DATAEXPORT', 'DELETE', 0),
('dataexport', 'UPDATE_DATAEXPORT', 'DATAEXPORT', 'UPDATE', 0);