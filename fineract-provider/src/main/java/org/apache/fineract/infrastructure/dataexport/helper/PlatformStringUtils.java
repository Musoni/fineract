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

import java.util.ArrayList;
import java.util.List;

/** 
 * Utility class containing methods that manipulate strings 
 **/
public class PlatformStringUtils {
    
    /** 
     * create an array of strings from the specified strings
     * 
     * @param first -- the first string to be added to the array
     * @param more -- additional strings to be added to the array
     * @return array of strings
     **/
    public static String[] toArray(final String first, final String ... more) {
        List<String> arrayList = new ArrayList<String>();
        
        // add the first string to the array
        arrayList.add(first);
        
        // make sure there are more strings to be added
        if (more != null && more.length > 0) {
            // iterate over the list of strings
            for (String string : more) {
                // add additional string to the array of strings
                arrayList.add(string);
            }
        }
        
        // convert to array containing all of the elements in this list 
        // in proper sequence (from first to last element)
        return arrayList.toArray(new String[arrayList.size()]);
    }
}
