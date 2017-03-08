/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.infrastructure.dataexport.helper;

import org.apache.commons.lang.WordUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mifosplatform.infrastructure.dataexport.data.MysqlDataType;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XlsFileHelper {
	public static void createFile(final SqlRowSet sqlRowSet, final File file) {
		try {
			final SqlRowSetMetaData sqlRowSetMetaData = sqlRowSet.getMetaData();
            final int columnCount = sqlRowSetMetaData.getColumnCount();
			
			// Create a new spreadsheet workbook
            XSSFWorkbook workbook = new XSSFWorkbook();
            // Create a blank sheet for the workbook
            XSSFSheet sheet = workbook.createSheet();
            // create a new cell style object
            XSSFCellStyle cellStyle  = workbook.createCellStyle(); 
            // create a new data format object 
    		XSSFDataFormat dataFormat = workbook.createDataFormat();

            int rowIndex = 0;
            int columnIndex = 0;
            Row row = sheet.createRow(rowIndex++);
            
            for (int i=1; i<=columnCount; i++) {
            	// create a new cell for each columns for the header row
            	Cell cell = row.createCell(columnIndex++);
            	// get the column label of the dataset
            	String columnLabel = WordUtils.capitalize(sqlRowSetMetaData.getColumnLabel(i));
            	// set the value of the cell
            	cell.setCellValue(columnLabel);
            }
            
            while (sqlRowSet.next()) {
            	columnIndex = 0;
            	row = sheet.createRow(rowIndex++);
            	
            	for (int i=1; i<=columnCount; i++) {
            		Cell cell = row.createCell(columnIndex++);
            		String columnTypeName = sqlRowSetMetaData.getColumnTypeName(i);
            		MysqlDataType mysqlDataType = MysqlDataType.newInstance(columnTypeName);
            		String stringValue = sqlRowSet.getString(i);
            		
            		if (stringValue != null) {
            			switch (mysqlDataType.getCategory()) {
	            			case NUMERIC:
								// TINYINT(1) is also treated as an alias for a BOOL in MySQL in certain versions of the JDBC connector, option tinyInt1isBit
								// See: http://stackoverflow.com/questions/16798744/why-does-tinyint1-function-as-a-boolean-but-int1-does-not/35488212#35488212
								if(mysqlDataType.equals(MysqlDataType.TINYINT) && sqlRowSetMetaData.getPrecision(i) == 1 && (stringValue.equals("true") || stringValue.equals("false")))
								{
									// Handle the cell as string, it is already a casted boolean:
									cell.setCellType(Cell.CELL_TYPE_STRING);
									cell.setCellValue(stringValue);
								}
								else
								{
									final double numberAsDouble = Double.parseDouble(stringValue);
									cell.setCellType(Cell.CELL_TYPE_NUMERIC);
									cell.setCellValue(numberAsDouble);
								}
	            				break;
	            				
	            			case DATE_TIME:
	            				DateFormat dateFormat;
	            				Date date;
	            				
	            				switch (mysqlDataType) {
	            					case DATE:
	            					case DATETIME:
	            						String mysqlDateFormat = "yyyy-MM-dd";
	            						String excelDateFormat = "MM/DD/YYYY";
	            						
	            						if (mysqlDataType.equals(MysqlDataType.DATETIME)) {
	            							mysqlDateFormat = "yyyy-MM-dd HH:mm:ss";
	            							excelDateFormat = "MM/DD/YYYY HH:MM:SS";
	            						}
	            						
	            						dateFormat = new SimpleDateFormat(mysqlDateFormat);
	            						date = dateFormat.parse(stringValue);
	            						
	            						cellStyle.setDataFormat(dataFormat.getFormat(excelDateFormat));
	            						
	            						cell.setCellValue(date);
	            						cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	            						cell.setCellStyle(cellStyle);
	            						break;
	            						
	            					default:
	            						cell.setCellValue(stringValue);
	            						break;
	            				}
	            				break;
	            		
	            			default:
	            				cell.setCellType(Cell.CELL_TYPE_STRING);
	                			cell.setCellValue(stringValue);
	            				break;
	            		}
            			
            		} else {
            			cell.setCellValue(stringValue);
            		}
            	}
            }
            
            //Write the workbook in file system
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
			
		} catch (Exception exception) {
        	exception.printStackTrace();
        }
	}
}
