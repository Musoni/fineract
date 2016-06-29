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

CREATE TABLE IF NOT EXISTS `m_entity_datatable_check` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `application_table_name` varchar(200) NOT NULL,
  `x_registered_table_id` int(11) NOT NULL,
  `status_enum` int(11) NOT NULL,
  `system_defined` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  FOREIGN KEY (x_registered_table_id) REFERENCES x_registered_table(id),
  UNIQUE KEY `unique_entity_check` (`application_table_name`,`x_registered_table_id`,`status_enum`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1;


INSERT INTO m_permission (grouping, code, entity_name, action_name, can_maker_checker)
VALUE ("datatable","READ_ENTITY_DATATABLE_CHECK","ENTITY_DATATABLE_CHECK","READ",0);

INSERT INTO m_permission (grouping, code, entity_name, action_name, can_maker_checker)
VALUE ("datatable","CREATE_ENTITY_DATATABLE_CHECK","ENTITY_DATATABLE_CHECK","CREATE",0);

INSERT INTO m_permission (grouping, code, entity_name, action_name, can_maker_checker)
VALUE ("datatable","DELETE_ENTITY_DATATABLE_CHECK","ENTITY_DATATABLE_CHECK","DELETE",0);


