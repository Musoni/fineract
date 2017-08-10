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

import au.com.bytecode.opencsv.CSVWriter;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.apache.fineract.infrastructure.dataexport.api.DataExportApiConstants;
import org.apache.fineract.infrastructure.dataexport.data.DataExportCoreTable;
import org.apache.fineract.infrastructure.dataexport.data.MysqlDataType;
import org.apache.fineract.useradministration.data.AppUserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** 
 * Helper class that provides useful methods to manage CSV files 
 **/
public class CsvFileHelper {
    public static final char SEPARATOR = ';';
    public static final char QUOTE_CHARACTER = CSVWriter.NO_QUOTE_CHARACTER;
    public static final char ESCAPE_CHARACTER = CSVWriter.NO_ESCAPE_CHARACTER;
    public static final String ENCODING = "UTF-8";
    
    private final static Logger logger = LoggerFactory.getLogger(CsvFileHelper.class);
    
    /**
     * Creates a new CSV file
     * 
     * @param sqlRowSet
     * @param file
     */
    public static void createFile(final SqlRowSet sqlRowSet, final File file, 
    		final HashMap<Long, CodeValueData> codeValueMap, final HashMap<Long, AppUserData> appUserMap, 
    		final DataExportCoreTable coreTable) {
    	try {
            // create a new CSVWriter object
            final CSVWriter csvWriter = new CSVWriter(new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), ENCODING)), SEPARATOR, QUOTE_CHARACTER,
                    ESCAPE_CHARACTER, DataExportApiConstants.WINDOWS_END_OF_LINE_CHARACTER);
            final SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
            final int columnCount = sqlRowSetMetaData.getColumnCount();
            final String[] headers = new String[columnCount];
            final List<String[]> data = new ArrayList<>();
            
            int columnIndex = 0;
            
            for (int i=1; i<=columnCount; i++) {
            	// get the column label of the dataset
            	String columnLabel = WordUtils.capitalize(sqlRowSetMetaData.getColumnLabel(i));
            	
            	// add column label to headers array
            	headers[columnIndex++] = columnLabel;
            }
            
            while (sqlRowSet.next()) {
            	// create a new empty string array of length "columnCount"
            	final String[] rowData = new String[columnCount];
            	
            	int rowIndex = 0;
            	
            	for (int i=1; i<=columnCount; i++) {
            		String columnTypeName = sqlRowSetMetaData.getColumnTypeName(i);
            		MysqlDataType mysqlDataType = MysqlDataType.newInstance(columnTypeName);
            		String columnValue = sqlRowSet.getString(i);
            		String columnName = sqlRowSetMetaData.getColumnName(i);
            		
            		// replace code value id with the code value name
            		AbstractMap.SimpleEntry<String, MysqlDataType> columnValueDataType = 
            				DataExportUtils.replaceCodeValueIdWithValue(codeValueMap, columnName, columnValue, mysqlDataType);
            		
            		// update the column value
            		columnValue = columnValueDataType.getKey();
            		
            		// replace app user id with respective username
            		columnValueDataType = 
            				DataExportUtils.replaceAppUserIdWithUserName(appUserMap, columnName, columnValue, mysqlDataType);
            		
            		// update the column value
            		columnValue = columnValueDataType.getKey();
            		
            		rowData[rowIndex++] = StringEscapeUtils.escapeCsv(columnValue);
            	}
            	
            	// add the row data to the array list of row data
            	data.add(rowData);
            }
            
            // write file headers to file
            csvWriter.writeNext(headers);
            
            // write file data to file
            csvWriter.writeAll(data);
            
            // close stream writer
            csvWriter.close();
        }
        
        catch (Exception exception) {
            logger.error(exception.getMessage(), exception);
        }
    }
}
