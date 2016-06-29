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

ALTER TABLE `sms_messages_outbound` ADD `external_id` BIGINT(20) NULL COMMENT 'ID of the message in the smsOutboundMessage table in the mlite-sms database' AFTER `id`;
ALTER TABLE `sms_messages_outbound` ADD `source_address` VARCHAR(50) NULL COMMENT 'Sender of the SMS message.' AFTER `status_enum`;
ALTER TABLE `sms_messages_outbound` CHANGE `message` `message` VARCHAR(254) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL;


create table sms_configuration ( id int primary key auto_increment, name varchar(50) not null, value varchar(200) not null );

INSERT INTO `sms_configuration` (`id`, `name`, `value`) VALUES (NULL, 'API_BASE_URL', 'http://localhost:8080/mlite-sms/api/v1/sms');

INSERT INTO `sms_configuration` (`id`, `name`, `value`) VALUES (NULL, 'API_AUTH_USERNAME', 'root');

INSERT INTO `sms_configuration` (`id`, `name`, `value`) VALUES (NULL, 'API_AUTH_PASSWORD', 'localhost');

INSERT INTO `sms_configuration` (`id`, `name`, `value`) VALUES (NULL, 'SMS_CREDITS', '1000');

INSERT INTO `sms_configuration` (`id`, `name`, `value`) VALUES (NULL, 'SMS_SOURCE_ADDRESS', 'Musoni Services');

INSERT INTO `sms_configuration` (`id`, `name`, `value`) VALUES (NULL, 'COUNTRY_CALLING_CODE', '31');
