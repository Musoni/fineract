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

def buildBranch(name) {
    sh './gradlew clean build war'
}

def s3Upload(String name) {
    sh 'aws s3 cp $WORKSPACE/build/libs/ s3://musonisystem-configurations/' + name + '/release/ --exclude "*" --include "*.war" --only-show-errors --recursive'
}

node {
    checkout scm

    def name = env.BRANCH_NAME
    if (name == 'develop') {
        buildBranch(name)
        s3Upload(name)
    } else if (name.startsWith('release/')) {
        buildBranch(name)
    } else if (name == 'demo') {
        buildBranch(name)
        s3Upload(name)
    } else if (name == 'master') {
        buildBranch(name)
        s3Upload("prod")

    } else {
        error "Don't know what to do with this branch: ${name}"
    }
}
