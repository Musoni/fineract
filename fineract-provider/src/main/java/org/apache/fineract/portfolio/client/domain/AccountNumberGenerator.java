/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.client.domain;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.fineract.infrastructure.accountnumberformat.domain.AccountNumberFormat;
import org.apache.fineract.infrastructure.accountnumberformat.domain.AccountNumberFormatEnumerations.AccountNumberPrefixType;
import org.apache.fineract.infrastructure.accountnumberformat.domain.CustomAccountType;
import org.apache.fineract.infrastructure.codes.domain.CodeValue;
import org.apache.fineract.portfolio.group.domain.Group;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.savings.domain.SavingsAccount;
import org.apache.fineract.portfolio.shareaccounts.domain.ShareAccount;
import org.springframework.stereotype.Component;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Example {@link AccountNumberGenerator} for clients that takes an entities
 * auto generated database id and zero fills it ensuring the identifier is
 * always of a given <code>maxLength</code>.
 */
@Component
public class AccountNumberGenerator {

    private final static int maxLength = 9;

    private final static String ID = "id";
    private final static String CLIENT_TYPE = "clientType";
    private final static String OFFICE_NAME = "officeName";
    private final static String OFFICE_EXTERNAL_ID = "officeExternalId";
    private final static String LOAN_PRODUCT_SHORT_NAME = "loanProductShortName";
    private final static String SAVINGS_PRODUCT_SHORT_NAME = "savingsProductShortName";
    private final static String SHARE_PRODUCT_SHORT_NAME = "sharesProductShortName" ;
    
    public String generate(Client client, AccountNumberFormat accountNumberFormat) {
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put(ID, client.getId().toString());
        propertyMap.put(OFFICE_NAME, client.getOffice().getName());
        CodeValue clientType = client.clientType();
        if (clientType != null) {
            propertyMap.put(CLIENT_TYPE, clientType.label());
        }
        String externalId = client.getOffice().getExternalId();
        if(externalId !=null){
            propertyMap.put(OFFICE_EXTERNAL_ID,externalId);
        }
        return generateAccountNumber(propertyMap, accountNumberFormat);
    }

    public String generateCustomAccount(Client client, AccountNumberFormat accountNumberFormat){
        final Map<String,String> customMap = new HashMap<>();
        customMap.put(CustomAccountType.ENTITY_ID.getCode(), client.getId().toString());
        
        if (client.getOffice() != null && client.getOffice().getExternalId() != null) {
            customMap.put(CustomAccountType.OFFICE_EXTERNAL_ID.getCode(), client.getOffice().getExternalId());
        }
        
        final String officeId = (client.getOffice() != null && client.getOffice().getId() != null) ? 
        		client.getOffice().getId().toString() : "0";
        final String staffId = (client.getStaff() != null && client.getStaff().getId() != null) ? 
        		client.getStaff().getId().toString() : "0";
        		
        customMap.put(CustomAccountType.OFFICE_ID.getCode(), officeId);
        customMap.put(CustomAccountType.STAFF_ID.getCode(), staffId);
        
        return generateCustomAccountNumberWithMustacheTemplate(accountNumberFormat,customMap);
    }

    private String compileCustomNumberFormat(final String customTemplate,final Map<String,String> paramsToCreateClientFormat){
        final MustacheFactory mf = new DefaultMustacheFactory();
        final Mustache mustache = mf.compile(new StringReader(customTemplate), "custom number format");

        final StringWriter stringWriter = new StringWriter();
        mustache.execute(stringWriter, paramsToCreateClientFormat);

        return stringWriter.toString();
    }

    public String generate(Loan loan, AccountNumberFormat accountNumberFormat) {
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put(ID, loan.getId().toString());
        propertyMap.put(OFFICE_NAME, loan.getOffice().getName());
        propertyMap.put(LOAN_PRODUCT_SHORT_NAME, loan.loanProduct().getShortName());
        return generateAccountNumber(propertyMap, accountNumberFormat);
    }

    public String generateCustomAccount(final Loan loan, final AccountNumberFormat accountNumberFormat){
        final Map<String,String> customMap = new HashMap<>();
        customMap.put(CustomAccountType.ENTITY_ID.getCode(),loan.getId().toString());

        if(loan.isIndividualLoan()){
            customMap.put(CustomAccountType.OFFICE_ID.getCode(),loan.getClient().getOffice().getId().toString());
            if(loan.getClient().getOffice().getExternalId() !=null){
                customMap.put(CustomAccountType.OFFICE_EXTERNAL_ID.getCode(),loan.getClient().getOffice().getExternalId());
            }
            customMap.put(CustomAccountType.CLIENT_ID.getCode(),loan.getClient().getId().toString());
        }else{
            /**  this is a group loan get group office  **/
            customMap.put(CustomAccountType.OFFICE_ID.getCode(),loan.group().getOffice().getId().toString());
            if(loan.group().getOffice().getExternalId() !=null){
                customMap.put(CustomAccountType.OFFICE_EXTERNAL_ID.getCode(),loan.group().getOffice().getExternalId());
            }
            customMap.put(CustomAccountType.GROUP_ID.getCode(),loan.group().getId().toString());
        }

        customMap.put(CustomAccountType.LOAN_PRODUCT.getCode(),loan.getLoanProduct().getId().toString());
        customMap.put(CustomAccountType.LOAN_PRODUCT_SHORT_NAME.getCode(),loan.getLoanProduct().getShortName());
        customMap.put(CustomAccountType.STAFF_ID.getCode(),loan.getLoanOfficer().getId().toString());
        return generateCustomAccountNumberWithMustacheTemplate(accountNumberFormat,customMap);
    }

    public String generateCustomAccount(Group group,AccountNumberFormat accountNumberFormat){
        final Map<String,String> customMap = new HashMap<>();
        customMap.put(CustomAccountType.ENTITY_ID.getCode(), group.getId().toString());
        customMap.put(CustomAccountType.OFFICE_ID.getCode(),group.getOffice().getId().toString());
        customMap.put(CustomAccountType.STAFF_ID.getCode(),group.getStaff().getId().toString());
        customMap.put(CustomAccountType.GROUP_NAME.getCode(),group.getName());
        if(group.getOffice().getExternalId() !=null){
            customMap.put(CustomAccountType.OFFICE_EXTERNAL_ID.getCode(),group.getOffice().getExternalId());
        }
        return generateCustomAccountNumberWithMustacheTemplate(accountNumberFormat,customMap);

    }


    public String generateCustomAccount(SavingsAccount savingsAccount,AccountNumberFormat accountNumberFormat){
        final Map<String,String> customMap = new HashMap<>();

        if(savingsAccount.isIndividualSavings()){
            customMap.put(CustomAccountType.CLIENT_ID.getCode(), savingsAccount.getClient().getId().toString());
            customMap.put(CustomAccountType.OFFICE_ID.getCode(),savingsAccount.getClient().getOffice().getId().toString());
            if(savingsAccount.getClient().getOffice().getExternalId() !=null){
                customMap.put(CustomAccountType.OFFICE_EXTERNAL_ID.getCode(), savingsAccount.getClient().getOffice().getExternalId());
            }
        }else{
            /** group savings get group savings info**/
            customMap.put(CustomAccountType.OFFICE_ID.getCode(),savingsAccount.group().getOffice().getId().toString());
            if(savingsAccount.group().getOffice().getExternalId() !=null){
                customMap.put(CustomAccountType.OFFICE_EXTERNAL_ID.getCode(), savingsAccount.group().getOffice().getExternalId());
            }
            customMap.put(CustomAccountType.GROUP_ID.getCode(), savingsAccount.group().getId().toString());


        }
        customMap.put(CustomAccountType.ENTITY_ID.getCode(), savingsAccount.getId().toString());
        customMap.put(CustomAccountType.SAVING_PRODUCT_SHORT_NAME.getCode(),savingsAccount.savingsProduct().getShortName());
        customMap.put(CustomAccountType.SAVINGS_PRODUCT.getCode(),savingsAccount.savingsProduct().getId().toString());
        customMap.put(CustomAccountType.STAFF_ID.getCode(), savingsAccount.getSavingsOfficer().getId().toString());
        return generateCustomAccountNumberWithMustacheTemplate(accountNumberFormat,customMap);
    }



    public String generate(SavingsAccount savingsAccount, AccountNumberFormat accountNumberFormat) {
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put(ID, savingsAccount.getId().toString());
        propertyMap.put(OFFICE_NAME, savingsAccount.office().getName());
        propertyMap.put(SAVINGS_PRODUCT_SHORT_NAME, savingsAccount.savingsProduct().getShortName());
        return generateAccountNumber(propertyMap, accountNumberFormat);
    }

    public String generate(ShareAccount shareaccount, AccountNumberFormat accountNumberFormat) {
    	Map<String, String> propertyMap = new HashMap<>();
    	propertyMap.put(ID, shareaccount.getId().toString());
    	propertyMap.put(SHARE_PRODUCT_SHORT_NAME, shareaccount.getShareProduct().getShortName());
    	return generateAccountNumber(propertyMap, accountNumberFormat) ;
    }
    
    private String generateAccountNumber(Map<String, String> propertyMap, AccountNumberFormat accountNumberFormat) {
        int maxLength = AccountNumberGenerator.maxLength;
        if(accountNumberFormat !=null && accountNumberFormat.getZeroPadding() !=null){
            maxLength =  accountNumberFormat.getZeroPadding();
        }
        String accountNumber = StringUtils.leftPad(propertyMap.get(ID), maxLength, '0');
        if (accountNumberFormat != null && accountNumberFormat.getPrefixEnum() != null) {
            AccountNumberPrefixType accountNumberPrefixType = AccountNumberPrefixType.fromInt(accountNumberFormat.getPrefixEnum());
            String prefix = null;
            switch (accountNumberPrefixType) {
                case CLIENT_TYPE:
                    prefix = propertyMap.get(CLIENT_TYPE);
                break;

                case OFFICE_NAME:
                    prefix = propertyMap.get(OFFICE_NAME);
                break;

                case LOAN_PRODUCT_SHORT_NAME:
                    prefix = propertyMap.get(LOAN_PRODUCT_SHORT_NAME);
                break;

                case SAVINGS_PRODUCT_SHORT_NAME:
                    prefix = propertyMap.get(SAVINGS_PRODUCT_SHORT_NAME);
                break;

                case OFFICE_EXTERNAL_ID:
                    prefix = propertyMap.get(OFFICE_EXTERNAL_ID);
                break;

                default:
                break;

            }
            accountNumber = StringUtils.overlay(accountNumber, prefix, 0, 0);
        }
        return accountNumber;
    }
    
    public String generateCenterAccountNumber(Group group, AccountNumberFormat accountNumberFormat) {
    	Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put(ID, group.getId().toString());
        propertyMap.put(OFFICE_NAME, group.getOffice().getName());        
        return generateAccountNumber(propertyMap, accountNumberFormat);
    }


    private String generateCustomAccountNumberWithMustacheTemplate(final AccountNumberFormat accountNumberFormat,final Map<String,String> allowmableParamsForCustomFormat){
        final String accountNumber = accountNumberFormat.getCustomPattern();
        final JsonObject jsonObject = (JsonObject) new JsonParser().parse(accountNumber);
        final String pattern = jsonObject.get("pattern").getAsString();
        final Map<String,String> formatMapAllowableParams = new LinkedHashMap<>();
        String patternPadding = null;
        if(jsonObject.has("patternPadding")){
            patternPadding = jsonObject.get("patternPadding").getAsString();
        }

        /**
         * if the pattern padding is not null use the pattern padding to format numbers using left padding
         * pattern Padding is of the format Ex 3-3-3 meaning zeros to the left for {staffId}-{officeId}-{clientId}
         * so if {1-1-63} becomes {0001-0001-00063}
         */

        if(patternPadding !=null){
            String[] paddingFormat = patternPadding.split("-");
            int i= 0;
            int paddingFormatSize = paddingFormat.length;
            final Matcher patternMatcher = Pattern.compile("\\{\\{(.*?)}}").matcher(pattern);
            while(patternMatcher.find()){
                for(Map.Entry<String, String> entry : allowmableParamsForCustomFormat.entrySet()){
                    if(patternMatcher.group(1).equals(entry.getKey())){
                        if(i < paddingFormatSize){ // no need for pattern padding matching custom pattern
                            formatMapAllowableParams.put(entry.getKey(),StringUtils.leftPad(entry.getValue(),Integer.parseInt(paddingFormat[i]), '0'));
                        }else{  formatMapAllowableParams.put(entry.getKey(),entry.getValue()); }
                        i++;
                    }
                }
            }
            return compileCustomNumberFormat(pattern, formatMapAllowableParams);

        }

        for(Map.Entry<String, String> entry : allowmableParamsForCustomFormat.entrySet()){
            formatMapAllowableParams.put(entry.getKey(),entry.getValue());
        }
        return compileCustomNumberFormat(pattern, formatMapAllowableParams);
    }


    public String generateGroupAccountNumber(Group group, AccountNumberFormat accountNumberFormat) {
        Map<String, String> propertyMap = new HashMap<>();
        propertyMap.put(ID, group.getId().toString());
        propertyMap.put(OFFICE_NAME, group.getOffice().getName());
        return generateAccountNumber(propertyMap, accountNumberFormat);
    }
}