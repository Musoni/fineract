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
package org.apache.fineract.infrastructure.dataexport.helper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFileHelper {
    public static final String IDENTITIER_TYPE = "MSISDN";
    private final static Logger logger = LoggerFactory.getLogger(XmlFileHelper.class);

    
    /** 
     * creates a new XML file for Mpesa Kenya payment service provider
     * 
     *  data list of array containing comma separated string data (e.g. "1000", "254700266291", "[Phone Details][Mobile Phone Number]")
     * @param file An abstract representation of XML file to created
     * 
     * @return void
     * @throws IOException 
     **/
    public static void writeToFile(final List<String[]> fileData, final File file) {
        try {
            
            /*for(String[] stringList : fileData) {
                // create a new instance of the Customer.class
                MpesaKenyaOutgoingPaymentCustomer customer = new MpesaKenyaOutgoingPaymentCustomer();
                
                // create a new instance of the Identifier.class
                MpesaKenyaOutgoingPaymentIdentifier identifier = new MpesaKenyaOutgoingPaymentIdentifier();
                
                // create a new instance of the Amount.class
                MpesaKenyaOutgoingPaymentAmount amount = new MpesaKenyaOutgoingPaymentAmount();
                
                // set the value of the Identifier.identifierType property
                identifier.setIdentifierType(IDENTITIER_TYPE);
                
                // set the value of the Identifier.IdentifierValue property
                identifier.setIdentifierValue(stringList[1]);
                
                // set the value of the "Amount.Value" property
                amount.setValue(stringList[0]);
                
                // set the value of the Customer.Identifier property
                customer.setIdentifier(identifier);
                
                // set the value of the Customer.Amount property
                customer.setAmount(amount);
                
                // add the new "Entry" object to the "BulkPaymentRequest.Customers" array list
                bulkPaymentRequest.getCustomers().add(customer);
            }
            
            // create new instance of the JAXBContext.class
            JAXBContext jaxbContext = JAXBContext.newInstance(MpesaKenyaOutgoingPaymentBulkPaymentRequest.class);
            
            // create new instance of the Marshaller.class
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            
            // XML data will be formatted with linefeeds and indentation
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); 
            
            // remove XML declaration (<?xml version="1.0" encoding="UTF-8" standalone="yes"?>)
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            
            // create a new string writer stream
            StringWriter stringWriter = new StringWriter();
            
            // create XML file from BulkPaymentRequest.class object
            jaxbMarshaller.marshal(bulkPaymentRequest, stringWriter);
            
            // convert file to string for further manipulation
            String xmlOutput = stringWriter.toString();
            
            // replace the system's line feed with the windows line feed
            xmlOutput = xmlOutput.replace(DataExportApiConstants.UNIX_END_OF_LINE_CHARACTER,
                    DataExportApiConstants.WINDOWS_END_OF_LINE_CHARACTER);
            
            // convert back to file
            FileUtils.writeStringToFile(file, xmlOutput);*/
        }
        
        catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
    }
}
