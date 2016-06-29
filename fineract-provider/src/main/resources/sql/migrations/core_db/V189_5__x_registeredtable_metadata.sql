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

Alter table `x_registered_table`
drop primary key;

Alter table `x_registered_table`
add column id int NOT NULL AUTO_INCREMENT primary key first;

create table `x_registered_table_metadata`(
`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
`registered_table_id` int NOT NULL,
`table_name` varchar(150),
`field_name` varchar(100),
`label_name` varchar(100),
`ordering`  int,
PRIMARY KEY (`id`),
FOREIGN KEY(registered_table_id) REFERENCES x_registered_table(id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;