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

DROP TABLE IF EXISTS `sms_campaign`;

CREATE TABLE `sms_campaign`(
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `campaign_name` varchar(100) NOT NULL,
  `campaign_type` int NOT NULL,
  `runreport_id` int NOT NULL,
  `param_value` text, 
  `status_enum` int NOT NULL,
  `message` text NOT NULL,
  `closedon_date` date,
  `closedon_userid` bigint(20),
  `submittedon_date` date,
  `submittedon_userid` bigint(20),
  `approvedon_date` date,
  `approvedon_userid` bigint(20),
  `recurrence` varchar(100),
  `next_trigger_date` datetime,
  `last_trigger_date` datetime,
  `recurrence_start_date` datetime,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`runreport_id`) REFERENCES `stretchy_report` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'READ_SMS_CAMPAIGN',  'SMS_CAMPAIGN',  'READ',  '0'
);

INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'CREATE_SMS_CAMPAIGN',  'SMS_CAMPAIGN',  'CREATE',  '0'
);

INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'CREATE_SMS_CAMPAIGN_CHECKER',  'SMS_CAMPAIGN',  'CREATE',  '0'
);

INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'UPDATE_SMS_CAMPAIGN',  'SMS_CAMPAIGN',  'UPDATE',  '0'
);

INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'UPDATE_SMS_CAMPAIGN_CHECKER',  'SMS_CAMPAIGN',  'UPDATE',  '0'
);
INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'DELETE_SMS_CAMPAIGN',  'SMS_CAMPAIGN',  'DELETE',  '0'
);


INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'DELETE_SMS_CAMPAIGN_CHECKER',  'SMS_CAMPAIGN',  'DELETE',  '0'
);


INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'ACTIVATE_SMS_CAMPAIGN',  'SMS_CAMPAIGN',  'ACTIVATE',  '0'
);


INSERT INTO  `m_permission` (
`id` ,
`grouping` ,
`code` ,
`entity_name` ,
`action_name` ,
`can_maker_checker`
)
VALUES (
'0',  'organisation',  'CLOSE_SMS_CAMPAIGN',  'SMS_CAMPAIGN',  'CLOSE',  '0'
);


