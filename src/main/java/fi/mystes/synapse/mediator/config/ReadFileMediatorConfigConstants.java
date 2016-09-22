/**
 * Copyright 2016 Mystes Oy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fi.mystes.synapse.mediator.config;

import org.apache.synapse.config.xml.XMLConfigConstants;

import javax.xml.namespace.QName;

public class ReadFileMediatorConfigConstants {

    public static final String NAMESPACE_STRING = XMLConfigConstants.SYNAPSE_NAMESPACE;
    public static final String ROOT_TAG_NAME = "readFile";
    public static final QName ROOT_TAG = new QName(NAMESPACE_STRING, ROOT_TAG_NAME);
    public static final QName ATT_PROPERTY = new QName("property");
    public static final QName ATT_FILENAME = new QName("fileName");

    public static final QName ATT_ATTACH_XPATH = new QName("attachXpath");


}
