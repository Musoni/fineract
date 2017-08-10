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
package org.apache.fineract.template.restwebservice;

import java.net.URI;
import java.util.Arrays;

import javax.ws.rs.core.UriBuilder;

import org.apache.fineract.restwebservice.PlatformRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;

public class TemplateRestClient extends PlatformRestClient {
	private final static Logger logger = LoggerFactory.getLogger(PlatformRestClient.class);
	
	/**
	 * TemplateRestClient public constructor
	 *
	 * @param securityContext
	 */
	public TemplateRestClient(final SecurityContext securityContext) {
		super(securityContext);
	}
	
	/**
	 * Retrieves string data from the specified url
	 * 
	 * @param url
	 * @return string data
	 */
	public String retrieveDataFromUrl(final String url) {
		this.configureClient();
		
		String data = null;
		
		try {
			// build the URI
            final URI uri = UriBuilder.fromUri(url).build();
            
            // HTTP request entity consisting of HTTP Authorization header 
            final HttpEntity<String> requestEntity = this.getHttpRequestEntity(null, MediaType.APPLICATION_JSON, 
                    Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN, MediaType.TEXT_HTML));
            
            // execute the HTTP request
            final ResponseEntity<String> responseEntity = this.executeHttpRequest(uri, HttpMethod.GET, requestEntity, 
                    String.class);
            
            if (responseEntity != null) {
            	data = responseEntity.getBody();
            }
			
		} catch (final Exception exception) {
			logger.error(exception.getMessage(), exception);
		} 
		
		return data;
	}
}
