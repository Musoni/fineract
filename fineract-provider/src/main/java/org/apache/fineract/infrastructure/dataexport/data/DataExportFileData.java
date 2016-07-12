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
package org.apache.fineract.infrastructure.dataexport.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataExportFileData {
    private final File file;
    private final String fileName;
    private final String contentType;
    private final InputStream inputStream;
    private static final Logger logger = LoggerFactory.getLogger(DataExportFileData.class);

    /**
     * @param file
     * @param fileName
     * @param contentType
     * @param inputStream
     */
    public DataExportFileData(final File file, final InputStream inputStream, final String fileName, final String contentType) {
        this.file = file;
        this.fileName = fileName;
        this.contentType = contentType;
        this.inputStream = inputStream;
    }

    /**
     * @param file
     * @param fileName
     * @param contentType
     */
    public DataExportFileData(final File file, final String fileName, final String contentType) {
        this(file,null,fileName,contentType);
    }

    /**
     * @param fileName
     * @param contentType
     * @param inputStream
     */
    public DataExportFileData(final InputStream inputStream, final String fileName, final String contentType) {
        this(null,inputStream,fileName,contentType);
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @return the inputStream
     */
    public InputStream getInputStream() {
        InputStream inputStream = this.inputStream;

        try {
            if (inputStream == null) {
                inputStream = new FileInputStream(this.file);
            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }

        return inputStream;
    }
}
