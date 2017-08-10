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
package org.apache.fineract.accounting.closure.storeglaccountbalance.data;

public enum GLClosureFileFormat {
    CSV(0, "csv"),
    GP(1, "gp"),
    T24(2, "t24"),
    XLS(3, "xls"),
    XML(4, "xml");

    private final Integer value;
    private final String format;

    private GLClosureFileFormat(final Integer value, final String format) {
        this.value = value;
        this.format = format;
    }

    public static GLClosureFileFormat fromInteger(final Integer value){
        GLClosureFileFormat dataExportFileFormat = CSV;
        for (GLClosureFileFormat fileFormat : GLClosureFileFormat.values()) {
            if (fileFormat.value == value) {
                dataExportFileFormat = fileFormat;
                break;
            }
        }
        return dataExportFileFormat;
    }
    
    public static GLClosureFileFormat fromString(final String format) {
        GLClosureFileFormat dataExportFileFormat = CSV;
        
        for (GLClosureFileFormat fileFormat : GLClosureFileFormat.values()) {
        	if (fileFormat.format.equalsIgnoreCase(format)) {
        		dataExportFileFormat = fileFormat;
        		break;
        	}
        }
        
        return dataExportFileFormat;
    }

    public Integer getValue() {
        return value;
    }

    public String getFormat() { return format; }
    
    /** 
     * @return true if enum is equal to DataExportFileFormat.CSV, else false
     **/
    public boolean isCsv() {
        return this.equals(CSV);
    }

    /**
     * @return true if enum is equal to DataExportFileFormat.CSV, else false
     **/
    public boolean isGP() {
        return this.equals(GP);
    }

    /**
     * @return true if enum is equal to DataExportFileFormat.CSV, else false
     **/
    public boolean isT24() {
        return this.equals(T24);
    }
    
    /** 
     * @return true if enum is equal to DataExportFileFormat.XLS, else false 
     **/
    public boolean isXls() {
        return this.equals(XLS);
    }
    
    /** 
     * @return true if enum is equal to DataExportFileFormat.XML, else false 
     **/
    public boolean isXml() {
        return this.equals(XML);
    }
}
