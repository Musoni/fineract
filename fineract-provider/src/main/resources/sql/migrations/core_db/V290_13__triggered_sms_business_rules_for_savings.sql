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

INSERT INTO `stretchy_report` (`report_name`, `report_type`, `report_subtype`, `report_sql`, `description`, `core_report`, `use_report`)
VALUES ('Savings Account Activated', 'Sms', 'Triggered', 'SELECT mc.id, mc.firstname, mc.middlename as middlename, mc.lastname, mc.display_name as FullName, mc.mobile_no as mobileNo, mc.group_name as GroupName,\n mo.name as officename, ifnull(od.phoneNumber,\'\') as officenummber, ms.id as savingsId, ms.account_no as accountnumber, ms.`nominal_annual_interest_rate` as annualinterestrate\n FROM\n m_office mo\n JOIN m_office ounder ON ounder.hierarchy LIKE CONCAT(mo.hierarchy, \'%\')\n AND ounder.hierarchy like CONCAT(\'.\', \'%\')\n LEFT JOIN (\n select \n ms.id as savingsId, \n ifnull(mc.id,mc2.id) as id, \n ifnull(mc.firstname,mc2.firstname) as firstname, \n ifnull(mc.middlename,ifnull(mc2.middlename,(\'\'))) as middlename, \n ifnull(mc.lastname,mc2.lastname) as lastname, \n ifnull(mc.display_name,mc2.display_name) as display_name, \n ifnull(mc.status_enum,mc2.status_enum) as status_enum,\n ifnull(mc.mobile_no,mc2.mobile_no) as mobile_no,\n ifnull(mg.office_id,mc2.office_id) as office_id,\n ifnull(mg.staff_id,mc2.staff_id) as staff_id,\n mg.id as group_id, \nmg.display_name as group_name\n from\n m_savings_account ms\n left join m_group mg on mg.id = ms.group_id\n left join m_group_client mgc on mgc.group_id = mg.id\n left join m_client mc on mc.id = mgc.client_id\n left join m_client mc2 on mc2.id = ms.client_id\n order by savingsId\n ) mc on mc.office_id = ounder.id\n left join ml_office_details as od on od.office_id = mo.id\n left join m_savings_account ms on ms.id = mc.savingsId\n WHERE mc.status_enum = 300 and mc.mobile_no is not null\n and (mo.id = ${officeId} or ${officeId} = -1)\n and (mc.staff_id = ${staffId} or ${staffId} = -1)\nand (ms.id = ${savingsId} or ${savingsId} = -1)\nand (mc.id = ${clientId} or ${clientId} = -1)\nand (mc.group_id = ${groupId} or ${groupId} = -1) \nand (ms.account_type_enum = ${accountType} or ${accountType} = -1)', 'Savings account and client data of activated savings account', '0', '0'),
('Savings Deposit', 'Sms', 'Triggered', 'select ms.id as savingsId, mc.id, mc.firstname, ifnull(mc.middlename,\'\') as middlename, mc.lastname, mc.display_name as FullName, \nmobile_no as mobileNo, mc.group_name as GroupName, round(ms.account_balance_derived, ms.currency_digits) as SavingsBalance,\nifnull(od.phoneNumber,\'\') as officenummber,ms.`account_no` as SavingsAccountId, round(mst.amount, ms.currency_digits) as transactionAmount, \nifnull(mpd.receipt_number,\'\') as receiptNumber, rev.enum_value as transactionType\nFROM m_office mo\nJOIN m_office ounder ON ounder.hierarchy LIKE CONCAT(mo.hierarchy, \'%\')\nAND ounder.hierarchy like CONCAT(\'.\', \'%\')\nLEFT JOIN (\n select \n ms.id as savingsId, \n ifnull(mc.id,mc2.id) as id, \n ifnull(mc.firstname,mc2.firstname) as firstname, \n ifnull(mc.middlename,ifnull(mc2.middlename,(\'\'))) as middlename, \n ifnull(mc.lastname,mc2.lastname) as lastname, \n ifnull(mc.display_name,mc2.display_name) as display_name, \n ifnull(mc.status_enum,mc2.status_enum) as status_enum,\n ifnull(mc.mobile_no,mc2.mobile_no) as mobile_no,\n ifnull(mg.office_id,mc2.office_id) as office_id,\n ifnull(mg.staff_id,mc2.staff_id) as staff_id,\n mg.id as group_id, \nmg.display_name as group_name\n from\n m_savings_account ms\n left join m_group mg on mg.id = ms.group_id\n left join m_group_client mgc on mgc.group_id = mg.id\n left join m_client mc on mc.id = mgc.client_id\n left join m_client mc2 on mc2.id = ms.client_id\n order by savingsId\n ) mc on mc.office_id = ounder.id\nleft join ml_office_details as od on od.office_id = mo.id\nright join m_savings_account ms on ms.id = mc.savingsId\nright join(\nselect mst.amount, mst.transaction_type_enum as transactionType ,mst.id,mst.savings_account_id as savingsId,payment_detail_id\nfrom m_savings_account_transaction mst\nwhere mst.is_reversed = 0 \ngroup by savingsId\n) as mst on mst.savingsId = ms.id\nleft join m_payment_detail mpd on mpd.id = mst.payment_detail_id\nleft join \n(\nselect * from r_enum_value where enum_name = \'savings_transaction_type_enum\'\n) rev on mst.transactionType = rev.enum_id\nwhere mc.status_enum = 300 and mobile_no is not null and ms.`status_enum` = 300\nand (mo.id = ${officeId} or ${officeId} = -1)\nand (mc.staff_id = ${staffId} or ${staffId} = -1)\nand (ms.account_type_enum = ${accountType} or ${accountType} = -1)\ngroup by ms.id', 'Savings and client data for savings deposit', '0', '0'),
('Savings Withdrawal', 'Sms', 'Triggered', 'select ms.id as savingsId, mc.id, mc.firstname, ifnull(mc.middlename,\'\') as middlename, mc.lastname, mc.display_name as FullName, \nmobile_no as mobileNo, mc.group_name as GroupName, round(ms.account_balance_derived, ms.currency_digits) as SavingsBalance,\nifnull(od.phoneNumber,\'\') as officenummber,ms.`account_no` as SavingsAccountId, round(mst.amount, ms.currency_digits) as transactionAmount, \nifnull(mpd.receipt_number,\'\') as receiptNumber, rev.enum_value as transactionType\nFROM m_office mo\nJOIN m_office ounder ON ounder.hierarchy LIKE CONCAT(mo.hierarchy, \'%\')\nAND ounder.hierarchy like CONCAT(\'.\', \'%\')\nLEFT JOIN (\n select \n ms.id as savingsId, \n ifnull(mc.id,mc2.id) as id, \n ifnull(mc.firstname,mc2.firstname) as firstname, \n ifnull(mc.middlename,ifnull(mc2.middlename,(\'\'))) as middlename, \n ifnull(mc.lastname,mc2.lastname) as lastname, \n ifnull(mc.display_name,mc2.display_name) as display_name, \n ifnull(mc.status_enum,mc2.status_enum) as status_enum,\n ifnull(mc.mobile_no,mc2.mobile_no) as mobile_no,\n ifnull(mg.office_id,mc2.office_id) as office_id,\n ifnull(mg.staff_id,mc2.staff_id) as staff_id,\n mg.id as group_id, \nmg.display_name as group_name\n from\n m_savings_account ms\n left join m_group mg on mg.id = ms.group_id\n left join m_group_client mgc on mgc.group_id = mg.id\n left join m_client mc on mc.id = mgc.client_id\n left join m_client mc2 on mc2.id = ms.client_id\n order by savingsId\n ) mc on mc.office_id = ounder.id\nleft join ml_office_details as od on od.office_id = mo.id\nright join m_savings_account ms on ms.id = mc.savingsId\nright join(\nselect mst.amount, mst.transaction_type_enum as transactionType ,mst.id,mst.savings_account_id as savingsId,payment_detail_id\nfrom m_savings_account_transaction mst\nwhere mst.is_reversed = 0 \ngroup by savingsId\n) as mst on mst.savingsId = ms.id\nleft join m_payment_detail mpd on mpd.id = mst.payment_detail_id\nleft join \n(\nselect * from r_enum_value where enum_name = \'savings_transaction_type_enum\'\n) rev on mst.transactionType = rev.enum_id\nwhere mc.status_enum = 300 and mobile_no is not null and ms.`status_enum` = 300\nand (mo.id = ${officeId} or ${officeId} = -1)\nand (mc.staff_id = ${staffId} or ${staffId} = -1)\nand (ms.account_type_enum = ${accountType} or ${accountType} = -1)\ngroup by ms.id', 'Savings and client data for savings withdrawal', '0', '0');

INSERT INTO `r_enum_value` (`enum_name`, `enum_id`, `enum_message_property`, `enum_value`, `enum_type`)
VALUES ('account_type_enum', '-1', 'All', 'All', '0'),
('account_type_enum', '1', 'Individual Savings', 'Individual Savings', '0'),
('account_type_enum', '2', 'Group Savings', 'Group Savings', '0');

INSERT INTO `stretchy_parameter` (`parameter_name`, `parameter_variable`, `parameter_label`, `parameter_displayType`, `parameter_FormatType`, `parameter_default`, `selectAll`, `parameter_sql`, `parent_id`)
VALUES ('DefaultSavings', 'savingsId', 'Savings Account', 'none', 'number', '-1', 'Y', 'select ms.id \nfrom m_savings_account ms \nleft join m_client mc on mc.id = ms.client_id \nleft join m_office mo on mo.id = mc.office_id \nwhere mo.id = ${officeId} or ${officeId} = -1', '5'),
('SelectAccountType', 'accountType', 'Account Type', 'select', 'number', '-1', 'Y', "select\nenum_id as id,\nenum_value as value\nfrom r_enum_value\nwhere enum_name = 'account_type_enum'", NULL);

SET @SActi = (select id from `stretchy_report` where `report_name`='Savings Account Activated');
SET @SDep = (select id from `stretchy_report` where `report_name`='Savings Deposit');
SET @SWithdraw = (select id from `stretchy_report` where `report_name`='Savings Withdrawal');
SET @Office = (select id from `stretchy_parameter` where `parameter_name`='OfficeIdSelectOne');
SET @loanOfficer = (select id from `stretchy_parameter` where `parameter_name`='LoanOfficerSelectOneRec');
SET @DSavings = (select id from `stretchy_parameter` where `parameter_name`='DefaultSavings');
SET @DClient = (select id from `stretchy_parameter` where `parameter_name`='DefaultClient');
SET @DGroup = (select id from `stretchy_parameter` where `parameter_name`='DefaultGroup');
SET @AccountType = (select id from `stretchy_parameter` where `parameter_name`='SelectAccountType');

INSERT INTO `stretchy_report_parameter` (`report_id`, `parameter_id`, `report_parameter_name`)
VALUES (@SActi, @Office, 'Office'),
(@SDep, @Office, 'Office'),
(@SWithdraw, @Office, 'Office'),
(@SActi, @loanOfficer, 'loanOfficer'),
(@SDep, @loanOfficer, 'loanOfficer'),
(@SWithdraw, @loanOfficer, 'loanOfficer'),
(@SActi, @DSavings, 'savingsId'),
(@SActi, @DClient, 'clientId'),
(@SActi, @DGroup, 'groupId'),
(@SActi, @AccountType, 'accountType'),
(@SDep, @AccountType, 'accountType'),
(@SWithdraw, @AccountType, 'accountType');