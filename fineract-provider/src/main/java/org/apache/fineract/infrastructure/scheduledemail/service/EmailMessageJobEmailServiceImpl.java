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
package org.apache.fineract.infrastructure.scheduledemail.service;


import org.apache.xmlbeans.impl.tool.XMLBean;
import org.apache.fineract.infrastructure.configuration.data.SMTPCredentialsData;
import org.apache.fineract.infrastructure.configuration.service.ExternalServicesPropertiesReadPlatformService;
import org.apache.fineract.infrastructure.scheduledemail.EmailApiConstants;
import org.apache.fineract.infrastructure.scheduledemail.data.EmailMessageWithAttachmentData;
import org.apache.fineract.infrastructure.scheduledemail.domain.EmailConfiguration;
import org.apache.fineract.infrastructure.scheduledemail.domain.EmailConfigurationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Service
public class EmailMessageJobEmailServiceImpl implements EmailMessageJobEmailService {

    private final ExternalServicesPropertiesReadPlatformService externalServicesReadPlatformService;
    private static final Logger logger = LoggerFactory.getLogger( EmailMessageJobEmailServiceImpl.class);


    @Autowired
    private EmailMessageJobEmailServiceImpl(final ExternalServicesPropertiesReadPlatformService externalServicesReadPlatformService) {
        this.externalServicesReadPlatformService = externalServicesReadPlatformService;
    }

    @Override
    public void sendEmailWithAttachment(EmailMessageWithAttachmentData emailMessageWithAttachmentData) {
        try{
            SMTPCredentialsData smtpCredentialsData = this.externalServicesReadPlatformService.getSMTPCredentials();

            JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
            javaMailSenderImpl.setHost(smtpCredentialsData.getHost());
            javaMailSenderImpl.setPort(Integer.parseInt(smtpCredentialsData.getPort()));
            javaMailSenderImpl.setUsername(smtpCredentialsData.getUsername());
            javaMailSenderImpl.setPassword(smtpCredentialsData.getPassword());
            javaMailSenderImpl.setJavaMailProperties(this.getJavaMailProperties(smtpCredentialsData.getHost()));

            MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();

            // use the true flag to indicate you need a multipart message
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setTo(emailMessageWithAttachmentData.getTo());
            mimeMessageHelper.setText(emailMessageWithAttachmentData.getText());
            mimeMessageHelper.setSubject(emailMessageWithAttachmentData.getSubject());
            final List<File> attachments = emailMessageWithAttachmentData.getAttachments();
            if(attachments !=null && attachments.size() > 0){
                for(final File attachment : attachments){
                    if(attachment !=null){
                        mimeMessageHelper.addAttachment(attachment.getName(),attachment);
                    }
                }
            }

            javaMailSenderImpl.send(mimeMessage);

        }catch(Exception e){
            logger.error(e.getMessage(), e);
        }

    }


    private Properties getJavaMailProperties(final String smtpHost) {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.trust", smtpHost);

        return properties;
    }
}
